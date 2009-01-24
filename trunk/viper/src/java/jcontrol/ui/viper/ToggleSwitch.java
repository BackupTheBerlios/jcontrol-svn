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
 * The <code>ToggleSwitch</code> is a graphical component that can be in either an "on" (true) or
 * "off" (false) state. When the user changes the state, an <code>ActionEvent</code> of type
 * <code>ActionEvent.STATE_CHANGED</code> is fired.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class ToggleSwitch extends AbstractFocusComponent {

    /** Label for the off state. */
    private String m_onLabel;

    /** LAbel for the on state. */
    private String m_offLabel;

    /**
     * Create a new <code>ToggleSwitch</code>.
     * 
     * @param x The x-coordinate of this <code>ToggleSwitch</code>.
     * @param y The y-coordinate of this <code>ToggleSwitch</code>.
     */
    public ToggleSwitch(int x, int y) {
        this.x = x;
        this.y = y;
        state |= STATE_REVALIDATE;
    }

    /**
     * Get the current state of this <code>ToggleSwitch</code>.
     * 
     * @return The current state of the <code>ToggleSwitch</code>.
     */
    public boolean isSelected() {
        return (state & STATE_SELECTED) != 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * jcontrol.ui.viper.focus.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        if (e.m_key == KeyEvent.KEY_SELECT_PRESSED) {
            setSelected(!isSelected());
            if (listener != null)
                onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED, null));
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
            setSelected(!isSelected());
            if (listener != null)
                onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED, null));
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
                setSelected(!isSelected());
                if (listener != null)
                    onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED, null));
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
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) g.clearRect(x, y, width, height);
        g.setFont(font);
        if ((state & STATE_REVALIDATE) != 0) {
            // validate
            this.width = 13;
            this.height = 18;
            if (m_onLabel != null) {
                height += g.getFontHeight() + 1;
                int w = g.getTextWidth(m_onLabel);
                if (w > width) width = w;
            }
            if (m_offLabel != null) {
                height += g.getFontHeight() + 1;
                int w = g.getTextWidth(m_offLabel);
                if (w > width) width = w;
            }
        }
        int yoff = y;
        if (m_onLabel != null) {
            int w = g.getTextWidth(m_onLabel);
            if (w < width) {
                int xoff = (width - w) >> 1;
                g.clearRect(x, y, xoff, g.getFontHeight());
                g.drawString(m_onLabel, x + xoff, y);
                g.clearRect(x + xoff + w, y, width - (xoff + w), g.getFontHeight());
            } else {
                g.drawString(m_onLabel, x, y);
            }
            g.clearRect(x, yoff + g.getFontHeight(), width, 1); // clear line below text
            yoff += g.getFontHeight() + 1;
        }
        // draw the switch
        {
            int xoff = x + ((width - 13) >> 1);
            g.drawImage(new String[]{"\uFE01\uF1F9\u9D0D\u0D9D\uF9F1\u01FE\uFC00",
                    "\u0708\u1819\u1B1B\u1B1B\u1918\u180F\u0700"}, xoff, yoff + 3, 13, 13, 0, 0);
            if ((state & STATE_FOCUS) == STATE_FOCUS) {
                drawDottedRect(g, xoff + 1, yoff + 4, 10, 10);
            }
            if (isSelected()) {
                g.clearRect(x, yoff + 16, width, 2);
            } else {
                g.clearRect(x, yoff, width, 3);
            }
            g.drawImage(new String[]{"\u7F80\uA8A8\u807F\uFE01\u1515\u01FE"}, xoff + 3,
                    isSelected() ? yoff : yoff + 10, 6, 8, isSelected() ? 6 : 0, 0);
        }
        if (m_offLabel != null) {
            yoff += 19;
            int w = g.getTextWidth(m_offLabel);
            if (w < width) {
                int xoff = (width - w) >> 1;
                g.clearRect(x, yoff, xoff, g.getFontHeight());
                g.drawString(m_offLabel, x + xoff, yoff);
                g.clearRect(x + xoff + w, yoff, width - (xoff + w), g.getFontHeight());
            } else {
                g.drawString(m_offLabel, x, yoff);
            }
            g.clearRect(x, yoff + g.getFontHeight(), width, 1); // clear line below text

        }
        g.setFont(null);
        state &= ~(STATE_DIRTY_MASK | STATE_REVALIDATE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#setBounds(int, int, int, int)
     */
    @Override
    public synchronized void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

    /**
     * Set the state of this <code>ToggleSwitch</code>.
     * 
     * @param onoff The new state of the <code>ToggleSwitch</code>.
     */
    public void setSelected(boolean onoff) {
        if (onoff == isSelected()) return;
        if (onoff) {
            state |= STATE_SELECTED;
        } else {
            state &= ~STATE_SELECTED;
        }
        redrawInternalAndParent();
    }

    /**
     * Sets the inscription label for this <code>ToggleSwitch</code>. The onLabel is painted above,
     * the offLabel is painted below the switch.
     * 
     * @param onLabel the on label
     * @param offLabel the off label
     */
    public void setText(String onLabel, String offLabel) {
        m_onLabel = onLabel;
        m_offLabel = offLabel;
        state |= STATE_REVALIDATE;
    }

}
