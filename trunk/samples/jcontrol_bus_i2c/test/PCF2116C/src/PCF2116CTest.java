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
import jcontrol.bus.i2c.PCF2116C;

/**
 * <p>
 * PCF2116CTest shows how to use the PCF2116C lcd driver class of the jcontrol.bus.i2c library.
 * </p>
 * 
 * @version $Revision$
 */

public class PCF2116CTest {

    /** DisplayConsole */
    DisplayConsole console;

    /** Device Reference */
    PCF2116C pcf2116c;

    /**
     * Application Constructor.
     */
    public PCF2116CTest() {

        // init DisplayConsole
        console = new DisplayConsole();

        // display startup message
        console.println("PCF2116C Test Program.");
        console.println();

        try {
            // initiate device connection
            pcf2116c = new PCF2116C(0x74);
        } catch (IOException e) {
            console.println("ERROR: Couldn't connect to device!");
        }

        try {
            // Print message into line 0 at position 2.
            pcf2116c.setPosition((byte) 0, (byte) 2);
            pcf2116c.print("WELCOME");
            // Print message into line 1 at position 1.
            pcf2116c.setPosition((byte) 1, (byte) 5);
            pcf2116c.print("to");
            // Print message into line 2 at position 2.
            pcf2116c.setPosition((byte) 2, (byte) 2);
            pcf2116c.print("JControl");
        } catch (IOException e) {
            console.println("ERROR: Couldn't print messages!");
        }
        console.println("The following Message should now");
        console.println("be seen on a connected I²C Display");
        console.println("(like the 'EAT123'):");
        console.println();
        console.println("                     WELCOME");
        console.println("                            to");
        console.println("                     JControl");

    }

    public static void main(String[] args) {
        new PCF2116CTest();
        for (;;)
            ;// Stops program execution.
    }
}