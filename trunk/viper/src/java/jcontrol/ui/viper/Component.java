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
import jcontrol.io.Resource;
import jcontrol.util.Queue;

/**
 * <p>
 * A <i>Component</i> is an object having a graphical representation that can be displayed on the
 * screen and that can interact with the user. Examples of components are the buttons, labels and
 * checkboxes etc of a typical graphical user interface.
 * </p>
 * <p>
 * The <code>Component</code> class is the abstract superclass of the JControl/Viper components.
 * Class <code>Component</code> may also be extended directly to create new components.
 * </p>
 * <p>
 * To work properly, components must be part of a container-tree (see <code>Container</code>), with
 * a Frame instance as top-level element.<br>
 * There are two different types of components. Some components, such as labels or borders don't
 * have any interactive behavior. Their only purpose is displaying text, images, measuring values or
 * whatever. They can neither be selected by any user action nor they can fire events.<br>
 * <i>Interactive</i> components, such as buttons, combo boxes, list boxes, radio buttons etc, are
 * controllable by the user via keyboard and/or touch screen and they can even fire action events.
 * </p>
 * Get more information and demo applications at <a
 * href="http://www.jcontrol.org">www.jcontrol.org</a>.<br>
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */

public abstract class Component {

    protected static final int STATE_VISIBLE = 1 << 0;

    protected static final int STATE_DIRTY_UPDATE = 1 << 1;

    protected static final int STATE_DIRTY_REPAINT = 1 << 2 | STATE_DIRTY_UPDATE;

    protected static final int STATE_DIRTY_PAINT_ALL = 1 << 3 | STATE_DIRTY_REPAINT;

    protected static final int STATE_DIRTY_MASK = STATE_DIRTY_REPAINT | STATE_DIRTY_UPDATE
            | STATE_DIRTY_PAINT_ALL;

    protected static final int STATE_ABORT_UPDATE = 1 << 14;

    protected static final int STATE_ANIMATED = 1 << 4 | STATE_DIRTY_UPDATE;

    /**
     * If set, the last update has been accomplished successfully. Otherwise, the update method has
     * returned without calling paint, for any reason.
     */
    protected static final int STATE_UPDATED = 1 << 5;

    protected static final int STATE_REVALIDATE = 1 << 6;

    protected static final int STATE_WIDTH_FIXED = 1 << 13;

    protected static final int STATE_HEIGHT_FIXED = 1 << 12;

    protected static final int STATE_SIZE_FIXED = STATE_WIDTH_FIXED | STATE_HEIGHT_FIXED;

    protected static final int STATE_COVERED = 1 << 7;

    protected static final int STATE_DISPOSED = 1 << 8;

    protected static final int STATE_FOCUS = 1 << 9;

    protected static final int RESERVED1 = 1 << 10;

    protected static final int RESERVED2 = 1 << 11;

    /**
     * Selected state for buttons, comboboxes etc,
     */
    protected static final int STATE_SELECTED = 1 << 15;

    /**
     * The component's x location. This variable should not be modified directly!
     * 
     * @see #setBounds(int, int, int, int)
     */
    public int x;

    /**
     * The component's y location. This variable should not be modified directly!
     * 
     * @see #setBounds(int, int, int, int)
     */
    public int y;

    /**
     * The component's width. This variable should not be modified directly!
     * 
     * @see #setBounds(int, int, int, int)
     */
    public int width;

    /**
     * The component's height. This variable should not be modified directly!
     * 
     * @see #setBounds(int, int, int, int)
     */
    public int height;

    /**
     * The parent container of the component. This variable should not be modified!
     */
    public IContainer parent;

    /**
     * The state control flag. This should only be modified directly if you know exactly what you're
     * doing.
     */
    public int state = STATE_VISIBLE | STATE_DIRTY_PAINT_ALL;

    /**
     * A font resource for drawing text. Components are self-responsible for making use of this
     * resource.
     */
    protected Resource font;

    /**
     * Static event queue for all components.
     */
    protected static Queue eventQueue;

    /**
     * Returns the applications root IFrame object.
     * 
     * @return the applications root IFrame.
     */
    public IFrame getFrame() {
        if (parent == null) {
            if (this instanceof IFrame) { return (IFrame) this; }
            return null;
        }
        IContainer p = parent;
        for (; ((Component) p).parent != null;)
            p = ((Component) p).parent;
        if (p instanceof IFrame) return (IFrame) p;
        return null;
    }

