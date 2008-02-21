/*
 * PWMExample.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */
import jcontrol.lang.ThreadExt;
import jcontrol.io.PWM;

/**
 * This example demonstrates the usage of class <code>PWM</code>.
 */
public class StampLED {
    /** PWM frequency */
    int frequency = 2000;
    /** PWM channel */
    int channel = 2; // 2 = LED, 3=buzzer
  
    /** 
     * Set PWM frequency, duty cycle and turns the signal on.
     */
    public StampLED() {

        PWM.setFrequency(frequency);
        PWM.setActive(channel, true);
    
        for(;;) {
            // slide from 0 to 255
	        for(int i = 0; i<256; i++) {
			    PWM.setDuty(channel, i);
                try {
				    ThreadExt.sleep(1);
    	        } catch(InterruptedException e) {}
	        }
    	    // slide from 255 to 0
	        for(int i = 255; i>= 0; i--) {
		    	PWM.setDuty(channel, i);
			    try {
				    ThreadExt.sleep(1);
    			} catch(InterruptedException e) {}
	        }
	    }
  }

  /**
   * Main method. Program execution starts here.
   */
  public static void main(String args[]) {
    new StampLED();
    
    for (;;) {} // sleep
  }
}
