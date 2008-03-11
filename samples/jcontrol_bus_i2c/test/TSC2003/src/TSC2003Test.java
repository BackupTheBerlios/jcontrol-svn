/*
 * Copyright (C) 2004-2008 DOMOLOGIC Home Automation GmbH This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

import java.io.IOException;
import jcontrol.io.Display; // import jcontrol.comm.DisplayConsole;
import jcontrol.lang.ThreadExt;
import jcontrol.bus.i2c.TSC2003;

/**
 * <p>
 * LM75Test shows how to read the LM75 temperature detector class of the jcontrol.bus.i2c library.
 * </p>
 * 
 * @version $Revision$
 */
public class TSC2003Test {

    Display display;

    /**
     * Application Constructor.
     */
    public TSC2003Test() {

        display = new Display();
        boolean draw = true;

        display.drawString("TSC2003 Test Program:", 0, 0);
        for (;;) {
            try {
                TSC2003 tsc2003 = new TSC2003(0x94);
                // tsc2003.setHighspeed();
                for (;;) {
                    if (tsc2003.isPressed()) {
                        int xPos = tsc2003.getXPixel8();
                        int yPos = tsc2003.getYPixel8();
                        if (yPos > 75) {
                            draw = !draw;
                            display.clearRect(0, 0, 128, 64);
                            if (draw) {
                                display.drawString("TSC2003 Test Program (Draw):", 0, 0);
                            } else {
                                display.drawString("TSC2003 Test Program (Record):", 0, 0);
                            }
                        } else {
                            if (draw) {
                                display.setPixel(xPos, yPos);
                            } else {
                                display.clearRect(0, 10, 128, 64);
                                display.drawString("x: ".concat(Integer.toString(xPos)), 43, 30);
                                display.drawString("y: ".concat(Integer.toString(yPos)), 70, 30);
                            }
                        }
                        try {
                            ThreadExt.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            } catch (IOException e) {
                display.drawString("No TSC2003-Device found", 0, 0);
            }
            try {
                ThreadExt.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        jcontrol.io.Backlight.setBrightness(255);
        new TSC2003Test();
    }
}