/*
 * SetupStandbyTime.java
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
import jcontrol.system.RTC;
import jcontrol.system.Time;

/**
 * This class is used to change the time that may pass until JControl switches to standby mode.
 * The maximum time is one hour.
 * 
 * @author Marcus Timmermann
 * @version 1.0
 * @see jcontrol.system.setup.SystemSetup
 */
public class SetupStandbyTime {

    private static Display lcd   = null;
    public static Keyboard keys = null;


    /** The main method. */
    public static void main(String[] args) {
        lcd    = new Display();                 // init display
        keys = new Keyboard();
        try {
            go(lcd,keys);        
        } catch (java.io.IOException e) {
        }
        
    }

    /**
     * This method is run from the main menu.
     * 
     * @param d      the global Display object.
     * @param k      the global Keyboard object.
     */
    public static void go(Display d, Keyboard k) throws java.io.IOException {
        lcd = d;
        keys = k;
        int time;
        {
            String delay=Management.getProperty("system.standbytimer");
            time = (delay!=null)?Integer.parseInt(delay):-1;
        }
        int oldtime = time;
        int selected = (time>=0)?0:1;
        lcd.clearRect(1, 20, 126, 43);
        lcd.drawString("Standby/Alarm", 5, 21);
        lcd.setDrawMode(Display.XOR);
        lcd.fillRect(1, 20, 126, 9);
        int l = lcd.drawString("Standby ", 5, 34);        
        if (time!=0) {
            lcd.drawString("after", 5+l, 34);
            lcd.drawString("seconds", 50+l, 34);
        }
        int w = lcd.getTextWidth(getTimeString(time));
        lcd.drawString(getTimeString(time), l+46-w, 34);
        int m = lcd.drawString("Alarm at", 5, 50);
        switch(selected) {
            case 0: lcd.fillRect(25+l, 32, 23,10); break;
            case 1: lcd.fillRect(7+m, 49, 12,8);                            
        }
        Time wakeup = new Time();
        int mode = RTC.getAlarm(wakeup);        
        boolean onoff = (mode>0);
        if (wakeup.hour<0 || wakeup.hour>23) wakeup.hour = 0;
        if (wakeup.minute<0 || wakeup.minute>59) wakeup.minute = 0;
        int oldhour = wakeup.hour;
        int oldminute = wakeup.minute;
        lcd.drawString(String.valueOf(oldhour/10),  8+m, 50);
        lcd.drawString(String.valueOf(oldhour%10), 13+m, 50);
        lcd.drawString(":", 19+m, 50);
        lcd.drawString(String.valueOf(oldminute/10), 22+m, 50);
        lcd.drawString(String.valueOf(oldminute%10), 27+m, 50);
        lcd.drawString(onoff?" Enabled  ":" Disabled ", 50+m, 50);
        char c=0;
        do {
            c=keys.read();
            switch (selected) {
                case 0:  // standby time
                    switch (c) {
                        case 'U': if(time==0) time=10; else time+=5-(time%5); break;
                        case 'R':
                        case 'u': if(time==0) time=10; else time++;  break;
                        case 'D': time-=((time%5==0)?5:0)+(time%5); break;
                        case 'L':
                        case 'd': time--;  break;
                        case 'S': case 'M':
                            selected++;
                            lcd.fillRect(26+l, 33, 21,8);
                            lcd.fillRect(7+m, 49, 12,8);                            
                            break;
                    }
                    if (time<10) time=0;
                    if (time>300) time=300;
                    if (time != oldtime) {
                        lcd.drawString(getTimeString(oldtime), l+46-w, 34);
                        if (oldtime==0 || time==0) {
                            lcd.drawString("after", 5+l, 34);
                            lcd.drawString("seconds", 50+l, 34);
                        }
                        w = lcd.getTextWidth(getTimeString(time));
                        lcd.drawString(getTimeString(time), l+46-w, 34);
                        oldtime = time;
                    }
                    break;
                case 1:   // hours
                    switch (c) {
                        case 'U':                            
                        case 'u': 
                        case 'R':
                            wakeup.hour++;
                            wakeup.hour%=24; 
                            break;
                        case 'D': 
                        case 'd':
                        case 'L': 
                            wakeup.hour+=23;
                            wakeup.hour%=24; 
                            break;
                        case 'S':
                        case 'M': 
                            selected++;
                            lcd.fillRect(7+m, 49, 12,8);
                            lcd.fillRect(21+m, 49, 12,8);
                            break;
                    }                   
                    if (wakeup.hour!=oldhour) {
                        lcd.drawString(String.valueOf(oldhour/10),  8+m, 50);
                        lcd.drawString(String.valueOf(oldhour%10), 13+m, 50);
                        oldhour = wakeup.hour;
                        lcd.drawString(String.valueOf(oldhour/10),  8+m, 50);
                        lcd.drawString(String.valueOf(oldhour%10), 13+m, 50);
                    }
                    break;
                case 2:   // minutes
                    switch (c) {
                        case 'R':
                        case 'U': 
                        case 'u': wakeup.minute++; break;
                        case 'L':
                        case 'D': 
                        case 'd': wakeup.minute+=59;break;
                        case 'S': 
                        case 'M':
                            selected++;
                            lcd.fillRect(21+m, 49, 12,8);
                            lcd.setDrawMode(Display.INVERSE);
                            lcd.drawString(onoff?" Enabled  ":" Disabled ", 50+m, 50);
                            break;
                    }
                    wakeup.minute%=60;
                    if (wakeup.minute!=oldminute) {
                        lcd.drawString(String.valueOf(oldminute/10), 22+m, 50);
                        lcd.drawString(String.valueOf(oldminute%10), 27+m, 50);
                        oldminute = wakeup.minute;
                        lcd.drawString(String.valueOf(oldminute/10), 22+m, 50);
                        lcd.drawString(String.valueOf(oldminute%10), 27+m, 50);
                    }
                    break;
                case 3:   // wakeup on, off
                    switch (c) {
                        case 'R':
                        case 'L':
                        case 'U': 
                        case 'u': 
                        case 'D': 
                        case 'd': onoff^=true; break;
                        case 'S':
                        case 'M': 
                            selected++;
                            break;
                    }
                    lcd.drawString(onoff?" Enabled  ":" Disabled ", 50+m, 50);
                    break;
            }

        } while (selected<4);
        wakeup.second=0;                // not used -> to default value
        if (onoff) {
            RTC.setAlarm(wakeup, RTC.DAILY);
        } else {
            RTC.setAlarm(wakeup, RTC.OFF);        
        }
        if(time>=0) {
            Management.setProperty("system.standbytimer", String.valueOf(time));
            Management.powerOff(time);
        }
        Management.saveProperties();
        lcd.setDrawMode(Display.NORMAL);

    }

    /**
     * Composes a special string from seconds.
     * @param time
     * @return String
     */
    private static String getTimeString(int time) {
        if (time==0) return "off";
        if (time==-1) return "no";
        return String.valueOf(time);
    }
}