/*
 * SetupBatt.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

package jcontrol.system.setup;

import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.lang.ThreadExt;

/**
 * This class indicates the battery status.
 * 
 * @author Marcus Timmermann
 * @version 1.0
 * @see jcontrol.system.setup.SystemSetup
 */
public class SetupBatt {

    // the battery image
    // width = 21
    // height = 30
    public static final String[] BATTERY_IMG = new String[] {
        "\uF8F4\uE2E2\uE2C1\uC1C1\uCEDD\u5DDD\uCEC1\uC1C1\uE2E2\uE2F4\uF800",
        "\uFFFF\uFFFF\uFFFF\uFFFF\uFDFD\uF0FD\uFDFF\uFFFF\uFFFF\uFFFF\uFF00",
        "\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\uFF00",
        "\u070F\u1F1F\u1F3F\u3F3F\u3B3B\u3B3B\u3B3F\u3F3F\u1F1F\u1F0F\u0700"};

    // the fill-level image
    // width = 4
    // height = 23
    public static final String[] BATTFILL_IMG = new String[] { "\u0102\u0202\u0404\u0404\u0404\u0404\u0404\u0402\u0202\u0100"};

    public static final String[] TEXT = new String[] {
        "Time/Date",
        ":",
        ".",
        "Credits",
        "System Info",
        "File-Transfer",
        "Bus Address",
        "Battery status",
        "Energy level at",
        "Status",
        "Ok",
        "LOW"};

    private static Display lcd  = null;      // init display
    public static Keyboard keys = null;      // init keyboard


    /** The main method. */
    public static void main(String[] args) {
        lcd    = new Display();                 // init display
        keys = new Keyboard();
        go(lcd,keys);
    }


    /**
     * This method is run from the main menu.
     * 
     * @param d      the global Display object
     * @param k      the global Keyboard object
     */
    public static void go(Display d, Keyboard k) {
        lcd = d;
        keys = k;
        lcd.setFont(Display.SYSTEMFONT);
        lcd.clearRect(1, 20, 126, 43);
        int status, oldstatus=100;
        lcd.drawString(TEXT[7], 5, 21);
        lcd.drawImage(BATTERY_IMG, 15, 31, 21, 30,0,0);
        lcd.drawString(TEXT[8], 45, 37); // energy level at
        lcd.drawString(TEXT[9], 45, 50); // status
        lcd.drawString("%", 116, 37);
        lcd.setDrawMode(Display.XOR);
        lcd.fillRect(1, 20, 126, 9);
        lcd.drawString(String.valueOf(oldstatus), 101, 37);        
        lcd.drawString(TEXT[10], 75, 49); // Ok        
        status=0;
        int loopcount = 0;
        try {
            do {
                status = (jcontrol.system.Management.getPowerStatus()-121)*105/69;
                if (status>100) status=100;
                if (status<0) status=0;
                if (status!=oldstatus && loopcount==0) {
                    lcd.drawString(String.valueOf(oldstatus), 101+(oldstatus<100?3:0), 37);
                    lcd.drawString(String.valueOf(status), 101+(status<100?3:0), 37);               
                    if (status<=30 && oldstatus>30) {
                        lcd.drawString(TEXT[10], 75, 49); // Ok
                        lcd.drawString(TEXT[11], 75, 49); // LOW
                    }
                    if (status>30 && oldstatus<=30) {
                        lcd.drawString(TEXT[11], 75, 49); // LOW
                        lcd.drawString(TEXT[10], 75, 49); // Ok
                    }
                    if (status>oldstatus) {
                        for (int c=oldstatus*22/100; c<status*22/100; c++) {
                            lcd.drawImage(BATTFILL_IMG, 16, 57-c, 19, 3,0,0);
                        }
                    } else if (status<oldstatus) {
                        for (int c=status*22/100; c<oldstatus*22/100; c++) {
                            lcd.drawImage(BATTFILL_IMG, 16, 57-c, 19, 3,0,0);
                        }
                    }
                    oldstatus = status;
                }
                ThreadExt.sleep(100);
                loopcount++;
                loopcount%=20;
            } while (keys.getRaw()==0);
        } catch (InterruptedException e) {}

        lcd.setDrawMode(Display.NORMAL);
    }
}
