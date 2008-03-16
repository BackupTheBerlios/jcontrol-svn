/*
 * VoleBarMeterExample.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import jcontrol.lang.Math;
import jcontrol.lang.ThreadExt;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.meter.BarMeter;

/**
 * <p>This example demonstrates how to use the
 * components BarMeter and Thermometer within
 * the GUI framework JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleBarMeterExample extends Frame {
  
  /**
   * Draw two BarMeters and a Thermometer
   */
  public VoleBarMeterExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create a vertical BarMeter with solid fill and inscriptions
    BarMeter bm1 = new BarMeter(5, 0, 30, 35, 0, 20,
                                BarMeter.ORIENTATION_VERTICAL,
                                BarMeter.FILL_SOLID);
    bm1.setCaption("Min", "Max");
    this.add(bm1);
    
    // create a horizontal BarMeter with line-style fill
    BarMeter bm2 = new BarMeter(5, 40, 100, 20, 0, 100, 
                                BarMeter.ORIENTATION_HORIZONTAL,
                                BarMeter.FILL_LINE);
    bm2.setCaption("0", "20");
    bm2.setNumericDisplay(5, 0, "%");
    this.add(bm2);
    
    // make the frame visible
    show();
    
    // generate random values
    for (;;) {
      bm1.setValue(Math.rnd(20));
      bm2.setValue(Math.rnd(100));
      
      try { ThreadExt.sleep(1000); } catch (InterruptedException e) {}
    }
  }

  /**
   * Start the example
   */
  public static void main(String[] args) {
    new VoleBarMeterExample();
  }
}
