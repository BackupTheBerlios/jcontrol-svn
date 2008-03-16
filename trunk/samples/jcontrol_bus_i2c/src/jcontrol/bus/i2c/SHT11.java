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

import jcontrol.io.GPIO;
import jcontrol.lang.ThreadExt;

/**
 * Accesses the SHT11 temperature sensor using the SM (I<sup>2</sup>C) bus. Refer to the LM75 data
 * sheet for details.
 * 
 * @author telkamp
 * @version $Revision$
 * @jcontrol.devices lib
 * @jcontrol.aml available="true" icon="chip.gif"
 */
public class SHT11 {

    public static final int SCK = 2;
    public static final int DATA = 3;

    public SHT11() {}

    public void reset() {
        GPIO.setMode(SCK, GPIO.PUSHPULL);
        GPIO.setMode(DATA, GPIO.PULLUP);
        for (int i = 1; i < 12; i++) {
            GPIO.setState(SCK, true);
            GPIO.setState(SCK, false);
        }
    }

    public int read(int command) {
        /* transmossion start */
        GPIO.setState(SCK, true);
        GPIO.setState(DATA, false);
        GPIO.setState(SCK, false);
        GPIO.setState(SCK, true);
        GPIO.setState(DATA, true);
        GPIO.setState(SCK, false);

        // send command (8 bits)
        for (int i = 7; i > 0; i--) {
            GPIO.setState(DATA, (command & (1 << i)) != 0);
            GPIO.setState(SCK, true);
            GPIO.setState(SCK, false);
        }
        GPIO.setState(DATA, true);
        int ack = GPIO.getState(DATA) ? 1 : 0;
        try {
            ThreadExt.sleep(1);
        } catch (InterruptedException ie) {
        }

        return ack;
    }

    /**
     * Reads a temperature from the LM75. The temperature range is -55 to 127 degrees (°C) and the
     * accuracy is 0.5 degrees.
     * 
     * @return int temperature value multiplied by 10 (in °C)
     * @throws IOException on communication error
     * @jcontrol.aml applicable="true"
     */
    public int getTemp() throws IOException {
        // allocate buffer for temperature
        byte[] buf = new byte[2];
        // read content of 16-bit register #0x00
        // return integer value
        return (buf[0] * 10) + (((buf[1] & 0x80) * 5) >> 7);
    }

}
