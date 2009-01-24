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

/**
 * <p>
 * A container object is a component that can contain other components.
 * </p>
 * <p>
 * Containers are used to group components that should e.g. become visible or invisible at the same
 * time, have the same font, being replaced by other components all at once etc.<br>
 * Normally, it is not nescessary to define any bounds for a container as component bounds are
 * always treated as absolute display coordinates. Thus, components simply ignore their parent's
 * bounds.
 * </p>
 * <p>
 * Components added to a container are tracked in an array. If the number of added components
 * exceeds the initial array size, the array will be resized, i.e. enlarged.<br>
 * Removing a component will <b>not</b> immediately dispose the component or delete the
 * corresponding array index. First, the component is marked as to be deleted, then, on the next
 * paint event, the graphic area covered by the component is cleared and the array index the
 * component is stored in is set to <code>null</code>.<br>
 * In other words, removing a component will not result in a smaller data array. The array is
 * neither trimmed nor are the components beyond the removed one shifted forward. Adding a component
 * to the container will use the first free index in the array to store it in. Consequentely in a
 * container, components are not necessarily stored in the same order they have been added.
 * </p>
 * <b>Note:</b> As the drawing order of components in a container is not fixed, components should if
 * possible not cover each other. Otherwise, it is not guaranteed that the last added component will
 * always be painted above a previously added component. The only component that may (or should) be
 * covered by other components is the <code>Border</code>.
 * 
 * @author Marcus Timmermann
 * @see IFrame
 * @since Viper 1.0
 * @version $Revision$
 */
public class Container extends Component implements IContainer {

    /**
     * The children of the container.<br>
     * This field must <b>not</b> be modified by the application.
     */
    Component[] children;

    /**
     * The index of the child component that has keyboard focus or -1 if this container does not
     * have keyboard focus.<br>
     * This field must <b>not</b> be modified by the application.
     */
    int focusIndex = -1;

    /**
     * Create a new <code>Container</code> with a default initial capacity of 10. Using this
     * constructor corresponds to the call <code>new Container(10)</code>.
     * 
     * @see #Container(int estimatedMaxCapacity)
     */
    public Container() {
        this(10);
    }

