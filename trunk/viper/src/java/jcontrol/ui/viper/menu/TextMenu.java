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
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * The class <code>TextMenu</code> implements a simple graphical text-based scrolling menu.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class TextMenu extends Menu {

    /** The font height. */
    private int m_fontHeight;

    /**
     * Creates a textMenu.
     * 
     * @param x the menu's x position
     * @param y the menu's y position
     * @param width the width
     * @param height the height
     */
    public TextMenu(String[] items, int x, int y, int width, int height) {
        super(items, x, y, width, height);
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
            case KeyEvent.KEY_UP_PRESSED:
                if (selectNextOrPrev(false)) redrawInternalAndParent();
                return null;
            case KeyEvent.KEY_DOWN_PRESSED:
                if (selectNextOrPrev(true)) redrawInternalAndParent();
                return null;
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
            case KeyEvent.KEY_UP_PRESSED:
                if (selectNextOrPrev(false)) redrawInternalAndParent();
                return null;
            case KeyEvent.KEY_DOWN_PRESSED:
                if (selectNextOrPrev(true)) redrawInternalAndParent();
                return null;
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
        if (e.type == TouchEvent.TYPE_TOUCH_RELEASED) return RESULT_NONE;
        if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) {
            int visibleItems = (height - 8) / m_fontHeight;
            int yoffset = y + 4;
            if (visibleItems > m_items.length) {
                visibleItems = m_items.length;
                yoffset += (((height - 8) - visibleItems * m_fontHeight) >> 1);
            } else {
                yoffset += (((height - 8) % m_fontHeight) >> 1);
            }
            // in between bounds
            if (e.y < y + yoffset) {
                // scroll up
                if (selectNextOrPrev(false)) {
                    redrawInternalAndParent();
                }
                return RESULT_ACCEPTED;
            }

            if (e.y >= y + height - yoffset) {
                // scroll up
                if (selectNextOrPrev(true)) {
                    redrawInternalAndParent();
                }
                return RESULT_ACCEPTED;
            }
            if (e.type != TouchEvent.TYPE_TOUCH_PRESSED) return RESULT_ACCEPTED;
            int selection;
            if (e.y > y + height - 8 - yoffset) {
                selection = visibleItems - 1 + m_scrollValue;
            } else {
                selection = ((e.y - (yoffset + y)) / m_fontHeight) + m_scrollValue;
            }
            if (!m_inactives[selection]) {
                if (m_selectedIndex != selection) {
                    redrawInternalAndParent();
                    m_selectedIndex = selection;
                }

                if (listener != null) {
                    onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                            m_items[m_selectedIndex]));
                    return RESULT_EXECUTED;
                }
            }
            return RESULT_ACCEPTED;
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
        switch (state & STATE_DIRTY_MASK) {
            case STATE_DIRTY_PAINT_ALL:
                g.clearRect(x, y, width, height);
            default:
                // draw the text
                g.setFont(font);
                m_fontHeight = g.getFontHeight();
                int visibleItems = (height - 8) / m_fontHeight;
                int yoffset = y + 4;
                if (visibleItems > m_items.length) {
                    visibleItems = m_items.length;
                    yoffset += (((height - 8) - visibleItems * m_fontHeight) >> 1);
                } else {
                    yoffset += (((height - 8) % m_fontHeight) >> 1);
                }

                int selectionPos = visibleItems >> 1; // keep selection bar at center position
                if (m_selectedIndex >= m_items.length) {
                    m_selectedIndex = m_items.length - 1;
                }
                if (m_selectedIndex < 0 && m_items.length > 0) {
                    m_selectedIndex = 0;
                }
                if (m_selectedIndex <= selectionPos) {
                    selectionPos = m_selectedIndex;
                }
                // check if scroll value is valid
                if (m_selectedIndex < m_scrollValue + selectionPos) {
                    // scroll up
                    m_scrollValue = m_selectedIndex - selectionPos;
                }
                if (m_selectedIndex >= m_scrollValue + selectionPos) {
                    m_scrollValue = m_selectedIndex - selectionPos;
                }
                if (m_scrollValue > m_items.length - visibleItems) {
                    m_scrollValue = m_items.length - visibleItems;
                }
                if (m_scrollValue < 0) {
                    m_scrollValue = 0;
                }

                { // draw or clear arrows
                    int center = x + ((width - 5) >> 1); // the center position for the arrows
                    if (m_scrollValue > 0) {
                        // up arrow
                        g.drawImage(new String[]{"\u1818\u3C3C\u7EFF\u7E3C\u3C18\u1800"}, center,
                                y, 11, 4, 0, 0);
                    } else {
                        g.clearRect(x, y, width, 4);
                    }
                    if (m_scrollValue + visibleItems < m_items.length) {
                        // down arrow
                        g.drawImage(new String[]{"\u1818\u3C3C\u7EFF\u7E3C\u3C18\u1800"}, center, y
                                + height - 4, 11, 4, 0, 4);
                    } else {
                        g.clearRect(x, y + height - 4, width, 4);
                    }
                }
                {
                    for (int c = 0; c < visibleItems; c++) {
                        int h = m_fontHeight;
                        if ((yoffset + h - (y + 4)) > height - 8) {
                            h = height - 8 - yoffset + (y + 1);
                        }
                        g.clearRect(x, yoffset, 3, h);
                        int w = g.drawString(m_items[c + m_scrollValue], x + 3, yoffset, width - 6,
                                h, 0, 0);
                        g.clearRect(x + 3 + w, yoffset, width - 3 - w, h);
                        if (m_selectedIndex == c + m_scrollValue) {
                            if ((state & STATE_FOCUS) == 0) {
                                drawDottedRect(g, x, yoffset, width, h);
                            } else {
                                g.setDrawMode(Graphics.XOR);
                                g.fillRect(x, yoffset, width, h);
                                g.setDrawMode(Graphics.NORMAL);
                            }
                        }
                        if (m_inactives[c + m_scrollValue]) {
                            g.setDrawMode(Graphics.AND);
                            String[] mask = new String[]{"\u55AA\u55AA\u55AA\u55AA"};
                            for (int j = 0; j < w; j += 8) {
                                for (int k = 0; k < h - 2; k += 8) {
                                    g.drawImage(mask, x + 3 + j, yoffset + k,
                                            j > w - 8 ? w & 7 : 8, k > h - 8 ? h & 7 : 8, 0, 0);
                                }
                            }
                            g.setDrawMode(jcontrol.io.Graphics.NORMAL);
                        }
                        yoffset += h;
                    }
                    if (yoffset < y + height - 8) {
                        g.clearRect(x, yoffset, width, height - 8 - yoffset);
                    }
                }
                g.setFont(null);
        }
        state &= ~(STATE_DIRTY_MASK | STATE_REVALIDATE);
    }

}
