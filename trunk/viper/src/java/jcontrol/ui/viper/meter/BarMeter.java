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
 * This class implements a bar graph, used for analog output in equalizers etc.
 * </p>
 * 
 * <pre>
 *    ______________                        ______________
 *   |XXXXXXX       | Horizontal, solid    |IIIIII        | Horizontal, line
 *    ¯¯¯¯¯¯¯¯¯¯¯¯¯¯                        ¯¯¯¯¯¯¯¯¯¯¯¯¯¯
 *        ___                                 ___
 *       |   |                               |   |
 *       |XXX|                               |===|
 *       |XXX| Vertical, solid               |===| Vertical, line
 *       |XXX|                               |===|
 *       |XXX|                               |===|
 *        ¯¯¯                                 ¯¯¯
 * </pre>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @since Viper 1.0
 * @version $Revision$
 */
public class BarMeter extends AbstractMeter {

    public static final int NONE = 0;

    public static final int STYLE_SHOW_NUMERIC_VALUE = 4;

    /** Line style */
    public static final int STYLE_FILL_LINE = 1;

    /** Solid style */
    public static final int STYLE_FILL_SOLID = 0;

    /** Vertical bar graph */
    public static final int STYLE_ORIENTATION_VERTICAL = 2;

    /** Horizontal bar graph */
    public static final int STYLE_ORIENTATION_HORIZONTAL = 0;

    /** Optional caption text */
    private String m_captionMin, m_captionMax;

    private int m_lastNumericWidth;

    /** Internal drawing parameters of the bar meter */
    private int m_barWidth;

    private int m_barHeight;

