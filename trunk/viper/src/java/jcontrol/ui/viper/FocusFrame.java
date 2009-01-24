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
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Management;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.ActionListener;
import jcontrol.ui.viper.event.IEvent;
import jcontrol.ui.viper.event.IKeyListener;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.util.Queue;

/**
 * <p>
 * A Frame is the starting point for creating a graphical user interface. Only one frame instance
 * should exist in an application. <br>
 * An instance of this class should be used to create a keyboard-focus based application. TODO:
 * Rotary touch sensor not implemented.
 * </p>
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.IFrame
 * @see jcontrol.ui.viper.TouchFrame
 * @see jcontrol.ui.viper.Frame
 * @since Viper 1.0
 * @version $Revision$
 */
public class FocusFrame extends Component implements IFrame, IKeyListener {

    private static final int TYPE_KEYBOARD = 1 << 10;

    /** Keyboard access */
    public static Keyboard keyboard;

    public static Graphics graphics;

    private Container content;

    private Component outline;

    /** time interval between key buffer reads and graphics update in millis */
    private static final int SLEEP_INTERVAL = 50;

    /**
     * <p>
     * Constructs a new Frame.
     * </p>
     * <p>
     * The dimensions are equivalent to the display size. They may be changed by using
     * <code>setBounds()</code>.
     * </p>
     */
    public FocusFrame() {
        state |= TYPE_KEYBOARD;
        // top-level components are initially invisible
        state &= ~STATE_VISIBLE;
        state &= ~STATE_DIRTY_MASK;
        initDimensions();
    }

