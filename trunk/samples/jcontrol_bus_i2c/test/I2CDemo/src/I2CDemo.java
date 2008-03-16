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
import jcontrol.lang.ThreadExt;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.meter.AnalogMeter;
import jcontrol.ui.vole.meter.Thermometer;

/**
 * Java file created by JControl/IDE
 * 
 * @author timmer
 * @version $Revision$
 */
public class I2CDemo extends Frame {

    public I2CDemo() {
        // create the left Diagram
        add(new Border("Temp.", 0, 0, 40, 64));
        add(new Border("Light", 42, 0, 86, 64));
        Thermometer thermometer = new Thermometer(3, 10, 35, 50, -100, 500);
        thermometer.setCaption("-10", "+50");
        thermometer.setNumericDisplay(3, 1, "°C");
        thermometer.setValue(0);
        add(thermometer);

        AnalogMeter luxmeter = new AnalogMeter(57, 5, 50, 50, 0, 1000, 130,
                AnalogMeter.ORIENTATION_CENTER, 20);
        luxmeter.setCaption("Dark", "Bright");
        luxmeter.setNumericDisplay(5, 0, "");
        luxmeter.setValue(0);
        add(luxmeter);

        add(new Label("lux", 77, 56, Label.ALIGN_CENTER));
        // show the frame
        show();

        // create sensor instances
        LM75 lm75 = new LM75(0x9e);
        TSL2550 tsl2550 = null;
        try {
            tsl2550 = new TSL2550(0x72);
            // tsl2550.setExtendedRange(true);
        } catch (IOException e2) {
            add(new Label("No TSL2550 found!", 45, 25, 80, 10, Label.ALIGN_CENTER));
        }
        for (;;) {
            if (lm75 != null) {
                try {
                    thermometer.setValue(lm75.getTemp());
                } catch (IOException e1) {
                    add(new Label("No LM75 found!", 0, 25, 60, 10, Label.ALIGN_CENTER));
                    lm75 = null;
                }
            }
            if (tsl2550 != null) {
                try {
                    int lux = tsl2550.getIllumination();
                    if (lux > 0) {
                        luxmeter.setValue(lux);
                    }
                } catch (IOException e) {
                }
            }
            // sleep
            try {
                ThreadExt.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        new I2CDemo();
    }
}
