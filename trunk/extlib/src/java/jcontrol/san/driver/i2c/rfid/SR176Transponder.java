/*
 * Copyright (C) 2008 The JControl Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package jcontrol.san.driver.i2c.rfid;

import java.io.IOException;

/**
 * RFID transponder with sr176.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author DOMOLOGIC Home Automation GmbH 2006
 * @version $Revision:36 $
 */
public class SR176Transponder extends AbstractRFIDTransponder {

    /**
     * The memory size.
     */
    private static final int CR176_MEMORY_SIZE = 11;

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.driver.i2c.rfid.AbstractRFIDTransponder#getUID()
     */
    public byte[] getUID() throws IOException {

        int i = 0;
        byte[] uid = new byte[8];

        while (i < 4) {
            byte[] data = readInternal(i);

            if (data.length != 2) {
                uid = null;
                throw new IOException();
            }
            uid[i * 2] = data[1];
            uid[i * 2 + 1] = data[0];
            i++;
        }
        return uid;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.driver.i2c.rfid.AbstractRFIDTransponder#read(int)
     */
    public byte[] read(int address) throws IOException {
        return readInternal(address + 4);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.driver.i2c.rfid.AbstractRFIDTransponder#size()
     */
    public int size() {
        return CR176_MEMORY_SIZE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDTransponder#toString()
     */
    public String toString() {

        try {
            byte[] uid = getUID();
            String uidString = "";
            for (int i = 0; i < uid.length; i++) {
                uidString = uidString.concat(Integer.toHexString((uid[i]) & 0xff));
            }
            return uidString;
        } catch (IOException e) {
            return "---";
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.driver.i2c.rfid.AbstractRFIDTransponder#write(int, byte[])
     */
    public void write(int address, byte[] buffer) throws IOException {
        writeInternal(address + 4, buffer);
    }
}
