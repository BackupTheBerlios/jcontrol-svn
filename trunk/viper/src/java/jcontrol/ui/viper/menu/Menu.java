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

import jcontrol.system.Management;
import jcontrol.ui.viper.AbstractFocusComponent;

/**
 * <p>
 * The abstract class <code>Menu</code> is the superclass for graphical menus.
 * </p>
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public abstract class Menu extends AbstractFocusComponent {

    protected int m_selectedIndex = -1;
    protected int m_scrollValue = 0;
    protected String[] m_items; // list of item names
    protected boolean[] m_inactives; // list of inactive flags

    public Menu(String[] items, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        m_items = new String[items != null ? items.length : 0];
        m_inactives = new boolean[m_items.length];
        if (items != null) Management.arraycopy(items, 0, m_items, 0, m_items.length);
        state |= STATE_REVALIDATE; // should be recomputed on paint
    }

    /**
     * Adds a menu item to this menu. This method should be used with care. Initializing a menu with
     * a list of menu items should always be done via the constructor instead. Adding or removing
     * items in a menu will cause an array copy which may lead to a lot of memory consumption for a
     * short period of time.
     * 
     * @param item the name of the menu item, can even specify an image name
     * @return the index of the new menu item
     * @see #insertMenuItem(String, int)
     */
    public int addMenuItem(String item) {
        {
            String[] newItems = new String[m_items.length + 1];
            if (m_items.length > 0) Management.arraycopy(m_items, 0, newItems, 0, m_items.length);
            newItems[newItems.length - 1] = item;
            m_items = newItems;
        }
        {
            boolean[] newInactives = new boolean[m_items.length];
            if (newInactives.length > 0)
                Management.arraycopy(m_inactives, 0, newInactives, 0, m_inactives.length);
            m_inactives = newInactives;
        }
        state |= STATE_REVALIDATE; // should be recomputed on paint
        redrawInternalAndParent();
        return m_items.length - 1;
    }

    /**
     * Returns the index of the specified item or <code>-1</code> if there is no such item in this
     * menu.
     * 
     * @param item the item to search for
     * @return the index of the specified item
     */
    public int getIndex(String item) {
        for (int i = 0; i < m_items.length; i++) {
            if (m_items[i].equals(item)) return i;
        }
        return -1;
    }

    /**
     * Returns the item at the specified index or <code>null</code> if the index is out of range.
     * 
     * @param index the index
     * @return the item at the specified index
     */
    public String getItem(int index) {
        if (index >= 0 && index < m_items.length) { return m_items[index]; }
        return null;
    }

    /**
     * Returns the number of menu items.
     * 
     * @return The numer of menu items.
     */
    public int getItemCount() {
        return m_items.length;
    }

    /**
     * Returns the index of the selected menu item or -1 if none is selected.
     * 
     * @return The index of the selected menu item.
     */
    public int getSelectedIndex() {
        return m_selectedIndex;
    }

    /**
     * Inserts a menu item at the specified position. This method should be used with care.
     * Initializing a menu with a list of menu items should always be done via the constructor
     * instead. Adding or removing items in a menu will cause an array copy which may lead to a lot
     * of memory consumption for a short period of time.
     * 
     * @param item the name of the menu item, can even specify an image name
     * @param index the position
     * @return the index of the new menu item
     * @see #addMenuItem(String)
     */
    public int insertMenuItem(String item, int index) {
        String[] newItems = new String[m_items.length + 1];
        boolean[] newInactives = new boolean[m_items.length + 1];
        int count = 0;
        int i = 0;
        for (; i < m_items.length; i++) {
            if (count == index) {
                newItems[count++] = item;
            }
            newInactives[count] = m_inactives[i];
            newItems[count++] = m_items[i];
        }
        if (count == i) {
            newItems[count] = item;
            newInactives[count] = m_inactives[i];
        }
        m_items = newItems;
        m_inactives = newInactives;
        state |= STATE_REVALIDATE;
        redrawInternalAndParent();
        return index;
    }

    /**
     * Removes a menu at the specified index item from this menu. This method should be used with
     * care. Adding or removing items in a menu will cause an array copy which may lead to a lot of
     * memory consumption for a short period of time. If a menu item should be removed temporary it
     * should better be <i>disabled</i> using the <code>setEnabled(int, boolean)</code> method
     * instead.
     * 
     * @param index the index to remove
     * @return boolean <code>true</code> if the item has been removed successfully,
     *         <code>false</code> otherwise
     * @see #setEnabled(int, boolean)
     */
    public boolean removeMenuItem(int index) {
        if (index >= 0 && index < m_items.length) {
            String[] newItems = new String[m_items.length - 1];
            boolean[] newInactives = new boolean[m_items.length - 1];
            int count = 0;
            for (int i = 0; i < m_items.length; i++) {
                if (i != index) {
                    newItems[count] = m_items[i];
                    newInactives[count] = m_inactives[i];
                    count++;
                }
            }

            m_items = newItems;
            m_inactives = newInactives;
            if (m_selectedIndex > m_items.length - 1) {
                m_selectedIndex--;
            }
            if (m_items.length == 0) {
                m_selectedIndex = -1;
            }
            state |= STATE_REVALIDATE; // should be recomputed on paint
            redrawInternalAndParent();
            return true;
        }
        return false;
    }

    /**
     * Selects the next or previous menu item in this menu.
     * <P>
     * 
     * @param updown if true, select the next item, the previous otherwise
     * @return true if the selection has changed
     */
    protected boolean selectNextOrPrev(boolean updown) {
        synchronized (m_items) {
            if (updown) {
                for (int i = m_selectedIndex + 1; i < m_items.length; i++) {
                    if (!m_inactives[i]) {
                        m_selectedIndex = i;
                        return true;
                    }
                }
            } else {
                for (int i = m_selectedIndex - 1; i >= 0; i--) {
                    if (!m_inactives[i]) {
                        m_selectedIndex = i;
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Enables or disables the menu item at the specified index. A disabled menu item is not
     * selectable from the user interface and draws with an inactive or "grayed" look.
     * 
     * @param index the index to enable or disable
     * @param enabled the new enabled state
     */
    public void setEnabled(int index, boolean enabled) {
        if (index >= 0 && index < m_inactives.length) {
            if (m_inactives[index] == enabled) {
                m_inactives[index] = !enabled;
                if (index == m_selectedIndex) {
                    m_selectedIndex = -1;
                }
                redrawInternalAndParent();
            }
        }
    }

    /**
     * Selects the item at the given zero-relative index in the receiver's list. If the item at the
     * index was already selected, it remains selected. Indices that are out of range are ignored.
     * 
     * @param index the index of the item to select
     * @return boolean <code>true</code> if the item has been selected successfully,
     *         <code>false</code> otherwise
     */
    public boolean setSelection(int index) {
        if (index >= 0 && index < m_items.length && m_selectedIndex != index) {
            if (m_items[index] == null || m_inactives[index]) return false;
            m_selectedIndex = index;
            redrawInternalAndParent();
            return true;
        } else {
            return false;
        }
    }

}