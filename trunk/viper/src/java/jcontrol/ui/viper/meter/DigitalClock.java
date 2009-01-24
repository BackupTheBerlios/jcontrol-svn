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
import jcontrol.system.Time;
import jcontrol.ui.viper.Component;

/**
 * <p>
 * This class provides an digital clock for JControl.
 * </p>
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public class DigitalClock extends Component {

    public static final int NONE = 0;

    public static final int STYLE_SHOW_SECONDS = RESERVED1;

    private int m_hour;

    private int m_minute;

    private int m_second;

    private int m_maxDigitWidth;

    /**
     * Creates a clock.
     * 
     * @param x the upper left x coordinate
     * @param y the upper left y coordinate
     * @param style STYLE_SHOW_SECONDS or NONE
     */
    public DigitalClock(int x, int y, int style) {
        this.x = x;
        this.y = y;
        state |= STATE_REVALIDATE;
        state |= (style & STYLE_SHOW_SECONDS);
    }

    /**
     * Draws a digit.
     * 
     * @param g
     * @param digit The digit to draw
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width of a digit
     * @param height The height of a digit
     */
    private void drawDigit(Graphics g, String digit, int x, int y, int width, int height) {
        // center digit, so calculate the space before and behind it
        int padWidth = (width - g.getTextWidth(digit)) >> 1;
        g.clearRect(x, y, padWidth, height);
        g.clearRect(x + width - padWidth, y, padWidth, height);
        g.drawString(digit, x + padWidth, y);
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
            default:

                g.setFont(font);
                if (width == 0 || height == 0 || (state & STATE_REVALIDATE) != 0) {
                    // validate
                    // calculate m_maxDigitWidth
                    for (int i = 0; i < 9; i++) {
                        int w = g.getTextWidth(String.valueOf(i));
                        if (w > m_maxDigitWidth) m_maxDigitWidth = w;
                    }
                    width = 4 * m_maxDigitWidth;
                    if ((state & STYLE_SHOW_SECONDS) != 0) {
                        width = 6 * m_maxDigitWidth + g.getTextWidth(":") * 2;
                    } else {
                        width = 4 * m_maxDigitWidth + g.getTextWidth(":");
                    }
                    height = g.getFontHeight();
                    state &= ~STATE_REVALIDATE;
                }
                int xoff = x;
                drawDigit(g, String.valueOf(m_hour / 10), xoff, y, m_maxDigitWidth, height);
                xoff += m_maxDigitWidth;
                drawDigit(g, String.valueOf(m_hour % 10), xoff, y, m_maxDigitWidth, height);
                xoff += m_maxDigitWidth;
                xoff += g.drawString(":", xoff, y);

                drawDigit(g, String.valueOf(m_minute / 10), xoff, y, m_maxDigitWidth, height);
                xoff += m_maxDigitWidth;
                drawDigit(g, String.valueOf(m_minute % 10), xoff, y, m_maxDigitWidth, height);
                xoff += m_maxDigitWidth;

                if ((state & STYLE_SHOW_SECONDS) != 0) {
                    xoff += g.drawString(":", xoff, y);
                    drawDigit(g, String.valueOf(m_second / 10), xoff, y, m_maxDigitWidth, height);
                    xoff += m_maxDigitWidth;
                    drawDigit(g, String.valueOf(m_second % 10), xoff, y, m_maxDigitWidth, height);
                }
                g.setFont(null);
                state &= ~STATE_DIRTY_MASK;
        }
    }

    /**
     * Sets a the specified time to the clock.
     * 
     * @param time the time to set
     */
    public void setValue(Time time) {
        m_hour = time.hour;
        m_minute = time.minute;
        m_second = time.second;
        redrawInternalAndParent();
    }
}