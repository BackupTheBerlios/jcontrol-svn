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
import jcontrol.lang.ThreadExt;
import jcontrol.comm.DisplayConsole;
import jcontrol.comm.I2C;

/**
 * TODO missing
 * 
 * @version $Revision$
 */
public class I2CPortScan extends I2C {

    public I2CPortScan(int address) {
        super(address);
    }

    public static String int2hex(int i) {
        switch (i) {
            case 0:
                return "0";
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "a";
            case 11:
                return "b";
            case 12:
                return "c";
            case 13:
                return "d";
            case 14:
                return "e";
            case 15:
                return "f";
        }
        return "-1";
    }

    public static String int2bin(int i) {
        switch (i) {
            case 0:
                return "0000";
            case 1:
                return "0001";
            case 2:
                return "0010";
            case 3:
                return "0011";
            case 4:
                return "0100";
            case 5:
                return "0101";
            case 6:
                return "0110";
            case 7:
                return "0111";
            case 8:
                return "1000";
            case 9:
                return "1001";
            case 10:
                return "1010";
            case 11:
                return "1011";
            case 12:
                return "1100";
            case 13:
                return "1101";
            case 14:
                return "1110";
            case 15:
                return "1111";
        }
        return "-1";
    }

    public static String nullen(int i) {
        if (i < 10) return "00";
        if (i < 100) return "0";
        return "";
    }

    public static void main(String[] args) {

        int i;

        DisplayConsole console = new DisplayConsole();

        console.println("I²C Portscan:");
        console.println("============");
        console.println();
        console.println("Antwort von Port ...");

        for (i = 0; i < 255; i += 2) { // LM75 bei 144 bei 0 0
            try {
                I2CTestDevice test = new I2CTestDevice(i);
                test.test();
                console.println("      0x".concat(int2hex(i >> 4)).concat(int2hex(i & 0x0f))
                        .concat("      ").concat(int2bin(i >> 4)).concat(" ").concat(
                                int2bin(i & 0x0f)).concat("      ").concat(nullen(i)).concat(
                                Integer.toString(i)).concat("    +1"));
                ThreadExt.sleep(1000);
            } catch (InterruptedException e) {
            } catch (IOException e) {
            }
        }
        console.println();
        console.println("Scan mit Port ".concat(Integer.toString(i - 1)).concat(" abgeschlossen"));
        for (;;) {
        }
    }

}
