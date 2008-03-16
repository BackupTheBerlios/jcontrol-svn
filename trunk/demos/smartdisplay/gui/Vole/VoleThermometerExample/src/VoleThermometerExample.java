/*
 * VoleThermometerExample.java
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
import jcontrol.ui.vole.meter.Thermometer;

/**
 * <p>This example demonstrates how to use the
 * component Thermometer within the GUI framework
 * JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleThermometerExample extends Frame {
  Thermometer tm1, tm2;
  
  /**
   * Create three AnalogMeters with different configurations.
   */
  public VoleThermometerExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    Container container = new Container();
    
    // create the first Thermometer
    //
    // NOTE: The String "\u00b0" hereby represents the
    //       UTF8 version of the 'degree' character
    tm1 = new Thermometer(8, 10, 50, 45, 0, 400);
    tm1.setNumericDisplay(5, 1, "\u00b0C");  
    tm1.setCaption("0\u00b0C", "+40\u00b0C");

    container.add(tm1);
    container.add(new Label("Inside Temp.", 8, 55, 50, 8, 
                            Label.ALIGN_CENTER)); 
  
    // create the second Thermometer
    //
    // NOTE: The String "\u00b0" hereby represents the
    //       UTF8 version of the 'degree' character
    tm2 = new Thermometer(68, 10, 50, 45, -300, 500);
    tm2.setNumericDisplay(5, 1, "\u00b0C");
    tm2.setCaption("-30\u00b0C", "+50\u00b0C");
    
    container.add(tm2);
    container.add(new Label("Outside Temp.", 68, 55, 50, 8,
                            Label.ALIGN_CENTER)); 
    
    // add a border 
    container.add(new Border("Thermometers", 0, 0, 128, 64));
    
    // add the container to the frame
    this.add(container);
    
    // make us visible
    show();    
    
    // create some random values
    for (;;) {
      tm1.setValue(Math.rnd(400));
      tm2.setValue(Math.rnd(800)-300);
      
      try {
        ThreadExt.sleep(1000);
      } catch (InterruptedException e) {}
    }    
  }

  // Instantiate and show the VoleAnalogMeterExample
  public static void main(String[] args) {
    new VoleThermometerExample();
  }
}
