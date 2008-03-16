/*
 * Typewriter.java
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

package jcontrol.demos.uielements;

import jcontrol.io.*;

/**
 * Virtual keyboard using one small QWERTY area fitting on the screen.
 *
 * @version 1.0
 * @author  Marcus Timmermann
 */
public class Typewriter {

    private static Display lcd;

    /**
     * Displays the virtual keyboard and returns the edited text.
     * @param word text to edit or empty for a new text
     * @param d the Display to display the keyboard
     * @param k the (physical) keyboard to read user commands
     * @return String the edited text
     */
    public static String getText(String word, Display lcd, Keyboard key) {
        Typewriter.lcd = lcd;
/*
         char[] keys = new char[] {
             '1','2','3','4','5','6','7','8','9','0','�','\"',
             'Q','W','E','R','T','Z','U','I','O','P','�','+',
             'A','S','D','F','G','H','J','K','L','�','�','#',
             'Y','X','C','V','B','N','M',',','.','-','~',' ','*'};
 */
        {
            lcd.setFont(Display.SYSTEMFONT);
            lcd.drawRect(0,0,128,12);
            char[] keys = new char[] {'1','2','3','4','5','6','7','8','9','0','-','+'};
            for (int c=0; c<12; c++) {
                lcd.drawChar(keys[c], 5+10*c, 15);
                lcd.drawLine(2+10*c, 14, 2+10*c, 23);
            }
            lcd.drawLine(122, 14, 122, 23);
            lcd.drawLine(2, 13, 122, 13);
//            keys = new char[] {'Q','W','E','R','T','Z','U','I','O','P','�','+'};
            keys = new char[] {'Q','W','E','R','T','Y','U','I','O','P','{','}'};
            for (int c=0; c<12; c++) {
                lcd.drawChar(keys[c], 8+10*c, 25);
                lcd.drawLine(6+10*c, 24, 6+10*c, 33);
            }
            lcd.drawLine(126, 24, 126, 33);
            lcd.drawLine(2, 23, 126, 23);
            keys = new char[] {'A','S','D','F','G','H','J','K','L',':','"','|'};
            for (int c=0; c<12; c++) {
                lcd.drawChar(keys[c], 11+10*c, 35);
                lcd.drawLine(8+10*c, 34, 8+10*c, 43);
            }
            lcd.drawLine(127, 34, 127, 43);
            lcd.drawLine(6, 33, 127, 33);
            keys = new char[] {'Z','X','C','V','B','N','M','<','>','?'};
            for (int c=0; c<10; c++) {
                lcd.drawChar(keys[c], 14+10*c, 45);
                lcd.drawLine(12+10*c, 44, 12+10*c, 53);
            }
        }
        lcd.drawLine(124, 44, 124, 53);
        lcd.drawLine(111, 44, 111, 53);
        lcd.drawLine(8, 43, 127, 43);
        lcd.drawLine(12, 53, 124, 53);
        lcd.drawString("del", 113, 45);
        lcd.drawString("SPACE", 55, 55);
        lcd.drawRect(45, 53, 45, 10);
        lcd.drawString("ok", 118, 56);
        boolean ok = false;
        int currKey = 24;
        if (word==null) {
            word = "";
        } else lcd.drawString(word, 5,2);
        int size = 0;
        //highlight(currKey);
        lcd.setDrawMode(Display.XOR);
        lcd.fillRect(9, 34, 9, 9);
        lcd.setDrawMode(Display.NORMAL);
        while (!ok) {
            switch (key.read()) {
                case 'U':
                    highlight(currKey);
                    currKey+=37;
                    currKey%=49;
                    highlight(currKey);
                    break;
                case 'u':
                case 'L':
                    highlight(currKey);
                    currKey+=48;
                    currKey%=49;
                    highlight(currKey);
                    break;
                case 'D':
                    highlight(currKey);
                    currKey+=12;
                    currKey%=49;
                    highlight(currKey);
                    break;
                case 'd':
                case 'R':
                    highlight(currKey);
                    currKey++;
                    currKey%=49;
                    highlight(currKey);
                    break;
                case 'S':                    
                    if ((currKey<=45 || currKey==47) && size<115) {
                        byte k = (byte)(new char[] {
                        					'1','2','3','4','5','6','7','8','9','0','-','+',
                        					'Q','W','E','R','T','Y','U','I','O','P','{','}',
                        					'A','S','D','F','G','H','J','K','L',':','"','|',
                        					'Z','X','C','V','B','N','M','<','>','?'})[currKey];
                        word = word.concat(new String(new byte[]{k}));
                        lcd.drawString(word, 5,2);
                    }
                    if (currKey==46) {
                        if (word.length()>1) {
                            lcd.setDrawMode(Display.XOR);
                            lcd.drawString(word, 5,2);
                            word = word.substring(0, word.length()-1);
                            lcd.setDrawMode(Display.NORMAL);
                            lcd.drawString(word, 5,2);
                        } else if (word.length()==1) {
                            lcd.setDrawMode(Display.XOR);
                            lcd.drawString(word, 5,2);
                            word = "";
                            lcd.setDrawMode(Display.NORMAL);
                        }
                    }
                    size = lcd.getTextWidth(word);
                    if (currKey==48) {
                        ok = true;
                    }
                    break;
            }

        }
        return word;
    }

    private static void highlight(int c) {
        lcd.setDrawMode(Display.XOR);
        if (c>=0 && c<12) {
            lcd.fillRect(3+10*c, 14, 9, 9);
        } else if (c>=12 && c<24) {
            lcd.fillRect(7+10*(c-12), 24, 9, 9);
        } else if (c>=24 && c<=35) {
            lcd.fillRect(9+10*(c-24), 34, c==35?8:9, 9);
        } else if (c>=36 && c<46) {
            lcd.fillRect(13+10*(c-36), 44, 9, 9);
        } else if (c==46) {
            lcd.fillRect(112, 44, 12, 9);
        } else if (c==47) {
            lcd.fillRect(46, 54, 43, 8);
        } else if (c==48) {
            lcd.fillRect(116, 55, 11, 8);
        }
        lcd.setDrawMode(Display.NORMAL);
    }

}
