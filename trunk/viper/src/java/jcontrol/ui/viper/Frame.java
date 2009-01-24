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
import jcontrol.io.IRotaryTouchDevice;
import jcontrol.io.Keyboard;
import jcontrol.io.MPR083;
import jcontrol.io.PWM;
import jcontrol.io.Resource;
import jcontrol.io.Touch;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Management;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.ActionListener;
import jcontrol.ui.viper.event.IEvent;
import jcontrol.ui.viper.event.IKeyListener;
import jcontrol.ui.viper.event.IRotaryTouchListener;
import jcontrol.ui.viper.event.ITouchListener;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;
import jcontrol.util.Queue;

/**
 * <p>
 * A Frame is the starting point for creating a graphical user interface. Only one frame instance
 * should exist in an application. <br>
 * An instance of this class should be used to create a keyboard and touch screen based application.
 * </p>
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.IFrame
 * @see jcontrol.ui.viper.TouchFrame
 * @see jcontrol.ui.viper.FocusFrame
 * @since Viper 1.0
 * @version $Revision$
 */
public class Frame extends Component implements IFrame, IKeyListener, IRotaryTouchListener {

    public static final int FIELD_STATE_IDLE = 0;

    public static final int FIELD_STATE_TOUCHED_TIMEOUT = 1;

    public static final int FIELD_STATE_TOUCHED = 2;

    public static final int FIELD_STATE_TOUCHED_MOVE = 3;

    public static final int FIELD_STATE_SELECTED = 4;

    /** Last touched rotary touch field. */
    int lastTouchedField = -1;

    /** Field state of the rotary touch sensor. */
    int fieldState = 0;

    /** Flag indicating which input devices are connected. */
    private static int inputDevices;

    /** Flag keyboard. Value: 0001. */
    private static final int FLAG_KEYBOARD_CONNECTED = 1 << 0;

    /** Flag touch device. Value: 0010. */
    private static final int FLAG_TOUCH_CONNECTED = 1 << 1;

    /** Flag rotary touch connected. Value: 0100. */
    private static final int FLAG_ROTARY_TOUCH_CONNECTED = 1 << 2;

    /** A keyboard device. */
    public static Keyboard keyboard;

    /** A pointing device. */
    public static IPointingDevice pointingDevice;

    /** A touch sensor device. */
    public static IRotaryTouchDevice rotaryTouchDevice;

    /** A custom display adapter. */
    public static Graphics graphics;

    /** The frame content. */
    private Container m_content;

    /** The frame outline. */
    private Component m_outline;

    /** The time interval between key buffer reads and graphics update in millis */
    private static final int SLEEP_INTERVAL = 50;

    private static TextViewer debugViewer;

    /**
     * Creates a new frame.
     * <p>
     * The dimensions are equivalent to the display size. They may be changed by using
     * <code>setBounds()</code>.
     * </p>
     */
    public Frame() {
        // inputDevices |= FLAG_KEYBOARD_CONNECTED | FLAG_TOUCH_CONNECTED;
        // top-level components are initially invisible
        state &= ~STATE_VISIBLE;
        state &= ~STATE_DIRTY_MASK;
        initDimensions();
    }

    /**
     * Creates a new frame.
     * <p>
     * The dimensions are equivalent to the display size. They may be changed by using
     * <code>setBounds()</code>.
     * </p>
     * 
     * @param graphics An instance of a custom display adapter.<br>
     *        If this parameter is <code>null</code>, this application will not have keyboard
     *        support.
     * @param keyboard An instance of a keyboard interface.
     * @param pointingDevice An instance of a pointing device, e.g. <code>jcontrol.io.Touch</code>,
     *        or <code>null</code>.<br>
     *        If this parameter is <code>null</code>, this application will not have touch support.
     */
    public Frame(Graphics graphics, Keyboard keyboard, IPointingDevice pointingDevice) {
        // top-level components are initially invisible
        state &= ~STATE_VISIBLE;
        state &= ~STATE_DIRTY_MASK;
        Frame.keyboard = keyboard;
        if (keyboard != null) inputDevices |= FLAG_KEYBOARD_CONNECTED;
        if (pointingDevice != null) inputDevices |= FLAG_TOUCH_CONNECTED;
        Frame.graphics = graphics;
        initDimensions();
    }

