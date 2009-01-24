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

/**
 * Classes for pointing devices must implementing this interface.
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public interface IPointingDevice {

    /**
     * /** used to calibrate the touch screen by the 4 corner-points of touch
     * 
     * <pre>
     *  (x0,y0)                  (x1,y1)
     *    +------------------------+
     *    |                        |
     *    |                        |
     *    |                        |
     *    |                        |
     *    |                        |
     *    +------------------------+
     *  (x3,y3)                  (x2,y2)
     * </pre>
     * 
     * @param x0 x-coordinate top left corner
     * @param y0 y-coordinate top left corner
     * @param x1 x-coordinate top right corner
     * @param y1 y-coordinate top right corner
     * @param x2 x-coordinate lower right corner
     * @param y2 y-coordinate lower right corner
     * @param x3 x-coordinate lower left corner
     * @param y3 y-coordinate lower left corner
     */
    public void calibrate(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3);

    /**
     * Returns the raw x-coordinate of the pointing device.
     * 
     * @return the raw x-coordinate
     */
    public int getRawX();

    /**
     * Returns the raw y-coordinate of the pointing device.
     * 
     * @return the raw y-coordinate
     */
    public int getRawY();

    /**
     * Returns the x-coordinate of the (last) touch.
     * 
     * @return the x-coordinate
     */
    public int getX();

    /**
     * Returns the y-coordinate of the (last) touch.
     * 
     * @return the y-coordinate
     */
    public int getY();

    /**
     * Returns if the pointing device is pressed.
     * 
     * @return true if the device is pressed, false otherwise
     */
    public boolean isPressed();

}
