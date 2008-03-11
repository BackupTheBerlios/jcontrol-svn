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
import jcontrol.lang.Math;
import jcontrol.util.DataProducer;

/**
 * Accesses the TMP75 temperature sensor using the SM (I<sup>2</sup>C) bus. Refer to the TMP75
 * data sheet for details.
 * 
 * @author rst, mgn
 * @version $Revision$
 */

public class TMP75 extends I2C implements DataProducer {

    private int temp;

    /**
     * Constructor of this class.
     * 
     * @param address slave address of the device
     */
    public TMP75(int address) {
        super(address);
        byte[] command = {(byte) 0x01, (byte) 0x60};
        try {
            this.write(command, 0, command.length);
        } catch (IOException e) {
        }
    }

    /**
     * Reads the current die temperatur of the TMP75.
     * 
     * @returns An integer representing the temperature multiplied by 10 (in °C) Centigrade.
     */
    public int getTemp() throws IOException {
        // set pointer to temperature register
        byte[] command = {(byte) 0x00};
        this.write(command, 0, command.length);
        // allocate buffer for temperature
        byte[] buf = new byte[2];
        // read content of 16-bit register #0x00
        read(buf, 0, 2);
        // calculate temperature value in 10 * Centigrade.
        return Math.scale((buf[0] << 8) | (buf[1] & 0xF0), 128, 5);
    }

    public int getValue() {
        return temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#getMin()
     */
    public int getMin() {
        return -550;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.misc.ValueProducer#getMax()
     */
    public int getMax() {
        return 1270;
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
