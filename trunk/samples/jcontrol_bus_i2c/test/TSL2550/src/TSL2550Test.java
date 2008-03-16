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
import jcontrol.bus.i2c.TSL2550;

/**
 * <p>
 * TSL2550Test shows how to read the TSL2550Test ambient light sensor class of the jcontrol.bus.i2c
 * library.
 * </p>
 * 
 * @version $Revision$
 */

public class TSL2550Test {

    /** DisplayConsole */
    DisplayConsole console;

    // von -0,50 -0,55 -0,60 ... -2,00
    // Index über folgende Formel: -(Exponent+0,5)/0,05
    // Ergebnis / 10000
    public int[] expfunc = new int[]{6065, 5769, 5488, 5220, 4966, 4724, 4493, 4274, 4066, 3867,
            3679, 3499, 3329, 3166, 3012, 2865, 2725, 2592, 2466, 2346, 2231, 2122, 2019, 1920,
            1827, 1738, 1653, 1572, 1496, 1423, 1353};

    /**
     * Application Constructor.
     */
    public TSL2550Test() {

        // init DisplayConsole
        console = new DisplayConsole();

        // display startup message
        console.println("TSL2550 Test Program.");
        console.println();

        for (;;) {
            try {
                // construct device object
                TSL2550 tsl2550 = new TSL2550(0x72);

                // array that will obtain the results
                int[] channelCounts = new int[2];
                int lux = 0;

                for (;;) {
                    channelCounts[0] = tsl2550.getCounts(0);
                    channelCounts[1] = tsl2550.getCounts(1);
                    RS232_Debug.print("Channel 0: ");
                    RS232_Debug.println(channelCounts[0]);
                    RS232_Debug.print("Channel 1: ");
                    RS232_Debug.println(channelCounts[1]);
                    if (channelCounts[0] != 4015 & channelCounts[1] != 4015) {
                        // console.println("channel 0 (ir & visible):
                        // ".concat(String.valueOf(channelCounts[0])));
                        // console.println("channel 1 (ir only ___):
                        // ".concat(String.valueOf(channelCounts[1])));
                        lux = (313 * (channelCounts[1] / 10)) / (channelCounts[0] / 10);
                        RS232_Debug.print("Exponent: ");
                        RS232_Debug.println(lux);
                        lux = (lux - 50) / 5;
                        RS232_Debug.print("Index: ");
                        RS232_Debug.println(lux);
                        if (lux < 0 | lux > 30) {
                            lux = -1;
                        } else {
                            lux = expfunc[lux]; // : 10000
                        }
                        RS232_Debug.print("Ergebnis: ");
                        RS232_Debug.println(lux);
                        lux = ((((lux / 50) * 23) / 100) * (channelCounts[0] / 100));
                        RS232_Debug.print("Lux: ");
                        RS232_Debug.println(lux);
                        if (lux > 0) {
                            console.println("light: ".concat(String.valueOf(lux)).concat(" lux"));
                        }
                    } else {
                        console.println("-- zu hell für den Sensor --");
                    }
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
        new TSL2550Test();
    }
}