    /**
     * <p>
     * Returns <code>true</code> if this component is set visible, and <code>false</code> otherwise.
     * </p>
     * If one of the component's ancestors is not visible or some other condition makes the
     * component not visible, e.g. it is covered by another component or whatever, this method may
     * still indicate that it is considered visible even though it may not actually be showing.
     * 
     * @return boolean the visible state of this component.
     */
    public boolean isVisible() {
        if (parent instanceof Container && !((Container) parent).isVisible()) return false;
        return isVisibleInternal();
    }

    protected boolean isVisibleInternal() {
        return (state & STATE_VISIBLE) != 0 && (state & STATE_DISPOSED) == 0;
    }

    /**
     * Paints this component. Here, all drawing action will occur by using the graphics parameter.
     * This method must be overwritten by any extending class.
     * 
     * @param g the application's graphics object.
     */
    public void paint(Graphics g) {
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
            g.clearRect(x, y, width, height);
        }

    }

    /**
     * Redraw.
     */
    protected void redrawInternalAndParent() {
        if (isVisibleInternal()) {
            state |= STATE_DIRTY_REPAINT;
            Component parent = (Component) this.parent;
            if (parent != null && parent.isVisibleInternal()
                    && (parent.state & STATE_DIRTY_MASK) == 0) parent.redrawInternalAndParent();
        }
    }

    /**
     * Causes the entire component to be marked as needing to be redrawn. The next time a paint
     * request is processed, the component will be completely painted.
     */
    public void repaint() {
        if (isVisibleInternal()) setDirty(STATE_DIRTY_REPAINT, true);
    }

    /**
     * Set the dimensions of this component. The component is repainted automatically.
     * 
     * @param x The x-coordinate of the upper left corner
     * @param y The y-coordinate of the upper left corner
     * @param width The width of this component
     * @param height The height of this component
     */
    public synchronized void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (width > 0) state |= STATE_WIDTH_FIXED;
        if (height > 0) state |= STATE_HEIGHT_FIXED;
        state |= STATE_REVALIDATE;
        if (isVisibleInternal()) setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

    /**
     * This method should not be called from the application. Sets/unsets the state flag of this
     * component to the state specified by the <code>state</code>-parameter. All parent containers
     * are set/unset dirty.
     * 
     * @param state the state to set
     * @param onoff set or not set the state
     */
    public synchronized void setDirty(int state, boolean onoff) {
        synchronized (this) {
            if (onoff) {
                if (!isVisible()) return;
                this.state |= state;
            } else {
                this.state &= ~state;
            }
        }
        Component parent = (Component) this.parent;
        if (parent != null && (parent.state & STATE_DIRTY_MASK) == 0)
            parent.setDirty(STATE_DIRTY_REPAINT, onoff);
    }

    /**
     * Sets the font for this component.
     * 
     * @param font the new font.
     */
    public synchronized void setFont(Resource font) {
        if (font == this.font || (this.font != null && this.font.equals(font))) return;
        this.font = font;
        state |= STATE_REVALIDATE;
        redrawInternalAndParent();

    }

    /**
     * Turns the component visible or invisible.
     * 
     * @param visible the visible state to set.
     */
    public synchronized void setVisible(boolean visible) {
        if (visible == isVisibleInternal()) return;
        if (visible) {
            state |= STATE_VISIBLE;
            setDirty(STATE_DIRTY_REPAINT, true);
        } else {
            setDirty(STATE_DIRTY_REPAINT, true);
            state &= ~(STATE_VISIBLE | STATE_SELECTED); // unselect component
        }
    }

    /**
     * Repaints the component if the dirty flag is set. This method should not be overwritten by
     * extending classes.
     * 
     * @param g the application's graphics object.
     */
    public synchronized void update(Graphics g) {
        state &= ~STATE_UPDATED;
        if (g == null || (state & STATE_DIRTY_MASK) == 0 || (state & STATE_COVERED) != 0) return;
        state |= STATE_UPDATED;
        if (!isVisible()) {
            state &= ~STATE_DIRTY_MASK;
            g.clearRect(x, y, width, height);
            if (parent instanceof Container) {
                parent.setDirty(this, x, y, width, height, STATE_DIRTY_REPAINT, true);
            }
        } else {
            paint(g);
        }
    }

}
