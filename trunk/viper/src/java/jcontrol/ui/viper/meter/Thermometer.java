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
import jcontrol.lang.Math;

/**
 * <p>
 * The class <code>Thermometer</code> implements a classic thermometer. The final size of the
 * thermometer depends on the width-to-height ratio given by the constructor or the
 * setBounds-method. The thermometer is drawn that it completely fits into its bounding box. A
 * thermometer without any caption has a width-to-height ratio of 2 to 7 plus text height. Any
 * caption will be drawn on the right side of the fluid column, and with a caption, the
 * width-to-height ratio will change. So, if the thermometer is too small or not visible at all, you
 * should try to increase the width first.
 * </p>
 * 
 * <pre>
 *       __
 *      /  \ caption max
 *      |  |
 *      |XX|
 *      |XX|
 *      |XX| caption min
 *     /XXXX\
 *    |XXXXXX|
 *     \XXXX/
 *      ¯¯¯¯
 *    value [unit]
 * </pre>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @since Viper 1.0
 * @version $Revision$
 */
public class Thermometer extends AbstractMeter {

    public static final int NONE = 0;

    public static final int STYLE_SHOW_NUMERIC_VALUE = 4;

    /** Optional caption text */
    private String m_captionMin, m_captionMax;

    private int m_lastNumericWidth;

    /** Internal drawing parameters for of thermometer */
    private int m_columnHeight;

    private int m_reservoirRadius;

    private int m_reservoirCenter;

    private int m_reservoirTop;

