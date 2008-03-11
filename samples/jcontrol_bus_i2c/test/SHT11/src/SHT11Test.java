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
import jcontrol.bus.i2c.SHT11;
import jcontrol.comm.DisplayConsole;

/**
 * <p>
 * LM75Test shows how to read the LM75 temperature detector class of the jcontrol.bus.i2c library.
 * </p>
 * 
 * @version $Revision$
 */

public class SHT11Test {

    /** DisplayConsole */
    DisplayConsole console;

    public SHT11Test() {

        // init DisplayConsole
        console = new DisplayConsole();

        // say hello
        console.println("SHT11 Test Program.");
        console.println();

        for (;;) {
            // construct device object
            SHT11 sht11 = new SHT11();
            sht11.reset();
            console.println(String.valueOf(sht11.read(0x03)));
            for (;;) {
            }
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new SHT11Test();
    }
}
