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
import jcontrol.lang.ThreadExt;
import jcontrol.san.driver.i2c.rfid.CRX14Reader;
import jcontrol.san.driver.i2c.rfid.SR176Transponder;
import jcontrol.san.interfaces.sensors.RFIDReader;
import jcontrol.san.interfaces.sensors.RFIDTransponder;

/**
 * Example for a RFID reader.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision:36 $
 */
public class RFIDExample {

    /**
     * main().
     */
    public static void main(String[] args) {

        Backlight.setBrightness(Backlight.MAX_BRIGHTNESS / 2);
        Display lcd = new Display();
        DisplayConsole con = new DisplayConsole(lcd);

        RFIDReader crx14 = new CRX14Reader(0xa0);

        while (true) {
            try {
                crx14.power(false);
                crx14.power(true);
                break;
            } catch (IOException e) {
                con.println("I2C error: power control failure");
                try {
                    ThreadExt.sleep(5000);
                } catch (InterruptedException e1) {
                    // ignore it
                }
                continue;
            }

        }
        con.print("waiting for RFID...");
        while (true) {
            try {
                RFIDTransponder transponder = new SR176Transponder();

                if (crx14.initiate(transponder)) {
                    con.println();
                    con.print("Chip ID: ".concat(String.valueOf(transponder.getChipId())));

                    boolean found = crx14.select(transponder);
                    if (found) {
                        con.print(" UID:".concat(transponder.toString()));
                        crx14.completion();
                        crx14.reset();
                    }
                }
            } catch (IOException e) {
                con.println(" I2C error:");
                continue;
            }
            try {
                ThreadExt.sleep(200);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
