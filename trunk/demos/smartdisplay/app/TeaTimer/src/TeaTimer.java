/*
 * TeaTimer.java
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

import jcontrol.io.Buzzer;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.Deadline;
import jcontrol.lang.DeadlineMissException;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Management;

/** 
 * Implements a simple TeaTimer.
 * <ul>
 * <li>Select your brewing time using up/down</li>
 * <li>Press the switch and the count-down starts ticking</li>
 * <li>After the desired time elapsed, the buzzer beeps</li>
 * <li>After 20 seconds or keypress the TeaTimer is ready for the next.</li>
 * </ul>
 * @version     1.0
 * @author      Helge Böhme
 */
public class TeaTimer {
    
    /**
     * Application entry point
     * @param args (not used)
     */
    public static void main(String args[]){
    	// lights on!
	   	jcontrol.io.Backlight.setBrightness(jcontrol.io.Backlight.MAX_BRIGHTNESS);

        go(new Display(),new Keyboard());
    }
        
    /**
     * Run this if you already have a Display and a Keyboard instance.
     * @param d Display to use
     * @param k Keyboard to use
     */
    public static void go(Display d,Keyboard k){
        int delay=0;
        {                                               // restrict context of s
            String s=Management.getProperty("system.standbytimer");
            if(s!=null) {                               // standby mode supported ?
                delay=Integer.parseInt(s);
                if(delay==0 || delay>60) delay=30;      // force standbydelay
                Management.powerOff(delay);
            }
        }
        try {
            d.drawImage(new Resource("TeaTimer.jcif"),0,0); // background                 
        } catch (java.io.IOException e) {}
        TeaTimer.d=d;                                   // for use with drawTime
        int sec=180;
        for(;;){
            {
                char c;
                do{
                    drawTime(sec);
                    c=k.read();
                    switch(c){
                        case 'U': sec+=10-(sec%10); break;
                        case 'R':
                        case 'u': sec++; break;
                        case 'D': sec-=((sec%10==0)?10:0)+(sec%10); break;
                        case 'L':
                        case 'd': sec--;  break;
                    }
                    if(sec<0) sec=0;
                    if(sec>599) sec=599;
                } while(c!='S' && c!='M');
            }
            if(delay>0) Management.powerOff(0);         // disable standby
            Deadline dl=new Deadline(1000);             // exact second clock
            synchronized(dl) {
                for(int s=sec-1;s>=0;s--){
                    if(k.getRaw()=='U') s=0;            // abort key
                    drawTime(s);
                    try {
                        if(s>0) dl.append(1000);        // next second
                    } catch (DeadlineMissException e) {}
                }
            }
            if(delay>0) Management.powerOff(delay);     // enable standby
            try {
                Buzzer b=new Buzzer();
                for (int c=0;c<10;c++) {
                    if(k.getRaw()!=0) break;
                    for (int i=0; i<4; i++) {
                        b.on(880);                
                        ThreadExt.sleep(150);
                        b.off();
                        ThreadExt.sleep(50);               
                    }
                    ThreadExt.sleep(1000);
                }
            } catch (InterruptedException e) {}
        }
    }
    
    private static Resource digits=null;
    private static Display d;
    
    /**
     * Just draws some digits with the current countdown time not using fonts.
     * @param t seconds remaining
     */
    static void drawTime(int t){
        int m=t/60;                         // minutes (1 digit)
        int s=t%60;                         // seconds (2 digits)
        try{
            if (digits==null) {
                digits=new Resource("Digits.jcif");
                d.drawImage(digits,75,32,3,17,110,0);   // draw the ':'
            }
            d.drawImage(digits,64,32,9,17,m*11,0);      // minutes
            d.drawImage(digits,80,32,9,17,(s/10)*11,0); // seconds high
            d.drawImage(digits,91,32,9,17,(s%10)*11,0); // soconds low
        } catch (java.io.IOException e){    // emergency sequence if no "Digits.jcif"
            byte[] time=new byte[4];
            time[0]=(byte)(m+'0');
            time[1]=':';
            time[2]=(byte)((s/10)+'0');
            time[3]=(byte)((s%10)+'0');
            d.drawString(new String(time),64,40);
        }
    }
}