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

import jcontrol.io.Display;
import jcontrol.io.Graphics;
import jcontrol.lang.Math;
import jcontrol.system.Management;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * The class <code>ComboBox</code> implements a choice user interface object. Use the select-, up-
 * and down-keys on the keyboard or the touch screen to open the combo box and to select an item.
 * </p>
 * When an item is selected, an <code>ActionEvent</code> of type
 * <code>ActionEvent.ITEM_SELECTED</code> is fired. </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class ComboBox extends AbstractFocusComponent {

    /** The box offset. Value: 2. */
    private static final int BOX_OFFSET = 2;

    /** Default scroll bar width. Value: 9. */
    private static final int SCROLL_BAR_WIDTH = 9;

    /** */
    private static final int STATE_OPEN_PAINTED = RESERVED2;

    /** */
    private static final int STATE_OPEN_UPWARDS = RESERVED1;

    /** The height of the box. */
    private int m_boxHeight;

    /** The current item. */
    private int m_currentItem = -1;

    /** The height of the font. */
    private int m_fontHeight;

    /** The combo box items. */
    private String[] m_items;

    /** The scroll value. */
    private int m_scrollValue = 0;

    /**
     * Creates a new empty <code>ComboBox</code> at the given x- and y-coordinates. The preferred
     * width and height is internally computed by taking the contained items dimensions.
     * 
     * @param x The x-coordinate of this <code>ComboBox</code>.
     * @param y The y-coordinate of this <code>ComboBox</code>.
     * @param width The preferred width of this <code>ComboBox</code>. The ComboBox is enlarged if
     *        the text width exceeds this value.
     */
    public ComboBox(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        state |= STATE_WIDTH_FIXED;
        state |= STATE_REVALIDATE; // should be recomputed on paint
        m_items = new String[0];
    }

    /**
     * Creates a new <code>ComboBox</code> with a list of items at the given x- and y-coordinates
     * with the specified width and height.
     * 
     * @param items An array of text-items.
     * @param x The x-coordinate of this <code>ComboBox</code>.
     * @param y The y-coordinate of this <code>ComboBox</code>.
     * @param width The width of this <code>ComboBox</code>.
     */
    public ComboBox(String[] items, int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
        setItems(items);
    }

    /**
     * Adds an item to this <code>ComboBox</code>.
     * 
     * @param item The item to add to the <code>ComboBox</code>.
     */
    public synchronized void add(String item) {
        String[] newItems = new String[m_items.length + 1];
        if (m_items.length > 0) Management.arraycopy(m_items, 0, newItems, 0, m_items.length);
        newItems[newItems.length - 1] = item;
        m_items = newItems;
        state |= STATE_REVALIDATE; // should be recomputed on paint
        if (m_currentItem < 0) m_currentItem = 0;
        redrawInternalAndParent();
    }

    /**
     * Close the combo box.
     */
    private synchronized void close(boolean forceRepaint) {
        state &= ~STATE_SELECTED;
        IFrame root = getFrame();
        if (root != null) {
            int previousState = state;
            int boxY = (state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS ? y - m_boxHeight + 1 : y
                    + height;
            parent.setDirty(this, x, boxY, width, m_boxHeight - 1, STATE_COVERED, false);
            state = previousState; // maybe this combobox is covered by another combobox
        }
        if (forceRepaint) {
            setDirty(STATE_DIRTY_PAINT_ALL, true);
        } else {
            state |= STATE_DIRTY_PAINT_ALL;
        }
    }

    /**
     * Returns the currently selected index.
     * 
     * @return the selected index
     */
    public synchronized int getSelectedIndex() {
        if (m_currentItem < m_items.length && m_currentItem >= 0) { return m_currentItem; }
        return -1;
    }

    /**
     * Returns the currently selected item
     * 
     * @return the selected item
     */
    public synchronized String getSelectedItem() {
        if (m_currentItem < m_items.length && m_currentItem >= 0) { return m_items[m_currentItem]; }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        // user opens the combo box or makes a choice
        if ((e.m_key == KeyEvent.KEY_SELECT_PRESSED)) {
            if ((state & STATE_SELECTED) != 0) { // user makes a choice
                int item = m_currentItem;
                close(false);
                m_currentItem = item;
                if (listener != null && m_currentItem >= 0 && m_currentItem < m_items.length) {
                    onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                            m_items[m_currentItem]));
                }
            } else if (m_items.length > 0) { // user opens the combo box
                state |= STATE_SELECTED | STATE_DIRTY_REPAINT;
                int previousState = state;

                if (parent != null) { // so even root container is null
                    int boxY = (state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS ? y - m_boxHeight
                            + 1 : y + height;
                    parent.setDirty(this, x, boxY, width, m_boxHeight - 1, STATE_COVERED, true);
                }
                state = previousState; // state has been overwritten with STATE_COVERED
                // but may not simply be reset to ~STATE_COVERED because
                // it maybe is really covered by another combobox or whatever

            }
            return null;
        } else if (e.m_key == KeyEvent.KEY_UP_PRESSED) {
            // move to previous item
            if ((state & STATE_SELECTED) == 0) return e; // let the container transfer the focus
            m_currentItem--;
            if (m_currentItem < 0) {
                close(false);
            } else {
                state |= STATE_DIRTY_REPAINT;
            }
            return null;
        } else if (e.m_key == KeyEvent.KEY_DOWN_PRESSED) {
            // move to next item
            if ((state & STATE_SELECTED) == 0) return e; // let the container transfer the focus
            m_currentItem++;
            if (m_currentItem > m_items.length - 1) {
                m_currentItem = m_items.length - 1;
            } else {
                state |= STATE_DIRTY_REPAINT;
            }
            return null;
        } else if ((state & STATE_SELECTED) != 0 && (e.m_key & KeyEvent.TYPE_KEY_PRESSED) != 0) {
            // user leaves box without making a selection
            close(false);
            return e;
        } else { // don't consume this event
            return e;
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
        // user opens the combo box or makes a choice
        if ((e.getRotaryTouchState() == RotaryTouchEvent.STATE_TOUCHED)) {
            if ((state & STATE_SELECTED) != 0) { // user makes a choice
                int item = m_currentItem;
                close(false);
                m_currentItem = item;
                if (listener != null && m_currentItem >= 0 && m_currentItem < m_items.length) {
                    onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                            m_items[m_currentItem]));
                }
            } else if (m_items.length > 0) { // user opens the combo box
                state |= STATE_SELECTED | STATE_DIRTY_REPAINT;
                int previousState = state;

                if (parent != null) { // so even root container is null
                    int boxY = (state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS ? y - m_boxHeight
                            + 1 : y + height;
                    parent.setDirty(this, x, boxY, width, m_boxHeight - 1, STATE_COVERED, true);
                }
                state = previousState; // state has been overwritten with STATE_COVERED
                // but may not simply be reset to ~STATE_COVERED because
                // it maybe is really covered by another combobox or whatever

            }
            return null;
        } else if (e.getRotaryTouchState() == RotaryTouchEvent.ROTATES_LEFT) {
            // move to previous item
            if ((state & STATE_SELECTED) == 0) return e; // let the container transfer the focus
            m_currentItem--;
            if (m_currentItem < 0) {
                close(false);
            } else {
                state |= STATE_DIRTY_REPAINT;
            }
            return null;
        } else if (e.getRotaryTouchState() == RotaryTouchEvent.ROTATES_RIGHT) {
            // move to next item
            if ((state & STATE_SELECTED) == 0) return e; // let the container transfer the focus
            m_currentItem++;
            if (m_currentItem > m_items.length - 1) {
                m_currentItem = m_items.length - 1;
            } else {
                state |= STATE_DIRTY_REPAINT;
            }
            return null;
        } else if ((state & STATE_SELECTED) != 0 && (e.getRotaryTouchState() != 0)) {
            // user leaves box without making a selection
            close(false);
            return e;
        } else { // don't consume this event
            return e;
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
                if ((state & STATE_SELECTED) == 0) {
                    // not open yet
                    if (e.type == TouchEvent.TYPE_TOUCH_PRESSED && e.x >= x && e.x < x + width
                            && e.y >= y && e.y < y + height) { // touched
                        // inside
                        // open the combo box
                        state |= STATE_SELECTED | STATE_DIRTY_REPAINT;
                        int previousState = state;

                        if (parent != null) { // so even root container is !null
                            if ((state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS) {
                                int boxY = y - m_boxHeight + BOX_OFFSET;
                                parent.setDirty(this, x, boxY, width, m_boxHeight - BOX_OFFSET,
                                        STATE_COVERED, true);
                            } else {
                                int boxY = y + height - BOX_OFFSET;
                                parent.setDirty(this, x, boxY + 1, width, m_boxHeight - BOX_OFFSET,
                                        STATE_COVERED, true);
                            }
                        }
                        state = previousState; // state has been overwritten with STATE_COVERED
                        // but may not simply be reset to ~STATE_COVERED because
                        // it maybe is really covered by another combobox or whatever

                        return RESULT_ACCEPTED;
                    }
                    return RESULT_NONE;
                } else {
                    // combo box is open
                    if (e.x >= x && e.x < x + width) {
                        int boxY = (state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS ? y
                                - m_boxHeight + BOX_OFFSET : y + height - BOX_OFFSET;
                        if (e.y >= y && e.y < y + height) { // touched inside main area
                            if (e.type == TouchEvent.TYPE_TOUCH_PRESSED) {
                                close(false); // touched on top area
                                return RESULT_ACCEPTED;
                            } else {
                                return RESULT_ACCEPTED;
                            }
                        } else if (e.y > boxY + BORDER_WIDTH
                                && e.y < boxY + m_boxHeight - BORDER_WIDTH) {
                            // maybe inside selection area
                            int visibleItems = (m_boxHeight - (BORDER_WIDTH << 1)) / m_fontHeight;
                            int m_scrollBarSize = m_fontHeight < 9 ? 9 : m_fontHeight;
                            if (m_items.length > visibleItems
                                    && e.x > x + width - m_scrollBarSize - BORDER_WIDTH) {
                                // clicked on scrollbar
                                if (e.y < boxY + m_scrollBarSize + BORDER_WIDTH) {
                                    if (m_currentItem > 0) {
                                        m_currentItem--;
                                        state |= STATE_DIRTY_REPAINT;
                                        // setDirty(STATE_DIRTY_REPAINT, true);
                                    }
                                } else if (e.y > boxY + m_boxHeight - m_scrollBarSize
                                        - BORDER_WIDTH) {
                                    if (m_currentItem < m_items.length - 1) {
                                        m_currentItem++;
                                        state |= STATE_DIRTY_REPAINT;
                                        // setDirty(STATE_DIRTY_REPAINT, true);
                                    }
                                } else {
                                    // touched between scroll buttons
                                    int newItem = Math.scale(e.y
                                            - (boxY + m_scrollBarSize + BORDER_WIDTH), m_boxHeight
                                            - (m_scrollBarSize * 3) + (BORDER_WIDTH << 1),
                                            m_items.length);
                                    if (newItem != m_currentItem && newItem >= 0
                                            && newItem < m_items.length) {
                                        m_currentItem = newItem;
                                        state |= STATE_DIRTY_REPAINT;
                                        // setDirty(STATE_DIRTY_REPAINT, true);
                                    }
                                }
                                return RESULT_ACCEPTED;
                            }
                            int newItem = (((e.y - boxY + BORDER_WIDTH) / m_fontHeight) + m_scrollValue);
                            if (newItem < 0) {
                                newItem = 0;
                            } else if (newItem >= m_items.length) {
                                newItem = m_items.length - 1;
                            }
                            if (newItem != m_currentItem) {
                                m_currentItem = newItem;
                                state |= STATE_DIRTY_REPAINT;
                                // setDirty(STATE_DIRTY_REPAINT, true);
                            } else {
                                if (e.type == TouchEvent.TYPE_TOUCH_PRESSED) {
                                    close(false); // touched on the current selection
                                    onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                                            m_items[m_currentItem]));
                                }
                            }
                            return RESULT_EXECUTED;
                        }
                    }
                    // touched or dragged beside open combo box
                    close(false);
                    return RESULT_NONE;
                }
            case TouchEvent.TYPE_TOUCH_RELEASED:
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    int boxY = (state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS ? y - m_boxHeight
                            + BOX_OFFSET : y + height - BOX_OFFSET;
                    if (e.x >= x && e.x < x + width && e.y >= boxY && e.y < boxY + m_boxHeight) {
                        // released inside selection area
                        int visibleItems = (m_boxHeight - (BORDER_WIDTH << 1)) / m_fontHeight;
                        int m_scrollBarSize = m_fontHeight < 9 ? 9 : m_fontHeight;
                        if (m_items.length > visibleItems
                                && e.x > x + width - m_scrollBarSize - BORDER_WIDTH) {
                            // released on scrollbar
                        } else {
                            close(false);
                            onActionEvent(new ActionEvent(this, ActionEvent.ITEM_SELECTED,
                                    m_items[m_currentItem]));
                            return RESULT_EXECUTED;
                        }
                    }
                    return RESULT_ACCEPTED;
                }
                break;
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
        if (m_currentItem < 0 && m_items.length > 0) {
            m_currentItem = 0;
        }
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
            g.clearRect(x, y, width, height);
        }
        int boxY;
        if ((state & STATE_SELECTED) != STATE_SELECTED
                && (state & STATE_OPEN_PAINTED) == STATE_OPEN_PAINTED && parent != null) {
            // combobox has been closed but is still painted open
            if ((state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS) {
                boxY = y - m_boxHeight + BOX_OFFSET - 1;
                parent.setDirty(this, x, boxY, width, m_boxHeight - BOX_OFFSET + 1,
                        STATE_DIRTY_REPAINT, true);
                g.clearRect(x, boxY, width, m_boxHeight - BOX_OFFSET + 1);
            } else {
                boxY = y + height - BOX_OFFSET;
                parent.setDirty(this, x, boxY + 1, width, m_boxHeight - BOX_OFFSET,
                        STATE_DIRTY_REPAINT, true);
                g.clearRect(x, boxY + BOX_OFFSET, width, m_boxHeight - BOX_OFFSET + 1);
            }
            state &= ~STATE_OPEN_PAINTED;
            state |= STATE_ABORT_UPDATE;
            return;
        }
        boxY = (state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS ? y - m_boxHeight + 1 : y
                + height - 1;
        g.setFont(font);
        m_fontHeight = g.getFontHeight();
        int m_scrollBarSize = m_fontHeight < 9 ? 9 : m_fontHeight;
        if (width == 0 || height == 0 || (state & STATE_REVALIDATE) != 0) {
            // validate
            if ((state & STATE_WIDTH_FIXED) == 0 || width <= 0) {
                if (width < SCROLL_BAR_WIDTH + 2) {
                    width = SCROLL_BAR_WIDTH + 2;
                }
                for (int i = 0; i < m_items.length; i++) {
                    int w = m_items[i] != null ? g.getTextWidth(m_items[i])
                            : 0 + (SCROLL_BAR_WIDTH + 2);
                    if (width < w) {
                        width = w;
                    }
                }
            }
            height = m_fontHeight + (BORDER_WIDTH << 1);
            m_boxHeight = (BORDER_WIDTH << 1) + (m_fontHeight * m_items.length);
            Component root = (Component) getFrame();
            if (root != null) {
                if (y + height + m_boxHeight - BOX_OFFSET > root.y + root.height) {
                    // decide the direction to open
                    int space = root.y + root.height - y - (BOX_OFFSET + height);
                    if ((space / m_fontHeight) > 3) {
                        // open downwards with scrollbar
                        state &= ~STATE_OPEN_UPWARDS;
                        m_boxHeight = space;
                        m_boxHeight /= m_fontHeight;
                        m_boxHeight *= m_fontHeight; // abrunden
                        m_boxHeight += (BORDER_WIDTH << 1);
                    } else {
                        // open upwards
                        state |= STATE_OPEN_UPWARDS;
                        if (y - m_boxHeight + BOX_OFFSET < root.y) {
                            // scroll items upwards
                            m_boxHeight = y - root.y;
                            m_boxHeight /= m_fontHeight;
                            m_boxHeight *= m_fontHeight; // abrunden
                            m_boxHeight += (BORDER_WIDTH << 1);
                        }
                    }
                }
            }
            state &= ~STATE_REVALIDATE;
        }
        {
            g.clearRect(x + BORDER_WIDTH, y + BORDER_WIDTH, 1, height - (BORDER_WIDTH << 1)); // clear
            // area
            // before
            // text
            int textWidth = 0;
            if (m_items[m_currentItem] != null && m_currentItem >= 0
                    && m_currentItem < m_items.length) { // draw text
                textWidth = g.drawString(m_items[m_currentItem], x + BORDER_WIDTH + 1, y
                        + BORDER_WIDTH, width - m_scrollBarSize - ((BORDER_WIDTH << 1) + 1), -1, 0,
                        0);
            }
            // clear area behind text
            g.clearRect(x + BORDER_WIDTH + textWidth, y + BORDER_WIDTH, width - textWidth
                    - m_scrollBarSize - (BORDER_WIDTH << 1), height - (BORDER_WIDTH << 1));
        }
        // draw rect
        g.drawLine(x + 1, y, x + width - 2, y); // top line
        g.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1); // bottom line
        g.drawLine(x, y + 1, x, y + height - 2); // left line
        g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2); // right line
        { // draw the arrow button
            int xoff = x + width - SCROLL_BAR_WIDTH - 1;
            g.drawLine(xoff, y + 1, xoff, y + height - 2); // center line
            xoff++;
            g.drawImage(new String[]{"\u0008\u1838\u1808\u0000"}, xoff, y + 1, 7, height < 10 ? 6
                    : 8, 0, height < 10 ? 1 : 0);
            g.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 3);
            g.drawLine(xoff + 1, y + height - 2, xoff + 7, y + height - 2);
            if (height > 10) {
                g.clearRect(xoff, y + 8, 7, height - 10);
            }
        }
        if (((state & STATE_FOCUS) == STATE_FOCUS)) {
            g.setDrawMode(Display.XOR);
            g.fillRect(x + 1, y + 1, width - SCROLL_BAR_WIDTH - 2, height - 2);
            g.setDrawMode(Display.NORMAL);
        }
        if ((state & STATE_SELECTED) == STATE_SELECTED) {
            state |= STATE_OPEN_PAINTED;
            // the ComboBox is open
            int visibleItems = (m_boxHeight - (BORDER_WIDTH << 1)) / m_fontHeight;
            if (m_currentItem >= m_items.length) {
                m_currentItem = m_items.length - 1;
            }
            if (m_currentItem < 0 && m_items.length > 0) {
                m_currentItem = 0;
            }
            // check if scroll value is valid
            if (m_currentItem < m_scrollValue) {
                // scroll up
                m_scrollValue = m_currentItem;
            }
            if (m_currentItem >= m_scrollValue + visibleItems) {
                m_scrollValue = m_currentItem - visibleItems + 1;
            }
            if (m_scrollValue > m_items.length - visibleItems) {
                m_scrollValue = m_items.length - visibleItems;
            }
            if (m_scrollValue < 0) {
                m_scrollValue = 0;
            }

            boolean showSB = visibleItems < m_items.length;
            {
                int yoffset = boxY + 1; // top line of selection box+1
                g.drawLine(x, yoffset, x, boxY + m_boxHeight - 2); // left line
                if (!showSB)
                    g.drawLine(x + width - 1, yoffset, x + width - 1, boxY + m_boxHeight - 2); // right
                // line
                if ((state & STATE_OPEN_UPWARDS) == STATE_OPEN_UPWARDS) {
                    g.drawLine(x + 1, yoffset - 1, x + width - 2, yoffset - 1); // top line
                } else {
                    g.drawLine(x + 1, y + height + m_boxHeight - 2, x + width - 2, y + height
                            + m_boxHeight - 2); // bottom
                    // line
                }
                // draw all items
                for (int c = 0; c < visibleItems; c++) {
                    g.clearRect(x + 1, yoffset, 2, m_fontHeight); // clear area before text
                    int w = g.drawString(m_items[c + m_scrollValue] != null ? (String) m_items[c
                            + m_scrollValue] : "", x + 2, yoffset, width
                            - (showSB ? SCROLL_BAR_WIDTH + 1 : 2), m_fontHeight, 0, 0);
                    g.clearRect(x + 1 + w, yoffset,
                            width - (showSB ? SCROLL_BAR_WIDTH + 1 : 2) - w, m_fontHeight); // clear
                    // area
                    // behind
                    // text
                    if (c + m_scrollValue == m_currentItem) { // draw selection bar
                        g.setDrawMode(Display.XOR);
                        g.fillRect(x + 1, yoffset, width - (showSB ? SCROLL_BAR_WIDTH + 1 : 2),
                                m_fontHeight);
                        g.setDrawMode(Display.NORMAL);
                    }
                    yoffset += m_fontHeight;
                }
            }
            if (showSB) {
                int xoff;
                // up arrow
                g.drawImage(new String[]{"\uFF80\uC0C8\uC4C8\uC0FE\uFF00"}, xoff = width + x
                        - SCROLL_BAR_WIDTH, boxY + 1, 9, 8, 0, 0);
                // down arrow
                g.drawImage(new String[]{"\uFF01\u8191\uA191\u81FD\uFF00"}, xoff, boxY
                        + m_boxHeight - 9, 9, 8, 0, 0);

                // left line beside slider
                g.drawLine(xoff = width + x - SCROLL_BAR_WIDTH, boxY + 9, xoff, boxY + m_boxHeight
                        - 9);
                // right line beside slider
                g.drawLine(xoff = x + width - 1, boxY + 9, xoff, boxY + m_boxHeight - 9);

                xoff = x + width - 8;
                if (m_boxHeight > 26) {
                    int yoff = (m_scrollValue * (m_boxHeight - 26) / (m_items.length - visibleItems));
                    g.clearRect(xoff, boxY + 9, 7, yoff);
                    // slider
                    g.drawImage(new String[]{"\u81C1\uD5D5\uD5C1\uFD00"}, xoff, yoff + boxY + 9, 7,
                            8, 0, 0);
                    g.clearRect(xoff, yoff + boxY + 17, 7, m_boxHeight - yoff - 26);
                } else {
                    g.clearRect(xoff, boxY + 9, 7, m_boxHeight - 17);
                }
            }
        } else {
            state &= ~STATE_OPEN_PAINTED;
        }
        g.setFont(null);
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Removes the first occurrence of <code>item</code> from the <code>ComboBox</code>.
     * 
     * @param item The item to remove from the <code>ComboBox</code>.
     * @return Success of remove operation.
     */
    public synchronized boolean remove(String item) {
        String[] newItems = new String[m_items.length - 1];
        int count = 0;
        for (int i = 0; i < m_items.length; i++) {
            if (m_items[i] != item) {
                newItems[count] = m_items[i];
                count++;
            }
        }
        if (count == m_items.length) { return false; }
        m_items = newItems;
        if (m_currentItem > m_items.length - 1) {
            m_currentItem--;
        }
        if (m_items.length == 0) {
            m_currentItem = -1;
        }
        state |= STATE_REVALIDATE; // should be recomputed on paint
        redrawInternalAndParent();
        return true;
    }

    /**
     * Sets the combo box items.
     * 
     * @param items the items to set
     */
    public void setItems(String[] items) {
        m_items = new String[items != null ? items.length : 0];
        if (items != null) {
            Management.arraycopy(items, 0, m_items, 0, m_items.length);
            m_currentItem = 0;
        } else {
            m_currentItem = -1;
        }
        state |= STATE_REVALIDATE; // should be recomputed on paint
    }

    /**
     * Selects the item at the given zero-relative index in the receiver's list. If the item at the
     * index was already selected, it remains selected. Indices that are out of range are ignored.
     * 
     * @param index the index of the item to select
     * @return the selected item, or <code>null</code> if the index is out of range or the ComboBox
     *         was open and therefore, the item could not be selected.
     */
    public synchronized String setSelection(int index) {
        if ((state & STATE_SELECTED) != 0) { return null; }
        if (index < m_items.length && index >= 0 && index != m_currentItem) {
            m_currentItem = index;
            redrawInternalAndParent();
            return m_items[index];
        }
        return null;
    }

}
