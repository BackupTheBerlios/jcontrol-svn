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
package jcontrol.ui.viper.menu;

import jcontrol.io.Graphics;
import jcontrol.ui.viper.Component;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * The class <code>MenuBar</code> implements a simple menu bar.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class MenuBar extends Menu {

    /** The top alignment. Value: 0. */
    public static final int ALIGN_TOP = 0;

    /** The bottom alignment. */
    public static final int ALIGN_BOTTOM = RESERVED1;

    /** Max. width. */
    private int m_maxWidth;

    /** Font size. */
    private int m_fontSize;

    /**
     * Creates a menu bar.
     * 
     * @param items the items to add to the menu
     * @param align ALIGN_TOP or ALIGN_BOTTOM
     */
    public MenuBar(String[] items, int align) {
        super(items, 0, 0, 0, 0);
        state |= (align & ALIGN_BOTTOM);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        switch (e.m_key) {
            case KeyEvent.KEY_SELECT_PRESSED:
                if (m_selectedIndex >= 0 && m_selectedIndex < m_items.length && listener != null) {
                    onActionEvent(new ActionEvent(this, ActionEvent.MENU_ACTION,
                            m_items[m_selectedIndex]));
                }
                return null;
            case KeyEvent.KEY_RIGHT_PRESSED:
                if (selectNextOrPrev(true)) redrawInternalAndParent();
                return null;
            case KeyEvent.KEY_LEFT_PRESSED:
                if (selectNextOrPrev(false)) redrawInternalAndParent();
                return null;
            case KeyEvent.KEY_UP_PRESSED:
                e.m_key = KeyEvent.KEY_TRANSFER_FOCUS_FORWARD;
                return e;
            case KeyEvent.KEY_DOWN_PRESSED:
                e.m_key = KeyEvent.KEY_TRANSFER_FOCUS_BACKWARD;
                return e;
        }
        return e;
    }

    /*
     * (non-Javadoc)
     * 
     * @seejcontrol.ui.viper.AbstractFocusComponent#onRotaryTouchEvent(jcontrol.ui.viper.event.
     * RotaryTouchEvent)
     */
    @Override
    public RotaryTouchEvent onRotaryTouchEvent(RotaryTouchEvent e) {
        switch (e.getRotaryTouchState()) {
            case KeyEvent.KEY_SELECT_PRESSED:
                if (m_selectedIndex >= 0 && m_selectedIndex < m_items.length && listener != null) {
                    onActionEvent(new ActionEvent(this, ActionEvent.MENU_ACTION,
                            m_items[m_selectedIndex]));
                }
                return null;
            case KeyEvent.KEY_RIGHT_PRESSED:
                if (selectNextOrPrev(true)) redrawInternalAndParent();
                return null;
            case KeyEvent.KEY_LEFT_PRESSED:
                if (selectNextOrPrev(false)) redrawInternalAndParent();
                return null;
            case KeyEvent.KEY_UP_PRESSED:
                // e.m_key = KeyEvent.KEY_TRANSFER_FOCUS_FORWARD;
                return e;
            case KeyEvent.KEY_DOWN_PRESSED:
                // e.m_key = KeyEvent.KEY_TRANSFER_FOCUS_BACKWARD;
                return e;
        }
        return e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * jcontrol.ui.viper.AbstractFocusComponent#onTouchEvent(jcontrol.ui.viper.event.TouchEvent)
     */
    @Override
    public int onTouchEvent(TouchEvent e) {
        if (e.type != TouchEvent.TYPE_TOUCH_PRESSED) return RESULT_NONE;

        int h = height < m_fontSize + 2 ? height : m_fontSize + 2;
        int yoff = (state & ALIGN_BOTTOM) == ALIGN_BOTTOM ? y + height - h : y;
        if (e.x >= x && e.x < x + width && e.y >= yoff && e.y < yoff + h) {
            int visibleItems = (width - 12) / m_maxWidth;
            if (e.x <= x + 6) {
                if (m_scrollValue > 0) {
                    m_scrollValue--;
                    selectNextOrPrev(false);
                    redrawInternalAndParent();
                    return RESULT_ACCEPTED;
                }
            } else if (e.x >= x + width - 6) {
                if (m_scrollValue + visibleItems - 1 < m_items.length - 1) {
                    m_scrollValue++;
                    selectNextOrPrev(true);
                    redrawInternalAndParent();
                    return RESULT_ACCEPTED;
                }
            } else {
                int selected = (e.x - (x + 5)) / m_maxWidth;
                if (selected < visibleItems) {
                    if (!m_inactives[selected + m_scrollValue]) {
                        m_selectedIndex = selected + m_scrollValue;
                        redrawInternalAndParent();
                        onActionEvent(new ActionEvent(this, ActionEvent.MENU_ACTION,
                                m_items[m_selectedIndex]));
                        return RESULT_EXECUTED;
                    }
                }
            }
        }
        return RESULT_NONE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public synchronized void paint(Graphics g) {
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
            g.clearRect(x, y, width, height);
        }
        g.setFont(font);
        if (m_maxWidth == 0 || width == 0 || height == 0
                || (state & STATE_REVALIDATE) == STATE_REVALIDATE) {
            m_maxWidth = 0;
            m_fontSize = g.getFontHeight();
            for (int i = 0; i < m_items.length; i++) {
                int w = g.getTextWidth(m_items[i]);
                if (w > m_maxWidth) m_maxWidth = w;
            }
            m_maxWidth += 2; // one pixel on each side
            height = m_fontSize + 2;
            Component root = (Component) getFrame();
            width = root.width;
            y = (state & ALIGN_BOTTOM) == ALIGN_BOTTOM ? root.y + root.height - height : root.y;
            state &= ~STATE_REVALIDATE;
        }
        int visibleItems = (this.width - 12) / m_maxWidth;

        // check if scroll value is valid
        if (m_selectedIndex < m_scrollValue) {
            // scroll up
            m_scrollValue = m_selectedIndex;
        }
        if (m_selectedIndex >= m_scrollValue + visibleItems) {
            m_scrollValue = m_selectedIndex - visibleItems + 1;
        }
        if (m_scrollValue > m_items.length - visibleItems) {
            m_scrollValue = m_items.length - visibleItems;
        }
        if (m_scrollValue < 0) {
            m_scrollValue = 0;
        }

        g.fillRect(x, y, width, height);
        if (m_scrollValue > 0) {
            // left arrow
            int space = ((height - 7) >> 1); // space between top and arrow
            g.drawImage(new String[]{"\u7763\u4100\u4163\u7700"}, x, y + space, 4, 7, 0, 0);
        }
        {
            int xpos = x + 6; // centered x pos
            for (int i = m_scrollValue; i < m_scrollValue + visibleItems; i++) {
                if (i >= m_items.length) break;
                int w = g.getTextWidth(m_items[i]);
                if (i == m_selectedIndex) {
                    g.setDrawMode(Graphics.NORMAL);
                } else {
                    g.setDrawMode(Graphics.INVERSE);
                }
                g.clearRect(xpos, y + 1, m_maxWidth - 2, m_fontSize);
                int sxpos = xpos + (((m_maxWidth - 2) - w) >> 1);
                g.drawString(m_items[i], sxpos, y + 1, -1, height - 2, 0, 0);
                if (m_inactives[i]) {
                    g.setDrawMode(Graphics.OR);
                    String[] mask = new String[]{"\u55AA\u55AA\u55AA\u55AA"};
                    for (int j = 0; j < w; j += 8) {
                        for (int k = 0; k < height - 2; k += 8) {
                            g.drawImage(mask, sxpos + j, y + 1 + k, j > w - 8 ? w & 7 : 8,
                                    k > height - 2 - 8 ? (height - 2) & 7 : 8, 0, 0);
                        }
                    }
                    g.setDrawMode(jcontrol.io.Graphics.NORMAL);
                }
                if (i == m_selectedIndex && (state & STATE_FOCUS) == STATE_FOCUS) {
                    g.setDrawMode(Graphics.INVERSE);
                    drawDottedRect(g, xpos - 1, y, m_maxWidth, m_fontSize + 2);
                    g.setDrawMode(Graphics.NORMAL);
                }
                xpos += m_maxWidth;
                g.setDrawMode(Graphics.NORMAL);

            }

            g.fillRect(xpos, y + 1, width - 4 - xpos, m_fontSize); // fill up with black
        }
        if (m_scrollValue + visibleItems - 1 < m_items.length - 1) {
            // right arrow
            int space = ((height - 7) >> 1); // space between top and arrow
            g.drawImage(new String[]{"\u7763\u4100\u4163\u7700"}, x + width - 4, y + space, 4, 7,
                    3, 0);
        }
        g.setFont(null);
        state &= ~STATE_DIRTY_MASK;
    }

}
