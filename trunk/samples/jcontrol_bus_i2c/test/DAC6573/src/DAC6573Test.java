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

/**
 * <p>
 * The DAC6573Test shows how to set the DA Converter DAC6573 from Texas Instruments, using the class
 * jcontrol.comm.i2c.
 * </p>
 * 
 * @version $Revision$
 */
public class DAC6573Test {

    /** DisplayConsole */
    DisplayConsole console;

    int value;

    /**
     * Application Constructor.
     */
    public DAC6573Test() {
        int channel = 4;

        // init DisplayConsole
        console = new DisplayConsole();

        // say hello
        console.println("DAC6573 Test Program");
        console.println();

        for (;;) {
            try {
                // construct device object
                DAC6573 dac6573 = new DAC6573(0x98); // 99 opening device for write access
                // 98 opening device for read access
                for (;;) {
                    for (int i = 0; i < 1024; i = i + 10) {
                        value = i;

                        dac6573.setOutput1024(channel, value);
                        console.println("Setting Channel  ".concat(String.valueOf(channel)).concat(
                                " zu ").concat(String.valueOf(value)));

                        try {
                            ThreadExt.sleep(50);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            } catch (IOException e) {
                console.println("No I²C Device found...");
            }
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new DAC6573Test();
    }
}
