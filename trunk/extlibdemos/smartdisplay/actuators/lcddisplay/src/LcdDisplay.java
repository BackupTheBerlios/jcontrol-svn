/*
 * Copyright (C) 2008 The JControl Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

import java.io.IOException;

import jcontrol.comm.DisplayConsole;
import jcontrol.io.Backlight;
import jcontrol.io.Display;
import jcontrol.san.driver.i2c.display.PCF8574TLcdDisplay;
import jcontrol.san.interfaces.actuators.SimpleDisplay;

/**
 * Demo for the Conrad LCD-Display with PCF8574T (Philips) using the SM (I<sup>2</sup>C) bus.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class LcdDisplay {

    /**
     * main()
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Backlight.setBrightness(Backlight.MAX_BRIGHTNESS / 2);
        Display lcd = new Display();
        DisplayConsole con = new DisplayConsole(lcd);

        SimpleDisplay external_lcd = new PCF8574TLcdDisplay(0x42);

        con.println("display something");

        external_lcd.clear();
        external_lcd.drawString("LCD-Display", 1);
        external_lcd.drawString("Michael", 2);

        while (true) {

        }
    }

}
