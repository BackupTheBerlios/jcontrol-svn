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
import jcontrol.bus.i2c.MAX6633;

/**
 * <p>
 * MAX6633Test shows how to read the MAX6633 temperature detector class of the jcontrol.bus.i2c
 * library.
 * </p>
 * 
 * @version $Revision$
 */
public class MAX6633Test {

    /** DisplayConsole */
    DisplayConsole console;

    /**
     * Application Constructor.
     */
    public MAX6633Test() {

        // init DisplayConsole
        console = new DisplayConsole();

        // display startup message
        console.println("MAX6633 Test Program.");
        console.println();

        // connect to device
        MAX6633 max6633 = new MAX6633(0x80);

        for (;;) {
            int temp = 0;
            for (;;) {
                try {
                    temp = max6633.getTemp();// get temperature value
                } catch (IOException e) {
                    console.println("Error: Couldn't read from I2C-Object!");
                }
                int whole = temp / 10;
                int parts = temp % 10;// one tenth Centigrade
                console.println("Temperature  = ".concat(Integer.toString(whole)).concat(".")
                        .concat(Integer.toString(parts)).concat("°C"));
                try {
                    ThreadExt.sleep(500);// do nothing for 500 msecs
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new MAX6633Test();// start measuring
    }
}
