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
 * Interface for a RFID reader.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:36 $
 */
public interface RFIDReader {

    /**
     * Complete the reader.
     * 
     * @throws IOException if an error occurred.
     */
    public void completion() throws IOException;

    /**
     * Initiate the transponder.
     * 
     * @param transponder the transponder.
     * @return
     * @throws IOException
     */
    public boolean initiate(RFIDTransponder transponder) throws IOException;

    /**
     * Turns on or off the power of this reader depending on the value of parameter .
     * 
     * @param power If true turns the power on, otherwise turns the power off
     */
    public void power(boolean power) throws IOException;

    /**
     * Read data.
     * 
     * @param address read at address
     * @return the data as byte array.
     * @throws IOException if an error occurred.
     */
    public byte[] read(int address) throws IOException;

    /**
     * Reset.
     * 
     * @throws IOException if an error occurred.
     */
    public void reset() throws IOException;

    /**
     * Select a transponder.
     * 
     * @param transponder the transponder to select.
     * @return <code>true</code>, if the transponder can be selected, otherwise
     *         <code>false</code>.
     * @throws IOException if an error occurred.
     */
    public boolean select(RFIDTransponder transponder) throws IOException;

    /**
     * Write data.
     * 
     * @param address write at address.
     * @param buffer the buffer to write
     * @return
     * @throws IOException
     */
    public void write(int address, byte[] buffer) throws IOException;
}
