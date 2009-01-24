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
 * The class <code>DigitalMeter</code> draws decimal values with a specified font and a specified
 * number of digits. Several display parameters are user controllable.
 * </p>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @since Viper 1.0
 * @version $Revision$
 */
public class DigitalMeter extends AbstractMeter {

    int m_maxDigitWidth;

    /**
     * Creates a new <code>DigitalMeter</code> at the specified x- and y-coordinates. The font used
     * for writing the current meter value can be set by using the method <code>setFont</code>. The
     * width and height of the <code>DigitalMeter</code> are calculated automatically and depend on
     * the used font and the number of digits.
     * 
     * @param x The x-coordinate for the upper left of the bounding box.
     * @param y The y-coordinate for the upper left of the bounding box.
     */
    public DigitalMeter(int x, int y) {
        this.x = x;
        this.y = y;
        state |= STATE_REVALIDATE;
    }

    /**
     * Draws one digit, centered at the given position
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
        int padWidth = (width - g.getTextWidth(digit) + 3) >> 1;
        g.clearRect(x, y, padWidth, height);
        g.clearRect(x + width - padWidth, y, padWidth, height);
        g.drawString(digit, x + padWidth, y);
    }

    /**
     * Draws the DigitalMeter's value
     */
    private void drawValue(Graphics g) {
        g.setFont(font);
        int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
        int digitHeight = g.getFontHeight();
        int xoff = x + (m_digits * m_maxDigitWidth) + (decimals > 0 ? g.getTextWidth(".") : 0);
        int v = m_value < 0 ? -m_value : m_value;
        int i = 0;
        for (; i < m_digits && ((v != 0) || i <= decimals); i++) {
            // skip decimal point
            if ((decimals > 0) && (i == decimals)) {
                xoff -= g.getTextWidth(".");
            }
            // draw digit
            xoff -= m_maxDigitWidth;
            drawDigit(g, String.valueOf(v % 10), xoff, y, m_maxDigitWidth, digitHeight);
            v /= 10;
        }
        // optionally, draw a minus sign
        if ((m_value < 0) && (i < m_digits)) {
            xoff -= m_maxDigitWidth;
            drawDigit(g, "-", xoff, y, m_maxDigitWidth, digitHeight);
            i++;
        }
        // clear digits before number
        for (; i < m_digits; i++) {
            // clear position
            xoff -= m_maxDigitWidth;
            g.clearRect(xoff, y, m_maxDigitWidth, digitHeight);
        }
        g.setFont(null);
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
                if ((state & STATE_REVALIDATE) == STATE_REVALIDATE) {
                    // calculate m_maxDigitWidth
                    for (int i = 0; i < 9; i++) {
                        int w = g.getTextWidth(String.valueOf(i));
                        if (w > m_maxDigitWidth) m_maxDigitWidth = w;
                    }
                    state &= ~STATE_REVALIDATE;
                }
                int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
                // when width or height missing, calculate preferred size
                if (width == 0 || height == 0) {
                    // calculate the preferred size of this digital meter.
                    width = (m_digits * m_maxDigitWidth) + (decimals > 0 ? g.getTextWidth(".") : 0);
                    height = g.getFontHeight();
                }
                // clear the background of this metering element
                g.clearRect(x + 1, y + 1, width - 2, height - 2);
                // draw a decimal point when necessary
                if (decimals > 0) {
                    int xoff = x + ((m_digits - decimals) * m_maxDigitWidth);
                    g.drawString(".", xoff, y);
                }
                g.setFont(null);
            default:
                drawValue(g); // draw the current value
        }
        state &= ~STATE_DIRTY_MASK;
    }
}