    /**
     * <p>
     * Constructs a new Frame.
     * </p>
     * <p>
     * The dimensions are equivalent to the display size. They may be changed by using
     * <code>setBounds()</code>.
     * </p>
     * 
     * @param graphics An instance of a custom display adapter.
     * @param keys An instance of a keyboard interface.<br>
     *        If this parameter is <code>null</code>, this application will not have keyboard
     *        support.
     */
    public FocusFrame(Graphics graphics, Keyboard keys) {
        // top-level components are initially invisible
        state &= ~STATE_VISIBLE;
        state &= ~STATE_DIRTY_MASK;
        FocusFrame.keyboard = keys;
        if (keys != null) state |= TYPE_KEYBOARD;
        FocusFrame.graphics = graphics;
        initDimensions();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#add(jcontrol.ui.viper.Component)
     */
    public void add(Component component) {
        if (content == null) {
            // create a root Container
            setContent(new Container());
        }
        content.add(component);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#clearFocus(jcontrol.ui.viper.IContainer)
     */
    public void clearFocus(IContainer icontainer) {
        if (icontainer == null || icontainer == this) {
            if (content != null && (content.state & STATE_FOCUS) == STATE_FOCUS) {
                icontainer = content;
            } else if (outline != null && (outline.state & STATE_FOCUS) == STATE_FOCUS) {
                if (outline instanceof Container) {
                    icontainer = (Container) outline;
                } else if (outline instanceof IFocusable
                        && (outline.state & STATE_FOCUS) == STATE_FOCUS) {
                    outline.state &= ~STATE_FOCUS;
                    outline.state |= STATE_DIRTY_REPAINT;
                    return;
                }
            }
        }
        if (icontainer instanceof Container) {
            Container container = (Container) icontainer;
            if ((container.state & STATE_FOCUS) == STATE_FOCUS && container.isVisible()) {
                synchronized (container) {
                    container.state &= ~STATE_FOCUS;
                    container.state |= STATE_DIRTY_REPAINT;
                    if (container.focusIndex >= 0
                            && container.focusIndex < container.children.length) {
                        Component c = container.children[container.focusIndex];
                        container.focusIndex = -1;
                        if (c != null && c.isVisible()) {
                            if (c instanceof IFocusable) {
                                synchronized (c) {
                                    c.state &= ~STATE_FOCUS;
                                    c.state |= STATE_DIRTY_REPAINT;
                                }
                            } else if (c instanceof Container) {
                                clearFocus((Container) c);
                            }
                        }
                    }
                    container.focusIndex = -1;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#getContent()
     */
    public Container getContent() {
        return content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#getOutline()
     */
    public Component getOutline() {
        return outline;
    }

    /**
     * Init display dimensions.
     */
    private void initDimensions() {
        x = 0;
        y = 0;
        String str = Management.getProperty("display.dimensions");
        if (str == null) {
            width = 128; // jcvm8 default
            height = 64; // jcvm8 default
        } else {
            int firstx = str.indexOf("x", 0);
            width = Integer.parseInt(str.substring(0, firstx));
            height = Integer.parseInt(str.substring(firstx + 1, str.indexOf("x", firstx + 1)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#onEvent(jcontrol.ui.viper.Container, java.lang.Object)
     */
    public int onEvent(IContainer container, IEvent event) {
        if (event instanceof KeyEvent) {
            if (container == this) {
                if (outline != null && (outline.state & STATE_FOCUS) == STATE_FOCUS) {
                    if (outline instanceof IFocusable) {
                        event = ((IFocusable) outline).onKeyEvent((KeyEvent) event);
                    } else if (outline instanceof Container) {
                        if (onEvent((Container) outline, event) == 1) return 1;
                    }
                } else {
                    if (content != null) {
                        if (onEvent(content, event) == 1) return 1;
                    }
                }
            } else if (container instanceof Container) {
                Container source = (Container) container;
                if ((source.state & STATE_FOCUS) == STATE_FOCUS && source.focusIndex >= 0
                        && source.focusIndex < source.children.length) {
                    Component c = source.children[source.focusIndex];
                    if (c instanceof IFocusable) {
                        event = ((IFocusable) c).onKeyEvent((KeyEvent) event);
                    } else if (c instanceof Container) {
                        if (onEvent((Container) c, event) == 1) return 1;
                    }
                }
            }
            if (event == null) return 1;
            switch (((KeyEvent) event).m_key) {
                case KeyEvent.KEY_TRANSFER_FOCUS_BACKWARD:
                case KeyEvent.KEY_UP_PRESSED:
                case KeyEvent.KEY_LEFT_PRESSED:
                    if (container == this) {// transfer focus only if we are the top-most Container
                        if (transferFocus(this, IFocusable.TRANSFER_FOCUS_BACKWARD)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                case KeyEvent.KEY_TRANSFER_FOCUS_FORWARD:
                case KeyEvent.KEY_DOWN_PRESSED:
                case KeyEvent.KEY_RIGHT_PRESSED:
                    if (container == this) {// transfer focus only if we are the top-most Container
                        if (transferFocus(this, IFocusable.TRANSFER_FOCUS_FORWARD)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                default:
            }
            return 0;
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.event.IKeyListener#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    public KeyEvent onKeyEvent(KeyEvent e) {
        onEvent(this, e);
        return e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#remove(jcontrol.ui.viper.Component)
     */
    public void remove(Component component) {
        if (content != null) content.remove(component);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#removeAll()
     */
    public void removeAll() {
        if (content != null) content.removeAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#repaint()
     */
    @Override
    public void repaint() {
        if (!isVisible()) return;
        this.state |= STATE_DIRTY_REPAINT;
        if (content != null) content.repaint();
        if (outline != null) outline.repaint();
    }

    /**
     * 
     */
    public boolean requestFocus(Component c) {
        if (c == null || !c.isVisible()) return false;
        synchronized (c) {
            if (c.parent instanceof Container) {
                Container parent = (Container) c.parent;
                int i = -1;
                for (i = 0; i < parent.children.length; i++) {
                    if (parent.children[i] == c) {
                        break;
                    }
                }
                if (i != -1) {
                    parent.focusIndex = i;
                    parent.state |= STATE_FOCUS | STATE_DIRTY_REPAINT;
                    c.state |= STATE_FOCUS | STATE_DIRTY_REPAINT;
                    if (requestFocus(parent)) { return true; }
                }
            } else if (c.parent == this) {
                c.state |= STATE_FOCUS | STATE_DIRTY_REPAINT;
                state |= STATE_FOCUS;
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#setContent(jcontrol.ui.viper.Container)
     */
    public synchronized void setContent(Container container) {
        if (container != content) {
            if (content != null) {
                synchronized (content) {
                    if (container == null) {
                        content.dispose();
                    } else {
                        content.parent = null;
                    }
                    content = null;
                }
            }
            if (container != null) {
                if (container.font == null && this.font != null) {
                    container.setFont(font);
                }
                container.parent = this;
            }
            content = container;
            if (outline != null) {
                synchronized (outline) {
                    state |= STATE_DIRTY_PAINT_ALL;
                    outline.repaint();
                }
            } else {
                state |= STATE_DIRTY_PAINT_ALL;
                if (content != null) {
                    transferFocus(this, IFocusable.TRANSFER_FOCUS_FORWARD);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#setDirty(int, int, int, int, int, boolean)
     */
    public boolean setDirty(Object source, int x, int y, int width, int height, int state, boolean b) {
        this.state |= STATE_DIRTY_UPDATE;
        boolean result = false;
        if (content != null && (source == null || source != content)) {
            content.setDirty(null, x, y, width, height, state, b);
            result |= true;
        }
        if (outline != null && (source == null || source != outline)) {
            if (outline instanceof Container) {
                if (((Container) outline).setDirty(null, x, y, width, height, state, b)) {
                    result |= true;
                }
            } else {
                if ((x < outline.x + outline.width) && (y < outline.y + outline.height)
                        && (x + width > outline.x) && (y + height > outline.y)) {
                    if (b) {
                        if (outline.isVisible()) {
                            outline.state |= state;
                            result |= true;
                        }
                    } else {
                        outline.state &= ~state;
                        result |= true;
                    }
                }
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#setFont(jcontrol.io.Resource)
     */
    @Override
    public synchronized void setFont(Resource font) {
        if (font != this.font) {
            if (outline != null && (outline.font == null || outline.font == this.font)) {
                outline.setFont(font);
            }
            if (content != null && (content.font == null || content.font == this.font)) {
                content.setFont(font);
            }
            this.font = font;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#setStaticElement(jcontrol.ui.viper.Component)
     */
    public void setOutline(Component component) {
        if (component != outline) {
            outline = null;
            if (component != null) {
                if (component.font == null && this.font != null) {
                    component.setFont(font);
                }
                component.parent = this;
                outline = component;
                if (outline != null && (content == null || (content.state & STATE_FOCUS) == 0)) {
                    transferFocus(null, IFocusable.TRANSFER_FOCUS_FORWARD);

                }
            } else {
                outline = null;
            }
            repaint();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible == ((state & STATE_VISIBLE) != 0)) return;
        if (content != null) {
            content.setVisible(visible);
        }
        if (outline != null) {
            outline.setVisible(visible);
        }
        if (visible) {
            if ((state & STATE_VISIBLE) != 0) return;
            state |= STATE_VISIBLE | STATE_DIRTY_REPAINT;
            // init keyboard
            if ((state & TYPE_KEYBOARD) != 0 && keyboard == null) {
                keyboard = new Keyboard();
            }
            // init display
            if (graphics == null) graphics = new Display();
            if ((state & TYPE_KEYBOARD) != 0 && (state & STATE_FOCUS) == 0)
                transferFocus(this, IFocusable.TRANSFER_FOCUS_FORWARD);
            new Thread() {

                @Override
                public void run() {
                    // setPriority(9);
                    int key = -1;
                    eventQueue = new Queue(2);
                    for (; (state & STATE_VISIBLE) != 0;) {
                        boolean worked = false;
                        if ((state & TYPE_KEYBOARD) != 0) {
                            key = workKeyEvents(key);
                            worked |= ((state & STATE_UPDATED) != 0);
                        }
                        if (worked) { // update graphics after user input
                            update(graphics);
                            worked |= ((state & STATE_UPDATED) != 0);
                        }
                        worked |= workEventQueue();
                        update(graphics); // always here
                        worked |= ((state & STATE_UPDATED) != 0);
                        try {
                            if (!worked) {
                                ThreadExt.sleep(SLEEP_INTERVAL);
                            } else {
                                ThreadExt.sleep(1);
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }.start();
        } else {
            state &= ~STATE_VISIBLE;
            keyboard = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#transferFocus(jcontrol.ui.viper.IContainer, int)
     */
    public boolean transferFocus(IContainer c, int direction) {
        if (c == this || c == null) {
            boolean found = false;
            int startPosition = 0;
            if (outline != null && (outline.state & STATE_FOCUS) == STATE_FOCUS) {
                startPosition = 0;
            } else if (content != null && (content.state & STATE_FOCUS) == STATE_FOCUS) {
                startPosition = 1;
            }
            int position = startPosition;
            int count = 0;
            do {
                switch (position & 1) {
                    case 0:
                        if (outline instanceof Container) {
                            found = transferFocus((Container) outline, direction);
                        } else if (outline instanceof IFocusable) {
                            if (((outline).state & STATE_FOCUS) == STATE_FOCUS) {
                                synchronized (outline) {
                                    // remove focus from outline
                                    outline.state &= ~STATE_FOCUS;
                                    outline.repaint();
                                }
                            } else if (outline.isVisible()) {
                                outline.setDirty(STATE_DIRTY_REPAINT | STATE_FOCUS, true);
                                found = true;
                            }
                        }
                        if (found && content != null) content.state &= ~STATE_FOCUS;
                        if (startPosition == 0) count++;
                        break;
                    case 1:
                        if (content != null) {
                            found = transferFocus(content, direction);
                            if (found && outline != null) outline.state &= ~STATE_FOCUS;
                        }
                        if (startPosition == 1) count++;
                        break;
                }
                position++;
            } while (!found && count < 2);
            if (found) {
                state |= STATE_FOCUS;
            } else {
                state &= ~STATE_FOCUS;
            }
            return found;
        } else if (c instanceof Container) {
            synchronized (c) {
                Container caller = (Container) c;
                boolean found = false;
                // select first, last or current selected component
                int position = ((caller.state & STATE_FOCUS) == STATE_FOCUS) ? caller.focusIndex
                        : -1;
                if (direction == IFocusable.TRANSFER_FOCUS_FORWARD) {
                    if (position == -1) { // search first focusable in container
                        for (int i = 0; i < caller.children.length; i++) {
                            if (caller.children[i] instanceof Container
                                    || caller.children[i] instanceof IFocusable) {
                                position = i;
                                break;
                            }
                        }
                    }
                } else {
                    if (position == -1) {// search last focusable in container
                        for (int i = caller.children.length - 1; i >= 0; i--) {
                            if (caller.children[i] instanceof Container
                                    || caller.children[i] instanceof IFocusable) {
                                position = i;
                                break;
                            }
                        }
                    }
                }

                for (; !found && position < caller.children.length && position >= 0;) {
                    Component child = caller.children[position];
                    if (child != null) {
                        if (child.isVisible()) {
                            if (child instanceof Container) {
                                found = transferFocus((Container) child, direction);
                            } else if (child instanceof IFocusable) {
                                if ((child.state & STATE_FOCUS) == STATE_FOCUS) {
                                    synchronized (child) {
                                        // remove focus from child
                                        child.state &= ~STATE_FOCUS;
                                        child.state |= STATE_DIRTY_REPAINT;
                                    }
                                } else if (child.isVisible()) {
                                    child.state |= STATE_DIRTY_REPAINT | STATE_FOCUS;
                                    found = true;
                                }
                            }
                        }
                    }
                    if (!found) {
                        if (direction == IFocusable.TRANSFER_FOCUS_FORWARD) {
                            position++;
                        } else {
                            position--;
                            // if there is no parent object go forward to the last component
                        }
                    }
                }
                if (found) {
                    caller.focusIndex = position;
                    caller.state |= STATE_FOCUS | STATE_DIRTY_REPAINT;
                    return true;
                }

                // reset focus flags
                caller.focusIndex = -1;
                caller.state &= ~STATE_FOCUS;
                caller.state |= STATE_DIRTY_REPAINT;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#update(jcontrol.io.Graphics)
     */
    @Override
    public void update(Graphics g) {
        state &= ~STATE_UPDATED;

        if ((state & STATE_DIRTY_MASK) != 0) {
            if (!isVisible() || (state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
                g.clearRect(x, y, width, height);
                state &= ~STATE_DIRTY_MASK;
                state |= STATE_UPDATED;
                if (!isVisible()) return;
            }
        }
        if (outline != null) {
            outline.update(g);
            if ((outline.state & STATE_UPDATED) != 0) {
                state |= STATE_UPDATED;
            }
            outline.state &= ~STATE_ABORT_UPDATE;
        }
        synchronized (this) {
            if (content != null) {
                content.update(g);
                if ((content.state & STATE_UPDATED) != 0) {
                    state |= STATE_UPDATED;
                }
                content.state &= ~STATE_ABORT_UPDATE;
            }
        }
    }

    /**
     * @return
     */
    private boolean workEventQueue() {
        // work event queue
        ActionEvent event;
        boolean result = false;
        for (; (event = (ActionEvent) eventQueue.pop()) != null;) {
            ActionListener listener = event.source.getActionListener();
            if (listener != null) {
                listener.onActionEvent(event);
            }
            result = true;
        }
        return result;
    }

    /**
     * @param key
     * @return
     */
    private int workKeyEvents(int key) {
        if (keyboard == null) return -1;
        char k;
        if ((k = keyboard.getKey()) != 0) {
            switch (k) {
                case 'u':
                case 'U':
                    key = KeyEvent.KEY_UP;
                    break;
                case 'd':
                case 'D':
                    key = KeyEvent.KEY_DOWN;
                    break;
                case 'L':
                    key = KeyEvent.KEY_LEFT;
                    break;
                case 'R':
                    key = KeyEvent.KEY_RIGHT;
                    break;
                case 'S':
                    key = KeyEvent.KEY_SELECT;
                    break;
                default:
                    key = -1;
            }
        }
        if (key > -1) {
            if (k != 0) { // the key has been pressed now
                onKeyEvent(new KeyEvent(key | KeyEvent.TYPE_KEY_PRESSED, k));
                state |= STATE_UPDATED;
            } else { // the key has been pressed in previous loop
                onKeyEvent(new KeyEvent(key, k));
                key = -1;
                state |= STATE_UPDATED;
            }
        }
        return key;
    }

}
