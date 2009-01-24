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
 * The <code>RockerSwitch</code> is a graphical component that can be in either an "on" (true) or
 * "off" (false) state. When the user changes the state, an <code>ActionEvent</code> of type
 * <code>ActionEvent.STATE_CHANGED</code> is fired.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class RockerSwitch extends AbstractFocusComponent {

    /**
     * Creates a new RockerSwitch.
     * 
     * @param x The x-coordinate of this <code>RockerSwitch</code>.
     * @param y The y-coordinate of this <code>RockerSwitch</code>.
     */
    public RockerSwitch(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 21;
        this.height = 12;
        state |= STATE_SIZE_FIXED;
    }

    /**
     * Get the current state of this <code>RockerSwitch</code>.
     * 
     * @return The current state of the RockerSwitch.
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
            setSelected(!isSelected());
            // fire action event
            if (listener != null)
                onActionEvent(new ActionEvent(this, ActionEvent.STATE_CHANGED, null));
            return null; // we consumed that event
        } else
            return e; // let the Container handle this KeyEvent
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
        } else
            return e; // let the Container handle this KeyEvent
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
        // draw the switch
        if (isSelected()) {
            // switch image (on)
            g.drawImage(new String[]{
                    "\uFCFE\uFFFF\u0202\u0204\u04FC\u0404\u0404\u0404\u0404\uFC00",
                    "\u0303\u0303\u0303\u0302\u0203\u0202\u0202\u0202\u0202\u0300"}, x + 1, y + 1,
                    19, 10, 0, 0);
        } else {
            // switch image (off)
            g.drawImage(new String[]{
                    "\uFC04\u0404\u0404\u0404\u04FC\u0404\u0202\u02FF\uFFFE\uFC00",
                    "\u0302\u0202\u0202\u0202\u0203\u0202\u0303\u0303\u0303\u0300"}, x + 1, y + 1,
                    19, 10, 0, 0);
        }

        if (((state & STATE_FOCUS) == 0)) {
            g.setDrawMode(Graphics.INVERSE);
            g.drawRect(x, y, width, height); // clear focus rectangle
            g.setDrawMode(Graphics.NORMAL);
        } else {
            // draw dotted rectangle
            drawDottedRect(g, x, y, width, height);
        }
        state &= ~(STATE_DIRTY_MASK | STATE_REVALIDATE);
    }

    /**
     * Set the state of this <code>RockerSwitch</code>.
     * 
     * @param onoff The new state of the RockerSwitch.
     */
    public void setSelected(boolean onoff) {
        if (onoff) {
            state |= STATE_SELECTED;
        } else {
            state &= ~STATE_SELECTED;
        }
        redrawInternalAndParent();
    }

}
