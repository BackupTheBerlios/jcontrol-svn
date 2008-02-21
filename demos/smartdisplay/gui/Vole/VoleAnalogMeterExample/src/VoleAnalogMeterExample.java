/*
 * VoleAnalogMeterExample.java
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
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Container;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.meter.AnalogMeter;

/**
 * <p>This example demonstrates how to use the
 * component AnalogMeter within the GUI framework
 * JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleAnalogMeterExample extends Frame {
  AnalogMeter am1, am2, am3;
  
  /**
   * Create three AnalogMeters with different configurations.
   */
  public VoleAnalogMeterExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    Container container = new Container();

    // create the first AnalogMeter
    am1 = new AnalogMeter(0, 10, 30, 30, 0, 70, 90,
                          AnalogMeter.ORIENTATION_LEFT, 10);
    am1.setNumericDisplay(4, 1, null);
            
    container.add(am1);
    container.add(new Label("RPM", 0, 42, 30, 10, 
                            Label.ALIGN_CENTER)); 
  
    // create the second AnalogMeter
    am2 = new AnalogMeter(35, 10, 58, 30, 0, 120, 180, 
                          AnalogMeter.ORIENTATION_CENTER, 20);
    am2.setNumericDisplay(4, 1, null);
    
    container.add(am2);
    container.add(new Label("Volt", 35, 42, 58, 10, 
                            Label.ALIGN_CENTER)); 
    
    // create the third AnalogMeter
    am3 = new AnalogMeter(100, 10, 30, 30, 0, 200, 90,
                          AnalogMeter.ORIENTATION_RIGHT, 10);
    am3.setNumericDisplay(4, 2, null);
    
    container.add(am3);
    container.add(new Label("Ampere", 95, 42, 30, 10,
                            Label.ALIGN_CENTER)); 

    // add a border 
    container.add(new Border("AnalogMeters", 0, 0, 128, 51));
    
    // add the container to the frame
    this.add(container);
    
    // make us visible
    show();
    
    
    // create some random values
    for (;;) {
      am1.setValue(Math.rnd(70));
      am2.setValue(Math.rnd(120));
      am3.setValue(Math.rnd(200));
      
      try {
        ThreadExt.sleep(1000);
      } catch (InterruptedException e) {}
    }    
  }

  // Instantiate and show the VoleAnalogMeterExample
  public static void main(String[] args) {
    new VoleAnalogMeterExample();
  }
}
