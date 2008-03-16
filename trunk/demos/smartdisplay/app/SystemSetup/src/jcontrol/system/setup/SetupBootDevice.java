/*
 * SetupBootDevice.java
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
import jcontrol.io.Flash;
import jcontrol.io.Keyboard;
import jcontrol.system.Management;

/**
 * This class is used to change the time that may pass until JControl switches to standby mode.
 * The maximum time is one hour.
 * 
 * @author Marcus Timmermann
 * @version 1.0
 * @see jcontrol.system.setup.SystemSetup
 */
public class SetupBootDevice {

    private static Display lcd   = null;
    public static Keyboard keys = null;

    /**
     * Program entry point.
     * @param args (not used)
     */
    public static void main(String[] args) {
        lcd    = new Display();                 // init display
        keys = new Keyboard();
        try {
            go(lcd,keys);        
        } catch (java.io.IOException e) {}
    }

    /**
     * This method is run from the main menu.
     * 
     * @param d      the global Display object.
     * @param k      the global Keyboard object.
     */
    public static int go(Display d, Keyboard k) throws java.io.IOException {
        lcd = d;
        keys = k;
        lcd.clearRect(1, 20, 126, 43);
        lcd.drawString("Boot-Bank", 5, 21);
        lcd.setDrawMode(Display.XOR);
        lcd.fillRect(1, 20, 126, 9);
        lcd.setDrawMode(Display.NORMAL);
        int mod;
        {
            String info=Management.getProperty("flash.format");
            mod=Integer.parseInt(info.substring(info.length()-1,info.length()));
        }
        String banks=Management.getProperty("system.userbank");
        if(banks==null || mod<2) {
            lcd.drawString("sorry, no flash-banking supported",4,36);
            keys.read();
            return 0;
        }
        int bank = Integer.parseInt(banks);
        int l = lcd.drawString("Set flash bank to boot from:", 10, 36);
        char c=0;
        byte[] sl=new byte[256];
        do {
            lcd.drawString(String.valueOf(bank), 15+l, 36);
            lcd.clearRect(1, 46, 126, 8);
            {
                Flash f=new Flash(bank);
                f.setMode(Flash.CODE); // get access to program memory
                f.read(sl,0,256,0);
 
                // checking old JControl Shared Library format (JCSL)
                if(sl[0]=='S' && sl[1]=='L'){
                    int ptr=(sl[2]<<8)+(sl[3]&255);
                    if(ptr!=0 && ptr!=-1){
                        ptr=(sl[6]<<8)+(sl[7]&255);
                        if(ptr!=0) lcd.drawString(new String(sl,ptr+2,sl[ptr+1]), 30, 46);
                    }
                }
 
                // checking new JControl Shared Library format (JCSL)
                if(sl[0]=='J' && sl[1]=='C' && sl[2]=='S' && sl[3]=='L'){
                    int ptr=(sl[14]<<8)+(sl[15]&255);
                    if(ptr!=0 && ptr!=-1){
                        ptr=(sl[18]<<8)+(sl[19]&255);
                        if(ptr!=0) lcd.drawString(new String(sl,ptr+2,sl[ptr+1]), 30, 46);
                    }
                }
            }
            c=keys.read();
            switch (c) {
                case 'R':
                case 'U': 
                case 'u': bank++;  break;
                case 'L':
                case 'D': 
                case 'd': bank+=mod-1; break;                
            } 
            bank%=mod;
        } while ((c!='S') && (c!='M'));
        Management.setProperty("system.userbank",String.valueOf(bank));
        Management.saveProperties(); // make selection permanent
        while(keys.getRaw()!=0) ;   // wait for key release
        Management.switchBank();    // switch to selected bank or just continue if 0
        return bank;
    }

}