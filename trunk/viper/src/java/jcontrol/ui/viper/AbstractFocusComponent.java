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
import jcontrol.ui.viper.event.ActionListener;
import jcontrol.ui.viper.event.ActionProducer;
import jcontrol.ui.viper.event.IRotaryTouchListener;
import jcontrol.ui.viper.event.ITouchListener;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * Abstract superclass for components that can be controlled by the keyboard.
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public abstract class AbstractFocusComponent extends Component implements IFocusable,
        ITouchListener, IRotaryTouchListener, ActionProducer {

    /** The default border with. */
    protected static final int BORDER_WIDTH = 1;

    /** The action listeners. */
    protected ActionListener listener;

    /**
     * Draw a dotted rectangle.
     * 
     * @param g the global graphics object
     * @param x
     * @param y
     * @param width
     * @param height
     */
    protected void drawDottedRect(Graphics g, int x, int y, int width, int height) {
        boolean dotted1 = true;
        boolean dotted2 = (height & 1) != 0;
        for (int i = x; i < x + width; i++) { // to right
            if (dotted1) g.setPixel(i, y);
            if (dotted2) g.setPixel(i, y + height - 1);
            dotted1 ^= true;
            dotted2 ^= true;
        }
        dotted1 = false;
        dotted2 = (width & 1) == 0;
        for (int i = y + 1; i < y + height - 1; i++) { // to left
            if (dotted1) g.setPixel(x, i);
            if (dotted2) g.setPixel(x + width - 1, i);
            dotted1 ^= true;
            dotted2 ^= true;
        }
    }

    /**
     * Returns the ActionListener of this Component.
     * 
     * @return the ActionListener of this Component.
     */
    public ActionListener getActionListener() {
        return listener;
    }

    /**
     * Pushes the ActionEvent to the global event queue.
     * 
     * @param e the ActionEvent
     */
    protected void onActionEvent(ActionEvent e) {
        if (listener != null) eventQueue.push(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.event.IKeyListener#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    public abstract KeyEvent onKeyEvent(KeyEvent event);

    /*
     * (non-Javadoc)
     * 
     * @seejcontrol.ui.viper.event.IRotaryTouchListener#onRotaryTouchEvent(jcontrol.ui.viper.event.
     * RotaryTouchEvent)
     */
    public abstract RotaryTouchEvent onRotaryTouchEvent(RotaryTouchEvent e);

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.event.ITouchListener#onTouchEvent(jcontrol.ui.viper.event.TouchEvent)
     */
    public abstract int onTouchEvent(TouchEvent event);

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFocusable#requestFocus()
     */
    public boolean requestFocus() {
        if ((state & STATE_VISIBLE) == STATE_VISIBLE) {
            if (parent != null) {
                IFrame root = getFrame();
                if (root != null) {
                    root.clearFocus(null); // clear focus tree
                    if (root.requestFocus(this)) { return true; }
                }
            }
        }
        return false;
    }

    /**
     * Add an ActionListener to the component. It will receive ActionEvents when the component is
     * selected.
     * 
     * @param listener an ActionListener
     */
    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#setVisible(boolean)
     */
    @Override
    public synchronized void setVisible(boolean visible) {
        if (visible == isVisible()) return;
        super.setVisible(visible);
        if (!visible && (state & STATE_FOCUS) == STATE_FOCUS && parent != null) {
            IFrame root = getFrame();
            if (root != null) {
                root.transferFocus(root, IFocusable.TRANSFER_FOCUS_FORWARD);
            }
        }
    }

}
