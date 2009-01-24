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
import jcontrol.lang.Math;
import jcontrol.system.Management;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * Instances of this class represent a selectable user interface object that displays a list of
 * strings and issues notificiation when a string is selected via the keyboard or the touch screen.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class ListBox extends AbstractFocusComponent {

    /** The default scroll bar width. Value: 9, */
    private static final int SCROLL_BAR_WIDTH = 9;

    /** Style constant for neither border nor scroll bar look. Value: 0. */
    public static final int STYLE_NONE = 0;

    /** Style constant for a bordered look. */
    public static final int STYLE_SHOW_BORDER = RESERVED1;

    /** Style constant for a bordered look with scroll bar. */
    public static final int STYLE_SHOW_SCROLLBAR = RESERVED2 | STYLE_SHOW_BORDER;

    /**
     * Style constant for top alignment, i.e. the text is always aligned at the top of the ListBox.
     * Note that this may result in a blank area at the bottom of the ListBox even if the entire
     * number of lines exceeds the number of visible lines.
     */
    public static final int STYLE_ALIGN_TOP = STATE_SELECTED; // selected flag is not needed here

    /**
     * Style constant for center alignment, i.e. the text is always centered vertically. This is the
     * default alignment. Value: 0.
     */
    public static final int STYLE_ALIGN_CENTER = 0;

    /** The current selected item id. */
    private int m_selection = -1;

    /** The scroll value. */
    private int m_scrollValue;

    /** The list items. */
    private String[] m_items;

    /** The height of the font. */
    private int m_fontHeight;

    /**
     * Creates a list box.
     * 
     * @param x The x coordinate on the display.
     * @param y The y coordinate on the display.
     * @param width The text field width.
     * @param height The tet field height.
     * @param style STYLE_NONE or STYLE_SHOW_BORDER or STYLE_SHOW_SCROLLBAR or STYLE_ALIGN_TOP or
     *        STYLE_ALIGN_CENTER
     */
    public ListBox(int x, int y, int width, int height, int style) {
        this(null, x, y, width, height, style);
    }

    /**
     * Creates a list box.
     * 
     * @param items The text as String array.
     * @param x The x coordinate on the display.
     * @param y The y coordinate on the display.
     * @param width The text field width.
     * @param height The text field height.
     * @param style STYLE_NONE or STYLE_SHOW_BORDER or STYLE_SHOW_SCROLLBAR or STYLE_ALIGN_TOP or
     *        STYLE_ALIGN_CENTER
     */
    public ListBox(String[] items, int x, int y, int width, int height, int style) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        state |= (style & (STYLE_SHOW_SCROLLBAR | STYLE_SHOW_BORDER | STYLE_ALIGN_TOP));
        state |= STATE_REVALIDATE;
        m_items = new String[items != null ? items.length : 0];
        if (items != null) {
            Management.arraycopy(items, 0, m_items, 0, m_items.length);
            m_selection = 0;
        }
    }

    /**
     * Add a line to the end of the text field.
     * 
     * @param item The text to add.
     */
    public synchronized void add(String item) {
        String[] newItems = new String[m_items.length + 1];
        if (m_items.length > 0) Management.arraycopy(m_items, 0, newItems, 0, m_items.length);
        newItems[newItems.length - 1] = item;
        m_items = newItems;
        state |= STATE_REVALIDATE;
        if (m_selection < 0) m_selection = 0;
        redrawInternalAndParent();
    }

    /**
     * @param updown
     * @return xxx
     */
    private int findNextOrPrev(boolean updown) {
        synchronized (m_items) {
            if (updown) {
                for (int i = m_selection + 1; i < m_items.length; i++) {
                    if (!m_items[i].equals("-")) { return i; }
                }
            } else {
                for (int i = m_selection - 1; i >= 0; i--) {
                    if (!m_items[i].equals("-")) { return i; }
                }
            }
            return -1;
        }
    }

    /**
     * Returns the currently selected index.
     * 
     * @return the selected index
     */
    public synchronized int getSelectedIndex() {
        if (m_selection < m_items.length && m_selection >= 0) { return m_selection; }
        return -1;
    }

    /**
     * Returns the currently selected item.
     * 
     * @return the selected item
     */
    public synchronized String getSelectedItem() {
        if (m_selection < m_items.length && m_selection >= 0) { return m_items[m_selection]; }
        return null;
    }

    /**
     * Insert a line of text in the text field at the given zero-relative index.
     * 
     * @param line The line to insert.
     * @param item The text for the row.
     */
    public synchronized void insert(int line, String item) {
        String[] newItems = new String[m_items.length + 1];
        int count = 0;
        int i = 0;
        for (; i < m_items.length; i++) {
            if (count == line) {
                newItems[count++] = item;
            }
            newItems[count++] = m_items[i];
        }
        if (count == i) {
            newItems[count] = item;
        }
        if (m_selection < 0) m_selection = 0;
        m_items = newItems;
        state |= STATE_REVALIDATE;
        redrawInternalAndParent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        switch (e.m_key) {
            case KeyEvent.KEY_UP_PRESSED:
                int selection = findNextOrPrev(false);
                if (selection >= 0) {
                    setSelection(selection);
                }
                return null; // event consumed
            case KeyEvent.KEY_DOWN_PRESSED:
                selection = findNextOrPrev(true);
                if (selection >= 0) {
                    setSelection(selection);
                }
                return null; // event consumed
            case KeyEvent.KEY_SELECT_PRESSED: // user chooses the current item and exists the list
                if (listener != null && m_selection >= 0 && m_selection < m_items.length) {
                    onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                            m_items[m_selection]));
                }
                return null;
            default:
                return e; // let the Container manage this event
        }
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
            case RotaryTouchEvent.ROTATES_LEFT:
                int selection = findNextOrPrev(false);
                if (selection >= 0) {
                    setSelection(selection);
                }
                return null; // event consumed
            case RotaryTouchEvent.ROTATES_RIGHT:
                selection = findNextOrPrev(true);
                if (selection >= 0) {
                    setSelection(selection);
                }
                return null; // event consumed
            case RotaryTouchEvent.ROTATES_NONE: // user chooses the current item and exists the list
                if (e.getRotaryTouchState() == RotaryTouchEvent.STATE_IDLE) {
                    if (listener != null && m_selection >= 0 && m_selection < m_items.length) {
                        onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                                m_items[m_selection]));
                    }
                    return null;
                }
            default:
                return e; // let the Container manage this event
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * jcontrol.ui.viper.AbstractFocusComponent#onTouchEvent(jcontrol.ui.viper.event.TouchEvent)
     */
    @Override
    public int onTouchEvent(TouchEvent e) {
        if (m_fontHeight == 0) return RESULT_NONE; // should never happen

        switch (e.type) {
            case TouchEvent.TYPE_TOUCH_DRAGGED:
            case TouchEvent.TYPE_TOUCH_PRESSED:
                if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) { // touched inside
                    state |= STATE_FOCUS;
                    int visibleItems = (height - 2) / m_fontHeight;
                    if ((state & STYLE_SHOW_SCROLLBAR) == STYLE_SHOW_SCROLLBAR
                            && m_items.length > visibleItems) {
                        if (e.x > x + width - SCROLL_BAR_WIDTH) {
                            // clicked on scrollbar
                            int selection;
                            if (e.y < y + 9) {
                                selection = findNextOrPrev(false);

                            } else if (e.y > y + height - 9) {
                                selection = findNextOrPrev(true);

                            } else {
                                // touched between scroll buttons
                                selection = Math.scale(e.y - (y + 9), height - 18, m_items.length);
                                if (selection < 0 || selection >= m_items.length
                                        || m_items[selection].equals("-")) { return RESULT_ACCEPTED; }
                            }
                            if (selection >= 0) {
                                setSelection(selection);
                            }
                            return RESULT_EXECUTED;
                        }
                    }
                    int yoffset = 1;
                    if (visibleItems > m_items.length) {
                        visibleItems = m_items.length;
                        yoffset += (((height - 4) - visibleItems * m_fontHeight) >> 1);
                    } else {
                        yoffset += (((height - 4) % m_fontHeight) >> 1);
                    }
                    int selection;
                    if (e.y > y + height - 2 - yoffset) {
                        selection = visibleItems - 1 + m_scrollValue;
                    } else {
                        selection = ((e.y - (yoffset + y)) / m_fontHeight) + m_scrollValue;
                    }
                    if (e.type == TouchEvent.TYPE_TOUCH_PRESSED && m_selection == selection) {
                        // touched twice on same item
                        if (listener != null) {
                            onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                                    m_items[m_selection]));
                        }
                        return RESULT_EXECUTED;
                    }
                    if (selection < 0 || selection >= m_items.length
                            || m_items[selection].equals("-")) { return RESULT_ACCEPTED; }
                    setSelection(selection);
                    return RESULT_ACCEPTED;
                }
                state &= ~STATE_FOCUS;
                return RESULT_NONE;
            case TouchEvent.TYPE_TOUCH_RELEASED:
                if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) { return RESULT_ACCEPTED; }
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
                int visibleItems = (height - 4) / m_fontHeight;
                int yoffset = y + 2;
                if (visibleItems > m_items.length) {
                    visibleItems = m_items.length;
                    if ((state & STYLE_ALIGN_TOP) == 0) {
                        yoffset += (((height - 4) - visibleItems * m_fontHeight) >> 1);
                    }
                } else {
                    if ((state & STYLE_ALIGN_TOP) == 0) {
                        yoffset += (((height - 4) % m_fontHeight) >> 1);
                    }
                }
                if ((state & STATE_REVALIDATE) != 0) {
                    int selectionPos = visibleItems >> 1; // keep selection bar at center position
                    if (m_selection >= m_items.length) {
                        m_selection = m_items.length - 1;
                    }
                    if (m_selection < 0 && m_items.length > 0) {
                        m_selection = 0;
                    }
                    if (m_selection <= selectionPos) {
                        selectionPos = m_selection;
                    }
                    // check if scroll value is valid
                    if (m_selection < m_scrollValue + selectionPos) {
                        // scroll up
                        m_scrollValue = m_selection - selectionPos;
                    }
                    if (m_selection >= m_scrollValue + selectionPos) {
                        m_scrollValue = m_selection - selectionPos;
                    }
                    if (m_scrollValue > m_items.length - visibleItems) {
                        m_scrollValue = m_items.length - visibleItems;
                    }
                    if (m_scrollValue < 0) {
                        m_scrollValue = 0;
                    }
                }
                boolean showSB = m_items.length > visibleItems
                        && (state & STYLE_SHOW_SCROLLBAR) == STYLE_SHOW_SCROLLBAR;
                if ((state & STYLE_SHOW_BORDER) == STYLE_SHOW_BORDER) {
                    g.drawRect(x, y, showSB ? width - SCROLL_BAR_WIDTH + 1 : width, height);
                }
                {
                    for (int c = 0; c < visibleItems; c++) {
                        int h = m_fontHeight;
                        if ((yoffset + h - (y + 2)) > height - 4) {
                            h = height - 4 - yoffset + (y + 1);
                        }
                        g.clearRect(x + 2, yoffset, 1, h);
                        int w;
                        if (m_items[c + m_scrollValue].equals("-")) {
                            w = width - (showSB ? SCROLL_BAR_WIDTH + 5 : 6);
                            g.clearRect(x + 3, yoffset, w, m_fontHeight);
                            g.drawLine(x + 3, yoffset + (m_fontHeight >> 1), x + 3 + w, yoffset
                                    + (m_fontHeight >> 1));
                        } else {
                            w = g.drawString(m_items[c + m_scrollValue], x + 3, yoffset, width
                                    - (showSB ? SCROLL_BAR_WIDTH + 4 : 5), h, 0, 0);
                        }
                        g.clearRect(x + 2 + w, yoffset, width - (showSB ? SCROLL_BAR_WIDTH + 3 : 4)
                                - w, h);
                        if (m_selection == c + m_scrollValue) {
                            g.setDrawMode(Graphics.XOR);
                            g.fillRect(x + 2, yoffset, width - (showSB ? SCROLL_BAR_WIDTH + 3 : 4),
                                    h);
                            g.setDrawMode(Graphics.NORMAL);
                        }
                        yoffset += h;
                    }
                    if (yoffset < y + height - 4) {
                        g.clearRect(x + 2, yoffset, width - (showSB ? SCROLL_BAR_WIDTH + 3 : 4),
                                height - 4 - yoffset);
                    }
                    if ((state & STATE_FOCUS) == STATE_FOCUS) {
                        // draw selection bar
                        drawDottedRect(g, x + 1, y + 1,
                                width - (showSB ? SCROLL_BAR_WIDTH + 1 : 2), height - 2);
                    } else {
                        g.setDrawMode(Graphics.INVERSE);
                        g.drawRect(x + 1, y + 1, width - (showSB ? SCROLL_BAR_WIDTH + 1 : 2),
                                height - 2);
                        g.setDrawMode(Graphics.NORMAL);
                    }
                }
                g.setFont(null);
                if (showSB) {
                    // paint the scrollbar
                    int xoff;
                    g.drawImage(new String[]{"\u80C0\uC8C4\uC8C0\uFEFF"}, xoff = x + width
                            - SCROLL_BAR_WIDTH + 1, y + 1, 8, 8, 0, 0); // up
                    // arrow
                    g.drawImage(new String[]{"\u0181\u91A1\u9181\uFDFF"}, xoff, y + height - 9, 8,
                            8, 0, 0); // down
                    // arrow
                    g.drawLine(--xoff, y, x + width - 2, y); // top line
                    g.drawLine(xoff, y + height - 1, x + width - 2, y + height - 1); // bottom line
                    g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2);
                    // height - 25 pixels space
                    if (height >= 25) {
                        // draw a thumb
                        int scroll = m_scrollValue * 100 / (m_items.length - visibleItems);
                        scroll = y + 9 + (height - 26) * scroll / 100;
                        g.clearRect(++xoff, y + 9, 7, scroll - (y + 9));
                        g.drawImage(new String[]{"\u81C1\uD5D5\uD5C1\uFD00"}, xoff, scroll, 7, 8,
                                0, 0);
                        g.clearRect(xoff, scroll + 8, 7, y + height - scroll - 17);
                    }

                }
        }
        state &= ~(STATE_DIRTY_MASK | STATE_REVALIDATE);
    }

    /**
     * Removes a line from the text field.
     * 
     * @param line The line to remove.
     */
    public synchronized void remove(int line) {
        if (line >= 0 && line < m_items.length) {
            String[] newItems = new String[m_items.length - 1];
            int count = 0;
            for (int i = 0; i < m_items.length; i++) {
                if (i != line) {
                    newItems[count] = m_items[i];
                    count++;
                }
            }

            m_items = newItems;
            if (m_selection > m_items.length - 1) {
                m_selection--;
            }
            if (m_items.length == 0) {
                m_selection = -1;
            }
            state |= STATE_REVALIDATE; // should be recomputed on paint
            redrawInternalAndParent();
        }
    }

    /**
     * Selects the item at the given zero-relative index in the receiver's list. If the item at the
     * index was already selected, it remains selected. Indices that are out of range are ignored.
     * 
     * @param index the index of the item to select
     */
    public synchronized void setSelection(int index) {
        if (index >= 0 && index < m_items.length) {
            if (index != m_selection) {
                m_selection = index;
                state |= STATE_REVALIDATE;
                redrawInternalAndParent();
            }
        }
    }

}
