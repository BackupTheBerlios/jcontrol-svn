/*
 * SystemSetup.java
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

import jcontrol.io.Buzzer;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Download;
import jcontrol.system.Management;
import jcontrol.system.RTC;

/**
 * This is the main setup menu for the JControl Sticker.
 *
 * @author Marcus Timmermann
 * @version 1.01
 * @see jcontrol.system.setup.SetupBatt
 * @see jcontrol.system.setup.SetupContrast
 * @see jcontrol.system.setup.SetupSound
 * @see jcontrol.system.setup.SetupStandbyTime
 * @see jcontrol.system.setup.SetupTime
 */
public class SystemSetup {


    /**
     * The global Display object
     */
    protected static Display lcd = null;
    /**
     * The global Keyboard object
     */
    protected static Keyboard keys = null;

    /**
     * The main method.
     *
     * @param args   keep it empty
     */
    public static void main(String [] args) throws java.io.IOException {
	    // lights on!
   		jcontrol.io.Backlight.setBrightness(jcontrol.io.Backlight.MAX_BRIGHTNESS);

        keys = new Keyboard();               // get keyboard
        lcd = new Display();                 // get display

        while(keys.getRaw()=='S') try {
            ThreadExt.sleep(10);        // nach dem Einschalten muss die Taste losgelassen werden
        } catch (InterruptedException e) {}
        if(!RTC.isAlarm()) {            // nur wenn kein Alarm ist, Bank-Switching aktivieren
            try {
                lcd.drawImage(new Resource("jcontrol_splash.jcif"), 0,0);
            } catch (java.io.IOException e) {}
            int countdown = 100;
            boolean keypressed = false;
            while (countdown>0) {
                try {
                    ThreadExt.sleep(10);
                } catch (InterruptedException e) {}
                switch(keys.getRaw()){
                case 'U':
                    countdown=-1;                    
//                    lcd.clearDisplay();
                    break;
                case 'D':
                    countdown=0;
                case 'S':
                    keypressed=true;
                    break;
                case 0:
                    if(keypressed) countdown=0;
                    else           countdown--;
                }
            }
            if (countdown==0) {
                Management.switchBank();         // auf zuletzt selektierte Bank schalten oder einfach fortsetzen
            }

        }
//            Time wakeup = new Time();
//            RTC.getAlarm(wakeup);
//            Time time   = new Time();
//            if (wakeup.hour==time.hour && wakeup.minute==time.minute && RTC.isAlarm()) {
        new SystemSetup$Alarm();
//        {
//            String d=Management.getProperty("system.standbytimer");
//            if(d!=null)Management.powerOff(Integer.parseInt(d));
//        }

        int select=0;
        mainloop:for (;;) {
            select = SystemSetup$Menu.choose(lcd,keys, select);
            switch (select) {
                case 0:
                    SetupBatt.go(lcd,keys);
                    break;
                case 1:
                    SetupTime.go(lcd,keys);
                    break;
                case 2:
                    SetupContrast.go(lcd,keys);
                    break;
                case 3:
                    SetupSound.go(lcd,keys);
                    break;
                case 7:
                    SetupStandbyTime.go(lcd,keys);
                    break;
                case 5:
                    fileXfer();
                    break;
                case 4:
                    if(SetupBootDevice.go(lcd,keys)>0) break mainloop;
                    break;
                case 6:
                    jcontrol.demos.misc.VisitCard_JControl.go(lcd,keys);
                    break;
                case 9:
                    jcontrol.system.Management.powerOff(-1);
                case 8:
                    String file = SetupExplorer.go(lcd, keys);
                    if(file!=null) {
                        jcontrol.system.Management.start(file,null,true);
                        break mainloop;
                    }
            }
        }
    }

    /**
     * This is a small animation to introduce a flash image upload.
     */
    static void fileXfer() throws java.io.IOException {
        lcd.clearRect(1, 20, 126, 43);
        lcd.drawString("Preparing upload procedure...", 15,22);
//        lcd.drawString("Please wait...", 40,50);
        {
            String s="parameters: ".concat(Management.getProperty("rs232.baudrate")).concat(" 8N1");
            lcd.drawString(s,(128-lcd.getTextWidth(s))>>1,50);
        }
        lcd.setDrawMode(Display.XOR);
        int delay;
        {
            String d=Management.getProperty("system.standbytimer");
            delay=(d!=null)?Integer.parseInt(d):0;
        }
        Management.powerOff(0);
        Download d=new Download();
        Thread t=new Thread(d);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        int pos = 0, oldpos = 0;
        for (;t.isAlive();) {
            lcd.setDrawMode(Display.XOR);
            if (oldpos>0 && oldpos<4) lcd.drawImage(new String[] {"\u0C14\u2231\u3214\u0800"}, 41+7*oldpos, (oldpos==2?31:32), 7, 6,0,0);
            lcd.setDrawMode(Display.NORMAL);

            if (pos==0) {
                lcd.drawImage(new String[] {"\u186E\u898D\u8B89\u8A94\uFC00"}, 39, 35);
                lcd.drawImage(new String[] {"\u186E\u8989\u898A\u8A9A\uFC00"}, 69, 35);
            } else if (pos>3) {
                lcd.drawImage(new String[] {"\u186E\u8989\u898A\u8A9A\uFC00"}, 39, 35);
                lcd.drawImage(new String[] {"\u186E\u898D\u8B89\u8A94\uFC00"}, 69, 35);
            } else {
                lcd.drawImage(new String[] {"\u0C14\u2231\u3214\u0800"}, 41+7*pos, (pos==2?31:32), 7, 6,0,0);
                lcd.drawImage(new String[] {"\u186E\u8989\u898A\u8A9A\uFC00"}, 39, 35);
                lcd.drawImage(new String[] {"\u186E\u8989\u898A\u8A9A\uFC00"}, 69, 35);
            }
            oldpos = pos;
            pos++; pos%=12;
            try {
                ThreadExt.sleep(500);
            } catch (InterruptedException e) {}
        }
        if(delay>0) Management.powerOff(delay);
        if (d.error) throw new java.io.IOException();
    }

}

