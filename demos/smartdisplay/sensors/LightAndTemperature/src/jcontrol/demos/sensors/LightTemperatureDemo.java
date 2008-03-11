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
import jcontrol.bus.i2c.TSL2561LuxConversion;
import jcontrol.lang.ThreadExt;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.TextArea;
import jcontrol.ui.vole.meter.AnalogMeter;
import jcontrol.ui.vole.meter.Thermometer;

/**
 * This demo displays temperature and light sensor information obtained from a TMP75 and a TSL2561
 * sensor connected via the JControl's I<sup>2</sup>C bus.
 * 
 * @author Remi Seiler, Thomas Roebbenack
 * @version $Revision$
 */
public class LightTemperatureDemo extends Frame {

    private static final String ERROR_NO_SENSORS = "No sensors found!";
    private static final String ERROR_NO_TSL = "No TSL2561/BH1017 found!";
    private static final String ERROR_NO_LM = "No LM75 found!";
    private static final String NO_ERROR = "Reading LM75 + TSL2561/BH1017";

    private static final int ERROR_NO_SENSORS_STATE = 0;
    private static final int ERROR_NO_TSL_STATE = 1;
    private static final int ERROR_NO_LM_STATE = 2;
    private static final int NO_ERROR_STATE = 3;

    private int m_state = ERROR_NO_SENSORS_STATE;
    private int m_oldState = ERROR_NO_SENSORS_STATE;

    public LightTemperatureDemo() {
        Border lmBorder = new Border("Temp.", 0, 0, 40, 54);
        lmBorder.setVisible(false);
        add(lmBorder);
        Border tslBorder = new Border("Light", 42, 0, 86, 54);
        tslBorder.setVisible(false);
        add(tslBorder);

        Thermometer thermometer = new Thermometer(3, 10, 35, 40, -100, 500);
        thermometer.setCaption("-10", "+50");
        thermometer.setNumericDisplay(3, 1, "\u00b0C");
        thermometer.setValue(0);
        thermometer.setVisible(false);
        add(thermometer);

        AnalogMeter luxmeter = new AnalogMeter(57, 5, 50, 40, 0, 3000, 130,
                AnalogMeter.ORIENTATION_CENTER, 20);
        luxmeter.setCaption("Dark", "Bright");
        luxmeter.setNumericDisplay(5, 0, "");
        luxmeter.setValue(0);
        luxmeter.setVisible(false);
        add(luxmeter);

        TextArea status = new TextArea(0, 54, 128, 10, false);
        status.insert(0, ERROR_NO_SENSORS);
        add(status);

        Label lux = new Label("lux", 77, 46, Label.ALIGN_CENTER);
        lux.setVisible(false);
        add(lux);

        show();

        TMP75 tmp = null;
        TSL2561 tsl = null;
        BH1710 bh = null;

        for (;;) {
            if (tmp == null) {
                tmp = new TMP75(0x9e);
            }
            if (tmp != null) {
                try {
                    thermometer.setValue(tmp.getTemp());
                } catch (IOException e1) {
                    tmp = null;
                }
            }
            if (tsl == null && bh == null) {
                try {
                    tsl = new TSL2561(0x72);
                    // tsl.setTimingFast();
                } catch (IOException e2) {
                    try {
                        bh = new BH1710(0x46);
                    } catch (IOException e3) {
                    }
                }
            }
            if (tsl != null) {
                try {
                    int ch0 = tsl.getChannel0();
                    int ch1 = tsl.getChannel1();
                    try {
                        int l = TSL2561LuxConversion.calculateLux(false, (short) 2, (short) ch0,
                                (short) ch1, true);
                        if (l < 0 || l > 1000) {
                            luxmeter.setValue(3000);
                        } else {
                            luxmeter.setValue(l);
                        }
                    } catch (IllegalArgumentException e) {
                        luxmeter.setValue(3000);
                    }
                } catch (IOException e) {
                    tsl = null;
                }
            } else if (bh != null) {
                try {
                    int lux2 = bh.getContinuouslyMeasurementH();
                    luxmeter.setValue(lux2);
                } catch (IOException e) {
                    bh = null;
                }
            }

            if (tmp == null && tsl == null && bh == null) {
                m_state = ERROR_NO_SENSORS_STATE;
            } else if (tmp == null) {
                m_state = ERROR_NO_LM_STATE;
            } else if (tsl == null && bh == null) {
                m_state = ERROR_NO_TSL_STATE;
            } else {
                m_state = NO_ERROR_STATE;
            }
            if (m_state != m_oldState) {
                m_oldState = m_state;
                status.remove(0);
                switch (m_state) {
                    case ERROR_NO_SENSORS_STATE:
                        status.insert(0, ERROR_NO_SENSORS);
                        thermometer.setVisible(false);
                        lmBorder.setVisible(false);
                        luxmeter.setVisible(false);
                        tslBorder.setVisible(false);
                        lux.setVisible(false);
                        break;
                    case ERROR_NO_TSL_STATE:
                        status.insert(0, ERROR_NO_TSL);
                        thermometer.setVisible(true);
                        lmBorder.setVisible(true);
                        luxmeter.setVisible(false);
                        tslBorder.setVisible(false);
                        lux.setVisible(false);
                        break;
                    case ERROR_NO_LM_STATE:
                        status.insert(0, ERROR_NO_LM);
                        thermometer.setVisible(false);
                        lmBorder.setVisible(false);
                        luxmeter.setVisible(true);
                        tslBorder.setVisible(true);
                        lux.setVisible(true);
                        break;
                    case NO_ERROR_STATE:
                        status.insert(0, NO_ERROR);
                        thermometer.setVisible(true);
                        lmBorder.setVisible(true);
                        luxmeter.setVisible(true);
                        tslBorder.setVisible(true);
                        lux.setVisible(true);
                        break;
                }
            }

            try {
                ThreadExt.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        new LightTemperatureIntro();
        new LightTemperatureDemo();
    }
}
