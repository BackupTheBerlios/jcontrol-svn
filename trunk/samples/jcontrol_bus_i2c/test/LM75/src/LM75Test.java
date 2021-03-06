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

import jcontrol.comm.DisplayConsole;
import jcontrol.lang.ThreadExt;
import jcontrol.bus.i2c.LM75;

/**
 * <p>
 * LM75Test shows how to read the LM75 temperature detector class of the jcontrol.bus.i2c library.
 * </p>
 * 
 * @version $Revision$
 */

public class LM75Test {

    /** DisplayConsole */
    DisplayConsole console;

    /**
     * Application Constructor.
     */
    public LM75Test() {

        // init DisplayConsole
        console = new DisplayConsole();

        // say hello
        console.println("LM75 Test Program.");
        console.println();

        for (;;) {
            try {
                // construct device object
                LM75 lm75 = new LM75(0x9e);
                int temp;

                for (;;) {

                    temp = lm75.getTemp();
                    console.println("temp: ".concat(Integer.toString(temp / 10)).concat(".")
                            .concat(Integer.toString(temp % 10)).concat("�C"));

                    try {
                        ThreadExt.sleep(500);
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
        new LM75Test();
    }
}
