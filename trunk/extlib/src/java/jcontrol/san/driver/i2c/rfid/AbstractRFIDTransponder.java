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

import jcontrol.san.interfaces.sensors.RFIDReader;
import jcontrol.san.interfaces.sensors.RFIDTransponder;

/**
 * Abstract class for a {@link RFIDTransponder}.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:36 $
 */
public abstract class AbstractRFIDTransponder implements RFIDTransponder {

    /**
     * The chip ID:
     */
    private int chipId;

    /**
     * The {@link RFIDReader}.
     */
    private RFIDReader reader;

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDTransponder#getChipId()
     */
    public int getChipId() {
        return chipId;
    }

    /**
     * Read from the reader.
     * 
     * @param address the adresse
     * @return the data as byte array.
     * @throws IOException if an error occurred.
     */
    protected byte[] readInternal(int address) throws IOException {
        return reader.read(address);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDTransponder#reset()
     */
    public void reset() throws IOException {
        reader.reset();
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDTransponder#setChipId(int)
     */
    public void setChipId(int id) {
        this.chipId = id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.RFIDTransponder#setReader(jcontrol.san.interfaces.sensors.RFIDReader)
     */
    public void setReader(RFIDReader reader) {
        this.reader = reader;
    }

    /**
     * Write data.
     * 
     * @param address the address
     * @param buffer the buffer to write.
     * @throws IOException if an error occurred.
     */
    protected void writeInternal(int address, byte[] buffer) throws IOException {
        reader.write(address, buffer);
    }
}
