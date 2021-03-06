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
package jcontrol.ui.viper.event;

/**
 * <p>
 * This event may be generated by the application's top level Frame object when the screen has been
 * touched. It is sent down the component tree to any TouchListener component, for example a button.
 * </p>
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.event.ITouchListener
 * @since Viper 1.0
 * @version $Revision$
 */
public class TouchEvent implements IEvent {

    /** Event type for touch released. Value: 0. */
    public static final int TYPE_TOUCH_RELEASED = 0;

    /** Event type for touch pressed. */
    public static final int TYPE_TOUCH_PRESSED = 1 << 0;

    /** Event type for touch dragged. */
    public static final int TYPE_TOUCH_DRAGGED = 1 << 1 | TYPE_TOUCH_PRESSED;

    /** The x position of the last touch. */
    public int x;

    /** The y position of the last touch. */
    public int y;

    /** The type of the last touch. */
    public int type;

    /**
     * Creates a new touch event.
     * 
     * @param type the type
     * @param x the x position
     * @param y the y position
     */
    public TouchEvent(int type, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

}
