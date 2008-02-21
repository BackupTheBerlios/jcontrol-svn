/*
 * SetupSound.java
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
import jcontrol.system.Management;

/**
 * This class is used to setup the sound.<BR>
 * It is possible to activate/deactivate music (iMelody), keyboard-beeps and system-sounds.
 *
 * @author Marcus Timmermann
 * @version 1.0
 * @see jcontrol.system.setup.SystemSetup
 */
public class SetupSound {


    public static Display  lcd  = null;
    public static Keyboard keys = null;

    // switch image (on)
    public static String[] on_image = new String[] {
        "\uE010\uE0F0\u28F0\u2800\uFCFE\uFFFF\u0202\u0204\u04FC\u0404\u0404\u0404\u0404\uFC00\uE010\uE0F0\u10E0",
        "\u0001\u0001\u0001\u0000\u0303\u0303\u0303\u0302\u0203\u0202\u0202\u0202\u0202\u0300\u0001\u0001\u0001"};
    // switch image (off)
    public static String[] off_image = new String[] {
        "\uE010\uE0F0\u28F0\u2800\uFC04\u0404\u0404\u0404\u04FC\u0404\u0202\u02FF\uFFFE\uFC00\uE010\uE0F0\u10E0",
        "\u0001\u0001\u0001\u0000\u0302\u0202\u0202\u0202\u0203\u0202\u0303\u0303\u0303\u0300\u0001\u0001\u0001"};



    /** The main method. */
    public static void main(String[] args) {
        lcd  = new Display();                   // init display
        keys = new Keyboard();
        go(lcd,keys);
    }

