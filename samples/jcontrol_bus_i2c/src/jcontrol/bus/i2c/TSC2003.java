/*
 * Copyright (C) 2004-2008 DOMOLOGIC Home Automation GmbH This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jcontrol.bus.i2c;

import java.io.IOException;

import jcontrol.comm.I2C;

/**
 * TODO missing
 * 
 * @author xxx
 * @version $Revision$
 */
public class TSC2003 extends I2C {

    public TSC2003(int address) {
        super(address);
    }

    public void setHighspeed() throws IOException {
        write((char) 0x08);
    }

    public int getXPos() throws IOException {
        byte[] buf = new byte[2];
        read(new byte[]{(byte) 0xC0}, buf, 0, 2);
        return ((buf[0] & 0xff) * 16) + ((buf[1] & 0xF0) >> 4);
    }

    public int getYPos() throws IOException {
        byte[] buf = new byte[2];
        read(new byte[]{(byte) 0xD0}, buf, 0, 2);
        return ((buf[0] & 0xff) * 16) + ((buf[1] & 0xF0) >> 4);
    }

}
