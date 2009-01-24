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
 * A semantic event which indicates that a component-defined action occured. This event is generated
 * by a component (such as a Button) when the component-specific action occurs (such as being
 * pressed). The event is pushed on the global event queue. The application frame continously works
 * the event queue by passing any ActionEvent on to the event's destination ActionListener.
 * </p>
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.event.ActionListener
 * @see jcontrol.ui.viper.event.ActionProducer
 * @since Viper 1.0
 * @version $Revision$
 */
public class ActionEvent implements IEvent {

    /** A button has been released. Value: 0. */
    public final static int BUTTON_RELEASED = 0;

    /** A button has been pressed. Value: 1. */
    public final static int BUTTON_PRESSED = 1;

    /** A menu item has been selected. Value: 2. */
    public final static int MENU_ACTION = 2;

    /** A component's entry (in a list or whatever) has been selected. Value: 3. */
    public final static int ITEM_SELECTED = 3;

    /** A component's value has changed (e.g. in a slider). Value: 4. */
    public final static int VALUE_CHANGED = 4;

    /** An component's state has changed (e.g. a check box). Value: 5. */
    public final static int STATE_CHANGED = 5;

    /** The object that created this action event. */
    public ActionProducer source;

    /** The command String (optional) */
    public String command;

    /** The type of this action event. */
    public int type;

    /**
     * Constructs an action event.
     * 
     * @param source the source component that generated the event.
     * @param type the event type, such as BUTTON_PRESSED.
     */
    public ActionEvent(ActionProducer source, int type) {
        this.type = type;
        this.source = source;
    }

    /**
     * Constructs an action event.
     * 
     * @param source the source ActionProducer that generated the event.
     * @param type the event type, such as BUTTON_PRESSED.
     * @param command a String representing the command of this event
     */
    public ActionEvent(ActionProducer source, int type, String command) {
        this.type = type;
        this.source = source;
        this.command = command;
    }

}