    /**
     * This method is run from the main menu.
     *
     * @param d      the global Display object.
     * @param k      the global Keyboard object.
     */
    public static void go(Display d, Keyboard k) {
        lcd = d;
        keys = k;
        int choices = 4;
        int select = 0, oldselect = 0, xoffset = 1;
        boolean settings[] = new boolean[]{Management.getProperty("buzzer.enable").equals("true"),
                                           Management.getProperty("buzzer.keyboardbeep").equals("true"),
                                           Management.getProperty("buzzer.systembeep").equals("true")};
        lcd.clearRect(1, 20, 126, 43);
        lcd.setDrawMode(Display.XOR);
        lcd.drawString("Sound", 5, 21);
        lcd.fillRect(1, 20, 126, 9);
        lcd.setFont(Display.SYSTEMFONT);
        lcd.drawImage(new String[] {
        	          "\u40A0\u1008\u041E\u1F18\u1818\uF8F8",
                          "\u0000\u0102\u040F\u0701\u0101\u0100"},
                      114, 50, 12,12,0,0);
        drawSelection(xoffset,0);
        // iMelody image
        lcd.drawImage(new String[] {
                          "\u0080\u8080\u4020\u1008\u04C2\uB907\u0106\u38C0",
                          "\u1F20\u607F\u68D0\uA850\uA87F\uBD1F\u0E00\u807F",
                          "\u0000\u0000\u0000\u0103\u060D\u1B1C\u100C\u0300"},
                      xoffset+6,30,16,21,0,0);
        if (settings[0]) {
            // notes
            lcd.drawImage(new String[] {
                              "\uC462\u0100\u0000\u8000",
                              "\u0700\uC07C\u0231\u1F00",
                              "\u0408\u1000\u0000\u0000"},
                          xoffset+23, 30,7,21,0,0);
        } else {
            lcd.drawLine(xoffset+6, 31, xoffset+22, 50);
            lcd.drawLine(xoffset+22, 31, xoffset+6, 50);
        }
        // Key-beep image
        lcd.drawImage(new String[] {
                          "\u7880\u0000\uE010\u1020\uC102\u0D16\uDA64\uA850\uA040\u8000\u0000",
                          "\u001F\u2438\u1F00\u0000\u80C3\uA498\uA0DB\uEDB6\u5831\u1160\u8000",
                          "\u0000\u0000\u0000\u1816\u1110\u1010\u1010\u1011\u1608\u0402\u0100"},
                      xoffset+40,30,21,21,0,0);
        if (settings[1]) {
            // note
            lcd.drawImage(new String[] {"\u0000\uF008\u84C2\uFF00",
                                        "\u0C0E\u0700\u0101\u0000"},
                          xoffset+61,30, 7,12,0,0);
        }

        // system sound image
        lcd.drawImage(new String[] {
                          "\uFC02\u01F9\uF1F2\uE2E6\uC6CA\u8A92\u1222\u24C4\u9E41\uEF28\u1F09\uE7DE\u811E\uE000",
                          "\uF788\u5053\uA3A7\u474F\u8F9F\u1F3F\u0080\u807F\u0B84\uFF0C\uF8B0\u6FF6\u03F0\u0F00",
                          "\u0000\u0101\u0202\u0404\u0808\u111F\u1108\u0804\u0F10\u1E02\u1E10\u0F00\u0100\u0000"},
                      xoffset+77,30, 27, 21,0,0);
        if (settings[2]) {
            // notes
            lcd.drawImage(new String[] {"\u04C2\u2110\uF800",
                                        "\u4C87\u0006\u0300",
                                        "\u0000\u0100\u0000"},
                          xoffset+105, 30, 5,17,0,0);
        }

        for (int i = 0; i<choices-1; i++) {
            lcd.drawImage(settings[i]?on_image:off_image, xoffset+i*37, 52, 34, 10,0,0);
        }
        char c=0;
        do {

            switch (c) {
                case 'L':
                case 'U':
                case 'u':
                    select+=choices-1;
                    select%=choices;
                    break;
                case 'R':
                case 'D':
                case 'd':
                    select++;
                    select%=choices;
                    break;
                case 'S':
                case 'M':
                    switch (select) {
                        // iMelody on/off
                        case 0:
                            lcd.drawImage(new String[] {
                                              "\uC462\u0100\u0000\u8000",
                                              "\u0700\uC07C\u0231\u1F00",
                                              "\u0408\u1000\u0000\u0000"},
                                          xoffset+23, 30,7,21,0,0);
                            lcd.drawLine(xoffset+6, 31, xoffset+22, 50);
                            lcd.drawLine(xoffset+22, 31, xoffset+6, 50);
                            Management.setProperty("buzzer.enable",settings[0]?"false":"true");
                            break;
                            // Keyboard beep on/off
                        case 1:
                            lcd.drawImage(new String[] {"\u0000\uF008\u84C2\uFF00",
                                                        "\u0C0E\u0700\u0101\u0000"},
                                          xoffset+61,30, 7,12,0,0);
                            Management.setProperty("buzzer.keyboardbeep",settings[1]?"false":"true");
                            break;
                            // system sounds on/off
                        case 2:
                            lcd.drawImage(new String[] {"\u04C2\u2110\uF800",
                                                        "\u4C87\u0006\u0300",
                                                        "\u0000\u0100\u0000"},
                                          xoffset+105, 30, 5,17,0,0);
                            Management.setProperty("buzzer.systembeep",settings[2]?"false":"true");
                            break;
                    }

                    settings[select]^=true;
                    lcd.setDrawMode(Display.NORMAL);
                    drawSelection(xoffset,select);
                    lcd.setDrawMode(Display.XOR);
                    lcd.drawImage(settings[select]?on_image:off_image, xoffset+select*37, 52, 34, 10,0,0);
                    break;
            }
            if (select != oldselect) {
                drawSelection(xoffset,oldselect);
                drawSelection(xoffset,select);
                oldselect = select;
            }
            c=keys.read();
        } while ((c!='S' && c!='M') || select!=choices-1);
        Management.saveProperties();
        lcd.setDrawMode(Display.NORMAL);

    }

    /**
     * draws a black rectangle around the selected item.
     *
     * @param xoffset xoffset from left border
     * @param select  selected item
     */
    private static void drawSelection(int xoffset, int select) {
        if (select == 3) {
            lcd.fillRect(114, 50, 12,13);
        } else {
            lcd.fillRect(xoffset+select*37, 52, 34,11);
        }

    }

}
