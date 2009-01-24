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

import java.io.IOException;

import jcontrol.io.Display;
import jcontrol.io.Graphics;
import jcontrol.io.IPointingDevice;
import jcontrol.io.PWM;
import jcontrol.io.Resource;
import jcontrol.io.Touch;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Management;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.ActionListener;
import jcontrol.ui.viper.event.IEvent;
import jcontrol.ui.viper.event.ITouchListener;
import jcontrol.ui.viper.event.TouchEvent;
import jcontrol.util.Queue;

/**
 * <p>
 * A Frame is the starting point for creating a graphical user interface. Only one frame instance
 * should exist in an application. <br>
 * An instance of this class should be used to create a touch screen based application.
 * </p>
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.IFrame
 * @see jcontrol.ui.viper.FocusFrame
 * @see jcontrol.ui.viper.Frame
 * @since Viper 1.0
 * @version $Revision$
 */
public class TouchFrame extends Component implements IFrame {

    private static final int TYPE_TOUCH = 1 << 11;
    public static Graphics graphics;
    private Container content;
    private Component outline;
    /** time interval between key buffer reads and graphics update in millis */
    private static final int SLEEP_INTERVAL = 50;

    public static IPointingDevice pointingDevice;

    /**
     * Constructs a new Frame with a default Display and Touch.
     */
    public TouchFrame() {
        state |= TYPE_TOUCH;
        // top-level components are initially invisible
        state &= ~STATE_VISIBLE;
        state &= ~STATE_DIRTY_MASK;
        initDimensions();
    }

    /**
     * Constructs a new Frame.
     * 
     * @param graphics An instance of a custom display adapter. <br>
     *        If this parameter is <code>null</code>, a default <code>jcontrol.io.Display</code>
     *        -object will be created.
     * @param pointingDevice An instance of a pointing device, e.g. <code>jcontrol.io.Touch</code>,
     *        or <code>null</code>.<br>
     *        If this parameter is <code>null</code>, this application will not have touch support.
     * @see jcontrol.ui.viper.FocusFrame
     */
    public TouchFrame(Graphics graphics, IPointingDevice pointingDevice) {
        // top-level components are initially invisible
        state &= ~STATE_VISIBLE;
        state &= ~STATE_DIRTY_MASK;
        TouchFrame.pointingDevice = pointingDevice;
        if (pointingDevice != null) state |= TYPE_TOUCH;
        TouchFrame.graphics = graphics;
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

    public Container getContent() {
        return content;
    }

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
        if (event instanceof TouchEvent) {
            if (container == this) {
                int result = ITouchListener.RESULT_NONE;
                if (content != null) {
                    result = onEvent(content, event);
                    if (result > ITouchListener.RESULT_NONE) {
                        if (outline != null && (outline.state & STATE_FOCUS) == STATE_FOCUS) {
                            if (outline instanceof Container) {
                                clearFocus((Container) outline);
                            } else if (outline instanceof ITouchListener) {
                                outline.state &= ~STATE_FOCUS;
                                outline.state |= STATE_DIRTY_REPAINT;
                            }
                        }
                        return result;
                    }
                }
                if (outline instanceof ITouchListener) {
                    result = ((ITouchListener) outline).onTouchEvent((TouchEvent) event);
                    if (result > ITouchListener.RESULT_NONE) {
                        if (content != null && (content.state & STATE_FOCUS) == STATE_FOCUS) {
                            clearFocus(content);
                        }
                        return result;
                    }
                } else if (outline instanceof Container) {
                    result = onEvent((Container) outline, event);
                    if (result > ITouchListener.RESULT_NONE) {
                        if (content != null && (content.state & STATE_FOCUS) == STATE_FOCUS) {
                            clearFocus(content);
                        }
                        return result;
                    }
                }
                return result;
            } else if (container instanceof Container) {
                Container source = (Container) container;
                synchronized (source.children) {
                    int start = source.focusIndex >= 0 ? source.focusIndex : 0;
                    for (int i = start; i < source.children.length + start; i++) {
                        Component c = source.children[i % source.children.length];
                        if (c != null) {
                            if ((c.state & STATE_VISIBLE) == STATE_VISIBLE
                                    && (c.state & STATE_DISPOSED) == 0) {
                                int result = ITouchListener.RESULT_NONE;
                                if (c instanceof ITouchListener) {
                                    result = ((ITouchListener) c).onTouchEvent((TouchEvent) event);
                                } else if (c instanceof IContainer) {
                                    result = onEvent((IContainer) c, event);
                                }
                                if (result > ITouchListener.RESULT_NONE) {
                                    if (source.focusIndex != i % source.children.length
                                            || (source.state & STATE_FOCUS) == 0) {
                                        clearFocus(source);
                                        source.focusIndex = i % source.children.length;
                                        c.state |= STATE_FOCUS | STATE_DIRTY_REPAINT;
                                    }
                                    source.state |= STATE_FOCUS | STATE_DIRTY_REPAINT;
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
        }
        return ITouchListener.RESULT_NONE;
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

    @Override
    public void repaint() {
        if (!isVisible()) return;
        this.state |= STATE_DIRTY_REPAINT;
        if (content != null) content.repaint();
        if (outline != null) outline.repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#requestFocus(jcontrol.ui.viper.Component)
     */
    public boolean requestFocus(Component component) {
        return false;
    }

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

    /**
     * Shows or hides the frame. When the frame is hidden, all event handlers are stopped.
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
            state |= STATE_VISIBLE | STATE_DIRTY_REPAINT;
            // init display
            if (graphics == null) graphics = new Display();
            // create a touch screen object
            if ((state & TYPE_TOUCH) != 0 && pointingDevice == null) {
                try {
                    pointingDevice = new Touch();
                } catch (IOException e) {
                    state &= ~TYPE_TOUCH;
                }
            }
            new Thread() {

                @Override
                public void run() {
                    // setPriority(9);
                    int touchType = 0;
                    eventQueue = new Queue(2);
                    for (; (state & STATE_VISIBLE) != 0;) {
                        boolean worked = false;
                        if ((state & TYPE_TOUCH) != 0) {
                            touchType = workTouchEvents(touchType);
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
            pointingDevice = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#transferFocus(jcontrol.ui.viper.IContainer, int)
     */
    public boolean transferFocus(IContainer c, int direction) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Paint all components or a menu.
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

    private int workTouchEvents(int touchType) {
        if (pointingDevice == null) return 0;
        state &= ~STATE_UPDATED;
        if (pointingDevice.isPressed()) {
            int result = onEvent(this, new TouchEvent(TouchEvent.TYPE_TOUCH_PRESSED | touchType,
                    pointingDevice.getX(), pointingDevice.getY()));
            state |= STATE_UPDATED;
            if (result > ITouchListener.RESULT_NONE && touchType == 0) {
                PWM.setFrequency(result == ITouchListener.RESULT_ACCEPTED ? 1000 : 2000);
                PWM.setDuty(3, -127);
                PWM.setActive(3, true);
                try {
                    ThreadExt.sleep(25);
                } catch (InterruptedException e) {
                }
                PWM.setActive(3, false);
            }
            return TouchEvent.TYPE_TOUCH_DRAGGED; // will be ORed on next touch event
        } else if (touchType > TouchEvent.TYPE_TOUCH_RELEASED) {
            state |= STATE_UPDATED;
            onEvent(this, new TouchEvent(TouchEvent.TYPE_TOUCH_RELEASED, pointingDevice.getX(),
                    pointingDevice.getY()));
        }
        return TouchEvent.TYPE_TOUCH_RELEASED;
    }
}