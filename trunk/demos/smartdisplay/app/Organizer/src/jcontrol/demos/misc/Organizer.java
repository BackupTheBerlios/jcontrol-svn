/*
 * Organizer.java
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

package jcontrol.demos.misc;

import java.io.IOException;

import jcontrol.demos.uielements.Clock;
import jcontrol.demos.uielements.MenuSelector;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Management;
import jcontrol.system.RTC;
import jcontrol.system.Time;

/** 
 * Presents a down scaled personal information manager.
 * 
 * @version 1.0
 * @author  Marcus Timmermann, Helge Boehme
 */
public class Organizer {

    private static Display lcd;
    private static Keyboard key;

    /**
     * Program entry point. Displays the clock an main menu.
     * @param args (not used)
     */
    public static void main(String[] args) {
	    // lights on!
   		jcontrol.io.Backlight.setBrightness(jcontrol.io.Backlight.MAX_BRIGHTNESS);

        lcd = new Display();
        key = new Keyboard();
        lcd.setFont(null);       
        {                                                       
            String d=Management.getProperty("system.standbytimer");
            if (d!=null) Management.powerOff(Integer.parseInt(d)); // get power off delay
        }       
        int select = 0;
        Clock clk;
        for(;;) { // main loop
            lcd.clearDisplay();
            if (RTC.isAlarm()) {
                Time wakeup = new Time();
                RTC.getAlarm(wakeup);
                RTC.resetAlarm();
                Date alarmDate=Date.getNextDate(wakeup);
                if (alarmDate!=null) {
                    lcd.drawString(time2string(alarmDate.time), 0, 45);
                    lcd.drawString("$".equals(alarmDate.text)?"no subject":alarmDate.text, 0, 56);
                    updateAlarm(alarmDate.time);
                }
                clk=new Clock(45,0,40,40,lcd, true);              // show the alarm clock
            } else {
                clk=new Clock(35,2,60,80,lcd, false);             // show the normal clock
            }
            while(clk.isRunning()){
                try {
                  ThreadExt.sleep(500);
                } catch (InterruptedException e) {
                  clk.stop();
                }
                if(key.getKey()!=0) clk.stop();
            }
            if(RTC.isAlarm()) continue;         // begin from above...
       key: for (;;) {    // the menu loop
                try {
                    lcd.drawImage(new Resource("organizer.jcif"),0,0); // draw the background image
                } catch (java.io.IOException e) {}
                lcd.setFont(null);
                select =  MenuSelector.listSelect(new String[] {     // let the user select one of the specified menu items
                                         "display time",
                                         "new appointment",
                                         "view appointments",
                                         "turn off"}, select, lcd, key);
                Date date = null;
                switch (select) {
                    case -1:
                    case 0:    // show the current time
                        select=0;
                        break key;
                    case 2:    // adjust appointment
                        date = listDates();  // show all stored dates and return date to change
                        if(date==null) break;
                    case 1:    // new appointment
                        date = DateInput.getDate(lcd,key, date);  // get a new date (date==null) or modify an existing (date!=null)
                        if (date!=null) {   // if the new date is valid
                            try {
                                date.store();
                            } catch (IOException e) {}
                            date = null;    // this object may be garbage collected now
                            updateAlarm(null);
                        }
                        break;
                    case 3:    // standby
                        lcd.clearDisplay(); 
                        while (key.getKey()!=0) {
                        }
                        jcontrol.system.Management.powerOff(-1);                   
                        break;               
                }
            }
        }
    }

    /**
     * Converts a {@link Time Time}-Object to a user readable sting.
     * @param time point of time to convert
     * @return String ASCII representation (german date format)
     */
    private static String time2string(Time time) {
        return new String(new byte[]{  // compose the current date
                                      (byte)((time.day/10)+'0'),
                                      (byte)((time.day%10)+'0'),
                                      (byte)'.',
                                      (byte)((time.month/10)+'0'),
                                      (byte)((time.month%10)+'0'),
                                      (byte)'.',
                                      (byte)((time.year/1000)+'0'),
                                      (byte)(((time.year/100)%10)+'0'),
                                      (byte)(((time.year/10)%10)+'0'),
                                      (byte)((time.year%10)+'0'),
                                      (byte)' ',
                                      (byte)((time.hour/10)+'0'),
                                      (byte)((time.hour%10)+'0'),
                                      (byte)':',
                                      (byte)((time.minute/10)+'0'),
                                      (byte)((time.minute%10)+'0')});
    }

