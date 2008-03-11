/*
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package jcontrol.demos.sensors;

import java.io.IOException;

import jcontrol.bus.i2c.BH1710;
import jcontrol.bus.i2c.TMP75;
import jcontrol.bus.i2c.TSL2561;
import jcontrol.io.Backlight;
import jcontrol.io.Display;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;

/**
 * Intro screen for the LightTemperatureDemo.
 * 
 * @author Remi Seiler, Thomas Roebbenack
 * @version $Revision$
 */
public class LightTemperatureIntro {

    public LightTemperatureIntro() {
        Display d = new Display();
        d.drawString("Willkommen!", 2, 5);
        d.drawString("Bitte schließen Sie einen oder beide", 2, 25);
        d.drawString("mitgelieferten Sensoren an die", 2, 35);
        d.drawString("dafür vorgesehenen I2C-", 2, 45);
        d.drawString("Schnittstelle(n) an.", 2, 55);

        try {
            d.drawImage(new Resource("jcontrol_small.jcif"), 100, 0);
        } catch (IOException e) {
        }

        Backlight.setBrightness(255);

        TMP75 tmp = null;
        TSL2561 tsl = null;
        BH1710 bh = null;

        intro: for (;;) {
            if (tmp == null) {
                tmp = new TMP75(0x9e);
            }
            if (tmp != null) {
                try {
                    tmp.getTemp();
                } catch (IOException e1) {
                    tmp = null;
                }
            }
            if (tsl == null) {
                try {
                    tsl = new TSL2561(0x72);
                } catch (IOException e2) {
                    try {
                        bh = new BH1710(0x46);
                    } catch (IOException e3) {
                    }
                }
            }
            if (tsl != null) {
                try {
                    tsl.getChannel0();
                } catch (IOException e) {
                    tsl = null;
                }
            } else if (bh != null) {
                try {
                    bh.getContinuouslyMeasurementL();
                } catch (IOException e) {
                    bh = null;
                }
            }

            if (tmp != null || tsl != null || bh != null) {
                break intro;
            }

            try {
                ThreadExt.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        d.clearDisplay();
    }
}
