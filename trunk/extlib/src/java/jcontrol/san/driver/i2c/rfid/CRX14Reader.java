/*
 * Copyright (C) 2008 The JControl Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package jcontrol.san.driver.i2c.rfid;

import java.io.IOException;

import jcontrol.comm.I2C;
import jcontrol.lang.ThreadExt;
import jcontrol.san.interfaces.sensors.RFIDReader;
import jcontrol.san.interfaces.sensors.RFIDTransponder;

/**
 * RFID reader with crx14 using the SM (I<sup>2</sup>C) bus.
 * 
 * @see <a href="doc-files/an2091.pdf">an2091 datasheet</a>
 * @see <a href="doc-files/crx14.pdf">crx14 datasheet</a>
 * @see <a href="doc-files/crx14_preliminary.pdf">crx14 preliminary datasheet</a>
 * @see <a href="doc-files/crx14_article.pdf">crx14 german article (Elektronik Informationen Nr. 10-2004)</a>
 * @see <a href="doc-files/RFID_with_JControl.xhtml">RFID (german)</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author DOMOLOGIC Home Automation GmbH 2006
 * @version $Revision:36 $
 */
public class CRX14Reader extends I2C implements RFIDReader {

    /**
     * wait.
     * 
     * @param delay in ms
     */
    private static final void delay(int delay) {
        try {
            ThreadExt.sleep(delay);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * Creates a new object.
     * 
     * @param address the i2c address
     */
    public CRX14Reader(int address) {
        super(address);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDReader#completion()
     */
    public void completion() throws IOException {
        byte cmd[] = new byte[] { (byte) 0x01 };
        byte data[] = new byte[] { (byte) 0x01, (byte) 0x0f };
        write(cmd, data, 0, 2);
        delay(20);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDReader#initiate(jcontrol.san.interfaces.sensors.RFIDTransponder)
     */
    public boolean initiate(RFIDTransponder transponder) throws IOException {
        byte cmd[] = new byte[] { (byte) 0x01 };
        byte data[] = new byte[] { (byte) 0x02, (byte) 0x06, (byte) 0x00 };

        write(cmd, data, 0, 3);
        delay(10);
        read(cmd, data, 0, 2);

        if (data[0] != 1)
            return false;

        transponder.setReader(this);
        transponder.setChipId((data[1] & 0xff) % 16);

        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDReader#power(boolean)
     */
    public void power(boolean power) throws IOException {

        // register
        byte cmd[] = { 0 };
        byte data[] = new byte[1];

        if (power) {
            // RF OUT is ON
            data[0] = 0x10;
        } else {
            // RF OUT is OFF
            data[0] = 0x00;
        }
        write(cmd, data, 0, 1);
        delay(30);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDReader#read(int)
     */
    public byte[] read(int adress) throws IOException {
        byte cmd[] = new byte[] { (byte) 0x01 };
        byte data[] = new byte[] { (byte) 0x02, (byte) 0x08, (byte) (adress & 0x0f) };

        write(cmd, data, 0, 3);
        delay(10);
        read(cmd, data, 0, 3);

        if (data[0] <= 0)
            return new byte[0];

        byte[] retval = new byte[data[0]];
        for (int i = 0; i < retval.length; i++) {
            if (i + 1 < data.length) {
                retval[i] = data[i + 1];
            }
        }
        return retval;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDReader#reset()
     */
    public void reset() throws IOException {
        byte cmd[] = new byte[] { (byte) 0x01 };
        byte data[] = new byte[] { (byte) 0x01, (byte) 0x0c };
        write(cmd, data, 0, 2);
        delay(30);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDReader#select(jcontrol.san.interfaces.sensors.RFIDTransponder)
     */
    public boolean select(RFIDTransponder transponder) throws IOException {
        byte cmd[] = new byte[] { (byte) 0x01 };
        byte data[] = new byte[] { (byte) 0x02, (byte) 0x0e, (byte) transponder.getChipId() };

        write(cmd, data, 0, 3);
        delay(10);
        read(cmd, data, 0, 2);

        if (data[0] == 0) {
            return false;
        }
        if (((data[1] & 0xff) % 16) != transponder.getChipId()) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDReader#write(int, byte[])
     */
    public void write(int adress, byte[] data) throws IOException {
        byte cmd[] = { (byte) 0x01, (byte) 0x04, (byte) 0x09, (byte) adress, data[0], data[1] };
        write(cmd, data, 0, 0);
    }
}
