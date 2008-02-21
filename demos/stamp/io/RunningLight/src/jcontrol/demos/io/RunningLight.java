/*
 * RunningLight.java
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

import jcontrol.io.GPIO;
import jcontrol.lang.ThreadExt;

/**
 * RunningLight demonstrates the usage of GPIOs provided by JControl.
 */
public class RunningLight {


    /** Define GPIO channels to be used by the RunningLight */
    static final int GPIO_START = 4;
    static final int GPIO_STOP = 11;

    /**
     * Main method. Init relay and attract it for one second.
     * @param args
     */
    public static void main(String args[]) {

        // simple control variable
        int pin;

        // set all pins to push pull mode and state LOW
        for (pin = GPIO_START; pin <= GPIO_STOP; pin++) {
            GPIO.setMode(pin, GPIO.PUSHPULL);
            GPIO.setState(pin, GPIO.LOW);
        }

        // application runs in endless loop
        for (;;) {

            for (pin = GPIO_START; pin <= GPIO_STOP; pin++) {
                // set GPIO channel to HIGH
                GPIO.setState(pin, GPIO.HIGH);
                // sleep 0.1 seconds
                try {
                    ThreadExt.sleep(100);
                } catch (InterruptedException e) {}
                // set GPIO channel back to LOW
                GPIO.setState(pin, GPIO.LOW);
            }

            for (pin = GPIO_STOP; pin >= GPIO_START; pin--) {
                // set GPIO channel to HIGH
                GPIO.setState(pin, GPIO.HIGH);
                // sleep 0.1 seconds
                try {
                    ThreadExt.sleep(100);
                } catch (InterruptedException e) {}
                // set GPIO channel back to LOW
                GPIO.setState(pin, GPIO.LOW);
            }
        }
    }
}
