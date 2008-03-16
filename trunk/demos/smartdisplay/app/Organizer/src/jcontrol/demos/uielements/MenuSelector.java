/*
 * MenuSelector.java
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
import jcontrol.lang.ThreadExt;

/**
 * Draws a simple text menu chooser on the display and returns the user chosen value.
 * 
 * @version 1.0
 * @author  Marcus Timmermann
 */
public class MenuSelector {

    /**
     * Displays the text menu, reads the keyboard and returns the chosen item number.
     * @param list   the menu item text list
     * @param select the preselected item
     * @param lcd    the display to draw the menu on
     * @param keys   the keyboard to query
     * @return int   the chosen value
     */
    public static int listSelect(String[] list, int select, Display lcd, Keyboard keys) {
        lcd.clearRect(1, 20, 126, 43);
        int oldshift = 0;
        int shift = select>4?select-4:0;
        int oldselect = -1;
        for (int c=shift; c<(shift+5<list.length?shift+5:list.length); c++) {
            lcd.drawString(list[c], 9, 21+8*(c-shift));
        }
        if (shift>0) lcd.drawImage(new String[] {"\u040E\u1F0E\u0400"}, 2, 21, 5, 3, 0, 0);
        if (shift<list.length-5) lcd.drawImage(new String[] {"\u040E\u1F0E\u0400"}, 2, 57, 5, 3, 0, 2);
        lcd.setDrawMode(Display.XOR);            
        char key = 0;
        int count=100;
        do {
            if (shift!=oldshift) {
                if (oldshift==0 || shift==0) {
                    lcd.drawImage(new String[] {"\u040E\u1F0E\u0400"}, 2, 21, 5, 3, 0, 0);
                }
                if (oldshift==list.length-5 || shift==list.length-5) {
                    lcd.drawImage(new String[] {"\u040E\u1F0E\u0400"}, 2, 57, 5, 3, 0, 2);
                }
                for (int c=oldshift; c<(oldshift+5<list.length?oldshift+5:list.length); c++) {     // draw old game name list
                    lcd.drawString(list[c], 9, 21+8*(c-oldshift));
                }
                oldselect = select;
                oldshift = shift;               
                for (int c=shift; c<(shift+5<list.length?shift+5:list.length); c++) {     // draw new game name list
                    lcd.drawString(list[c], 9, 21+8*(c-shift));
                } 

            }
            if (select!=oldselect) {
                if (oldselect>=0) lcd.fillRect(8,21+8*(oldselect-shift), 118, 8);     // old select bar
                lcd.fillRect(8,21+8*(select-shift), 118, 8);        // new select bar
                oldselect = select;
            }
            key = keys.read();
            switch (key) {
                case 'U':
                case 'u':
                    if (select>0) select--;
                    count=100;
                    break;
                case 'D':
                case 'd':
                    if (select<list.length-1) select++;
                    count=100;
                    break;
                default:
                    try {
                        ThreadExt.sleep(100);
                    } catch (InterruptedException e) {}
                    count--;
                    if(count==0) {
                        select=-1;
                        key='S';                              // abort after 10s
                    }
            }
            if (select<shift) {
                shift=select;
            } else if (select>shift+4) {
                shift=select-4;
            }

        } while (key!='S');
        lcd.setDrawMode(Display.NORMAL);
        return select;
    }
}
