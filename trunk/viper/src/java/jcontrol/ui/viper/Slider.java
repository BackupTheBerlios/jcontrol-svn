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
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * This class implements a slider user interface object. The slider can be moved horizontally using
 * the keyboard or the touch screen to simulate analog input. The slider issues notification by
 * throwing an ActionEvent of the type <code>ActionEvent.VALUE_CHANGED</code> when it is moved.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class Slider extends AbstractFocusComponent {

    /** The value of the silder. */
    private int m_value;

    /** Max. value of the slider. */
    private int m_max = 0;

    /** Min. value of the slider. */
    private int m_min = 0;

    /** The step of the slider. */
    private int m_step = 1;

    /**
     * Creates a slider and sets bounds.
     * 
     * @param x the upper left x coordinate.
     * @param y the upper left y coordinate.
     * @param width the width of the slider
     * @param min the slider's minimum value (>= 0)
     * @param max the slider's maximum value.
     */
    public Slider(int x, int y, int width, int min, int max) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = 9;
        m_min = min;
        m_max = max;
        m_value = min;
    }

    /**
     * Get slider value.
     * 
     * @return int The current value.
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
        boolean consumed = false;
        boolean changed = false;
        switch (e.m_key) {
            case KeyEvent.KEY_RIGHT_PRESSED:
                changed = setValue(m_value + m_step);
                consumed = true;
                break;
            case KeyEvent.KEY_LEFT_PRESSED:
                changed = setValue(m_value - m_step);
                consumed = true;
                break;
            case KeyEvent.KEY_UP_PRESSED:
                e.m_key = KeyEvent.KEY_TRANSFER_FOCUS_FORWARD;
                return e;
            case KeyEvent.KEY_DOWN_PRESSED:
                e.m_key = KeyEvent.KEY_TRANSFER_FOCUS_BACKWARD;
                return e;
        }
        // fire event
        if (consumed) {
            if (changed && listener != null)
                onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
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
        boolean consumed = false;
        boolean changed = false;
        switch (e.getRotaryTouchState()) {
            case RotaryTouchEvent.ROTATES_LEFT:
                changed = setValue(m_value + m_step);
                consumed = true;
                break;
            case RotaryTouchEvent.ROTATES_RIGHT:
                changed = setValue(m_value - m_step);
                consumed = true;
                break;
            /*
             * case RotaryTouchEvent.KEY_UP_PRESSED: event.m_key =
             * KeyEvent.KEY_TRANSFER_FOCUS_FORWARD; return event; case
             * RotaryTouchEvent.KEY_DOWN_PRESSED: event.m_key =
             * KeyEvent.KEY_TRANSFER_FOCUS_BACKWARD; return event;
             */
        }
        // fire event
        if (consumed) {
            if (changed && listener != null)
                onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
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
        switch (e.type) {
            case TouchEvent.TYPE_TOUCH_DRAGGED: // dragged
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    if (m_max > m_min) {
                        boolean change;
                        if (m_step > 1) {
                            change = setValue(m_min
                                    + Math.scale(e.x - x - 1, width - 7, m_max - m_min) / m_step
                                    * m_step);
                        } else {
                            change = setValue(m_min
                                    + Math.scale(e.x - x - 1, width - 7, m_max - m_min));
                        }
                        if (change && listener != null)
                            onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
                        return RESULT_EXECUTED;
                    } else {
                        return RESULT_NONE;
                    }
                }
                if ((state & STATE_FOCUS) == 0) return RESULT_NONE;
            case TouchEvent.TYPE_TOUCH_PRESSED:
                if ((e.x >= x && e.x < x + width && e.y >= y && e.y < y + height)) { // touched
                    // inside
                    int xpos = x + 1 + Math.scale(m_value - m_min, m_max - m_min, width - 7); // position
                    // of
                    // slider
                    if (e.x < xpos) {
                        if (setValue(m_value - m_step)) {
                            if (listener != null)
                                onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
                        }
                        return RESULT_EXECUTED;
                    } else if (e.x > xpos + 5) {
                        if (setValue(m_value + m_step)) {
                            if (listener != null)
                                onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, null));
                        }
                        return RESULT_EXECUTED;
                    } else {
                        // touched on slider
                        state |= STATE_SELECTED;
                        return RESULT_EXECUTED;
                    }
                }
                break;
            default: // touch released
                if ((state & STATE_SELECTED) == STATE_SELECTED
                        || (state & STATE_FOCUS) == STATE_FOCUS) {
                    state &= ~STATE_SELECTED;
                    return RESULT_ACCEPTED;
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
        int xpos = 1 + Math.scale(m_value - m_min, m_max - m_min, width - 7);

        if ((state & STATE_FOCUS) == STATE_FOCUS) {
            g.clearRect(x + 1, y + 1, xpos - 1, 2);
            g.clearRect(x + xpos + 5, y + 1, width - xpos - 6, 2);
        } else {
            g.clearRect(x, y, xpos, 3);
            g.clearRect(x + xpos + 4, y, width - xpos - 4, 3);
        }
        g.fillRect(x, y + 3, xpos, 3);
        g.fillRect(x + xpos + 4, y + 3, width - xpos - 4, 3);
        if ((state & STATE_FOCUS) == STATE_FOCUS) {
            g.clearRect(x + 1, y + 6, xpos - 1, 2);
            g.clearRect(x + xpos + 5, y + 6, width - xpos - 6, 2);
        } else {
            g.clearRect(x, y + 6, xpos, 3);
            g.clearRect(x + xpos + 4, y + 6, width - xpos - 4, 3);
        }
        g.drawImage(new String[]{"\uFFFF\u03FE", "\u0001\u0101"}, x + xpos, y, 5, 9, 0, 0);

        if ((state & STATE_FOCUS) == STATE_FOCUS) {
            g.drawRect(x, y, width, height);
        }
        state &= ~(STATE_DIRTY_MASK | STATE_REVALIDATE);
    }

    /**
     * Sets the step value for the slider.
     * 
     * @param step the step.
     */
    public void setStep(int step) {
        m_step = step;
    }

    /**
     * Set the slider value.
     * 
     * @param value The new value.
     * @return true if the slider's value has changed
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
