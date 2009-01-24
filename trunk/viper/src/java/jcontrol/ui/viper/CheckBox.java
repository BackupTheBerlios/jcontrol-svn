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
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * The <code>CheckBox</code> is a graphical component that can be in either an "on" (true) or "off"
 * (false) state. When the user changes the state, an <code>ActionEvent</code> of type
 * <code>STATE_CHANGED</code> is fired.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class CheckBox extends AbstractFocusComponent {

    /** The checkbox label- */
    private String m_label;

    /**
     * Create a new labeled <code>CheckBox</code> which is initially "off". When no width and height
     * are specified, a preferred size depending on the label dimensions is calculated.
     * 
     * @param text A <code>String</code> that is written on the <code>CheckBox</code>.
     * @param x The x-coordinate of this <code>CheckBox</code>.
     * @param y The y-coordinate of this <code>CheckBox</code>.
     */
    public CheckBox(String text, int x, int y) {
        m_label = text;
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new labeled <code>CheckBox</code> with specified bounds which is initially "off".
     * 
     * @param text A <code>String</code> that is written on the <code>CheckBox</code>.
     * @param x The x-coordinate of this <code>CheckBox</code>.
     * @param y The y-coordinate of this <code>CheckBox</code>.
     * @param width The width of this <code>CheckBox</code>.
     * @param height The height of this <code>CheckBox</code>.
     */
    public CheckBox(String text, int x, int y, int width, int height) {
        this(text, x, y);
        this.width = width;
        this.height = height;
        state |= STATE_WIDTH_FIXED;
    }

    /**
     * Get the current state of this <code>CheckBox</code>.
     * 
     * @return The current state of the check box.
     */
    public boolean isSelected() {
        return (state & STATE_SELECTED) != 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        if (e.m_key == KeyEvent.KEY_SELECT_PRESSED) {
            if (isSelected()) {
                state &= ~STATE_SELECTED;
            } else {
                state |= STATE_SELECTED;
            }
            state |= STATE_DIRTY_REPAINT;
            if (listener != null)
                onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED, m_label));
            return null; // we consumed that event
        } else {
            return e; // let the Container handle this KeyEvent
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
        if (e.getRotaryTouchState() == RotaryTouchEvent.STATE_SELECTED) {
            if (isSelected()) {
                state &= ~STATE_SELECTED;
            } else {
                state |= STATE_SELECTED;
            }
            // state |= STATE_DIRTY_REPAINT;
            state |= STATE_DIRTY_PAINT_ALL;
            if (listener != null)
                onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED, m_label));
            return null; // we consumed that event
        } else {
            return e; // let the Container handle this KeyEvent
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
        if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) { // touched inside
            if (e.type == TouchEvent.TYPE_TOUCH_PRESSED) {
                if (isSelected()) {
                    state &= ~STATE_SELECTED;
                } else {
                    state |= STATE_SELECTED;
                }
                state |= STATE_DIRTY_REPAINT;
                // setSelected(!isSelected());
                if (listener != null)
                    onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED, m_label));
                return RESULT_EXECUTED;
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
        // when no width is specified, compute preferred size
        if (width > 0 && width < 14) width = 14;
        if (height > 0 && height < 10) height = 10;
        g.setFont(font);
        if (width <= 0 || height <= 0
                || ((state & STATE_REVALIDATE) != 0 && (state & STATE_WIDTH_FIXED) == 0)) {
            // validate
            if ((state & STATE_WIDTH_FIXED) == 0) {
                width = (m_label != null ? g.getTextWidth(m_label) : 0) + 14;
            }
            if ((state & STATE_HEIGHT_FIXED) == 0) {
                height = g.getFontHeight() < 8 ? 10 : g.getFontHeight() + 2;
            }
            state &= ~STATE_REVALIDATE;
        }
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) g.clearRect(x, y, width, height);
        // draw the check box
        {
            String checkImg[] = new String[]{"\uFF81\u8181\u8181\u81FF\u0000\u0000\uFF81\u99B1\uE1B1\u99FF\u0C06\u0300"};
            if ((state & STATE_SELECTED) == 0) {
                g.drawImage(checkImg, x, y + ((height - 8) >> 1), 11, 8, 0, 0);
            } else {
                g.drawImage(checkImg, x, y + ((height - 8) >> 1), 11, 8, 12, 0);
            }
        }
        if (((state & STATE_FOCUS) == 0)) {
            g.setDrawMode(Display.INVERSE);
            g.drawRect(x + 11, y, width - 11, height); // clear focus rectangle
            g.setDrawMode(Display.NORMAL);
        }

        // draw the label
        int textw = 0;
        if (m_label != null) {
            int fheight = g.getFontHeight();
            if (fheight > height - 2) fheight = height - 2;
            int fonty = y + ((height - fheight + 1) >> 1);
            textw = g.drawString(m_label, x + 13, fonty, width - 14, fheight, 0, fonty < y ? y
                    - fonty : 0);
        }
        g.setFont(null);
        g.clearRect(x + 13 + textw, y + 1, width - 14 - textw, height - 2); // clear space behind
        // text

        if (((state & STATE_FOCUS) == STATE_FOCUS)) {
            drawDottedRect(g, x + 11, y, width - 11, height);
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Set the state of this <code>CheckBox</code>.
     * 
     * @param onoff The new state of the check box.
     */
    public void setSelected(boolean onoff) {
        if (onoff) {
            state |= STATE_SELECTED;
        } else {
            state &= ~STATE_SELECTED;
        }
        redrawInternalAndParent();
    }

    /**
     * Change the label if this check box.
     * 
     * @param text The new label.
     */
    public void setText(String text) {
        m_label = text;
        if ((state & STATE_WIDTH_FIXED) == 0) state |= STATE_REVALIDATE;
        redrawInternalAndParent();
    }

}
