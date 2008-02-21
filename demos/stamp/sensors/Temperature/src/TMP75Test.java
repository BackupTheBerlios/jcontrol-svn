/*
 * TMP75Test.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import java.io.IOException;
import jcontrol.comm.RS232;
import jcontrol.lang.ThreadExt;
import jcontrol.bus.i2c.TMP75;

/**
 * <p>
 * TMP75Test shows how to read the TMP75 temperature detector class of the jcontrol.bus.i2c library.
 * </p>
 */
public class TMP75Test {

    /** DisplayConsole */
    RS232 console;

    /**
     * Application Constructor.
     */
    public TMP75Test() {
        // init DisplayConsole
        try {
          console = new RS232();
      } catch (IOException exc) {
      }
        // display startup message
        console.println("TMP75 Test Program.");
        console.println();
        // connect to device
        TMP75 tmp75;
        tmp75 = new TMP75(0x9E);
        for (;;) {
            int temp = 0;
            for (;;) {
                try {
                    temp = tmp75.getTemp();//get temperature value
                } catch (IOException e) {
                    console.println("Error: Couldn't read from I2C-Object!");
                }
                int whole = temp / 10;
                int parts = temp % 10;//one tenth Centigrade
                console.println("Temperature  = ".concat(
                  Integer.toString(whole)).concat(".").concat(
                  Integer.toString(parts)).concat(" C"));

                try {
                    ThreadExt.sleep(500);//do nothing for 500 msecs
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new TMP75Test();// start measuring
    }
}