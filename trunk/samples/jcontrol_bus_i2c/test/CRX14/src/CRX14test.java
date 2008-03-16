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
 * LM75Test shows how to read the LM75 temperature detector class of the jcontrol.bus.i2c library.
 * </p>
 * 
 * @version $Revision$
 */
public class CRX14test {

    /** DisplayConsole */
    DisplayConsole console;
    int Value;

    /**
     * Application Constructor.
     */
    public CRX14test() {

        int ID1 = -1;
        int ID2 = -1;
        int length = -1;
        int data[] = {-1, -1};
        byte bdata[] = {1, 2};

        // init DisplayConsole
        console = new DisplayConsole();

        // say hello
        console.println("CRX14 Test Program");
        console.println();

        // String t = Integer.toString ((((byte)(1023%4))<<6) & 0xff);
        // console.println(t);

        // construct device object
        try {

            CRX14 crx14 = new CRX14(0xa0); // 99 opening device for write access
            // 98 opening device for read access
            // crx14.powerOff();
            crx14.powerOn();
            console.println("turn CRX14 on");

            console.println("CRX14 Parameter: ".concat(String.valueOf(crx14.getParameter())));
            ID1 = crx14.initiate();
            console.println("initiate CRX14, gefundene ID: ".concat(String.valueOf(ID1)));
            ID2 = crx14.select(ID1);
            console.println("select CRX14, aktivierte ID: ".concat(String.valueOf(ID2)));

            data = crx14.readBlock(0);
            console.println("UID0: ".concat(String.valueOf(data[0])).concat(" ").concat(
                    String.valueOf(data[1])));

            data = crx14.readBlock(1);
            console.println("UID1: ".concat(String.valueOf(data[0])).concat(" ").concat(
                    String.valueOf(data[1])));

            data = crx14.readBlock(2);
            console.println("UID2: ".concat(String.valueOf(data[0])).concat(" ").concat(
                    String.valueOf(data[1])));

            data = crx14.readBlock(3);
            console.println("UID3: ".concat(String.valueOf(data[0])).concat(" ").concat(
                    String.valueOf(data[1])));

            data = crx14.readBlock(12);
            console.println("Block 12: ".concat(String.valueOf(data[0])).concat(" ").concat(
                    String.valueOf(data[1])));

            data = crx14.readBlock(15);
            console.println("Block 15: ".concat(String.valueOf(data[0])).concat(" ").concat(
                    String.valueOf(data[1])));

            bdata[0] = (byte) (data[0] + 3);
            bdata[1] = (byte) data[1];

            // crx14.writeBlock(12, bdata);

            crx14.select(15);
            crx14.powerOff();
            console.println("CRX14 Parameter: ".concat(String.valueOf(crx14.getParameter())));

            for (;;) {
            }

        } catch (IOException e) {
            console.println("No I²C Device found...");
        }

        try {
            ThreadExt.sleep(50);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new CRX14test();
    }
}
