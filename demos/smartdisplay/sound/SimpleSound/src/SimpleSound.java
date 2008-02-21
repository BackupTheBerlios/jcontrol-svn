/*
 * SimpleSound.java
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
import jcontrol.io.Keyboard;
import jcontrol.lang.ThreadExt;

/**
 * Plays some beeps via the buzzer.
 */
public class SimpleSound {

    /**
     * standard constructor
     */
    public SimpleSound() {
        try {
            Keyboard k = new Keyboard();
            Buzzer b = new Buzzer();

            for (int i=0; i<10; i++) {
                // exit if any key is pressed
                if (k.getRaw()!=0) break;
                for (int j=0; j<4; j++) {
                    // beep with 880Hz
                    b.on(880);
                    // wait for 150ms
                    ThreadExt.sleep(150);
                    b.off();
                    // wait for 50ms
                    ThreadExt.sleep(50);
                }
                // wait for 1000ms
                ThreadExt.sleep(1000);
            }
        } catch (InterruptedException e) {}
    }


    /**
     * main method. Program execution starts here.
     */
    public static void main(String[] args){
    	new SimpleSound();
    }
}
