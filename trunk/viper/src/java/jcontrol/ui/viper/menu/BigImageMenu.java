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
import jcontrol.ui.viper.Component;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * This class <code>BigImageMenu</code> implements a graphical menu with only one (full page) menu
 * item visible. Menu items must be specified as image names.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class BigImageMenu extends Menu {

    /**
     * Creates a new BigImageMenu.
     * 
     * @param items the items to add to the menu. The strings should represent jcif image resource
     *        names.
     */
    public BigImageMenu(String[] items) {
        super(items, 0, 0, 0, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        switch (e.m_key) {
            case KeyEvent.KEY_RIGHT_PRESSED:
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
            case RotaryTouchEvent.STATE_IDLE:
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
        if (e.type != TouchEvent.TYPE_TOUCH_PRESSED) return RESULT_NONE;
        if (m_items.length > 0) {
            if (e.x >= x && e.x < x + width) {
                if (e.y > y + 4 && e.y < y + height - 4) {
                    if (m_selectedIndex >= 0 && m_selectedIndex < m_items.length
                            && listener != null) {
                        onActionEvent(new ActionEvent(this, ActionEvent.MENU_ACTION,
                                m_items[m_selectedIndex]));
                        return RESULT_EXECUTED;
                    }
                }
                { // check arrows
                    if (m_selectedIndex > 0) {
                        // up arrow
                        if (e.x < x + width - 4 && e.y >= y && e.y < y + 4) {
                            // touched on up arrow
                            if (selectNextOrPrev(false)) {
                                redrawInternalAndParent();
                                return RESULT_EXECUTED;
                            }
                        }
                    }
                    if (m_selectedIndex < m_items.length - 1) { // draw down-arrow
                        if (e.x < x + width - 4 && e.y >= y + height - 4 && e.y < y + height) {
                            // touched on down arrow
                            if (selectNextOrPrev(true)) {
                                redrawInternalAndParent();
                                return RESULT_EXECUTED;
                            }
                        }
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
        if (width == 0 || height == 0) {
            Component root = (Component) getFrame();
            x = root.x;
            y = root.y;
            width = root.width;
            height = root.height;
            state &= ~STATE_REVALIDATE;
        }
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
            g.clearRect(x, y, width, height);
        }
        if (m_items.length > 0) {
            if (m_selectedIndex < 0 || m_selectedIndex >= m_items.length) {
                m_selectedIndex = 0;
            }
            if (m_selectedIndex == 0) { // delete old up-arrow
                g.clearRect((width - 13) >> 1, 0, 13, 4);
            }
            if (m_selectedIndex == m_items.length - 1) { // delete old down-arrow
                g.clearRect((width - 13) >> 1, height - 4, 13, 4);
            }
            String item = m_items[m_selectedIndex]; // take active item
            try {
                Resource image = new Resource(item);
                int iwidth = ImageUtils.getWidth(image);
                int iheight = ImageUtils.getHeight(image);
                int xoff = (width - iwidth) >> 1;
                int yoff = (height - iheight) >> 1;
                g.drawImage(image, xoff, yoff, iwidth, iheight, 0, 0);
            } catch (IOException e) {
                g.drawString(item, x, (height - g.getFontHeight()) >> 1, width, -1, 0, 0);
            }
            // draw right-arrow
            g.drawImage(new String[]{"\uFFFC\uF040", "\u1F07\u0100"}, x + width - 4, y
                    + (height - 13) >> 1);
            if (m_inactives[m_selectedIndex]) {
                g.setDrawMode(jcontrol.io.Graphics.AND);
                String[] mask = new String[]{"\u55AA\u55AA\u55AA\u55AA"};
                for (int j = 0; j < width; j += 8) {
                    for (int k = 0; k < height; k += 8) {
                        g.drawImage(mask, x + j, y + k, j > width - 8 ? width % 8 : 8,
                                k > height - 8 ? height % 8 : 8, 0, 0);
                    }
                }
                g.setDrawMode(jcontrol.io.Graphics.NORMAL);
            }
            if (m_selectedIndex > 0) { // draw up-arrow
                g.drawImage(new String[]{"\u0808\u1C1C\u3E3E\u7F3E\u3E1C\u1C08\u0800"}, x
                        + (width - 13) >> 1, y, 13, 4, 0, 0);
            }
            if (m_selectedIndex < m_items.length - 1) { // draw down-arrow
                g.drawImage(new String[]{"\u0808\u1C1C\u3E3E\u7F3E\u3E1C\u1C08\u0800"}, x
                        + (width - 13) >> 1, y + height - 4, 13, 4, 0, 3);
            }
        }
        state &= ~STATE_DIRTY_MASK;
    }
}