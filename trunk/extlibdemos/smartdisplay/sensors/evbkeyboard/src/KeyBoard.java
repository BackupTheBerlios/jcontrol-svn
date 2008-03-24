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

import jcontrol.comm.DisplayConsole;
import jcontrol.io.Backlight;
import jcontrol.io.Display;
import jcontrol.lang.ThreadExt;
import jcontrol.san.driver.gpio.EvbKeyBoard;

/**
 * Demo for the Evb keyboard.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class KeyBoard {

    /**
     * main()
     */
    public static void main(String[] args) {

        Backlight.setBrightness(Backlight.MAX_BRIGHTNESS / 2);
        Display lcd = new Display();
        DisplayConsole con = new DisplayConsole(lcd);
        EvbKeyBoard keyboard = new EvbKeyBoard();

        con.println(keyboard.getName());

        while (true) {

            keyboard.waitForKey();
            con.println("keycode = ".concat(String.valueOf(keyboard.getLastKey()).concat(" : \"")
                    .concat(keyboard.getKeyString().concat("\""))));
            try {
                ThreadExt.sleep(500);
            } catch (InterruptedException e) {
                // ignore it
            }
        }
    }
}
