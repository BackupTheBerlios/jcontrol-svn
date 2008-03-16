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
import jcontrol.bus.i2c.MAX6633;
import jcontrol.bus.i2c.PCF2116C;
import jcontrol.bus.i2c.TSL2550;
import jcontrol.lang.ThreadExt;

public class MAX6633PCF2116TSL2550Test {

    PCF2116C pcf2116c;
    TSL2550 tsl2550;

    public MAX6633PCF2116TSL2550Test() {
        MAX6633 max6633 = new MAX6633(0x80);
        DisplayConsole console = new DisplayConsole();
        console.println("I²C Testprogramm using");
        console.println("MAX6633 + PCF2116C + TSL2550");
        try {
            tsl2550 = new TSL2550(0x72);
            pcf2116c = new PCF2116C(0x74);
        } catch (IOException e) {
        }
        byte cursorPos = 0x00;
        String output = " ";
        try {
            pcf2116c.setPosition((byte) 0, (byte) 1);
            pcf2116c.print("Temperatur");
            pcf2116c.setPosition((byte) 2, (byte) 0);
            pcf2116c.print("Grad Celsius");
        } catch (IOException e) {
        }
        for (;;) {
            try {
                output = "";
                int temp = 0;
                try {
                    temp = max6633.getTemp();
                } catch (IOException e) {
                }
                for (int i = 1000; i > 10; i /= 10) {
                    if ((temp & 0x7FFF) / i == 0) {// Vorzeichenbit ignorieren
                        output = output.concat(" ");
                    }
                }
                output = output.concat(String.valueOf(temp / 10).concat(
                        ".".concat(String.valueOf(temp % 10))));
                pcf2116c.setPosition((byte) 1, (byte) 3);
                pcf2116c.print(output);
                int[] channelCounts = new int[2];
                channelCounts[0] = tsl2550.getCounts(0);
                channelCounts[1] = tsl2550.getCounts(1);
                console.println();
                console.println("Counts at channel 0: ".concat(String.valueOf(channelCounts[0])));
                console.println("Counts at channel 1: ".concat(String.valueOf(channelCounts[1])));
                try {
                    ThreadExt.sleep(1000);
                } catch (InterruptedException e) {
                }
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        new MAX6633PCF2116TSL2550Test();
    }
}
