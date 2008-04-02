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

package jcontrol.san.driver.i2c.display;

import jcontrol.comm.I2C;
import jcontrol.san.interfaces.actuators.Actuator;

/**
 * Abstract class for the display actuators.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public abstract class AbstractDisplayI2CDriver extends I2C implements Actuator {

    /**
     * The i2c address.
     */
    protected int addr;

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public AbstractDisplayI2CDriver(int address) {
        super(address);
        addr = address;
    }

}
