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

import java.io.IOException;

import jcontrol.io.Graphics;
import jcontrol.io.Resource;
import jcontrol.toolkit.ImageUtils;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * The class <code>MultiImageMenu</code> implements a menu with several images. Menu items must be
 * specified as image names.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class MultiImageMenu extends Menu {

    /** Max. visible items. */
    private int m_maxVisibleItems = -1;

    /**
     * Creates an multiImageMenu.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     * @param maxVisibleItems the number of visible menu items, images will be clipped to the same
     *        size to fit into the menu's bounds.
     */
    public MultiImageMenu(String[] items, int x, int y, int width, int height, int maxVisibleItems) {
        super(items, x, y, width, height);
        m_maxVisibleItems = maxVisibleItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        switch (e.m_key) {
            case KeyEvent.KEY_SELECT:
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
            case KeyEvent.KEY_SELECT:
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

        if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) {
            int visibleItems = m_maxVisibleItems;
            if (visibleItems < 0 || visibleItems > m_items.length) {
                visibleItems = m_items.length;
            }
            int maxImageWidth = (width - 6) / visibleItems;
            if (e.x <= x + 3) {
                if (m_scrollValue > 0) {
                    m_scrollValue--;
                    selectNextOrPrev(false);
                    redrawInternalAndParent();
                    return RESULT_ACCEPTED;
                }
            } else if (e.x >= x + width - 3) {
                if (m_scrollValue + visibleItems - 1 < m_items.length - 1) {
                    m_scrollValue++;
                    selectNextOrPrev(true);
                    redrawInternalAndParent();
                    return RESULT_ACCEPTED;
                }
            } else {
                int selected = (e.x - (x + 3)) / maxImageWidth;
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
        if (m_items.length > 0) {
            int visibleItems = m_maxVisibleItems;
            if (visibleItems < 0 || visibleItems > m_items.length) {
                visibleItems = m_items.length;
            }
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
            int maxImageWidth = (width - 6) / visibleItems;
            int xoff = x + 3;
            int iWidth = maxImageWidth;
            int imageXoff;
            int iHeight;
            for (int i = 0; i < visibleItems && i + m_scrollValue < m_items.length; i++) {
                try {
                    Resource image = new Resource(m_items[i + m_scrollValue]);
                    iWidth = ImageUtils.getWidth(image);
                    if (iWidth > maxImageWidth) iWidth = maxImageWidth;
                    iHeight = ImageUtils.getHeight(image);
                    if (iHeight > height) iHeight = height;
                    imageXoff = ((maxImageWidth - iWidth) >> 1);
                    g.drawImage(image, xoff + imageXoff, y, iWidth, iHeight, 0, 0);
                } catch (IOException e) {
                    iWidth = maxImageWidth;
                    iHeight = g.getFontHeight();
                    imageXoff = ((maxImageWidth - iWidth) >> 1);
                    g.drawString(m_items[i + m_scrollValue],
                            xoff + ((maxImageWidth - iWidth) >> 1), (height - iHeight) >> 1,
                            iWidth, -1, 0, 0);
                }
                if (m_inactives[i + m_scrollValue]) {
                    g.setDrawMode(Graphics.AND);
                    String[] mask = new String[]{"\u55AA\u55AA\u55AA\u55AA\u55AA\u55AA\u55AA\u55AA"};
                    for (int j = 0; j < iWidth; j += 16) {
                        for (int k = 0; k < height - 2; k += 8) {
                            g.drawImage(mask, xoff + imageXoff + j, y + 1 + k, j > iWidth - 16
                                    ? iWidth & 15 : 16, k > height - 8 ? height & 7 : 8, 0, 0);
                        }
                    }
                    g.setDrawMode(jcontrol.io.Graphics.NORMAL);
                }
                if (iHeight < height) {
                    g.clearRect(xoff, y + iHeight, maxImageWidth, height - iHeight);
                }
                if (imageXoff > 0) {
                    g.clearRect(xoff, y, imageXoff, iHeight); // clear area on the left of image
                    g.clearRect(xoff + imageXoff + iWidth, y, imageXoff, iHeight); // clear area on
                    // the right of
                    // image
                }
                if (i + m_scrollValue == m_selectedIndex) { // paint selected item box
                    g.setDrawMode(jcontrol.io.Graphics.XOR);
                    g.fillRect(xoff + imageXoff, y, iWidth, height);
                    if ((state & STATE_FOCUS) != 0) {
                        drawDottedRect(g, xoff + imageXoff, y, iWidth, height);
                    }
                    g.setDrawMode(jcontrol.io.Graphics.NORMAL);
                }

                xoff += maxImageWidth;
            }
            if (m_scrollValue > 0) {
                // left arrow
                g.drawImage(new String[]{"\u107C\uFF00", "\u0000\u0100"}, x, y
                        + ((height - 9) >> 1));
            } else {
                g.clearRect(x, y + ((height - 9) >> 1), 3, 9);
            }
            if (m_scrollValue + visibleItems - 1 < m_items.length - 1) {
                // right arrow
                g.drawImage(new String[]{"\uFF7C\u1000", "\u0100\u0000"}, x + width - 3, y
                        + ((height - 9) >> 1));
            } else {
                g.clearRect(x + width - 3, y + ((height - 9) >> 1), 3, 9);
            }
        }
        state &= ~STATE_DIRTY_MASK;
    }
}