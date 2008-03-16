/*
 * Copyright (C) 2004-2008 DOMOLOGIC Home Automation GmbH This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jcontrol.bus.i2c;

import java.io.IOException;

import jcontrol.comm.I2C;
import jcontrol.util.DataProducer;

/**
 * Accesses the AD7416 temperature sensor using the SM (I<sup>2</sup>C) bus. Refer to the AD7416
 * data sheet for details.
 * 
 * @author rst, mgn
 * @version $Revision$
 */

public class AD7416 extends I2C implements DataProducer {

    private int otiLimit = 80;// power on default value
    private int hystLimit = 75;// power on default value

    static final boolean LOW = false;
    static final boolean HIGH = true;
    private int temp;

    /**
     * Constructor of this class. Valid addresses are 0x9x, where x is defined by hardwired
     * potentials tied to device pin 5(a2), 6(a1) and 7(a0). x = a2,a1,a0,0. If for example pins 5
     * to 7 are tied to Vcc (2.7V<->5.5V), the resulting slave address would be: 0x9E.
     * 
     * @param address slave address of the device
     */
    public AD7416(int address) {
        super(address);
        try {
            // write default configuration
            // byte0: destination is the configuration register
            // byte1: fault queue=0 (D4=D3=0), OTI is active LOW (D2=0),
            // OTI is in comperator mode (D1=0), device is NOT shutdown (D0=0)
            byte[] command = {(byte) 0x01, (byte) 0x00};
            this.write(command, 0, command.length);
        } catch (IOException e) {
        }
    }

    /**
     * Reads the current die temperatur of the AD7416.
     * 
     * @returns An integer representing the temperature multiplied by 10 (in °C) Centigrade.
     */
    public int getTemp() throws IOException {
        // allocate buffer for temperature
        byte[] buf = new byte[2];
        // read content of 16-bit register #0x00
        read(new byte[]{0x00}, buf, 0, 2);
        // prepare value
        // The factor 5/2 converts the returned value into 10 * Centigrade.
        int result = ((buf[0] * 4) + (((buf[1] & 0xC0)) >>> 6)) * 5 / 2;
        // return value
        return result;
    }

    /**
     * Sets the temperature limit on which the 'Over Temperature Indicator' pin (device pin 3) is
     * activated. This pin is set active low in the default configuration and can drive currents up
     * to 1mA.
     */
    public boolean setOTILimit(int limit) throws IOException {
        // check, if limit is inside boundaries
        if ((limit > 150) || (limit < -55)) { return false; }
        otiLimit = limit;
        // set pointer to oti register (0x03)
        byte[] command = {(byte) 0x03, (byte) limit};
        this.write(command, 0, command.length);
        return true;
    }

    /**
     * Sets the temperature limit on which the 'Over Temperature Indicator' pin (device pin 3) is
     * deactivated. This pin is set active low in the default configuration and can drive currents
     * up to 1mA.
     */
    public boolean setHystLimit(int limit) throws IOException {
        // check, if limit is inside boundaries
        if ((limit > 150) || (limit < -55)) { return false; }
        hystLimit = limit;
        // set pointer to hyst register (0x02)
        byte[] command = {(byte) 0x02, (byte) limit};
        this.write(command, 0, command.length);
        return true;
    }

    /**
     * Set active state of the oti pin (device pin 3).
     */
    public void setOTIActive(boolean level) throws IOException {
        byte[] register = {(byte) 0x01};
        byte[] buffer = {(byte) 0x00};
        this.read(register, buffer, 0, 1);
        if (level) {
            buffer[0] = (byte) (buffer[0] | 0x04);
        } else {
            buffer[0] = (byte) (buffer[0] & 0xFB);
        }
        byte[] command = {(byte) 0x01, buffer[0]};
        this.write(command, 0, command.length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#getMin()
     */
    public int getMin() {
        return -400;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#getMax()
     */
    public int getMax() {
        return 1250;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#getValue()
     */
    public int getValue() {
        return temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#getUnit()
     */
    public String getUnit() {
        return "°C";
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#getExponent()
     */
    public int getExponent() {
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#updateValue()
     */
    public void updateValue() {
        try {
            temp = getTemp();
        } catch (IOException e) {
            temp = 0;
        }

    }

}
