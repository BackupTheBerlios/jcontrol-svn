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
import java.io.IOException;

import jcontrol.bus.i2c.LM80;
import jcontrol.comm.DisplayConsole;
import jcontrol.io.Backlight;
import jcontrol.lang.ThreadExt;

/**
 * <p>
 * LM80Test shows how to read the LM80 temperature detector class of the jcontrol.bus.i2c library.
 * </p>
 * 
 * @version $Revision$
 */
public class LM80Test {

    /** DisplayConsole */
    DisplayConsole console;

    /**
     * Application Constructor.
     */
    public LM80Test() {
        Backlight.setBrightness(Backlight.MAX_BRIGHTNESS);

        // init DisplayConsole
        console = new DisplayConsole();

        // say hello
        console.println("LM80 Test Program.");
        console.println();

        for (;;) {
            try {
                // construct device object
                LM80 lm80 = new LM80(0x50, true);
                int temp;

                for (;;) {
                    lm80.updateValue();
                    temp = lm80.getValue();
                    console.println("temp: ".concat(String.valueOf(temp)));

                    try {
                        ThreadExt.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (IOException e) {
                console.println("IOException");
            }
            try {
                ThreadExt.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new LM80Test();
    }
}
