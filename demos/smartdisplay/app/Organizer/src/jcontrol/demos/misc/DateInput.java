/*
 * DateInput.java
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

import jcontrol.io.*;
import jcontrol.system.*;
import jcontrol.demos.uielements.Typewriter;

/**
 * This class implements an appointment dialog. You can set a point of time and
 * a description text either with {@link jcontrol.demos.uielements.Typewriter Typewriter}
 * or {@link jcontrol.demos.uielements.Typewalker Typewalker} (chosen by changing this
 * sourcefile).
 * 
 * @version 1.0
 * @author  Marcus Timmermann
 */
public class DateInput {

    private static Display lcd;
    private static Keyboard key;
    
    public static Date getDate(Display d, Keyboard k, Date date) {
        lcd = d;
        key = k;
        {
            lcd.clearRect(1, 20, 126, 43);
            lcd.setFont(Display.SYSTEMFONT);
            lcd.setDrawMode(Display.NORMAL);
            lcd.drawString(date==null?"new appointment":"modify appointment", 5, 21);
            lcd.setDrawMode(Display.XOR);
            lcd.fillRect(1, 20, 126, 9);
            lcd.setDrawMode(Display.NORMAL);
            lcd.drawString("date:", 3, 30);
            lcd.drawString("time:", 3, 38);
            lcd.drawString("subject:", 3, 46);
            lcd.drawRect(1,55, 42, 8);
            lcd.drawRect(43,55, 42, 8);
            lcd.drawRect(85,55, 42, 8);
        }
        if(date==null) date=new Date(new Time(),"");
        int hour,min,month,year,day;
        hour  = date.time.hour;
        min   = date.time.minute;
        month = date.time.month;
        year  = date.time.year;
        day   = date.time.day;
        String word = date.text;
        lcd.drawString(word, 35, 46);
        int select = 0;
        for (;;) {
            String value = new String(new byte[]{(byte)((day/10)+48), (byte)((day%10)+48)});
            if (select==2) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            int pos = lcd.drawString(value, 35, 30);
            lcd.setDrawMode(Display.NORMAL);
            pos+=lcd.drawString(".",  35+pos, 30)+1;
            value = new String(new byte[]{(byte)((month/10)+48), (byte)((month%10)+48)});

            if (select==1) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            pos+=lcd.drawString(value, 35+pos, 30);
            lcd.setDrawMode(Display.NORMAL);
            pos+=lcd.drawString(".",  35+pos, 30)+1;

            if (select==0) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            lcd.drawString(String.valueOf(year),  35+pos, 30);
            value = new String(new byte[]{(byte)((hour/10)+48), (byte)((hour%10)+48)});

            if (select==3) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            pos = lcd.drawString(value, 35, 38)+1;
            lcd.setDrawMode(Display.NORMAL);
            pos+=lcd.drawString(":",  35+pos, 38)+1;

            if (select==4) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            value = new String(new byte[]{(byte)((min/10)+48), (byte)((min%10)+48)});
            lcd.drawString(value, 35+pos, 38);


            if (select==7) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            lcd.drawString("cancel.", 10, 56, -1, 6, 0, 0);
            if (select==6) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            lcd.drawString("confirm", 50, 56, -1, 6, 0, 0);
            if (select==5) lcd.setDrawMode(Display.INVERSE);
            else lcd.setDrawMode(Display.NORMAL);
            lcd.drawString("subject", 93, 56, -1, 6, 0, 0);
            lcd.setDrawMode(Display.NORMAL);
            char c = key.read();
            switch (c) {
                case 'L':
                    select++;
                    select%=8;
                    break;
                case 'R':
                    select+=7;
                    select%=8;
                    break;        
                case 'U':
                case 'u':
                    switch (select) {
                        case 0:
                            year++;
                            if (year>2100) year = 2000;
                            int days = getDays(month,year);
                            if (day>days) day = days;
                            break;
                        case 1:
                            month = (month%12)+1;
                            days = getDays(month,year);
                            if (day>days) day = days;
                            break;
                        case 2:
                            day = (day%getDays(month,year))+1;
                            break;
                        case 3:
                            hour = (hour+1)%24;
                            break;
                        case 4:
                            min = (min+1)%60;
                            break;
                        case 5:
                        case 6:
                        case 7:
                            select++;
                            select%=8;
                            break;
                    }
                    break;
                case 'D':
                case 'd':
                    switch (select) {
                        case 0:
                            year--;
                            if (year<2000) year = 2100;
                            int days = getDays(month,year);
                            if (day>days) day = days;
                            break;
                        case 1:
                            month--; if (month==0) month = 12;
                            days = getDays(month,year);
                            if (day>days) day = days;
                            break;
                        case 2:
                            day--; if (day==0) day=getDays(month,year);
                            break;
                        case 3:
                            hour = (hour+23)%24;
                            break;
                        case 4:
                            min = (min+59)%60;
                            break;
                        case 5:
                        case 6:
                        case 7:
                            select+=7;
                            select%=8;
                            break;
                    }
                    break;
                case 'S':
                    if (select<5) {
                        select++;
                        select%=8;
                    } else if (select==5) { // subject
                        lcd.clearDisplay();
                                           // choose your Typewriter/walker here
                        word = Typewriter.getText(word, lcd, key);
                        //word = Typewalker.getText(word, lcd, key);
                        try{
                            lcd.drawImage(new Resource("organizer.jcif"),0,0);
                        } catch(java.io.IOException e){}
                        lcd.setFont(Display.SYSTEMFONT);
                        {
                            lcd.setDrawMode(Display.NORMAL);
                            lcd.drawString("new appointment", 5, 21);
                            lcd.setDrawMode(Display.XOR);
                            lcd.fillRect(1, 20, 126, 9);
                            lcd.setDrawMode(Display.NORMAL);
                            lcd.drawString("date:", 3, 30);
                            lcd.drawString("time:", 3, 38);
                            lcd.drawString("subject:", 3, 46);
                            lcd.drawRect(1,55, 42, 8);
                            lcd.drawRect(43,55, 42, 8);
                            lcd.drawRect(85,55, 42, 8);
                        }
                        if (word!=null) {
                            lcd.drawString(word, 35, 46);
                        }
                    } else if (select==6) { // ok
                        if (date == null) {
                            date = new Date(new Time(year, month, day, 0, hour, min, 0), word);
                        } else {
                            date.time.hour = hour;
                            date.time.minute = min;
                            date.time.year = year;
                            date.time.month = month;
                            date.time.day = day;
                            date.text = word;
                        }
                        return date;
                    } else if (select==7) { // cancel
                        return date;
                    }
            }
        }
    }

    /**
     * Returns the days per month availabe.
     * @param month the number of the month (1...12)
     * @param year of the month (only necessary for february)
     * @return int days per month
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