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

/**
 * Accesses the MAX6633 temperature sensor using the SM (I<sup>2</sup>C) bus. Refer to the MAX6633
 * data sheet for details.
 * 
 * @author rst. mgn
 * @version $Revision$
 */

public class MAX6633 extends I2C {

    /**
     * Constructor of this class.
     * 
     * @param address slave address of the device
     */
    public MAX6633(int address) {
        super(address);
    }

    /**
     * Reads the current die temperatur of the MAX6633.
     * 
     * @returns An integer representing the temperature multiplied by 10 (in °C) Centigrade.
     */
    public int getTemp() throws IOException {
        // allocate buffer for temperature
        byte[] buf = new byte[2];
        // read content of 16-bit register #0x00
        read(new byte[]{0x00}, buf, 0, 2);
        // prepare and integer value
        // The factor 5/8 converts the returned value into 10 * Centigrade.
        int value = ((buf[0] * 32) + (((buf[1] & 0xF8)) >>> 3)) * 5 / 8;
        return value;
    }
}
