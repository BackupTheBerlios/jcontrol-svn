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
package jcontrol.ui.viper;

import jcontrol.io.Graphics;

/**
 * <p>
 * This class provides a TextScroller component.
 * </p>
 * 
 * @author Helge Böhme , Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public class TextScroller extends Component {

    /** Constant value for center alignment */
    public static final int STYLE_ALIGN_CENTER = 0;
    /** Constant value for left alignment */
    public static final int STYLE_ALIGN_LEFT = 1;
    /** Constant value for right alignment */
    public static final int STYLE_ALIGN_RIGHT = 2;

    private String[] m_items;
    private int m_style = STYLE_ALIGN_LEFT;
    private int m_xpos[];
    private int m_step = 1;
    private int m_firstItem = 0;
    private int m_firstLine = 0;

    /**
     * Creates a new TextScroller containing the specified text.
     * 
     * @param items Some lines of text to display in this <code>TextScroller</code>.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param style Specifies the style for the text scroller.<br>
     *        Possible values are <code>STYLE_ALIGN_CENTER</code>,<code>STYLE_ALIGN_LEFT</code> or
     *        <code>STYLE_ALIGN_RIGHT</code>.
     */
    public TextScroller(String[] items, int x, int y, int width, int height, int style) {
        m_items = items;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        m_style = style & (STYLE_ALIGN_CENTER | STYLE_ALIGN_LEFT | STYLE_ALIGN_RIGHT);
        setDirty(STATE_ANIMATED, true);
    }

    /**
     * The animate method of the text scroller. Whenever this method is called from the
     * <code>AnimationContainer</code>, the component scrolls the text. The scroll speed and
     * direction depends on the m_step value, which can be set with <code>setStep(int m_step)</code>
     * .{@available}
     */
    private void animate(Graphics g) {
        int fontHeight = g.getFontHeight();
        if (m_step != 0) {
            if (m_step == 100) m_step = 0; // draw again
            int l = m_firstItem, ypos = -m_firstLine;
            while (ypos < height) {
                int h1 = ypos;
                if (h1 < 0) h1 = 0;
                int h2 = ypos + fontHeight + 1;
                if (h2 > height) h2 = height;
                g.clearRect(x, y + h1, width, h2 - h1);
                g.drawString(m_items[l], m_xpos[l], y + h1, width, h2 - h1, 0, h1 - ypos);
                ypos += fontHeight;
                l++;
                if (l == m_items.length) l = 0;
            }
        }
        m_firstLine += m_step;
        if (m_firstLine < 0) {
            m_firstLine += fontHeight;
            m_firstItem--;
            if (m_firstItem < 0) m_firstItem = m_items.length - 1;
        } else if (m_firstLine >= fontHeight) {
            m_firstLine -= fontHeight;
            m_firstItem++;
            if (m_firstItem == m_items.length) m_firstItem = 0;
        }
    }

    /**
     * Draws the text scroller.
     */
    @Override
    public synchronized void paint(Graphics g) {
        switch ((state & STATE_DIRTY_MASK)) {
            case STATE_DIRTY_PAINT_ALL:
                g.clearRect(x, y, width, height);
            case STATE_DIRTY_REPAINT:
                m_xpos = new int[m_items.length];
                for (int i = 0; i < m_items.length; i++) {
                    int p = g.getTextWidth(m_items[i]);
                    if (p > width) p = width;
                    if (m_style != STYLE_ALIGN_LEFT) p = width - p;
                    if (m_style == STYLE_ALIGN_CENTER) p >>= 1;
                    m_xpos[i] = x + p;
                }
            default:
                if ((state & STATE_ANIMATED) == STATE_ANIMATED) animate(g);
        }
        if ((state & STATE_ANIMATED) == STATE_ANIMATED) {
            state &= ~(STATE_DIRTY_PAINT_ALL & ~STATE_ANIMATED);
        } else {
            state &= ~STATE_DIRTY_PAINT_ALL;
        }
    }

    /**
     * Sets the step width (i.e. the speed) of this text scroller in pixels.
     * 
     * @param step The step width.
     */
    public void setStep(int step) {
        m_step = step;
    }
}