    /**
     * Constructs a new bar graph.
     * 
     * @param x The upper left x-coordinate.
     * @param y The upper left y-coordinate.
     * @param width The width.
     * @param height The height.
     * @param style STYLE_FILL_LINE or STYLE_FILL_SOLID, STYLE_ORIENTATION_VERTICAL or
     *        STYLE_ORIENTATION_HORIZONTAL, STYLE_SHOW_NUMERIC_VALUE.
     */
    public BarMeter(int x, int y, int width, int height, int style) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        m_style = style;
    }

    /**
     * Draws the BarMeter with the current value.
     */
    private void drawValue(Graphics g) {
        if ((m_style & STYLE_ORIENTATION_VERTICAL) == STYLE_ORIENTATION_VERTICAL) {
            // draw vertical bar meter
            int value;
            if (m_min < m_max) {
                value = Math.scale(m_value - m_min, m_max - m_min, m_barHeight - 2);
            } else if (m_max > m_min) {
                value = Math.scale(m_min - m_value, m_min - m_max, m_barHeight - 2);
            } else {
                value = 0;
            }
            int max = y + m_barHeight - 1;
            int min = max - value;
            if (((m_style & STYLE_FILL_LINE) == STYLE_FILL_LINE) && (m_barWidth > 3)) {
                // fill lined
                for (int i = max; i >= min; i--) {
                    if ((i % 2) != 0) {
                        g.drawLine(x + 2, i, x + m_barWidth - 3, i);
                    }
                }
            } else {
                // fill solid
                g.fillRect(x + 1, min, m_barWidth - 2, m_barHeight - min + y);
            }
            int h = m_barHeight - value - 2;
            if (h > 0) {
                g.clearRect(x + 1, y + 1, m_barWidth - 2, h);
            }
        } else {
            // draw horizontal bar meter
            int value;
            if (m_min < m_max) {
                value = Math.scale(m_value - m_min, m_max - m_min, m_barWidth - 2);
            } else if (m_max > m_min) {
                value = Math.scale(m_min - m_value, m_min - m_max, m_barWidth - 2);
            } else {
                value = 0;
            }
            if (((m_style & STYLE_FILL_LINE) == STYLE_FILL_LINE) && (m_barHeight > 3)) {
                // fill lined
                for (int i = x + 1; i <= x + value; i++) {
                    if ((i % 2) != 0) {
                        g.drawLine(i, y + 2, i, y + m_barHeight - 3);
                    }
                }
            } else {
                // fill solid
                g.fillRect(x + 1, y + 1, value, m_barHeight - 2);
            }
            int w = m_barWidth - value - 2;
            if (w > 0) {
                g.clearRect(x + value + 1, y + 1, w, m_barHeight - 2);
            }
        }
        // draw value and unit of numeric display
        if ((m_style & STYLE_SHOW_NUMERIC_VALUE) != 0) {
            String v = String.valueOf(m_value >= 0 ? m_value : -m_value);
            if (v.length() > m_digits) v = v.substring(0, m_digits);
            int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
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
            int xoff = (m_barWidth - numericWidth) >> 1;
            if (xoff < 0) xoff = 0;
            // yoff is the upper display line of the value text
            int yoff = height - g.getFontHeight();
            // if new value string is smaller than last one, delete space before and behind it
            int padWidth = (m_lastNumericWidth - numericWidth + 3) >> 1;
            if (padWidth > 0) {
                if (xoff > padWidth)
                    g.clearRect(x + xoff - padWidth, y + yoff, padWidth, g.getFontHeight());
                g.clearRect(x + xoff + numericWidth - 1, y + yoff, padWidth, g.getFontHeight());
            }
            g.drawString(v, x + xoff, y + yoff, numericWidth, g.getFontHeight(), 0, 0);
            m_lastNumericWidth = numericWidth;
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
                // determine maximum width of the caption
                int captionWidth = 0;
                if (m_captionMin != null) captionWidth = g.getTextWidth(m_captionMin);
                if (m_captionMax != null) {
                    if (g.getTextWidth(m_captionMax) > captionWidth)
                        captionWidth = g.getTextWidth(m_captionMax);
                }
                // draw the caption
                if ((m_style & STYLE_ORIENTATION_VERTICAL) == STYLE_ORIENTATION_VERTICAL) {
                    // set drawing parameters for vertical oriented bar meter
                    m_barWidth = width - captionWidth;
                    if (m_barWidth < 4) m_barWidth = width < 4 ? width : 4;
                    m_barHeight = height;
                    if ((m_style & STYLE_SHOW_NUMERIC_VALUE) != 0)
                        m_barHeight -= g.getFontHeight() + 1;
                    // draw caption
                    int xoff = m_barWidth + 1;
                    if ((xoff <= width) && (g.getFontHeight() < height)) {
                        if (m_captionMin != null)
                            g.drawString(m_captionMin, x + xoff, y + m_barHeight
                                    - g.getFontHeight(), width - xoff, g.getFontHeight(), 0, 0);
                        if (m_captionMax != null)
                            g.drawString(m_captionMax, x + xoff, y, width - xoff,
                                    g.getFontHeight(), 0, 0);
                    }
                } else {
                    // set drawing parameters for horizontal oriented bar meter
                    m_barWidth = width;
                    if (m_barWidth < 4) m_barWidth = 4;
                    m_barHeight = height;
                    if ((m_style & STYLE_SHOW_NUMERIC_VALUE) != 0 || m_captionMin != null
                            || m_captionMax != null) m_barHeight -= g.getFontHeight() + 1;
                    if (m_barHeight < 4) m_barHeight = height < 4 ? height : 4;
                    // draw caption
                    int yoff = m_barHeight + 1;
                    if ((yoff < height) && (captionWidth <= width)) {
                        if (m_captionMin != null) g.drawString(m_captionMin, x, y + yoff);
                        if (m_captionMax != null)
                            g.drawString(m_captionMax, x + width - g.getTextWidth(m_captionMax), y
                                    + yoff);
                    }
                }
                g.setFont(null);
                // draw a border around the bar meter
                g.drawRect(x, y, m_barWidth, m_barHeight);
            default:
                drawValue(g); // draw the current value
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Sets a caption text which is displayed for the min and max value.
     * 
     * @param captionMin Text to display at the right bottom (vertical bar meter) or at the left
     *        bottom (horizontal bar meter).
     * @param captionMax Text to display at the right top (vertical bar meter) or at the right
     *        bottom (horizontal bar meter).
     */
    public void setCaption(String captionMin, String captionMax) {
        // copy the given caption string to a local object
        if ((captionMin != null) && captionMin.equals(""))
            m_captionMin = null;
        else
            m_captionMin = captionMin;
        if ((captionMax != null) && captionMax.equals(""))
            m_captionMax = null;
        else
            m_captionMax = captionMax;
        redrawInternalAndParent();
    }
}