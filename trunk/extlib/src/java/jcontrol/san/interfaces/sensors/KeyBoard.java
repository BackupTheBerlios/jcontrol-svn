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

/**
 * Interface for a keyboard sensor.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:36 $
 */
public interface KeyBoard extends Sensor {

    /**
     * Return the actual pressed key or -1 if no key is pressed.
     * 
     * @return the actual pressed key or -1 if no key is pressed.
     */
    int getKey();

    /**
     * Returns the {@link String} from the last pressed key.
     * 
     * @return the {@link String} from the last pressed key.
     */
    String getKeyString();

    /**
     * Return the last pressed key or -1 if no key is pressed.
     * 
     * @return the last pressed key or -1 if no key is pressed.
     */
    int getLastKey();

    /**
     * Wait for a key and return the key value.
     * 
     * @return the key value.
     */
    int waitForKey();

}
