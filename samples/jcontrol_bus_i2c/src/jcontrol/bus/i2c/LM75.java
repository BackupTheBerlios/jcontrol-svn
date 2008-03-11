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
 * Accesses the LM75 temperature sensor using the SM (I<sup>2</sup>C) bus. Refer to the LM75 data
 * sheet for details.
 * 
 * @author boehme. mgn
 * @version $Revision$
 */
public class LM75 extends I2C implements DataProducer {

    private int temp;

    /**
     * Opens a connection to the specified LM75 device.
     * 
     * @param address of the device to open
     * @throws IOException if no device replies
     * @throws IllegalArgumentException if the address is invalid
     */
    public LM75(int address) {
        super(address);
    }

    /**
     * Reads a temperature from the LM75. The temperature range is -55 to 127 degrees (°C) and the
     * accuracy is 0.5 degrees.
     * 
     * @return int temperature value multiplied by 10 (in °C)
     * @throws IOException on communication error
     */
    public int getTemp() throws IOException {
        // allocate buffer for temperature
        byte[] buf = new byte[2];
        // read content of 16-bit register #0x00
        read(new byte[]{0x00}, buf, 0, 2);
        // return integer value
        return (buf[0] * 10) + (((buf[1] & 0x80) * 5) >> 7);
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
