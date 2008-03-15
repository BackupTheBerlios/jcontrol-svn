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
 * Interface for all sensors.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public interface Sensor {

    /**
     * Returns the exponent of the value to show.
     * 
     * @return the exponent of the value to show.
     */
    public int getExponent();

    /**
     * Returns the maximum value of this value sensor.
     * 
     * @return the maximum value of this value sensor.
     */
    int getMax();

    /**
     * Returns the minimum value of this value sensor.
     * 
     * @return the minimum value of this value sensor.
     */
    int getMin();

    /**
     * Returns a unit string for the graphical representation of the value.
     * 
     * @return a unit string for the graphical representation of the value.
     */
    String getUnit();

    /**
     * Returns the last measured value.
     * 
     * @return the last measured value.
     */
    int getValue();

    /**
     * Returns the last measured value as a {@link String} with the unit.
     * 
     * @return the last measured value as a {@link String} with the unit.
     */
    String toString();

    /**
     * Updates the measured value.
     */
    void updateValue();
}
