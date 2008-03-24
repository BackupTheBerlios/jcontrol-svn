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

package jcontrol.san.driver.i2c.temperature;

import java.io.IOException;

import jcontrol.comm.I2C;
import jcontrol.san.interfaces.sensors.Temperature;

/**
 * Abstract class for the temperature sensors.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:36 $
 */
public abstract class AbstractTempI2CDriver extends I2C implements Temperature {

    /**
     * The i2c address.
     */
    protected int addr;

    /**
     * The temperature.
     */
    protected int temp = 0;

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public AbstractTempI2CDriver(int address) {
        super(address);
        addr = address;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getExponent()
     */
    public int getExponent() {
        return -1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getUnit()
     */
    public String getUnit() {
        // degree celsius
        return "\u00b0C";
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getValue()
     */
    public int getValue() {
        return temp;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#toString()
     */
    public String toString() {
        // temperature resolution is 1/10 degree celsius
        int whole = temp / 10;
        int parts = temp % 10;

        return Integer.toString(whole).concat(".").concat(Integer.toString(parts))
                .concat(getUnit());
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#updateValue()
     */
    public void updateValue() {
        try {
            temp = getTemp();
        } catch (IOException e) {
            temp = 0;
        }
    }

}
