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
 * A rotary touch event.
 * 
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class RotaryTouchEvent implements IEvent {

    /** Indicates a right rotation of the sensor field. Value: 1. */
    public static final int ROTATES_RIGHT = 1;

    /** Indicates a no rotation of the sensor field. Value: 0. */
    public static final int ROTATES_NONE = 0;

    /** Indicates a left rotation of the sensor field. Value: -1. */
    public static final int ROTATES_LEFT = -1;

    /** Indicates the idle state of the sensor field. Value: 0. */
    public static final int STATE_IDLE = 0;

    /** Indicates the touched state of the sensor field. Value: 2. */
    public static final int STATE_TOUCHED = 2;

    /** Indicates the touched & moved state of the sensor field. Value: 3. */
    public static final int STATE_TOUCHED_MOVE = 3;

    /**
     * Indicates the selected state of the sensor field. Value: 4. Which means STATE_TOUCHED -->
     * Untouched --> Selected.
     */
    public static final int STATE_SELECTED = 4;

    /** The default touch resolution each touched field position is multiplied by this value. */
    public static final int TOUCH_RESOLUTION = 32;

    /** The touched sensor field. */
    private int m_touchedSensorField = -1;

    /** The rotary state. */
    private int m_rotaryState = STATE_IDLE;

    /** The rotate direction. */
    private int m_rotaryDirection = ROTATES_NONE;

    /**
     * Creates a rotary touch event.
     * 
     * @param touchedSensorField the touched sensor field.
     * @param rotaryState the rotary sensor state
     * @param rotateDirection the rotate direction
     * @see STATE_IDLE
     * @see STATE_TOUCHED
     * @see STATE_TOUCHED_MOVE
     * @see STATE_SELECTED
     * @see ROTATES_RIGHT
     * @see ROTATES_NONE
     * @see ROTATES_LEFT
     */
    public RotaryTouchEvent(int touchedSensorField, int rotaryState, int rotaryDirection) {
        m_touchedSensorField = touchedSensorField;
        m_rotaryState = rotaryState;
        m_rotaryDirection = rotaryDirection;
    }

    /**
     * Returns the rotate direction of the sensor field.
     * 
     * @return the rotate direction.
     * @see ROTATES_RIGHT
     * @see ROTATES_NONE
     * @see ROTATES_LEFT
     */
    public int getRotaryDirection() {
        return m_rotaryDirection;
    }

    /**
     * Returns the rotary touch state.
     * 
     * @return the touch state.
     * @see STATE_IDLE
     * @see STATE_TOUCHED
     * @see STATE_TOUCHED_MOVE
     * @see STATE_SELECTED
     */
    public int getRotaryTouchState() {
        return m_rotaryState;
    }

    /**
     * Returns the touched sensor field.
     * 
     * @return the touched sensor field or -1 if no field is touched
     */
    public int getTouchedSensorField() {
        return m_touchedSensorField;
    }
}