    /**
     * This method will list all date entries found in persistent storage. The
     * user may select one of the entries and either change the selected entry
     * or delete it or even quit without any change.
     * 
     * @return Date object chosen or <code>null</code>.
     */
    private static Date listDates() {
        Date result=null;
        Date[] list = new Date[4];   // the entry list (the size will be changed if necessary)
                                         // two items for every tlv entry (date and subject message)
        {   
            int index = 0;
            Date current=Date.getNextDate(new Time(2000,1,1,0,0,0,0)); // start searching here
            while (current!=null) {
                if (index>=list.length) {       // if list array is too small
                    Date[] newlist = new Date[list.length+4];
                    for (int c=0; c<list.length; c++) newlist[c] = list[c]; // enlarge the array
                    list = newlist;               
                }
                list[index++] = current;
                                                      // the subject message
                Time next=current.time;
                current=Date.getNextDate(new Time(next.year,next.month,next.day,next.dow,next.hour,next.minute+1,0));
            }
            if (index<=0) {  // if there are no entries, return immediately
                return null;
            } else {
                if (index<list.length) {       // trim to size
                    Date[] newlist = new Date[index];
                    for (int c=0; c<index; c++) newlist[c] = list[c];
                    list = newlist;
                }
            }

        }   // all entries were found and set into the list
        {   // now, create a choose dialog and wait for user input
            lcd.clearRect(1, 20, 126, 43);  // clear the main rectangle area
            {
                String[] image = new String[] {"\u040E\u1F0E\u0400"};  // the up and down arrow image
                // create menu items
                lcd.drawImage(image, 22, 57, 5, 3, 0, 0);   // up
                lcd.drawImage(image, 7, 57, 5, 3, 0, 2);    // down
                lcd.drawString("modify", 35, 55);           // change
                lcd.drawString("delete", 65, 55);          // delete
                lcd.drawString("back", 100, 55);          // quit

            }
            lcd.setDrawMode(Display.XOR);
            char c = 0;
            int shift = 0;
            int select = 0;
            for (int i=0; i<4; i++) {      // draw four entries on the screen (there isn't enough space for more than four :-))
                if (i+shift<list.length) {
                    int pos=lcd.drawString(time2string(list[i+shift].time), 9, 21+(i<<3))+14;
                    if(!"$".equals(list[i+shift].text))
                        lcd.drawString(list[i+shift].text,pos,21+(i<<3),116-pos,10,0,0);
                }
            }
            lcd.fillRect(6, 20, 120, 8); // draw a rectangle around the first entry as it is selected
                                          // select one of the menu items
            lcd.fillRect(select==0?2:select==1?17:select==2?30:select==3?60:95, 55, select<2?15:select==3?36:32, 7);
            mainloop:for (;;) {
                c = key.read(); // wait for keypress
                switch (c) {
                    case 'R':  // select the next menu item
                    case 'U':
                    case 'u':
                        lcd.fillRect(select==0?2:select==1?17:select==2?30:select==3?60:95, 55, select<2?15:select==3?36:32, 7);
                        select++;
                        select%=5;
                        lcd.fillRect(select==0?2:select==1?17:select==2?30:select==3?60:95, 55, select<2?15:select==3?36:32, 7);
                        break;
                    case 'L':  // select the previous menu item
                    case 'D':
                    case 'd':
                        lcd.fillRect(select==0?2:select==1?17:select==2?30:select==3?60:95, 55, select<2?15:select==3?36:32, 7);
                        select+=4;
                        select%=5;
                        lcd.fillRect(select==0?2:select==1?17:select==2?30:select==3?60:95, 55, select<2?15:select==3?36:32, 7);
                        break;
                    case 'S':  // button was pressed -> do something
                        switch (select) {
                            case 0:  // down
                            case 1:  // up
                                // scroll around
                                for (int i=0; i<4; i++) {
                                    if (i+shift<list.length) {
                                        int pos=lcd.drawString(time2string(list[i+shift].time), 9, 21+(i<<3))+14;
                                        if(!"$".equals(list[i+shift].text))
                                            lcd.drawString(list[i+shift].text,pos,21+(i<<3),116-pos,10,0,0);
                                    }
                                }
                                if (select==1) shift=(shift<=0?0:shift-1);
                                if (select==0) shift=(shift>=list.length-1?list.length-1:shift+1);
                                for (int i=0; i<4; i++) {
                                    if (i+shift<list.length) {
                                        int pos=lcd.drawString(time2string(list[i+shift].time), 9, 21+(i<<3))+14;
                                        if(!"$".equals(list[i+shift].text))
                                            lcd.drawString(list[i+shift].text,pos,21+(i<<3),116-pos,10,0,0);
                                    }
                                }
                                break;
                            case 2: // change
                                result=list[shift];
                            case 3: // delete
                                try {
                                    list[shift].remove();
                                    updateAlarm(null);
                                } catch (IOException e) {}
                            case 4:
                                break mainloop; // quit without change
                        }
                        break;
                }
            }
            lcd.setDrawMode(Display.NORMAL);             
            return result;
        }
    }

    /**
     * Updates the alarm settings of the RTC.
     * @param now point of time the next alarm will be searched from, if <code>null</code>
     *        the current time will be used
     */
    public static void updateAlarm(Time now) {
        if(now==null)
            now=new Time();
        else
            now=new Time(now.year,now.month,now.day,now.dow,now.hour,now.minute+1,0);
        Date wakeup=Date.getNextDate(now);
        if (wakeup!=null) {
            wakeup.time.second=0;       // not used -> to default value
            RTC.setAlarm(wakeup.time, RTC.DATED);
        } else {
            RTC.setAlarm(null, RTC.OFF);
        }
    }

}