    /**
     * <p>
     * Constructs a new Container. Containers can be extended, but are lightweight in this case and
     * must be contained by a parent somewhere higher up in the component tree (such as Frame for
     * example).
     * </p>
     * <p>
     * The parameter <code>estimatedMaxCapacity</code> specifies the initial size of the internal
     * data array the components will be stored in.<br>
     * If this number is exceeded the array will be resized, indeed. But this may consume a lot of
     * memory. For a short period of time, two data arrays must be kept in memory simultaniously to
     * copy the data from the small array to the larger one. In large applications this may exceed
     * the available memory. To avoid this, it is recommended to initialize the container as big as
     * it must never be enlarged any time. But it should of course neither be too large as then,
     * memory will be allocated that is never used by the application. In most cases, it is quite
     * easy for the programmer to know the number of components a container should hold. In simple
     * applications, components are once added an never removed. Thus, it is easy to count them.
     * When components should be removed and replaced by other ones at run-time, the maximum
     * capacity should include the sum of both, removed and newly added components. This is because
     * of the fact that removed components are not deleted from the data array immediately but on
     * the next painting action.
     * <p>
     * 
     * @param estimatedMaxCapacity the capacity the internal data array should be initialized with
     * @see #Container()
     */
    public Container(int estimatedMaxCapacity) {
        children = new Component[estimatedMaxCapacity];
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#add(jcontrol.ui.viper.Component)
     */
    public void add(Component component) {
        if (component == null) return;
        if (component.parent != null) return; // a component may not be added twice
        int nullIndex = -1;
        component.state &= ~STATE_DISPOSED;
        if (component.isVisible()) {
            component.state |= STATE_DIRTY_PAINT_ALL;
        } else {
            component.state &= ~STATE_DIRTY_MASK;
        }
        synchronized (children) {
            component.parent = this;
            for (int i = 0; i < children.length; i++) {
                if (nullIndex == -1 && children[i] == null) {
                    nullIndex = i;
                    break;
                }
            }
            if (nullIndex != -1) {
                children[nullIndex] = component;
            } else {
                // array is too small (shit)
                Component[] newChildren = new Component[children.length + 1];
                System.arraycopy(children, 0, newChildren, 0, children.length);
                newChildren[children.length] = component;
                children = newChildren;
            }
            // set components font
            if (font != null && component.font == null) {
                component.setFont(font);
            }

        }
        if (component.isVisible()) {
            redrawInternalAndParent();
        }
    }

    /**
     * Dispose.
     */
    protected void dispose() {
        state |= STATE_DISPOSED;
        synchronized (children) {
            parent = null;
            focusIndex = -1;
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    children[i].parent = null;
                    children[i].state |= STATE_DISPOSED;
                    if (children[i] instanceof Container) {
                        ((Container) children[i]).dispose();
                    }
                    children[i] = null;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#remove(jcontrol.ui.viper.Component)
     */
    public void remove(Component component) {
        if (component == null) return;
        synchronized (children) {
            int index = -1;
            for (int i = 0; i < children.length; i++) {
                if (children[i] == component) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                component.state |= STATE_DISPOSED;
                if (component instanceof Container) {
                    component.parent = null;
                    ((Container) component).removeAll();
                }
                if (focusIndex == index) {
                    IFrame root = getFrame();
                    if (root != null) root.transferFocus(null, IFocusable.TRANSFER_FOCUS_FORWARD);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#removeAll()
     */
    public void removeAll() {
        synchronized (children) {
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    children[i].state |= STATE_DISPOSED;
                }
            }
            if ((state & STATE_FOCUS) == STATE_FOCUS) {
                IFrame root = getFrame();
                if (root != null) {
                    root.clearFocus(parent);
                    root.transferFocus(null, IFocusable.TRANSFER_FOCUS_FORWARD);
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#repaint()
     */
    @Override
    public void repaint() {
        if (!isVisible()) return;
        state |= STATE_DIRTY_REPAINT;
        synchronized (children) {
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    if (children[i] instanceof Container) {
                        children[i].repaint();
                    } else {
                        if (children[i].isVisibleInternal()) {
                            children[i].state |= STATE_DIRTY_REPAINT;
                        }
                    }
                }
            }
        }
        redrawInternalAndParent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#setDirty(java.lang.Object, int, int, int, int, int,
     * boolean)
     */
    public boolean setDirty(Object source, int x, int y, int width, int height, int newstate,
            boolean onoff) {
        // if (!isVisible()) return false;
        boolean result = false;
        for (int i = 0; i < children.length; i++) {
            Component c = children[i];
            if (source == null || c != source) {
                if (c instanceof Container) {
                    if (((Container) c).setDirty(null, x, y, width, height, newstate, onoff)) {
                        this.state |= STATE_DIRTY_REPAINT;
                        result |= true;
                    }
                } else if (c != null) {
                    if ((x < c.x + c.width) && (y < c.y + c.height) && (x + width > c.x)
                            && (y + height > c.y)) {
                        if (onoff) {
                            if (c.isVisible()) {
                                this.state |= STATE_DIRTY_REPAINT;
                                c.state |= newstate;
                                result |= true;
                            }
                        } else {
                            this.state |= STATE_DIRTY_REPAINT;
                            c.state &= ~newstate;
                            result |= true;
                        }
                    }
                }
            }
        }
        if (parent != null && source != null) {
            parent.setDirty(this, x, y, width, height, newstate, onoff);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#setFont(jcontrol.io.Resource)
     */
    @Override
    public void setFont(Resource font) {
        synchronized (children) {
            for (int i = 0; i < children.length; i++) {
                // set font
                Component c = children[i];
                if (c != null && (c.font == null || c.font == this.font)) {
                    if (isVisibleInternal()) state |= STATE_DIRTY_REPAINT;
                    c.setFont(font);
                }
            }
        }
        this.font = font;
        redrawInternalAndParent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#setVisible(boolean)
     */
    @Override
    public synchronized void setVisible(boolean visible) {
        if (visible == isVisibleInternal()) return;
        if (visible) {
            state |= STATE_VISIBLE;
            repaint();
        } else {
            repaint();
            state &= ~(STATE_VISIBLE | STATE_SELECTED); // unselect component
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#update(jcontrol.io.Graphics)
     */
    @Override
    public void update(Graphics g) {
        state &= ~STATE_UPDATED; // reset update flag
        if (g == null || (state & STATE_DIRTY_MASK) == 0) return;
        paint(g);
        state &= ~STATE_DIRTY_MASK;
        synchronized (children) {
            int offset = focusIndex >= 0 ? focusIndex : 0;
            for (int i = offset; i < children.length + offset; i++) {
                Component c = children[i % children.length];
                if (c != null) {
                    synchronized (c) {
                        if ((c.state & STATE_DISPOSED) != 0) {
                            children[i % children.length] = null;
                            c.state &= ~STATE_DISPOSED;
                            g.clearRect(c.x, c.y, c.width, c.height);
                            if (parent != null) {
                                parent.setDirty(this, c.x, c.y, c.width, c.height,
                                        STATE_DIRTY_REPAINT, true);
                            }
                            c.parent = null;
                            state |= STATE_UPDATED;
                        } else {
                            c.update(g);
                        }

                        if ((c.state & STATE_UPDATED) != 0) {
                            state |= STATE_UPDATED;
                        }
                        if ((c.state & STATE_DIRTY_MASK) != 0) {
                            state |= STATE_DIRTY_REPAINT;
                        }
                        if ((c.state & STATE_ABORT_UPDATE) != 0) {
                            state |= STATE_DIRTY_REPAINT | STATE_ABORT_UPDATE;
                            c.state &= ~STATE_ABORT_UPDATE;
                            return;
                        }
                    }
                }
            }
        }

    }

}
