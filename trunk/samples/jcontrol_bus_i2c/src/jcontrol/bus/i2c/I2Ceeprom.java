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
 * Utilizes the I<sup>2</sup>C Bus for access to serial EEproms of the 24Cxx series.
 * 
 * @author Helge Böhme, mgn
 * @version $Revision$
 * @jcontrol.devices lib
 * @jcontrol.aml available="false"
 */
public class I2Ceeprom {

    public static final int TYPE_C02 = 2;
    public static final int TYPE_C04 = 4;
    public static final int TYPE_C08 = 8;
    public static final int TYPE_C16 = 16;

    /** EEPROM chip address */
    private int address;
    /** EEPROM size */
    private int size;
    /** EEPROM maximum burst transfer */
    private int maxburst;
    /** Command Buffer */
    private byte[] sendadr = new byte[1];

    /**
     * Constructs a new SerEPP access object
     * 
     * @param type the chip type of the serial EEprom, possible values are defined as constants
     * @param address the address of the chip as defined by its address input pins, on C04, C08 and
     *        C16 a number of LSBs is ignored.
     */
    public I2Ceeprom(int type, int address) {
        this.address = ((16 - type) & (address << 1)) + 0xa0;
        this.size = type << 7;
        this.maxburst = (type >= TYPE_C04) ? 16 : 8;
    }

    /**
     * Reads out the serial EEprom to the destination byte array.
     * 
     * @param data the byte array to fill with data
     * @param startindex the index in data to start filling
     * @param length number of bytes to read
     * @param wordadr the word address of the EEprom to start reading
     * @return the number of bytes read (could be less than array stop-startindex)
     * @throws IOException on communication error (maybe data is partially filled)
     */
    public int read(byte[] data, int startindex, int length, int wordadr) throws IOException {
        int read = 0;
        int stopindex = startindex + length;
        int i = size - wordadr + startindex;
        if (stopindex > i) stopindex = i; // max bytes
        while (startindex < stopindex) {
            sendadr[0] = (byte) (wordadr & 255);
            int count = stopindex - startindex; // transfer length
            i = 256 - (wordadr & 255);
            if (count > i) count = i; // page boundry
            int devadr = address + ((wordadr >> 7) & 14);
            I2C i2c = new I2C(devadr);
            i2c.read(sendadr, data, startindex, count);
            startindex += count;
            wordadr += count;
            read += count;
        }
        return read;
    }

    /**
     * Writes a byte array to the serial EEprom.
     * 
     * @param data the byte array to write
     * @param startindex the index in data to start transmitting
     * @param length number of bytes to write
     * @param wordadr the word address of the EEprom to start writing
     * @return the number of bytes written (could be less than array stop-startindex)
     * @throws IOException on communication error (maybe data is partially transmitted)
     */
    public int write(byte[] data, int startindex, int length, int wordadr) throws IOException {
        int written = 0;
        int stopindex = startindex + length;
        int i = size - wordadr + startindex;
        if (stopindex > i) stopindex = i; // max bytes
        while (startindex < stopindex) {
            sendadr[0] = (byte) (wordadr & 255);
            int count = stopindex - startindex; // transfer length
            i = maxburst - (wordadr & (maxburst - 1));
            if (count > i) count = i; // page boundry -> maximum burst
            int devadr = address + ((wordadr >> 7) & 14);
            I2C i2c = new I2C(devadr);
            i2c.write(sendadr, data, startindex, count);
            startindex += count;
            wordadr += count;
            written += count;
        }
        return written;
    }

}
