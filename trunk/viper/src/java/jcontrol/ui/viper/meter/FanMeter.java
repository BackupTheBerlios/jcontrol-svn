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
 * This class <code>FanMeter</code> implements a simple animation representing a fan. This component
 * can be used e.g. for system monitoring applications. The animation speed depends on the current
 * value relating to the minimum and maximum ranges that have been specified for this FanMeter.
 * </p>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @since Viper 1.0
 * @version $Revision$
 */
public class FanMeter extends AbstractMeter {

    public static final int NONE = 0;

    public static final int STYLE_SHOW_NUMERIC_VALUE = 4;

    /** Optional caption text */
    private String m_caption;

    /** Parameters for numeric display */
    private int m_lastNumericWidth;

    private int m_paintcount;

    private int m_maxDigitWidth;

    /**
     * Create a FanMeter.
     * 
     * @param x the x position
     * @param y the y position
     * @param style STYLE_SHOW_NUMERIC_VALUE or NONE
     */
    public FanMeter(int x, int y, int style) {
        this.x = x;
        this.y = y;
        m_style = style;
        setDirty(STATE_ANIMATED, true);
    }

    /**
     * Draws the Fan's value
     */
    protected void drawValue(Graphics g) {
        // draw value and unit of numeric display
        if ((m_style & STYLE_SHOW_NUMERIC_VALUE) != 0) {
            String v = String.valueOf(m_value >= 0 ? m_value : -m_value);
            int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
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
            int yoff;
            if (m_caption == null) {
                yoff = (height - g.getFontHeight()) >> 1;
                if (yoff < 0) yoff = 0;
            } else {
                yoff = (height - (g.getFontHeight() << 1)) >> 1;
                if (yoff < 0) yoff = 0;
                yoff += g.getFontHeight();
            }
            // if new value string is smaller than last one, delete space behind it
            if (numericWidth < m_lastNumericWidth)
                g.clearRect(x + 18 + numericWidth, y + yoff, m_lastNumericWidth - numericWidth, g
                        .getFontHeight());
            g.drawString(v, x + 18, y + yoff, numericWidth, g.getFontHeight(), 0, 0);
            m_lastNumericWidth = numericWidth;
        }

        g
                .drawImage(
                        new String[]{
                                "\uC0B0\uC8C4\uC2C2\uBDFF\uFFBD\uC2C2\uC4C8\uB0C0\uC030\u083C\u7A7A\uF1C1\uC1F1\u7A7A\u3C08\u30C0",
                                "\u030D\u1323\u4343\uBDFF\uFFBD\u4343\u2313\u0D03\u030C\u103C\u5E5E\u8F83\u838F\u5E5E\u3C10\u0C03"},
                        x, y + ((height - 16) >> 1), 16, 16, ((state & STATE_SELECTED) != 0) ? 0
                                : 16, 0);
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
                if (m_maxDigitWidth == 0 || (state & STATE_REVALIDATE) == STATE_REVALIDATE) {
                    // calculate m_maxDigitWidth
                    for (int i = 0; i < 9; i++) {
                        int w = g.getTextWidth(String.valueOf(i));
                        if (w > m_maxDigitWidth) m_maxDigitWidth = w;
                    }
                    state &= ~STATE_REVALIDATE;
                }
                // when width or height missing, calculate preferred size
                if (width == 0 || height == 0) {
                    int captionWidth = m_caption == null ? 0 : g.getTextWidth(m_caption);
                    int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
                    int numericWidth = m_maxDigitWidth * m_digits
                            + (decimals > 0 ? g.getTextWidth(".") : 0);
                    String unit = null;
                    if (m_dataProducer != null) unit = m_dataProducer.getUnit();
                    if (unit != null) numericWidth += g.getTextWidth(unit);
                    this.width = 18 + (numericWidth > captionWidth ? numericWidth : captionWidth);
                    this.height = (g.getFontHeight() < 8 ? 16 : g.getFontHeight() << 1);
                }
                // default last values
                m_lastNumericWidth = 0;
                // draw caption
                if (m_caption != null) {
                    int yoff = (height - (g.getFontHeight() << 1)) >> 1;
                    if (yoff < 0) yoff = 0;
                    g.drawString(m_caption, x + 18, y + yoff, width - 18, height - yoff, 0, 0);
                }
            default:
                if (m_value > 0) {
                    int sleep = 10 - jcontrol.lang.Math.scale(m_value, m_max > m_min ? m_max
                            - m_min : m_min - m_max, 10);
                    if (m_paintcount >= sleep) {
                        m_paintcount = 0; // paint this time
                        if ((state & STATE_SELECTED) != 0) {
                            state &= ~STATE_SELECTED;
                        } else {
                            state |= STATE_SELECTED;
                        }
                    } else {
                        m_paintcount++;
                        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_UPDATE) {
                            state &= ~STATE_UPDATED;
                            if ((state & STATE_DIRTY_REPAINT) == 0) break;
                        }
                    }
                }
                drawValue(g);// draw the current value
        }
        g.setFont(null);
        if (m_value == 0) {
            // keep the animated flag but reset the update-flag
            state &= ~(STATE_DIRTY_PAINT_ALL & ~(STATE_ANIMATED & ~STATE_DIRTY_UPDATE));
        } else {
            state &= ~(STATE_DIRTY_PAINT_ALL & ~STATE_ANIMATED);
        }
    }

    /**
     * Sets a caption text which is displayed.
     * 
     * @param caption Text to display
     */
    public void setCaption(String caption) {
        // copy the given caption strings to the local object
        if ((caption != null) && caption.equals("")) {
            m_caption = null;
        } else {
            m_caption = caption;
        }
        redrawInternalAndParent();
    }

}