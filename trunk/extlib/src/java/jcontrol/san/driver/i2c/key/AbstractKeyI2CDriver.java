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

import jcontrol.comm.I2C;
import jcontrol.san.interfaces.sensors.KeyBoard;

/**
 * Abstract class for the key sensors.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:36 $
 */
public abstract class AbstractKeyI2CDriver extends I2C implements KeyBoard {

    /**
     * The i2c address.
     */
    protected int addr;

    /**
     * The last pressed key.
     */
    protected int key = -1;

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public AbstractKeyI2CDriver(int address) {
        super(address);
        addr = address;
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
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getLastKey()
     */
    public int getLastKey() {
        return key;
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

}