    /**
     * Creates a new frame with an rotary touch.
     * <p>
     * The dimensions are equivalent to the display size. They may be changed by using
     * <code>setBounds()</code>.
     * </p>
     * 
     * @param graphics An instance of a custom display adapter.<br>
     * @param rotaryTouchDevice An instance of a rotary touch device, e.g.
     *        <code>jcontrol.io.MPR083</code>, or <code>null</code>.
     */
    public Frame(IRotaryTouchDevice rotaryTouchDevice) {
        // top-level components are initially invisible
        state &= ~STATE_VISIBLE;
        state &= ~STATE_DIRTY_MASK;
        inputDevices |= FLAG_TOUCH_CONNECTED;
        Frame.rotaryTouchDevice = rotaryTouchDevice;
        if (rotaryTouchDevice != null) inputDevices |= FLAG_ROTARY_TOUCH_CONNECTED;
        initDimensions();
        // debugViewer = new TextViewer(0,0, 100, 60, 0);
        // add(debugViewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#add(jcontrol.ui.viper.Component)
     */
    public void add(Component component) {
        if (m_content == null) {
            // create a root Container
            setContent(new Container());
        }
        m_content.add(component);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#clearFocus(jcontrol.ui.viper.IContainer)
     */
    public void clearFocus(IContainer icontainer) {
        if (icontainer == null || icontainer == this) {
            if (m_content != null && (m_content.state & STATE_FOCUS) == STATE_FOCUS) {
                icontainer = m_content;
            } else if (m_outline != null && (m_outline.state & STATE_FOCUS) == STATE_FOCUS) {
                if (m_outline instanceof Container) {
                    icontainer = (Container) m_outline;
                } else if (m_outline instanceof IFocusable
                        && (m_outline.state & STATE_FOCUS) == STATE_FOCUS) {
                    m_outline.state &= ~STATE_FOCUS;
                    m_outline.state |= STATE_DIRTY_REPAINT;
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
        return m_content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#getOutline()
     */
    public Component getOutline() {
        return m_outline;
    }

    /**
     * Inits the display dimensions.
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

    /**
     * @param container
     * @param event
     * @return
     */
    public int onEvent(IContainer container, IEvent event) {
        // Key events
        if (event instanceof KeyEvent) {
            if (container == this) {
                if (m_outline != null && (m_outline.state & STATE_FOCUS) == STATE_FOCUS) {
                    if (m_outline instanceof IFocusable) {
                        event = ((IFocusable) m_outline).onKeyEvent((KeyEvent) event);
                    } else if (m_outline instanceof Container) {
                        if (onEvent((Container) m_outline, event) == 1) return 1;
                    }
                } else {
                    if (m_content != null) {
                        if (onEvent(m_content, event) == 1) return 1;
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

            // Rotary touch events
        } else if (event instanceof RotaryTouchEvent) {
            if (container == this) {
                if (m_outline != null && (m_outline.state & STATE_FOCUS) == STATE_FOCUS) {
                    if (m_outline instanceof IRotaryTouchListener) {
                        event = ((IRotaryTouchListener) m_outline)
                                .onRotaryTouchEvent((RotaryTouchEvent) event);
                    } else if (m_outline instanceof Container) {
                        if (onEvent((Container) m_outline, event) == 1) return 1;
                    }
                } else {
                    if (m_content != null) {
                        if (onEvent(m_content, event) == 1) return 1;
                    }
                }
            } else if (container instanceof Container) {
                Container source = (Container) container;
                if ((source.state & STATE_FOCUS) == STATE_FOCUS && source.focusIndex >= 0
                        && source.focusIndex < source.children.length) {
                    Component c = source.children[source.focusIndex];
                    if (c instanceof IRotaryTouchListener) {
                        event = ((IRotaryTouchListener) c)
                                .onRotaryTouchEvent((RotaryTouchEvent) event);
                    } else if (c instanceof Container) {
                        if (onEvent((Container) c, event) == 1) return 1;
                    }
                    source.state |= STATE_FOCUS | STATE_DIRTY_REPAINT;
                }
            }
            if (event == null) return 1;
            RotaryTouchEvent rotaryTouchEvent = (RotaryTouchEvent) event;
            switch (rotaryTouchEvent.getRotaryDirection()) {
                // case KeyEvent.KEY_TRANSFER_FOCUS_BACKWARD:
                // case KeyEvent.KEY_UP_PRESSED:
                case RotaryTouchEvent.ROTATES_LEFT:
                    if (container == this) {// transfer focus only if we are the top-most Container
                        if (transferFocus(this, IFocusable.TRANSFER_FOCUS_BACKWARD)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                    // case KeyEvent.KEY_TRANSFER_FOCUS_FORWARD:
                    // case KeyEvent.KEY_DOWN_PRESSED:
                case RotaryTouchEvent.ROTATES_RIGHT:
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

            // Touch events
        } else if (event instanceof TouchEvent) {
            if (container == this) {
                int result = ITouchListener.RESULT_NONE;
                if (m_content != null) {
                    result = onEvent(m_content, event);
                    if (result > ITouchListener.RESULT_NONE) {
                        if (m_outline != null && (m_outline.state & STATE_FOCUS) == STATE_FOCUS) {
                            if (m_outline instanceof Container) {
                                clearFocus((Container) m_outline);
                            } else if (m_outline instanceof ITouchListener) {
                                m_outline.state &= ~STATE_FOCUS;
                                m_outline.state |= STATE_DIRTY_REPAINT;
                            }
                        }
                        return result;
                    }
                }
                if (m_outline instanceof ITouchListener) {
                    result = ((ITouchListener) m_outline).onTouchEvent((TouchEvent) event);
                    if (result > ITouchListener.RESULT_NONE) {
                        if (m_content != null && (m_content.state & STATE_FOCUS) == STATE_FOCUS) {
                            clearFocus(m_content);
                        }
                        return result;
                    }
                } else if (m_outline instanceof Container) {
                    result = onEvent((Container) m_outline, event);
                    if (result > ITouchListener.RESULT_NONE) {
                        if (m_content != null && (m_content.state & STATE_FOCUS) == STATE_FOCUS) {
                            clearFocus(m_content);
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
                            if (c.isVisible() && (c.state & STATE_DISPOSED) == 0) {
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
                                        c.state |= STATE_FOCUS;
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

    /**
     * This method is called when a key has been pressed on the keyboard. The KeyEvent object is
     * passed down through the component tree to the current focus component that may consume the
     * event or force the frame to transfer the keyboard focus to the next component.<br>
     * This method may be overwritten if any user specified keyboard behavior is desired. In this
     * case, the standard focus based keyboard functionality will no longer work.
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
     * @seejcontrol.ui.viper.event.IRotaryTouchListener#onRotaryTouchEvent(jcontrol.ui.viper.event.
     * RotaryTouchEvent)
     */
    public RotaryTouchEvent onRotaryTouchEvent(RotaryTouchEvent e) {
        onEvent(this, e);
        return e;
    }

    /**
     * @return
     */
    private boolean processEventQueue() {
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
     * Processes the key events.
     * 
     * @param key
     * @return -1 if keyboard is null...
     */
    private int processKeyEvents(int key) {
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
                onEvent(this, new KeyEvent(key | KeyEvent.TYPE_KEY_PRESSED, k));
                state |= STATE_UPDATED;
            } else { // the key has been pressed in previous loop
                onEvent(this, new KeyEvent(key, k));
                key = -1;
                state |= STATE_UPDATED;
            }
        }
        return key;
    }

    /**
     * Processes the rotary touch events
     * 
     * @param rotaryTouchType
     * @return
     * @throws IOException if rotary touch sensor is not working
     */
    public int processRotaryTouchEvents(int rotaryTouchType) throws IOException {

        // get state of the rotary touch device
        int rotaryState = rotaryTouchDevice.getRotaryState();
        boolean isTouched = (0 != (rotaryState & IRotaryTouchDevice.FLAG_STATUS_TOUCHED));
        int touchedField = rotaryState & IRotaryTouchDevice.FLAG_STATUS_CP_MASK;

        // 
        RotaryTouchEvent rotaryTouchEvent = null;

        // trigger state machine
        switch (fieldState) {
            case FIELD_STATE_IDLE: {
                if (isTouched) {
                    fieldState = FIELD_STATE_TOUCHED;
                    lastTouchedField = touchedField;
                    rotaryTouchEvent = new RotaryTouchEvent(touchedField, FIELD_STATE_TOUCHED,
                            RotaryTouchEvent.ROTATES_NONE);
                    // m_listener.pressed(position);
                }
                break;
            }
                /*
                 * case FIELD_STATE_TOUCHED_TIMEOUT: {
                 * 
                 * if (isTouched && lastTouchedField == touchedField) { break; // switch }
                 * fieldState = FIELD_STATE_TOUCHED; break; // | // fall trough \|/ // ' }
                 */

            case FIELD_STATE_TOUCHED: {
                if (isTouched && lastTouchedField == touchedField) {
                    break; // switch
                }
                if (!isTouched) {
                    rotaryTouchEvent = new RotaryTouchEvent(lastTouchedField, FIELD_STATE_SELECTED,
                            RotaryTouchEvent.ROTATES_NONE);
                    // m_listener.select( lastPosition );
                    // m_listener.released();
                    fieldState = FIELD_STATE_IDLE;
                    break; // switch
                }

                fieldState = FIELD_STATE_TOUCHED_MOVE;
                // |
                // fall trough \|/
                // '
                break;
            }

            case FIELD_STATE_TOUCHED_MOVE: {

                if (!isTouched) {
                    fieldState = FIELD_STATE_IDLE;
                    rotaryTouchEvent = new RotaryTouchEvent(-1, FIELD_STATE_IDLE,
                            RotaryTouchEvent.ROTATES_NONE);
                    lastTouchedField = touchedField;
                    // m_listener.released();
                    break; // switch
                }

                if (lastTouchedField != touchedField) {

                    if (lastTouchedField > touchedField) {
                        lastTouchedField -= 8;
                    }
                    int diff = touchedField - lastTouchedField;
                    if (diff < 4) {
                        rotaryTouchEvent = new RotaryTouchEvent(touchedField,
                                FIELD_STATE_TOUCHED_MOVE, RotaryTouchEvent.ROTATES_RIGHT);
                        // m_listener.rightRotate(position, diff);
                    } else if (diff > 4) {
                        rotaryTouchEvent = new RotaryTouchEvent(touchedField,
                                FIELD_STATE_TOUCHED_MOVE, RotaryTouchEvent.ROTATES_LEFT);
                        // m_listener.leftRotate(position, 8-diff);
                    }

                    // next time current position is last position
                    lastTouchedField = touchedField;
                }

                break;
            }

            default: {
                state = FIELD_STATE_IDLE;
                break;
            }

        } // end: case

        if (rotaryTouchEvent != null) {
            /*
             * switch (rotaryTouchEvent.getRotaryTouchState()) { case FIELD_STATE_IDLE: {
             * debugViewer.add("IDLE"); break; } case FIELD_STATE_SELECTED: {
             * debugViewer.add("SELECTED"); break; } case FIELD_STATE_TOUCHED: {
             * debugViewer.add("TOUCHED"); break; } case FIELD_STATE_TOUCHED_MOVE: {
             * debugViewer.add("TOUCHED_MOVED"); break; } }
             */

            onEvent(this, rotaryTouchEvent);
            state |= STATE_UPDATED;
        }
        return -1;
    }

    /**
     * Processes the touch events.
     * 
     * @param touchType
     * @return
     */
    private int processTouchEvents(int touchType) {
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
                    ThreadExt.sleep(16);
                } catch (InterruptedException e) {
                }
                PWM.setActive(3, false);
            }
            return TouchEvent.TYPE_TOUCH_DRAGGED; // will be ORed on next touch event
        } else if (touchType > 0) {
            state |= STATE_UPDATED;
            onEvent(this, new TouchEvent(TouchEvent.TYPE_TOUCH_RELEASED, pointingDevice.getX(),
                    pointingDevice.getY()));
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#remove(jcontrol.ui.viper.Component)
     */
    public void remove(Component component) {
        if (m_content != null) m_content.remove(component);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IContainer#removeAll()
     */
    public void removeAll() {
        if (m_content != null) m_content.removeAll();
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
        if (m_content != null) m_content.repaint();
        if (m_outline != null) m_outline.repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.IFrame#requestFocus(jcontrol.ui.viper.Component)
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
        if (container != m_content) {
            if (m_content != null) {
                synchronized (m_content) {
                    if (container == null) {
                        m_content.dispose();
                    } else {
                        m_content.parent = null;
                    }
                    m_content = null;
                }
            }
            if (container != null) {
                if (container.font == null && this.font != null) {
                    container.setFont(font);
                }
                container.parent = this;
            }
            m_content = container;
            if (m_outline != null) {
                synchronized (m_outline) {
                    state |= STATE_DIRTY_PAINT_ALL;
                    m_outline.repaint();
                }
            } else {
                state |= STATE_DIRTY_PAINT_ALL;
                if (m_content != null) {
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
        if (m_content != null && (source == null || source != m_content)) {
            m_content.setDirty(null, x, y, width, height, state, b);
            result |= true;
        }
        if (m_outline != null && (source == null || source != m_outline)) {
            if (m_outline instanceof Container) {
                if (((Container) m_outline).setDirty(null, x, y, width, height, state, b)) {
                    result |= true;
                }
            } else {
                if ((x < m_outline.x + m_outline.width) && (y < m_outline.y + m_outline.height)
                        && (x + width > m_outline.x) && (y + height > m_outline.y)) {
                    if (b) {
                        if (m_outline.isVisible()) {
                            m_outline.state |= state;
                            result |= true;
                        }
                    } else {
                        m_outline.state &= ~state;
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
            if (m_outline != null && (m_outline.font == null || m_outline.font == this.font)) {
                m_outline.setFont(font);
            }
            if (m_content != null && (m_content.font == null || m_content.font == this.font)) {
                m_content.setFont(font);
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
        if (component != m_outline) {
            m_outline = null;
            if (component != null) {
                if (component.font == null && this.font != null) {
                    component.setFont(font);
                }
                component.parent = this;
                m_outline = component;
                if (m_outline != null
                        && (m_content == null || (m_content.state & STATE_FOCUS) == 0)) {
                    transferFocus(null, IFocusable.TRANSFER_FOCUS_FORWARD);

                }
            } else {
                m_outline = null;
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
        if (m_content != null) {
            m_content.setVisible(visible);
        }
        if (m_outline != null) {
            m_outline.setVisible(visible);
        }

        if (visible) {
            if ((state & STATE_VISIBLE) != 0) return;
            state |= STATE_VISIBLE | STATE_DIRTY_REPAINT;

            // init display
            if (graphics == null) graphics = new Display();

            // init keyboard
            if ((inputDevices & FLAG_KEYBOARD_CONNECTED) != 0 && keyboard == null) {
                // keyboard = new Keyboard();
            }

            if ((inputDevices & FLAG_ROTARY_TOUCH_CONNECTED) != 0 && rotaryTouchDevice == null) {
                try {
                    rotaryTouchDevice = new MPR083(0x9A);
                } catch (IOException e) {
                }
            }

            // init ponting device
            if ((inputDevices & FLAG_TOUCH_CONNECTED) != 0 && pointingDevice == null) {
                try {
                    pointingDevice = new Touch();
                } catch (IOException e) {
                    inputDevices &= ~FLAG_TOUCH_CONNECTED;
                }
            }

            if ((inputDevices & FLAG_KEYBOARD_CONNECTED) != 0 && (state & STATE_FOCUS) == 0) {
                transferFocus(this, IFocusable.TRANSFER_FOCUS_FORWARD);
            }
            if ((inputDevices & FLAG_ROTARY_TOUCH_CONNECTED) != 0 && (state & STATE_FOCUS) == 0) {
                transferFocus(this, IFocusable.TRANSFER_FOCUS_FORWARD);
            }

            // Working thread
            new Thread() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.lang.Thread#run()
                 */
                @Override
                public void run() {
                    int key = -1;
                    int touchType = 0;
                    int rotaryTouchType = -1;
                    eventQueue = new Queue(2);
                    for (; (state & STATE_VISIBLE) != 0;) {
                        boolean isFinished = false;

                        // process the touch events
                        if ((key == -1 || touchType != 0)
                                && (inputDevices & FLAG_TOUCH_CONNECTED) != 0) {
                            touchType = processTouchEvents(touchType);
                            isFinished |= ((state & STATE_UPDATED) != 0);
                        }

                        // process the rotary touch events
                        if ((key == -1 || rotaryTouchType != -1)
                                && (inputDevices & FLAG_ROTARY_TOUCH_CONNECTED) != 0
                                && rotaryTouchDevice != null) {
                            try {
                                rotaryTouchType = processRotaryTouchEvents(rotaryTouchType);
                            } catch (IOException e) {
                                rotaryTouchType = -1;
                            }
                            isFinished |= ((state & STATE_UPDATED) != 0);
                        }

                        // process the key events
                        if ((key != -1 || touchType == 0)
                                && (inputDevices & FLAG_KEYBOARD_CONNECTED) != 0) {
                            key = processKeyEvents(key);
                            isFinished |= ((state & STATE_UPDATED) != 0);
                        }

                        // update graphics after user input
                        if (isFinished) {
                            update(graphics);
                            isFinished |= ((state & STATE_UPDATED) != 0);
                        }

                        isFinished |= processEventQueue();
                        update(graphics); // always here
                        isFinished |= ((state & STATE_UPDATED) != 0);
                        try {
                            if (!isFinished) {
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
            if (m_outline != null && (m_outline.state & STATE_FOCUS) == STATE_FOCUS) {
                startPosition = 0;
            } else if (m_content != null && (m_content.state & STATE_FOCUS) == STATE_FOCUS) {
                startPosition = 1;
            }
            int position = startPosition;
            int count = 0;
            do {
                switch (position & 1) {
                    case 0:
                        if (m_outline instanceof Container) {
                            found = transferFocus((Container) m_outline, direction);
                        } else if (m_outline instanceof IFocusable) {
                            if (((m_outline).state & STATE_FOCUS) == STATE_FOCUS) {
                                synchronized (m_outline) {
                                    // remove focus from outline
                                    m_outline.state &= ~STATE_FOCUS;
                                    m_outline.repaint();
                                }
                            } else if (m_outline.isVisible()) {
                                m_outline.setDirty(STATE_DIRTY_REPAINT | STATE_FOCUS, true);
                                found = true;
                            }
                        }
                        if (found && m_content != null) m_content.state &= ~STATE_FOCUS;
                        if (startPosition == 0) count++;
                        break;
                    case 1:
                        if (m_content != null) {
                            found = transferFocus(m_content, direction);
                            if (found && m_outline != null) m_outline.state &= ~STATE_FOCUS;
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
        if (m_outline != null) {
            m_outline.update(g);
            if ((m_outline.state & STATE_UPDATED) != 0) {
                state |= STATE_UPDATED;
            }
            m_outline.state &= ~STATE_ABORT_UPDATE;
        }
        synchronized (this) {
            if (m_content != null) {
                m_content.update(g);
                if ((m_content.state & STATE_UPDATED) != 0) {
                    state |= STATE_UPDATED;
                }
                m_content.state &= ~STATE_ABORT_UPDATE;
            }
        }
    }

}
