/*
 * Copyright (C) 2008 The JControl Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package jcontrol.san.driver.gpio;

import jcontrol.io.Keyboard;
import jcontrol.san.interfaces.sensors.KeyBoard;
import jcontrol.san.interfaces.sensors.Sensor;

/**
 * Evb-Keyboard on the GPIO.
 * 
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class EvbKeyBoard implements KeyBoard, Sensor {

    /**
     * The last pressed key.
     */
    private int key = -1;

    /**
     * The Evb-Keyboard.
     */
    private Keyboard keyboard;

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public EvbKeyBoard() {
        keyboard = new Keyboard();
        updateValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getExponent()
     */
    public int getExponent() {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getKey()
     */
    public int getKey() {
        key = keyboard.getRaw();
        return key;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getKeyString()
     */
    public String getKeyString() {

        if (key == 85) {
            return "U";
        } else if (key == 76) {
            return "L";
        } else if (key == 82) {
            return "R";
        } else if (key == 68) {
            return "D";
        } else if (key == 83) { return "S"; }
        return "";
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getLastKey()
     */
    public int getLastKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMax()
     */
    public int getMax() {
        return 255;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMin()
     */
    public int getMin() {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getName()
     */
    public String getName() {
        return "Evb-KeyBoard";
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getUnit()
     */
    public String getUnit() {
        return "key";
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getValue()
     */
    public int getValue() {
        return key;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#toString()
     */
    public String toString() {
        return getKeyString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#updateValue()
     */
    public void updateValue() {
        key = getKey();

    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#waitForKey()
     */
    public int waitForKey() {
        int key = 0;
        while (key == 0) {
            key = getKey();
        }
        return key;
    }
}
