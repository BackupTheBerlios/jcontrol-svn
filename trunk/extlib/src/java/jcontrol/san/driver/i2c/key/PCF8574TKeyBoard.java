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

package jcontrol.san.driver.i2c.key;

import jcontrol.san.interfaces.sensors.Sensor;

/**
 * Keyboard (C-Control 1) with PCF8574T (Philips) using the SM (I<sup>2</sup>C) bus.
 * 
 * 
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class PCF8574TKeyBoard extends AbstractKeyI2CDriver implements Sensor {

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public PCF8574TKeyBoard(int address) {
        super(address);
        updateValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getKey()
     */
    public int getKey() {

        // {todo} Auto-generated method stub
        return -1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getKeyString()
     */
    public String getKeyString() {
        // {todo} Auto-generated method stub
        return "";
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMax()
     */
    public int getMax() {
        return 15;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMin()
     */
    public int getMin() {
        return -1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getName()
     */
    public String getName() {
        return "KeyBoard-PCF8574T (0x".concat(Integer.toHexString(addr)).concat(")");
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#waitForKey()
     */
    public int waitForKey() {
        // {todo} Auto-generated method stub
        return -1;
    }

}
