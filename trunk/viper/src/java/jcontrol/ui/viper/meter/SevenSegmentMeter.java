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
package jcontrol.ui.viper.meter;

import jcontrol.io.Graphics;

/**
 * <p>
 * This class <code>SevenSegmentMeter.class</code> provides a seven-segment-based lcd-style counter
 * with a variable number of digits. The digits of the <code>SevenSegmentMeter</code> are freely
 * scalable.
 * </p>
 * 
 * <pre>
 *    ______________
 *   |  __  __  __  |
 *   | |__||__||__| |
 *   | |__||__||__| |
 *   |______________|
 * 
 * 
 * </pre>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @since Viper 1.0
 * @version $Revision$
 */
public class SevenSegmentMeter extends AbstractMeter {

    private static final String SEGMENTS = "\u003f\u0018\u0076\u007c\u0059\u006d\u006f\u0038\u007f\u007d";

    /** The last state of the segments */
    private byte[] m_lastSegments;

    /** Internal parameters representing width & height */
    private int m_digitWidth;

    private int m_digitHeight;

    /**
     * Creates a new SevenSegmentMeter.
     * <p>
     * NOTE: The size of the seven-segment digits will depend on the number of desired digits and
     * the size of the bounding box.
     * </p>
     * 
     * @param x The x coordinate for the upper left of the bounding box.
     * @param y The y coordinate for the upper left of the bounding box.
     * @param width The width of the bounding box.
     * @param height The height of the bounding box.
     */
    public SevenSegmentMeter(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        // array to save last state of segments
        m_lastSegments = new byte[m_digits];
    }

    /**
     * Draws a polygon, scaled by m_digitHeight.
     * 
     * @param xpoints The x coordinates
     * @param ypoints The y coordinates
     * @param xoff The x offset
     * @param yoff The y offset
     */
    private void drawPolygon(Graphics g, int xoff, int yoff, int[] xpoints, int[] ypoints,
            int npoints) {
        for (int i = 0; i < npoints - 1; i++) {
            g.drawLine(xoff + (((xpoints[i] * m_digitHeight) >> 6)), yoff
                    + (((ypoints[i] * m_digitHeight) >> 6)), xoff
                    + (((xpoints[i + 1] * m_digitHeight) >> 6)), yoff
                    + (((ypoints[i + 1] * m_digitHeight) >> 6)));
        }
        g.drawLine(xoff + (((xpoints[0] * m_digitHeight) >> 6)), yoff
                + (((ypoints[0] * m_digitHeight) >> 6)), xoff
                + (((xpoints[npoints - 1] * m_digitHeight) >> 6)), yoff
                + (((ypoints[npoints - 1] * m_digitHeight) >> 6)));
    }

    /**
     * Draws seven segements, given as bit mask
     * 
     * @param segments The seven segments
     * @param x The x coordinate
     * @param y The y coordinate
     */
    private void drawSegments(Graphics g, int segments, int x, int y) {
        if (segments != 0) {
            if ((segments & 0x1) != 0)
                drawPolygon(g, x, y, new int[]{2, 0, 0, 3, 6, 6}, new int[]{2, 4, 30, 31, 28, 7}, 6);
            if ((segments & 0x2) != 0)
                drawPolygon(g, x, y, new int[]{2, 0, 0, 2, 6, 6},
                        new int[]{33, 35, 58, 60, 57, 35}, 6);
            if ((segments & 0x4) != 0)
                drawPolygon(g, x, y, new int[]{8, 4, 6, 34, 36, 32}, new int[]{58, 62, 63, 63, 62,
                        58}, 6);
            if ((segments & 0x8) != 0)
                drawPolygon(g, x, y, new int[]{33, 33, 37, 39, 39, 37}, new int[]{36, 56, 61, 60,
                        34, 33}, 6);
            if ((segments & 0x10) != 0)
                drawPolygon(g, x, y, new int[]{33, 33, 37, 39, 39, 36}, new int[]{6, 27, 31, 29, 6,
                        3}, 6);
            if ((segments & 0x20) != 0)
                drawPolygon(g, x, y, new int[]{3, 7, 31, 34, 31, 31}, new int[]{0, 6, 6, 2, 0, 0},
                        6);
            if ((segments & 0x40) != 0)
                drawPolygon(g, x, y, new int[]{8, 5, 8, 32, 35, 32}, new int[]{29, 32, 34, 34, 32,
                        29}, 6);
        }
    }

