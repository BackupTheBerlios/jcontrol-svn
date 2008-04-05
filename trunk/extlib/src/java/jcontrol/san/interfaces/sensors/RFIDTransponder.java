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

package jcontrol.san.interfaces.sensors;

import java.io.IOException;

/**
 * Interface for a RFID transponder.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:36 $
 */
public interface RFIDTransponder {

    /**
     * Returns the chip id.
     * 
     * @return the chip id.
     */
    int getChipId();

    /**
     * Returns the UID a byte array.
     * 
     * @return the UID a byte array.
     * @throws IOException if an error occurred.
     */
    byte[] getUID() throws IOException;

    /**
     * Read data.
     * 
     * @param address read from addess.
     * @return the data as byte array.
     * @throws IOException if an error occured.
     */
    byte[] read(int address) throws IOException;

    /**
     * Reset the tranponder.
     * 
     * @throws IOException if an error occurred.
     */
    void reset() throws IOException;

    /**
     * Set the chip ID.
     * 
     * @param id the ID.
     */
    void setChipId(int id);

    /**
     * Set the {@link RFIDReader}.
     * 
     * @param reader the {@link RFIDReader}.
     */
    void setReader(RFIDReader reader);

    /**
     * Return the memory size.
     * 
     * @return the memory size.
     */
    int size();

    /**
     * Returns the {@link String} represented the class.
     * 
     * @return the {@link String} represented the class.
     */
    String toString();

    /**
     * Write data.
     * 
     * @param address write at address.
     * @param buffer the buffer to write.
     * @throws IOException if an error occurred.
     */
    void write(int address, byte[] buffer) throws IOException;
}