    /**
     * Constructs a new thermometer.
     * 
     * @param x The upper left x coordinate.
     * @param y The upper left y coordinate.
     * @param width The width
     * @param height The height
     * @param style STYLE_SHOW_NUMERIC_VALUE or NONE
     */
    public Thermometer(int x, int y, int width, int height, int style) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        m_style = style;
    }

    /**
     * Draws the upper part of an open circle needed for the top if the thermometer.
     * 
     * @param x1 center x
     * @param y1 center y
     * @param radius the radius
     */
    private void drawArc(Graphics g, int x1, int y1, int radius) {
        if (g == null) return;
        int x2 = 0;
        int y2 = radius;
        int error = 1 - radius;
        while (x2 <= y2) {
            g.setPixel(x1 - x2, y1 - y2);
            g.setPixel(x1 - y2, y1 - x2);
            g.setPixel(x1 + x2, y1 - y2);
            g.setPixel(x1 + y2, y1 - x2);
            x2++;
            if (error >= 0) {
                y2--;
                error -= (y2 << 1);
            }
            error += (x2 << 1) + 1;
        }
    }

    /**
     * Draws the Thermometer's value
     */
    private void drawValue(Graphics g) {
        // calculate some drawing parameters
        int columnRadius = (m_reservoirRadius + 1) >> 1;
        int xoff1 = x + m_reservoirCenter - columnRadius;
        // left border of the fluid column
        int xoff2 = x + m_reservoirCenter + columnRadius;
        // right border of the fluid column
        int yoff = y + m_reservoirTop;

        if (m_min < m_max) {
            yoff -= Math.scale(m_value - m_min, m_max - m_min, m_reservoirTop - columnRadius);
            // fluid y-position
        } else if (m_min > m_max) {
            yoff -= Math.scale(m_value - m_max, m_min - m_max, m_reservoirTop - columnRadius);
            // fluid y-position
        }
        g.clearRect(xoff1 + 1, y + columnRadius, xoff2 - xoff1 - 1, yoff - (y + columnRadius));
        // clear the empty portion of the fluid column
        if (m_reservoirRadius <= 4) {
            g
                    .fillRect(xoff1 + 1, yoff, xoff2 - xoff1 - 1, y + m_reservoirTop - yoff
                            + columnRadius);
            // draw the fluid
        } else {
            int xoff3 = (m_reservoirRadius - 2) >> 2;
            // relative position of the white reflection line
            g.drawLine(xoff1 + 1, yoff, xoff2 - 1, yoff);
            g.fillRect(xoff1 + 1, yoff + 1, xoff3, y + m_reservoirTop - yoff + columnRadius);
            // draw the fluid
            g.clearRect(xoff1 + xoff3 + 1, yoff + 1, 1, y + m_reservoirTop - yoff);
            // with reflection line
            g.fillRect(xoff1 + xoff3 + 2, yoff + 1, xoff2 - xoff1 - xoff3 - 2, y + m_reservoirTop
                    - yoff + columnRadius);
        }
        // draw value and unit of numeric display
        if ((m_style & STYLE_SHOW_NUMERIC_VALUE) != 0) {
            int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
            String v = String.valueOf(m_value >= 0 ? m_value : -m_value);
            if (v.length() > m_digits) v = v.substring(0, m_digits);
            if (decimals > 0) {
                // insert comma if m_decimals > 0
                int comma = v.length() - decimals;
                if (comma <= 0) {
                    v = "00000".concat(v).substring(comma + 4, v.length() + 5);
                    comma = 1;
                    if (v.length() > m_digits) v = v.substring(0, m_digits);
                }
                v = v.substring(0, comma).concat(".").concat(v.substring(comma, v.length()));
            }
            // set minus sign if negative
            if (m_value < 0) v = "-".concat(v);
            // append unit string if available
            if (m_dataProducer != null && m_dataProducer.getUnit() != null)
                v = v.concat(m_dataProducer.getUnit());
            g.setFont(font);
            int numericWidth = g.getTextWidth(v);
            if (numericWidth > width) {
                numericWidth = width;
            }
            // calculate position of the numeric display
            xoff1 = m_reservoirCenter - (numericWidth >> 1);
            if (xoff1 < 0) xoff1 = 0;
            // yoff is the upper display line of the value text
            yoff = height - g.getFontHeight();
            // if new value string is smaller than last one, delete space before
            // and behind it
            int padWidth = (m_lastNumericWidth - numericWidth + 3) >> 1;
            if (padWidth > 0) {
                if (xoff1 > padWidth)
                    g.clearRect(x + xoff1 - padWidth, y + yoff, padWidth, g.getFontHeight());
                g.clearRect(x + xoff1 + numericWidth - 1, y + yoff, padWidth, g.getFontHeight());
            }
            g.drawString(v, x + xoff1, y + yoff, numericWidth, g.getFontHeight(), 0, 0);
            m_lastNumericWidth = numericWidth;
            g.setFont(null);
        }
    }

    /**
     * Draws a filled circle using Bresenham's circle algorithm
     * 
     * @param x1 center x
     * @param y1 center y
     * @param radius the radius
     */
    private void fillCircle(Graphics g, int x1, int y1, int radius) {
        if (g == null) return;
        int x2 = 0;
        int y2 = radius;
        int error = 1 - radius;
        while (x2 <= y2) {
            g.drawLine(x1 + x2, y1 - y2, x1 - x2, y1 - y2);
            g.drawLine(x1 + y2, y1 - x2, x1 - y2, y1 - x2);
            g.drawLine(x1 + x2, y1 + y2, x1 - x2, y1 + y2);
            g.drawLine(x1 + y2, y1 + x2, x1 - y2, y1 + x2);
            x2++;
            if (error >= 0) {
                y2--;
                error -= (y2 << 1);
            }
            error += (x2 << 1) + 1;
        }
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
                g.setFont(font);
                // reset last values
                m_lastNumericWidth = 0;
                // set drawing parameters
                m_columnHeight = height - 2;
                if ((m_style & STYLE_SHOW_NUMERIC_VALUE) != 0) m_columnHeight -= g.getFontHeight();
                // radius of the fluid reservoir
                m_reservoirRadius = height / 7;
                // check if component is wide enough
                if (width < (m_reservoirRadius << 1)) {
                    m_reservoirRadius = width >> 1;
                }
                // determine maximum width of the caption
                int captionWidth = 0;
                if (m_captionMin != null) captionWidth = g.getTextWidth(m_captionMin);
                if (m_captionMax != null) {
                    if (g.getTextWidth(m_captionMax) > captionWidth)
                        captionWidth = g.getTextWidth(m_captionMax);
                }
                // columnRadius is the radius of the fluid column
                int columnRadius = ((m_reservoirRadius + 1) >> 1) + 2;
                // determine the center position of fluid reservoir
                m_reservoirCenter = width >> 1;
                if (m_reservoirCenter < columnRadius + captionWidth) {
                    // move the thermometer to the left border of the bounds
                    m_reservoirCenter = width - captionWidth - columnRadius;
                    if (m_reservoirCenter < m_reservoirRadius) {
                        // if the caption doesnt fit, make the fluid reservoir
                        // smaller
                        if (m_reservoirRadius + columnRadius + captionWidth > width) {
                            m_reservoirRadius = (((width - captionWidth - 2) << 1) - 1) / 3;
                            // check for minimum radius of reservoir
                            if (m_reservoirRadius < 2) {
                                m_reservoirRadius = width < 4 ? width >> 1 : 2;
                            }
                        }
                        m_reservoirCenter = m_reservoirRadius;
                    }
                }
                m_reservoirTop = m_columnHeight - (m_reservoirRadius << 1);
                // draw the thermometer
                columnRadius = (m_reservoirRadius + 1) >> 1;
                fillCircle(g, x + m_reservoirCenter, y + m_columnHeight - m_reservoirRadius,
                        m_reservoirRadius);
                // draw the fluid reservoir
                drawArc(g, x + m_reservoirCenter, y + columnRadius, columnRadius);
                g.drawLine(x + m_reservoirCenter - columnRadius, y + columnRadius, x
                        + m_reservoirCenter - columnRadius, y + m_columnHeight - m_reservoirRadius);
                // draw the fluid column
                g.drawLine(x + m_reservoirCenter + columnRadius, y + columnRadius, x
                        + m_reservoirCenter + columnRadius, y + m_columnHeight - m_reservoirRadius);
                // draw the caption
                columnRadius += m_reservoirCenter + 2;
                if (columnRadius <= width) {
                    if (m_captionMax != null)
                        g.drawString(m_captionMax, x + columnRadius, y, width - columnRadius, g
                                .getFontHeight(), 0, 0);
                    if (m_captionMin != null)
                        g.drawString(m_captionMin, x + columnRadius, y + m_reservoirTop
                                - g.getFontHeight() + 3, width - columnRadius, g.getFontHeight(),
                                0, 0);
                }
                g.setFont(null);
            default:
                drawValue(g);
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Sets a caption text which is displayed for the min and max value at the thermometer.
     * 
     * @param captionMin Text to display at the bottom
     * @param captionMax Text to display at the top
     */
    public void setCaption(String captionMin, String captionMax) {
        // copy the given caption strings to the local object
        if ((captionMin != null) && captionMin.equals("")) {
            m_captionMin = null;
        } else {
            m_captionMin = captionMin;
        }
        if ((captionMax != null) && captionMax.equals("")) {
            m_captionMax = null;
        } else {
            m_captionMax = captionMax;
        }
        redrawInternalAndParent();
    }

}