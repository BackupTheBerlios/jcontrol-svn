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
 * A radio button is a graphical component that can be in either an "on" (true) or "off" (false)
 * state. When a radio button's state is changed to "on", all other radio buttons in the same
 * container are set to "off". When the user changes the state, an <code>ActionEvent</code> of type
 * <code>STATE_CHANGED</code> is fired.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class RadioButton extends AbstractFocusComponent {

    /** The label of this radio button */
    private String m_label;

    /**
     * Create a new labeled <code>RadioButton</code> which is initially "off". When no width and
     * height are specified, a preferred size depending on the label dimensions is calculated.
     * 
     * @param label A <code>String</code> that is written on the <code>RadioButton</code>.
     * @param x The x-coordinate of this <code>RadioButton</code>.
     * @param y The y-coordinate of this <code>RadioButton</code>.
     */
    public RadioButton(String label, int x, int y) {
        m_label = label;
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new labeled <code>RadioButton</code> with specified bounds which is initially "off".
     * 
     * @param label A <code>String</code> that is written on the <code>RadioButton</code>.
     * @param x The x-coordinate of this <code>RadioButton</code>.
     * @param y The y-coordinate of this <code>RadioButton</code>.
     * @param width The width of this <code>RadioButton</code>.
     * @param height The height of this <code>RadioButton</code>.
     */
    public RadioButton(String label, int x, int y, int width, int height) {
        m_label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        state |= STATE_WIDTH_FIXED;
    }

    /**
     * Get the current state of this <code>RadioButton</code>.
     * 
     * @return The current state of the RadioButton.
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
            if (!isSelected()) setSelectedInternal(true, false);
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
        if (e.getRotaryTouchState() == RotaryTouchEvent.STATE_IDLE) {
            if (!isSelected()) setSelectedInternal(true, false);
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
                if (!isSelected()) setSelectedInternal(true, false);
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
        if (width > 0 && width < 14) width = 13;
        if (height > 0 && height < 10) height = 10;
        g.setFont(font);
        if (width <= 0 || height <= 0 || (state & STATE_REVALIDATE) != 0) {
            // validate
            if ((state & STATE_WIDTH_FIXED) == 0) {
                width = (m_label != null ? g.getTextWidth(m_label) : 0) + 13;
            }
            if ((state & STATE_HEIGHT_FIXED) == 0) {
                height = g.getFontHeight() < 8 ? 10 : g.getFontHeight() + 2;
            }
            state &= ~STATE_REVALIDATE;
        }
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) g.clearRect(x, y, width, height);
        // draw the radio box
        String img[] = new String[]{"\u1C22\u4141\u4122\u1C1C\u327D\u7D7F\u3E1C"};
        if (!isSelected()) {
            g.drawImage(img, x, y + ((height - 6) >> 1), 7, 7, 0, 0);
        } else {
            g.drawImage(img, x, y + ((height - 6) >> 1), 7, 7, 7, 0);
        }

        if (((state & STATE_FOCUS) == 0)) {
            g.setDrawMode(Display.INVERSE);
            g.drawRect(x + 10, y, width - 9, height); // clear focus rectangle
            g.setDrawMode(Display.NORMAL);
        }

        // draw the label
        int textWidth = 0;
        if (m_label != null) {
            int fontHeight = g.getFontHeight();
            if (fontHeight > height - 2) fontHeight = height - 2;
            int ypos = y + ((height - fontHeight + 1) >> 1);
            textWidth = g.drawString(m_label, x + 12, ypos, width - 13, fontHeight, 0, ypos < y ? y
                    - ypos : 0);
        }
        g.clearRect(x + 12 + textWidth, y + 1, width - 13 - textWidth, height - 2); // clear space
        // behind text

        if ((state & STATE_FOCUS) == STATE_FOCUS) {
            drawDottedRect(g, x + 10, y, width - 9, height);
        }
        g.setFont(null);
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Set the state of this <code>RadioButton</code>.
     * 
     * @param onoff The new state of the RadioButton.
     */
    public void setSelected(boolean onoff) {
        setSelectedInternal(onoff, true);
    }

    /**
     * Sets the selected radio button.
     * 
     * @param onoff
     * @param forceRepaint
     */
    private void setSelectedInternal(boolean onoff, boolean forceRepaint) {
        if (onoff) {
            state |= STATE_SELECTED;
            if (parent instanceof Container) {
                // switch all other radio buttons off
                for (int i = ((Container) parent).children.length - 1; i >= 0; i--) {
                    Component c = ((Container) parent).children[i];
                    if (c instanceof RadioButton && c != this) {
                        ((RadioButton) c).setSelectedInternal(false, forceRepaint);
                        if (((RadioButton) c).listener != null)
                            onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED,
                                    ((RadioButton) c).m_label));
                    }
                }
            }
        } else {
            state &= ~STATE_SELECTED;
        }
        if (forceRepaint) {
            redrawInternalAndParent();
        } else {
            state |= STATE_DIRTY_REPAINT;
        }
    }

    /**
     * Change the label of this radio button.
     * 
     * @param text The new label.
     */
    public void setText(String text) {
        m_label = text;
        if ((state & STATE_WIDTH_FIXED) == 0) state |= STATE_REVALIDATE;
        redrawInternalAndParent();
    }

}
