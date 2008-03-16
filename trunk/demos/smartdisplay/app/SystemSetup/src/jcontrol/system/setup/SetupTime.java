/*
 * SetupTime.java
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
import jcontrol.system.RTC;
import jcontrol.system.Time;
import jcontrol.ui.vole.meter.AnalogClock;


/**
 * This class is used to set date and time on the JControl Sticker.
 * 
 * @author Marcus Timmermann
 * @version 1.0
 * @see jcontrol.system.setup.SystemSetup
 */
public class SetupTime {

    private static Display    lcd   = null;
    private static Keyboard keys = null;

    /** The main method. */
    public static void main(String[] args) {
        lcd    = new Display();
        keys = new Keyboard();
        go(lcd,keys);
    }


    /**
     * This method is rfun from the main  menu.
     * 
     * @param d      the global Display object.
     * @param k      the global Keyboard object.
     */
    public static void go(Display d, Keyboard k) {
        lcd = d;
        keys = k;
        lcd.clearRect(1, 20, 126, 43);
        lcd.setFont(Display.SYSTEMFONT);
        lcd.drawString("Time/Date", 5, 21);
        lcd.setDrawMode(Display.XOR);
        lcd.fillRect(1, 20, 74, 9);
        lcd.setDrawMode(Display.NORMAL);
        lcd.drawLine(75, 20, 75, 63);
        lcd.drawLine(1, 46, 74, 46);
        int hours, minutes, seconds, day, month, year;
        {
            Time time = new Time();
            hours = time.hour;
            minutes = time.minute;
            seconds = time.second;
            day = time.day;
            month = time.month;
            year = time.year;
        }
        if (year>2099) {
            year = 2099;
        }
        if (year<2000) {
            year = 2000;
        }
        int diffhours=0, diffminutes=0, diffseconds=0, diffday=0, diffmonth=0, diffyear=0;
        int oldhours=-1, oldminutes=-1, oldseconds=-1, oldday=-1, oldmonth=-1, oldyear=-1;
        int select = 0, oldselect = -1;
        int blockheight = lcd.getFontHeight()+2;
        boolean changed = true;
        lcd.drawString(":",23, 33); // colon between hours and minutes
        lcd.drawString(":",39, 33); // colon between minutes and seconds
        lcd.drawString(".",23, 51); // dot between day and month
        lcd.drawString(".",39, 51); // dot between month and year        
        boolean pressed = false;
        int notPressSecs = 0;
        AnalogClock clock = new AnalogClock(81, 22, 19, true);
        clock.setGraphics(lcd);
        mainloop:for (;;) {
            lcd.setDrawMode(Display.XOR);
            if (!pressed) {
                Time time = new Time();
                hours = time.hour;
                minutes = time.minute;
                if (time.second!=seconds) {
                    seconds = time.second;
                    notPressSecs++;
                    if (notPressSecs==10) {
                        lcd.fillRect(10, 33, 12,blockheight);

                    }
                }
                day = time.day;
                month = time.month;
                year = time.year; 
                if (year>2099) {
                    year = 2099;
                }
                if (year<2000) {
                    year = 2000;
                }
            }
            if (select!=oldselect) {
                if (oldselect!=-1)
                    lcd.fillRect(oldselect<3?(10+(oldselect%3)*16):42-((oldselect%3)*16), (oldselect<3?33:50), (oldselect!=3?12:23),blockheight);
                oldselect = select;
                lcd.fillRect(oldselect<3?(10+(oldselect%3)*16):42-((oldselect%3)*16), (oldselect<3?33:50), (oldselect!=3?12:23),blockheight);
            }
            if ((hours+diffhours)%24!=oldhours) {
                if (oldhours!=-1) {
                    lcd.drawString(String.valueOf(oldhours/10),10, 34);
                    lcd.drawString(String.valueOf(oldhours%10),16, 34);
                }
                changed = true;
                oldhours = (hours+diffhours)%24;
                lcd.drawString(String.valueOf(oldhours/10),10, 34);
                lcd.drawString(String.valueOf(oldhours%10),16, 34);
            }
            if ((minutes+diffminutes)%60!=oldminutes) {
                if (oldminutes!=-1) {
                    lcd.drawString(String.valueOf(oldminutes/10),26, 34);
                    lcd.drawString(String.valueOf(oldminutes%10),32, 34);
                }
                changed = true;
                oldminutes = (minutes+diffminutes)%60;
                lcd.drawString(String.valueOf(oldminutes/10),26, 34);
                lcd.drawString(String.valueOf(oldminutes%10),32, 34);
            }
            if ((seconds+diffseconds)%60!=oldseconds) {
                if (oldseconds!=-1) {
                    lcd.drawString(String.valueOf(oldseconds/10),42, 34);
                    lcd.drawString(String.valueOf(oldseconds%10),48, 34);
                }
                changed = true;
                oldseconds = (seconds+diffseconds)%60;
                lcd.drawString(String.valueOf(oldseconds/10),42, 34);
                lcd.drawString(String.valueOf(oldseconds%10),48, 34);
            }
            if (year+diffyear!=oldyear) {
                if (oldyear!=-1) lcd.drawString(String.valueOf(oldyear), 42, 51);
                lcd.drawString(String.valueOf(year+diffyear), 42, 51);
                oldyear = year+diffyear;
            }
            if (((month-1+diffmonth)%12)+1!=oldmonth) {
                if (oldmonth!=-1) {
                    lcd.drawString(String.valueOf(oldmonth/10), 26, 51);
                    lcd.drawString(String.valueOf(oldmonth%10), 32, 51);
                }
                oldmonth = ((month-1+diffmonth)%12)+1;
                lcd.drawString(String.valueOf(oldmonth/10), 26, 51);
                lcd.drawString(String.valueOf(oldmonth%10), 32, 51);
            }
            int dcount = (getDays(oldmonth, oldyear));
            //int newday = ((day-1+diffday)%dcount)+1;
            if (day+diffday!=oldday || oldday>dcount) {
                if (oldday!=-1) {
                    lcd.drawString(String.valueOf(oldday/10), 10, 51);
                    lcd.drawString(String.valueOf(oldday%10), 16, 51);
                }
                oldday = ((day-1+diffday)%dcount)+1;
                lcd.drawString(String.valueOf(oldday/10), 10, 51);
                lcd.drawString(String.valueOf(oldday%10), 16, 51);

            }
            lcd.setDrawMode(Display.NORMAL);
            if (changed) clock.setValue((hours+diffhours)%24, (minutes+diffminutes)%60, (seconds+diffseconds)%60);
            changed = false;
            dcount = (getDays(oldmonth, oldyear));
            char c = (pressed)?keys.read():keys.getKey();
            if (c!=0) {
                if (!pressed && notPressSecs>=10) {
                    break mainloop;
                }
                pressed = true;
            }
            switch (c) {
                case 'D':
                case 'd': 
                case 'L':
                    switch (select) {
                        case 0: diffhours+=23;   break;
                        case 1: diffminutes+=59; break;
                        case 2: diffseconds+=59; break;
                        case 5: diffday+=dcount-1;break;
                        case 4: diffmonth+=11;   break;
                        case 3: 
                            if (year+diffyear>2000) {
                                diffyear--; 
                            } else {
                                diffyear = 2099-year;
                            }
                            break;
                    }                    
                    break;
                case 'U':
                case 'u':
                case 'R': 
                    switch (select) {
                        case 0: diffhours++;   break;
                        case 1: diffminutes++; break;
                        case 2: diffseconds++; break;
                        case 5: diffday++;     break;
                        case 4: diffmonth++;   break;
                        case 3: 
                            if (year+diffyear<2099) {
                                diffyear++;
                            } else {
                                diffyear = 2000-year;
                            }
                            break;
                    }                    
                    break;
                case 'S':
                case 'M':                    
                    if (select>=5) {
                        Time time = new Time(oldyear, oldmonth, oldday, 0, oldhours, oldminutes, oldseconds);
                        RTC.setTime(time);
                        break mainloop;
                    } else {
                        select++;
                        break;
                    }                    
            }
            if (!pressed) try {
                ThreadExt.sleep(100);
            } catch (InterruptedException e) {}
        }                
    }

    /**
     * Returns the number of days for the specified month.
     * 
     * @param month  The month
     * @param year   the year
     * @return number of days
     */
    private static int getDays(int month, int year) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11: return 30;
            case 2: if (((year%4==0) && !(year%100==0)) || (year%400==0)) return 29;
                else return 28;
            default: return 31;
        }
    }
}