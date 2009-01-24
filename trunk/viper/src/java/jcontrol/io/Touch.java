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

import jcontrol.comm.I2C;

/**
 * Class for a touch device. At the I²C-Address <code>0x90</code>.
 * 
 * @author Marcus Timmermann
 * @author Stephan Knoke
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class Touch extends I2C implements IPointingDevice {

    /** The display height. Value: 64. */
    private static final int DISPLAY_HEIGHT = 64;

    /** The display width. Value: 126. */
    private static final int DISPLAY_WIDTH = 128;

    /** Maximum x coordinate if the touch device. Value: 230. */
    private static final int X_MAX = 230;

    /** Minimum x coordinate if the touch device. Value: 25. */
    private static final int X_MIN = 25;

    /** Maximum y coordinate if the touch device. Value: 169. */
    private static final int Y_MAX = 169;

    /** Minimum y coordinate if the touch device. Value: 18. */
    private static final int Y_MIN = 18;

    /** Byte buffer. Size 1 */
    byte[] buf = new byte[1];

    /** x position of the last touch. */
    private int m_x;

    /** y position of the last touch. */
    private int m_y;

    /**
     * Creates a new touch with the default address <code>0x90</code> of the I²C-Device.
     * 
     * @throws IOException
     */
    public Touch() throws IOException {
        super(0x90);
        write((char) 0x08);
        GPIO.setMode(0, GPIO.PULLUP);
    }

    /**
     * Creates a new touch with the given address of the I²C-Device.
     * 
     * @param address the bus address of the I²C-Device.
     * @throws IOException
     */
    public Touch(int address) throws IOException {
        super(address);
        write((char) 0x08);
        GPIO.setMode(0, GPIO.PULLUP);
    }

    public void calibrate(int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3) {}

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.io.IPointingDevice#getRawX()
     */
    public int getRawX() {
        return m_x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.io.IPointingDevice#getRawY()
     */
    public int getRawY() {
        return m_y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.io.IPointingDevice#getX()
     */
    public int getX() {
        return m_x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.io.IPointingDevice#getY()
     */
    public int getY() {
        return m_y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.io.IPointingDevice#isPressed()
     */
    public boolean isPressed() {
        if (!GPIO.getState(8)) {
            if (buf == null) buf = new byte[1];
            try {
                buf[0] = (byte) 0xDa; // command value
                read(buf, buf, 0, 1);
                m_y = ((buf[0] & 0xff) - Y_MIN) * DISPLAY_HEIGHT / (Y_MAX - Y_MIN);
                // m_y = Math.scale((buf[0] & 0xff) - Y_MIN, Y_MAX - Y_MIN, DISPLAY_HEIGHT);

                buf[0] = (byte) 0xCa; // command value
                read(buf, buf, 0, 1);
                m_x = ((buf[0] & 0xff) - X_MIN) * DISPLAY_WIDTH / (X_MAX - X_MIN);
                // m_x = Math.scale((buf[0] & 0xff) - X_MIN, X_MAX - X_MIN, DISPLAY_WIDTH);
            } catch (IOException e) {
                return false;
            }
            return true;
        } else {
            buf = null;
            return false;
        }
    }
}