/**
 * Background alarm checker and notifier.
 * @version 1.0
 * @author  Helge Böhme
 */
class SystemSetup$Alarm extends Thread {

    /**
     * Alarm tone flag.
     */
    protected static boolean beeping = false;

    /**
     * Constructor starts automatically a background thread.
     */
    public SystemSetup$Alarm(){
        setDaemon(true);
        start();
    }

    /**
     * Checks every 10 seconds the RTC alarm flag and - if one occurs - uses the buzzer
     * and resets the alarm flag. 
     * @see java.lang.Runnable#run()
     * @see jcontrol.system.RTC
     */
    public void run() {
        for(;;){
            if(RTC.isAlarm()){
                if(!Management.getProperty("buzzer.enable").equals("true")) beeping=false;
                else {
                    Buzzer b=new Buzzer();
                    beeping = true;
                    int count = 0;
                    try {
                        while (beeping && count<10) {
                            for (int i=0; i<4; i++) {
                                b.on((short)880);
                                ThreadExt.sleep(150);
                                b.off();
                                ThreadExt.sleep(50);
                            }
                            count++;
                            ThreadExt.sleep(1000);
                        }
                    } catch (InterruptedException e) {}
                    beeping = false;
                    b.off();
                }
                RTC.resetAlarm();
            } else try {
                ThreadExt.sleep(10000);
            } catch (InterruptedException e) {}
        }
    }
}

/**
 * This class is used to show all graphics on the main menu screen.
 */
class SystemSetup$Menu {

    static int images = 10;
    static int shift = 0;

    /**
     * Displays a horizontal scrolling icon bar, the user may choose using the
     * keyboard.
     * @param lcd  the Display to use
     * @param keys the Keyboard to use
     * @param select preselection
     * @return int chosen function
     */
    static int choose(Display lcd,Keyboard keys, int select){
        int oldselect = -1, oldshift = -1;
        // the main frame image
        try {
            lcd.drawImage(new Resource("systemsetup.jcif"),0,0);
        } catch (java.io.IOException e) {}
mainloop:for (;;) {
            {
                if (select-shift>5) shift = select-5;
                if (select-shift<0) shift = select;
                if (shift!=oldshift) {
                    // the icons to choose
                    int xpos, xoff, width;
                    switch (shift) {
                        case 2:
                            xpos = 3;
                            width = 122;
                            xoff = 20*shift-1;
                            break;
                        case 3:
                            xpos = 5;
                            width = 119;
                            xoff = 20*shift+1;
                            break;
                        default:
                            xpos = 4;
                            width = 120;
                            xoff = 20*shift;
                            break;
                    }

                    try {
                        lcd.drawImage(new Resource("chooser.jcif"), xpos, 23, width, 40, xoff ,0);
                    } catch (java.io.IOException e) {}
                    if (oldshift==2) {
                        // delete obsolete pixels
                        lcd.clearRect(1,44,4,19);
                        lcd.clearRect(123,44,4,19);
                    }
                    if (shift>0) {
                        // left arrow
                        lcd.drawImage(new String[] {"\u107C\uFF00","\u0000\u0100"}, 1,30,3,9,0,0);
                    } else {
                        lcd.clearRect(1,30,3,9);
                    }
                    if (shift<4) {
                        // right arrow
                        lcd.drawImage(new String[] {"\uFF7C\u1000","\u0100\u0000"}, 124,30,3,9,0,0);
                    } else {
                        // no arrow
                        lcd.clearRect(124,30,3,9);
                    }

                }
                if (select!=oldselect) {
                    // set selection
                    lcd.setDrawMode(Display.XOR);
                    if (oldselect!=-1 && oldshift==shift)
                        lcd.fillRect(4+(oldselect-shift)*20, 23, 20, 22);
                    lcd.fillRect(4+(select-shift)*20, 23, 20, 22);
                    oldselect = select;
                    lcd.setDrawMode(Display.NORMAL);
                    oldshift = shift;

                }
            }
            switch (keys.read()) {
                case 'u':
                case 'U':
                case 'L':
                    if (select>0) select--;
                    SystemSetup$Alarm.beeping = false;
                    break;
                case 'd':
                case 'D':
                case 'R':
                    if (select<images-1) select++;
                    SystemSetup$Alarm.beeping = false;
                    break;
                case 'S':
                case 'M':
                    SystemSetup$Alarm.beeping = false;
                    break mainloop;
            }
        }
        return select;

    }

}