    /**
     * Draw a new value
     * 
     * @param segments The seven segments
     * @param x The x coordinate
     * @param y The y coordinate
     */
    protected void drawValue(Graphics g) {
        int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
        int xoff = x + width - ((width - 4 - (m_digits * m_digitWidth)) >> 1)
                + (decimals > 0 ? m_digitWidth >> 2 : 0);
        int yoff = y + ((height - m_digitHeight) >> 1);
        int v = m_value >= 0 ? m_value : -m_value;
        g.setDrawMode(Graphics.XOR);
        int i = 0;
        int segments;
        for (; i < m_digits && ((v != 0) || i <= decimals); i++) {
            // skip decimal point
            if ((decimals > 0) && (i == decimals)) {
                xoff -= m_digitWidth >> 2;
            }
            // draw segments
            segments = SEGMENTS.charAt(v % 10);
            xoff -= m_digitWidth;
            drawSegments(g, segments ^ m_lastSegments[i], xoff, yoff);
            m_lastSegments[i] = (byte) segments;
            v /= 10;
        }
        if (i < m_digits) {
            // draw minus sign
            segments = m_value >= 0 ? 0x0000 : 0x0040;
            xoff -= m_digitWidth;
            drawSegments(g, segments ^ m_lastSegments[i], xoff, yoff);
            m_lastSegments[i] = (byte) segments;
            i++;
        }
        // clear digits before number
        for (; i < m_digits; i++) {
            // clear position
            segments = m_lastSegments[i];
            xoff -= m_digitWidth;
            drawSegments(g, segments, xoff, yoff);
            m_lastSegments[i] = 0;
        }
        g.setDrawMode(Graphics.NORMAL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public synchronized void paint(Graphics g) {
        switch ((state & STATE_DIRTY_MASK)) {
            case STATE_DIRTY_PAINT_ALL:
                g.clearRect(x, y, width, height);
            case STATE_DIRTY_REPAINT:
                // reset last values
                for (int i = 0; i < m_digits; i++) {
                    m_lastSegments[i] = 0;
                }
                int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
                // set drawing parameters
                if (width <= height * (5 * m_digits) / 8) {
                    // width defines the digit size
                    m_digitWidth = (width - 4 - m_digits) / m_digits;
                    if (decimals > 0) m_digitWidth -= m_digitWidth / (4 * m_digits);
                    m_digitHeight = m_digitWidth * 8 / 5;
                    // increment to get more space between digits
                    m_digitWidth++;
                } else {
                    // height defines the digit size
                    m_digitHeight = height - 4;
                    m_digitWidth = m_digitHeight * 5 / 8 + 1; // (height-4)*5/8;
                    if (decimals > 0) m_digitWidth -= m_digitWidth / (4 * m_digits);
                }
                // draw the decimal's point
                if (decimals > 0) {
                    int xoff = x + width - ((width - 4 - (m_digits * m_digitWidth)) >> 1)
                            - ((decimals + 1) * m_digitWidth);
                    int yoff = y + ((height - m_digitHeight) >> 1);
                    drawPolygon(g, xoff, yoff, new int[]{43, 49, 49, 43},
                            new int[]{57, 57, 63, 63}, 4);
                }
            default:
                drawValue(g);
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.meter.AbstractMeter#setRange(int, int)
     */
    @Override
    public void setRange(int min, int max) {
        super.setRange(min, max);
        m_lastSegments = new byte[m_digits];
    }
}