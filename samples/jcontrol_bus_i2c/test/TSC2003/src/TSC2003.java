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

import jcontrol.lang.Math;
import java.io.IOException;
import jcontrol.io.GPIO;
import jcontrol.comm.I2C;

/**
 * TODO missing
 * 
 * @author Stephan Knoke, Marcus Timmermann
 * @version $Revision$
 */
public class TSC2003 extends I2C {

    public TSC2003(int address) throws IOException {
        super(address);
        setLowPower();
        GPIO.setMode(8, GPIO.PULLUP);
    }

    public void setLowPower() throws IOException {
        write((char) 0x00);
    }

    public int getXPos12() throws IOException {
        byte[] buf = new byte[2];
        read(new byte[]{(byte) 0xC0}, buf, 0, 2);
        return ((buf[0] & 0xff) << 4) + ((buf[1] & 0xF0) >> 4);
    }

    public int getYPos12() throws IOException {
        byte[] buf = new byte[2];
        read(new byte[]{(byte) 0xD0}, buf, 0, 2);
        return ((buf[0] & 0xff) << 4) + ((buf[1] & 0xF0) >> 4);
    }

    public int getXPixel12() throws IOException {
        // obere linke Ecke : x = 359
        // untere rechte Ecke: x = 3669
        return Math.scale(getXPos12() - 359, 3669 - 359, 128);
    }

    public int getYPixel12() throws IOException {
        // obere linke Ecke : y = 243
        // untere rechte Ecke: y = 2650
        return Math.scale(getYPos12() - 243, 2650 - 243, 64);
    }

    public int getXPos8() throws IOException {
        byte[] buf = new byte[1];
        read(new byte[]{(byte) 0xC2}, buf, 0, 1);
        return buf[0] & 0xff;
    }

    public int getYPos8() throws IOException {
        byte[] buf = new byte[1];
        read(new byte[]{(byte) 0xD2}, buf, 0, 1);
        return buf[0] & 0xff;
    }

    public int getXPixel8() throws IOException {
        // obere linke Ecke : x = 25
        // untere rechte Ecke: x = 230
        return Math.scale(getXPos8() - 25, 230 - 25, 128);
    }

    public int getYPixel8() throws IOException {
        // obere linke Ecke : y = 20
        // untere rechte Ecke: y = 169
        return Math.scale(getYPos8() - 20, 169 - 18, 64);
    }

    public boolean isPressed() {
        return (GPIO.getState(8) == false);
    }

}
