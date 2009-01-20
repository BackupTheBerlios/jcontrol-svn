/*
 * Copyright (C) 2009 The JControl Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */

import java.io.IOException;

import jcontrol.comm.I2C;

/**
 * Test the I2C address for a device.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class I2CTestDevice extends I2C {

    /**
     * int - bin array.
     */
    private static final String[] INT2BIN = {"0000", "0001", "0010", "0011", "0100", "0101",
            "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};

    /**
     * The I2C address.
     */
    private int address;

    /**
     * device exists on address.
     */
    private boolean exists = false;

    /**
     * Create a new object.
     * 
     * @param address The I2C address.
     */
    public I2CTestDevice(int address) {
        super(address);
        this.address = address;

        try {
            // test to read from the device
            read();
            exists = true;
        } catch (IOException e) {
            exists = false;
        }
    }

    /**
     * @return the exists
     */
    public boolean exists() {
        return exists;
    }

    /**
     * Returns the address as bin value.
     * 
     * @return The bin value as String.
     */
    public String getBinPort() {

        return INT2BIN[address >> 4].concat(" ").concat(INT2BIN[address & 0x0f]);
    }

    /**
     * Returns the address as hex value.
     * 
     * @return The hex value as String.
     */
    public String getHexPort() {
        String rt = "0".concat(Integer.toHexString(address));
        return "0x".concat(rt.substring(rt.length() - 2, rt.length()));
    }

}
