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

/**
 * Java file created by JControl/IDE
 * 
 * @author RSt
 * @version $Revision$
 */
import java.io.IOException;
import jcontrol.comm.DisplayConsole;
import jcontrol.lang.ThreadExt;
import jcontrol.bus.i2c.AD7416;

public class AD7416Test {

    AD7416 ad7416;

    /** DisplayConsole */
    DisplayConsole console;

    /**
     * Application Constructor.
     */
    public AD7416Test() {
        // init DisplayConsole
        console = new DisplayConsole();

        // display startup message
        console.println("AD7416 Test Program.");
        console.println();

        try {
            ad7416 = new AD7416(0x9E);
            ad7416.setOTILimit(30);
            ad7416.setHystLimit(26);
        } catch (IOException e) {
            console.println("Error: Couldn't connect to I2C device!");
        }

        for (;;) {
            int temp = 0;
            try {
                temp = ad7416.getTemp();// get temperature value
            } catch (IOException e) {
                console.println("Error: Couldn't read from I2C device!");
            }
            int whole = temp / 10;
            int parts = temp % 10;
            console.println("Temperature = ".concat(Integer.toString(whole)).concat(".").concat(
                    Integer.toString(parts)).concat("°C"));
            try {
                ThreadExt.sleep(500);// do nothing for 500 msecs
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        new AD7416Test();
    }
}
