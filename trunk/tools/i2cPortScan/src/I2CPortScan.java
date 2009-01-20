/*
 * Copyright (C) 2009 The JControl Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */

import jcontrol.comm.DisplayConsole;
import jcontrol.lang.ThreadExt;

/**
 * List all I2C addresses.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class I2CPortScan {

    /**
     * main()
     */
    public static void main(String[] args) {

        DisplayConsole console = new DisplayConsole();

        console.println("I²C scanning at port ...");
        console.println("===========================");

        for (int i = 0; i < 255; i += 2) {
            try {

                I2CTestDevice test = new I2CTestDevice(i);

                if (test.exists()) {
                    console.print(test.getHexPort());
                    console.print("      ");
                    console.print(test.getBinPort());
                    console.print("      ");
                    console.print(String.valueOf(i));
                    console.println();
                    ThreadExt.sleep(500);
                }

            } catch (InterruptedException e) {
                // ignore
            }
        }
        while (true) {
        }
    }
}
