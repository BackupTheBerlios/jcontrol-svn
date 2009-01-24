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
import jcontrol.system.Management;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * This class <CODE>TextViewer</CODE> implements a component to show multiple lines of text.<br>
 * The text can be scrolled, lines can be added, inserted and deleted.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class TextViewer extends AbstractFocusComponent {

    /** The default scroll bar width. Value: 9. */
    private static final int SCROLL_BAR_WIDTH = 9;

    /** Style constant for neither border nor scroll bar look. Value: 0. */
    public static final int STYLE_NONE = 0;

    /** Style constant for a bordered look. */
    public static final int STYLE_SHOW_BORDER = RESERVED1;

    /** Style constant for a bordered look with scroll bar. */
    public static final int STYLE_SHOW_SCROLLBAR = RESERVED2 | STYLE_SHOW_BORDER;

    /** The scroll value. */
    private int m_scrollValue;

    /** The items. */
    private String[] m_items;

    /** */
    private int m_lastYTouch = -1;

    /**
     * Creates an text viewer.
     * 
     * @param x The x coordinate on the display.
     * @param y The y coordinate on the display.
     * @param width The text field width.
     * @param height The text field height.
     * @param style STYLE_NONE or STYLE_SHOW_BORDER or STYLE_SHOW_SCROLLBAR
     */
    public TextViewer(int x, int y, int width, int height, int style) {
        this(null, x, y, width, height, style);
    }

    /**
     * Creates an text viewer.
     * 
     * @param items The text as String array.
     * @param x The x coordinate on the display.
     * @param y The y coordinate on the display.
     * @param width The text field width.
     * @param height The text field height.
     * @param style STYLE_NONE or STYLE_SHOW_BORDER or STYLE_SHOW_SCROLLBAR
     */
    public TextViewer(String[] items, int x, int y, int width, int height, int style) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        state |= (style & (STYLE_SHOW_SCROLLBAR | STYLE_SHOW_BORDER));
        state |= STATE_REVALIDATE;
        m_items = new String[items != null ? items.length : 0];
        if (items != null) Management.arraycopy(items, 0, m_items, 0, m_items.length);
    }

    /**
     * Add a row to the end of the text field.
     * 
     * @param item The text to add.
     */
    public synchronized void add(String item) {
        String[] newItems = new String[m_items.length + 1];
        if (m_items.length > 0) Management.arraycopy(m_items, 0, newItems, 0, m_items.length);
        newItems[newItems.length - 1] = item;
        m_items = newItems;
        state |= STATE_REVALIDATE;
        redrawInternalAndParent();
    }

    /**
     * Returns the number of lines in this TextViewer.
     * 
     * @return the number of lines
     */
    public int getLineCount() {
        return m_items.length;
    }

    /**
     * Returns the current scroll value.
     * 
     * @return the current scroll value.
     */
    public int getScrollValue() {
        return m_scrollValue;
    }

    /**
     * Insert a line of text in the text field.
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
                setScrollValue(m_scrollValue - 1);
                return null; // event consumed
            case KeyEvent.KEY_DOWN_PRESSED:
                setScrollValue(m_scrollValue + 1);
                return null; // event consumed
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
                setScrollValue(m_scrollValue - 1);
                return null; // event consumed
            case RotaryTouchEvent.ROTATES_RIGHT:
                setScrollValue(m_scrollValue + 1);
                return null; // event consumed
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
        switch (e.type) {
            case TouchEvent.TYPE_TOUCH_PRESSED:
                if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) { // touched inside
                    if ((state & STYLE_SHOW_SCROLLBAR) == STYLE_SHOW_SCROLLBAR) {
                        if (e.x > x + width - SCROLL_BAR_WIDTH) {
                            // clicked on scrollbar
                            if (e.y < y + 9) {
                                setScrollValue(m_scrollValue - 1);
                                return RESULT_ACCEPTED;
                            } else if (e.y > y + height - 9) {
                                setScrollValue(m_scrollValue + 1);
                                return RESULT_ACCEPTED;
                            }
                        }
                    }
                    if ((state & STATE_SELECTED) == 0) { // initial press
                        state |= STATE_SELECTED;

                        m_lastYTouch = e.y;
                        return RESULT_ACCEPTED;
                    }
                } else {
                    state &= ~STATE_SELECTED;
                }
                return RESULT_NONE;
            case TouchEvent.TYPE_TOUCH_DRAGGED:
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    if (e.y < m_lastYTouch) {
                        m_lastYTouch = e.y;
                        setScrollValue(m_scrollValue - 1);
                        return RESULT_ACCEPTED;
                    } else if (e.y > m_lastYTouch) {
                        m_lastYTouch = e.y;
                        setScrollValue(m_scrollValue + 1);
                        return RESULT_ACCEPTED;
                    }
                }
                return RESULT_NONE;
            case TouchEvent.TYPE_TOUCH_RELEASED:
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    state &= ~STATE_SELECTED;
                    if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) { return RESULT_ACCEPTED; }
                }
                return RESULT_NONE;
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
                int fontHeight = g.getFontHeight();
                int visibleItems = (height - 4) / fontHeight;
                int yoffset = y + 2;
                if (visibleItems > m_items.length) {
                    visibleItems = m_items.length;
                } else {
                    yoffset += (((height - 4) % fontHeight) >> 1);
                }
                if ((state & STATE_REVALIDATE) != 0) {
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
                        int h = fontHeight;
                        if ((yoffset + h - (y + 2)) > height - 4) {
                            h = height - 4 - yoffset + (y + 1);
                        }
                        g.clearRect(x + 2, yoffset, 1, h);
                        int w = g.drawString(m_items[c + m_scrollValue], x + 3, yoffset, width
                                - (showSB ? SCROLL_BAR_WIDTH + 3 : 4), h, 0, 0);
                        g.clearRect(x + 2 + w, yoffset, width - (showSB ? SCROLL_BAR_WIDTH + 3 : 4)
                                - w, h);
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
            state |= STATE_REVALIDATE; // should be recomputed on paint
            redrawInternalAndParent();
        }
    }

    /**
     * Sets the absolute scroll position of the text area.
     * 
     * @param scrollValue The new scroll position.
     */
    public void setScrollValue(int scrollValue) {
        if (scrollValue >= 0 && scrollValue < m_items.length) {
            m_scrollValue = scrollValue;
            state |= STATE_REVALIDATE;
            redrawInternalAndParent();
        }
    }

}
