/*
 * Copyright (C) 2008 DOMOLOGIC Home Automation GmbH This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/**
 * A simple rs232-write demo. Press a button on the evaluation board to generate an output to the
 * RS232-port.
 * 
 * @author Remi Seiler, mgn
 * @version $Revision$
 */

import java.io.IOException;

import jcontrol.comm.RS232;
import jcontrol.io.Backlight;
import jcontrol.io.Buzzer;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;

public class RS232Write {

    private static final int BAUDRATE = 19200;

    public static void main(String[] args) {
        Display lcd = new Display();
        Backlight.setBrightness(Backlight.MAX_BRIGHTNESS);
        Buzzer buzzer = new Buzzer();

        // some nice little output on the display
        lcd.drawString("JControl RS232 Write Example", 0, 0);

        RS232 rs232;
        try {
            rs232 = new RS232(BAUDRATE); // init RS232 access
        } catch (IOException e) {
            lcd.drawString("RS3232 ERROR!", 0, 12);
            return; // nothing to do because RS232 initialization failed
        }

        lcd.drawString("RS232 ready", 0, 12);
        // print the current baudrate on the display
        lcd.drawString("Baudrate at ".concat(String.valueOf(BAUDRATE)), 0, 20);

        lcd.drawString("Press a button...", 0, 34);

        for (;;) {
            Keyboard k = new Keyboard();
            int key = k.getKey();
            if (key == 'U') {
                rs232.print("up");
                buzzer.on(400, 50);
                buzzer.off();
            } else if (key == 'D') {
                rs232.print("down");
                buzzer.on(600, 50);
                buzzer.off();
            } else if (key == 'L') {
                rs232.print("left");
                buzzer.on(800, 50);
                buzzer.off();
            } else if (key == 'R') {
                rs232.print("right");
                buzzer.on(1000, 50);
                buzzer.off();
            } else if (key == 'S') {
                rs232.print("select");
                buzzer.on(1200, 50);
                buzzer.off();
            }
        }
    }
}
