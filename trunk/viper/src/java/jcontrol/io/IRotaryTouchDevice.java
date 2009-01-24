/*
 * $Id$
 * 
 * Copyright (C) 2005-2009 The JControl Group and individual authors listed below
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
package jcontrol.io;

import java.io.IOException;

/**
 * A class for a rotary touch sensor device (like MPR083) must implement this interface.
 * 
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public interface IRotaryTouchDevice {

    /** Position of the selected sensor field in the status register. */
    public static final int FLAG_STATUS_CP_MASK = (0xF); // Current Position Touched

    /** Position of the touched flag in the status register. */
    public static final int FLAG_STATUS_TOUCHED = (1 << 4); // TOUCH DETECTION (0=not detected,

    // 1=touch detected)

    /**
     * Returns the rotary state of the sensor.
     * 
     * @return a status register containing the state of the sensor
     * @throws IOException
     */
    public int getRotaryState() throws IOException;

    /**
     * Returns the number of the touched sensor field starting with 0. <br>
     * If no field is touched the MPR083 also returns 0. <br>
     * So this information must be used with the rotary state information which contains information
     * if a field is touched.
     * 
     * @return the number of the touched sensor field.
     * @throws IOException
     */
    public int getTouchedRotaryField() throws IOException;

}
