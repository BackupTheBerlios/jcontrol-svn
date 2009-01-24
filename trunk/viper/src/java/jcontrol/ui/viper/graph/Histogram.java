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
package jcontrol.ui.viper.graph;

import jcontrol.io.Graphics;
import jcontrol.lang.Math;
import jcontrol.ui.viper.meter.AbstractMeter;

/**
 * <p>
 * This class <CODE>Histogram</CODE> provides a histogram for JControl. <BR>
 * New values are pushed in from the right side, older values are moved over to the left. Thus, the
 * oldest value shown is the value on the very left.
 * </p>
 * 
 * <pre>
 *     |    X      X | maxvalue
 *     |X   X     XX |
 *     |XX  XXXXXXXX |
 *     |XXXXXXXXXXXXX| minvalue
 *     ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
 * </pre>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @see jcontrol.ui.viper.graph.Diagram
 * @since Viper 1.0
 * @version $Revision$
 */
public class Histogram extends AbstractMeter {

    /** Align the caption to the left side of the diagram */
    public static final int STYLE_ALIGN_LEFT = 0;

    /** Align the caption to the right side of the diagram */
    public static final int STYLE_ALIGN_RIGHT = 2;

    /** The minimal and maximal value of the diagram */
    private int m_resolution = 30;

    /** Optional caption text */
    private String m_captionMin, m_captionMax;

    /** Internal parameters */
    private int[] m_history;
    private int m_historyCounter = 0;
    private int m_displayWidth;
    private int m_displayHeight;

    /**
     * Creates a Histogram.
     * 
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param width the width.
     * @param height the height.
     * @param resolution the number of blocks that should be shown in the Histogram
     */
    public Histogram(int x, int y, int width, int height, int resolution) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        m_resolution = resolution;
        m_history = new int[width - 2 >= m_resolution ? m_resolution : width - 2];
    }

    /**
     * Draw all values of the histogram
     */
    private void drawValue(Graphics g) {
        synchronized (m_history) {
            if (m_min != m_max) {
                // draw histogram
                int xoff = (m_style & STYLE_ALIGN_RIGHT) == STYLE_ALIGN_RIGHT ? 0 : width
                        - m_displayWidth;
                int blockSize = (m_displayWidth + m_history.length - 3) / m_history.length;
                int x1 = xoff + 1;
                int x2;
                int y1;

                for (int c = 0; c < m_history.length; c++) {
                    int value = m_history[(m_historyCounter - c + m_history.length)
                            % m_history.length];
                    if (value < m_min) value = m_min;
                    if (value > m_max) value = m_max;
                    if (x1 >= 0) {
                        x2 = x1 + blockSize < xoff + m_displayWidth ? blockSize : xoff
                                + m_displayWidth - 1 - x1;
                        y1 = Math.scale(m_displayHeight - 2, m_max - m_min, value - m_min);
                        if (x2 > 0) { // kann wesentlich breiter werden, weil blockSize aufgerundet
                            // ist
                            g.clearRect(x + x1, y + 1, x2, m_displayHeight - y1 - 2);
                            g.fillRect(x + x1, y + m_displayHeight - y1 - 1, x2, y1);
                        } else
                            break;
                    }
                    x1 += blockSize;
                }
            }
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
                if (captionWidth > width) captionWidth = width;
                // get internal display width
                m_displayWidth = width - captionWidth - 2;
                if (m_displayWidth < 0) m_displayWidth = 0;
                // determine maximum width of the label
                int labelWidth = 0;
                // get internal display height
                m_displayHeight = height;
                if (labelWidth > 0) {
                    m_displayHeight -= g.getFontHeight() + 1;
                    if (m_displayHeight < 0) m_displayHeight = 0;
                }
                if ((m_style & STYLE_ALIGN_RIGHT) == STYLE_ALIGN_RIGHT) {
                    // draw rect, label and caption for right-aligned components
                    g.drawRect(x, y, m_displayWidth, m_displayHeight);
                    if (g.getFontHeight() <= height) {
                        if (m_captionMin != null)
                            g.drawString(m_captionMin, x + m_displayWidth + 1, y + m_displayHeight
                                    - g.getFontHeight() + 1, captionWidth, g.getFontHeight(), 0, 0);
                        if (m_captionMax != null)
                            g.drawString(m_captionMax, x + m_displayWidth + 1, y, captionWidth, g
                                    .getFontHeight(), 0, 0);
                    }
                } else {
                    // draw rect, label and caption for left-aligned components
                    g.drawRect(x + width - m_displayWidth, y, m_displayWidth, m_displayHeight);
                    if (g.getFontHeight() <= height) {
                        if (m_captionMin != null)
                            g.drawString(m_captionMin, x, y + m_displayHeight - g.getFontHeight()
                                    + 1, captionWidth, g.getFontHeight(), 0, 0);
                        if (m_captionMax != null)
                            g.drawString(m_captionMax, x, y, captionWidth, g.getFontHeight(), 0, 0);
                    }
                }
                g.setFont(null);
            default:
                // draw all values of the history
                drawValue(g);
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        if (this.width != width) {
            synchronized (m_history) {
                // rescale history values
                int[] history = new int[width - 2 >= m_resolution ? m_resolution : width - 2];
                int size = m_history.length - 1;
                for (int i = 0; i < size; i++) {
                    history[i] = m_history[(i + m_historyCounter) % m_history.length];
                    if (i >= history.length - 1) {
                        i = size;
                    }
                }
                m_historyCounter = 0;
                m_history = history;
            }
            this.width = width;
        }
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

    /**
     * Sets a caption text which is displayed for the min and max value at the left of the
     * histogram.
     * 
     * @param captionMin Text to display at the bottom
     * @param captionMax Text to display at the top
     * @param captionAlign STYLE_ALIGN_LEFT or STYLE_ALIGN_RIGHT
     */
    public void setCaption(String captionMin, String captionMax, int captionAlign) {
        // copy the given caption strings to the local object
        if ((captionMin != null) && captionMin.equals(""))
            m_captionMin = null;
        else
            m_captionMin = captionMin;
        if ((captionMax != null) && captionMax.equals(""))
            m_captionMax = null;
        else
            m_captionMax = captionMax;
        m_style = captionAlign;
        redrawInternalAndParent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.meter.AbstractMeter#setValue(int)
     */
    @Override
    public void setValue(int value) {
        synchronized (m_history) {
            if (value < m_min) {
                value = m_min;
            } else if (value > m_max) {
                value = m_max;
            }
            m_historyCounter++;
            m_historyCounter %= m_history.length;
            m_history[m_historyCounter] = value;
        }
        setDirty(STATE_DIRTY_UPDATE, true);
    }

}