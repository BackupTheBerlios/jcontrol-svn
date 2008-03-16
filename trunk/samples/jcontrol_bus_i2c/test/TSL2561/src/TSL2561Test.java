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

import jcontrol.bus.i2c.TSL2561;
import jcontrol.bus.i2c.TSL2561LuxConversion;
import jcontrol.comm.DisplayConsole;

/**
 * <p>
 * TSL2561Test shows how to read the TSL2561 light sensor class of the jcontrol.bus.i2c library.
 * </p>
 * 
 * @version $Revision$
 */
public class TSL2561Test {

    public static void main(String[] args) throws IOException {
        // init DisplayConsole
        DisplayConsole console = new DisplayConsole();

        // say hello
        console.println("TSL2561 Test Program.");
        console.println();

        // initialize the TSL2561
        TSL2561 tsl2550 = new TSL2561(0x72);
        // tsl2550.setTimingFast();
        // tsl2550.setGain16x();

        // read data
        for (;;) {
            int ch0 = tsl2550.getChannel0();
            int ch1 = tsl2550.getChannel1();
            console.println("ch0(raw)=".concat(Integer.toHexString(ch0)));
            console.println("ch1(raw)=".concat(Integer.toHexString(ch1)));
            try {
                int lux = TSL2561LuxConversion.calculateLux(false, (short) 2, (short) ch0,
                        (short) ch1, true);
                console.println("lux=".concat(Integer.toHexString(lux)));
            } catch (IllegalArgumentException e) {
                console.println("lux=calc error!");
            }
        }

        // tsl2550.powerOff();
    }

}
