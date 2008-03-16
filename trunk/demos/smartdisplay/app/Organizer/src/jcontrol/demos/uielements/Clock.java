/*
 * Clock.java
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

import jcontrol.io.Buzzer;
import jcontrol.io.Graphics;
import jcontrol.lang.Deadline;
import jcontrol.lang.DeadlineMissException;
import jcontrol.lang.ThreadExt;
import jcontrol.system.RTC;
import jcontrol.system.Time;
import jcontrol.ui.vole.meter.AnalogClock;

/**
 * This class <CODE>Clock</CODE> provides a full screen analog clock.
 *
 * @author Marcus Timmermann
 * @see jcontrol.ui.AnalogClock
 * @see jcontrol.system.RTC
 * @see jcontrol.system.Time
 */
public class Clock implements Runnable {

    private boolean alarm;
    private int x,y,width,height,radius;
    private boolean abort=false, running=true;
    private AnalogClock clock;

    /**
     * Create a big and self-updating clock.
     * 
     * @param lcd The graphics context
     * @param alarm When true, the clock can invoke alarms
     */
    public Clock(int x, int y, int width, int height, Graphics lcd, boolean alarm){
        this.alarm=alarm;
        this.x = x;
        this.y = y;        
        radius=width;
        if (height < width)
          radius = height;
        radius >>= 1;
        
        clock = new AnalogClock(x, y, radius, true);
        clock.setGraphics(lcd);
        clock.setVisible(true);
        
        new Thread(this).start();        
    }

    /**
     * This method can be run from a global menu context, using the global Keyboard and Display objects.
     */
    public void run() {
        running = true;
        int hours,minutes,seconds;
        {
            Time time = new Time();
            hours   = time.hour;
            minutes = time.minute;
            seconds = time.second;
        }

        Deadline dl=new Deadline(1000);
        synchronized(dl){
            int count = 0;
            Buzzer b=null;
            do {
                clock.setValue(hours, minutes, seconds);
                try {
                    if (alarm && count<20) {
                        if(b==null) b=new Buzzer();
                        for (int i=0; i<4; i++) {
                            b.on((short)880);                
                            ThreadExt.sleep(120);
                            b.off();
                            ThreadExt.sleep(30);               
                        }
                        count++;
                    }
                } catch (InterruptedException e) {
                    count=20;
                }
                seconds++;
                if (seconds>=60) {
                    seconds-=60; minutes++;
                    if (minutes>=60) {
                        minutes-=60; hours++;
                        if (hours>=24) hours-=24;
                    }
                    if(RTC.isAlarm()) abort=true;
                }
                if(!abort) try {
                    dl.append(1000);
                } catch (DeadlineMissException e) {}
            } while (!abort);
        }
        running=false;
    }
    

    /**
     * Return whether the clock thread is running.
     */
    public boolean isRunning() {
      return running;
    }


    /**
     * Stops the clock thread.
     */    
    public void stop() {
        abort=true;
    }
}