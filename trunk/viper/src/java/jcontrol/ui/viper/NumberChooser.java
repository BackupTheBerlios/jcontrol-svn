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
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * The class <code>NumberChosser</code> implements a label showing a number. The number can be
 * changed via the up-down buttons on the keyboard or by using the touch screen.<br>
 * When the user changes the state, an <code>ActionEvent</code> of type
 * <code>ActionEvent.VALUE_CHANGED</code> is fired.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class NumberChooser extends AbstractFocusComponent {

    /** Min. value of the number chooser. */
    private int m_min;

    /** Max. value of the number chooser. */
    private int m_max;

    /** Value of the number chooser. */
    private int m_value;

    /** */
    private int m_lastYTouch;

    /**
     * Creates a new number chooser.
     * 
     * @param x the x position
     * @param y the y position
     * @param min the minimum value the number chooser should be able to show
     * @param max the maximum value the number chooser should be able to show
     */
    public NumberChooser(int x, int y, int min, int max) {
        this.x = x;
        this.y = y;
        this.height = 0;
        m_min = min;
        m_max = max;
        m_value = m_min;
    }

    /**
     * Draw the graphical elements of this <code>Button</code>.
     */
    private void drawButton(Graphics g, int x, int y, int height, boolean plusminus) {
        g.drawLine(x + 1, y, x + 7, y);
        g.drawLine(x, y + 1, x, y + height - 2);
        g.drawLine(x + 1, y + height - 1, x + 7, y + height - 1);
        g.drawLine(x + 8, y + 1, x + 8, y + height - 2);
        g.clearRect(x + 1, y + 1, height == 5 ? 2 : 1, height - 2); // clear area left beside arrow
        // up-down image
        g.drawImage(new String[]{"\u040E\u1F0E\u0400"}, x + 2, y
                + ((height - (plusminus ? 2 : 1)) >> 1), 5, (height == 5 ? 2 : 3), 0, plusminus ? 0
                : (height == 5 ? 3 : 2));
        g.clearRect(x + (height == 5 ? 6 : 7), y + 1, height == 5 ? 2 : 1, height - 2); // clear
        // area
        // right
        // beside
        // arrow
    }

    /**
     * Returns the current value.
     */
    public int getValue() {
        return m_value;
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
                // increment
                if (setValue(m_value + 1)) {
                    onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
                }
                return null;
            case KeyEvent.KEY_DOWN_PRESSED:
                if (setValue(m_value - 1)) {
                    onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
                }
                return null;
            default:
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
        switch (e.getRotaryDirection()) {
            // increment value
            case RotaryTouchEvent.ROTATES_LEFT:

                if (setValue(m_value + 1)) {
                    onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
                }
                return null;
                // decrement value
            case RotaryTouchEvent.ROTATES_RIGHT:
                if (setValue(m_value - 1)) {
                    onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
                }
                return null;
            default:
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
        switch (e.type) {
            case TouchEvent.TYPE_TOUCH_PRESSED:
                if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) { // touched inside
                    if ((state & STATE_SELECTED) == 0) { // initial press
                        state |= STATE_SELECTED;

                        m_lastYTouch = e.y;
                        if (e.x > x + width - 9) { // touched on slider
                            if (e.y < y + (height >> 1)) {
                                if (setValue(m_value + 1)) {
                                    onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED,
                                            null));
                                }
                            } else {
                                // touched on down arrow
                                if (setValue(m_value - 1)) {
                                    onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED,
                                            null));
                                }
                            }
                        }
                    }
                    return RESULT_EXECUTED;
                } else {
                    state &= ~STATE_SELECTED;
                }
                return RESULT_NONE;
            case TouchEvent.TYPE_TOUCH_DRAGGED:
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    if (e.y < m_lastYTouch) {
                        m_lastYTouch = e.y;
                        if (m_value < m_max) {
                            m_value++;
                            redrawInternalAndParent();
                            if (listener != null) {
                                onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED,
                                        String.valueOf(m_value)));
                            }
                        }
                    } else if (e.y > m_lastYTouch) {
                        m_lastYTouch = e.y;
                        if (m_value > m_min) {
                            m_value--;
                            redrawInternalAndParent();
                            if (listener != null) {
                                listener.onActionEvent(new ActionEvent(this,
                                        ActionEvent.VALUE_CHANGED, String.valueOf(m_value)));
                            }
                        }
                    }
                    return RESULT_EXECUTED;
                }
                return RESULT_NONE;
            case TouchEvent.TYPE_TOUCH_RELEASED:
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    state &= ~STATE_SELECTED;
                    return RESULT_ACCEPTED;
                } else {
                    return RESULT_NONE;
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
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) g.clearRect(x, y, width, height);
        g.setFont(font);
        // validate
        if (width <= 0 || (state & STATE_REVALIDATE) != 0) {
            // get maximum digit width of this font
            int digitWidth = 0;
            // calculate m_maxDigitWidth
            for (int i = 0; i < 9; i++) {
                int w = g.getTextWidth(String.valueOf(i));
                if (w > digitWidth) digitWidth = w;
            }

            // get width of min value
            int v = m_min;
            int minWidth = 0;
            for (; v != 0;) {
                minWidth += digitWidth;
                v /= 10;
            }
            if (m_min < 0) minWidth += g.getTextWidth("-") + 1;

            // get width of max value
            v = m_max;
            int maxWidth = 0;
            for (; v != 0;) {
                maxWidth += digitWidth;
                v /= 10;
            }
            if (m_max < 0) maxWidth += g.getTextWidth("-") + 1;

            width = (maxWidth > minWidth ? maxWidth : minWidth) + 13; // maximum width + border(2) +
            // button(9) +
            // padding(2)
            height = g.getFontHeight() + 3;
            if (height < 9) height = 9;
            state &= ~STATE_REVALIDATE;
        }
        g.setDrawMode(Graphics.NORMAL);
        g.drawRect(x, y, width - 9, height); // outline
        {
            // draw new value
            if (((state & STATE_FOCUS) == STATE_FOCUS)) {
                g.setDrawMode(Graphics.INVERSE);
            } else {
                g.setDrawMode(Graphics.NORMAL);
            }
            int textw = g.drawString(String.valueOf(m_value), x + 2, y + 2, width - 12, -1, 0, 0);
            g.clearRect(x + 1, y + 1, 1, height - 2); // left of text
            g.clearRect(x + 2, y + 1, textw, 1); // top of text
            g.clearRect(x + 2 + textw, y + 1, width - 12 - textw, height - 2); // left of text
        }
        g.setDrawMode(Graphics.NORMAL);
        int buttonheight = height >> 1;
        if (buttonheight < 5) buttonheight = 5;
        drawButton(g, x + width - 9, y, buttonheight, true);
        drawButton(g, x + width - 9, y + buttonheight, buttonheight, false);
        g.setFont(null);
        // m_lastValue = m_value;
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Sets the specified value.
     * 
     * @param value the value to set
     * @return true if the value has changed
     */
    public boolean setValue(int value) {
        if (value == m_value) return false;
        int newvalue = value;
        if (m_min < m_max) {
            if (newvalue < m_min) {
                newvalue = m_min;
            } else if (newvalue > m_max) {
                newvalue = m_max;
            }
        } else {
            if (newvalue < m_max) {
                newvalue = m_max;
            } else if (newvalue > m_min) {
                newvalue = m_min;
            }
        }
        if (newvalue != m_value) {
            m_value = newvalue;
            redrawInternalAndParent();
            return true;
        }
        return false;
    }

}