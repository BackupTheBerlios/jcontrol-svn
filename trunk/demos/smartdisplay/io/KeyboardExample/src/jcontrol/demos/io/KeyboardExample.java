/*
 * KeyboardExample.java
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

package jcontrol.demos.io;

import jcontrol.io.Keyboard;
import jcontrol.io.Buzzer;
import jcontrol.io.Display;
import jcontrol.comm.DisplayConsole;

/**
 * Simple Keyboard demo. On keypress, a sound is played by the buzzer.
 * Sound frequency can be altered by up/down keys, sound duration by
 * left/right buttons. The pressed key is also shown on the display.
 */
public class KeyboardExample {
 
    public KeyboardExample() {
        
        // turn off automatic keyboard beep
        jcontrol.system.Management.setProperty( "buzzer.keyboardbeep", "false");

		// get keyboard and buzzer handler
        Keyboard keyboard = new Keyboard();
        Buzzer buzzer = new Buzzer();
        DisplayConsole console = new DisplayConsole( new Display());
        
        int frequency = 440;
        int duration = 200;
        
        for (;;) {
        	char c = keyboard.read();
			switch (c) {
				case 'U':
					// frequency up and beep
					if ( frequency < 880) {
						frequency += 20;
					}
					console.println( "up");
					break;
				case 'D':
					// frequency down and beep
					if ( frequency > 220) {
						frequency -= 20;
					}
					console.println( "down");
					break;
				case 'L':
					// frequency down and beep
					if ( duration > 20) {
						duration -= 20;
					}
					console.println( "left");
					break;
				case 'R':
					// frequency down and beep
					if ( duration < 500) {
						duration += 20;
					}
					console.println( "right");
					break;
				case 'S':
					console.println( "select");
			}
			buzzer.on( frequency, duration);
        }
    }

    public static void main(String[] args) {
        new KeyboardExample();
    }
}
