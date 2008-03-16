/*
 * VoleFanMeterExample.java
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
import jcontrol.ui.vole.AnimationContainer;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.meter.FanMeter;

/**
 * <p>This example demonstrates how to use the
 * component FanMeter within the GUI framework
 * JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleFanMeterExample extends Frame {
  
  /**
   * Create an animateable Fan and add it to an AnimationContainer 
   */
  public VoleFanMeterExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create two Fans
    FanMeter fan1 = new FanMeter(10, 10);
    fan1.setCaption("The 1st Fan");
    fan1.setNumericDisplay(5,0,"RPM");
    FanMeter fan2 = new FanMeter(10, 30);
    fan2.setCaption("Another Fan");
    fan2.setNumericDisplay(5,0,"RPM");
    
    // create a Border around them
    this.add(new Border("The Fans", 5, 0, 70, 50));
    
    // create an AnimationContainer and add the Fans
    AnimationContainer ac = new AnimationContainer();
    ac.add(fan1);
    ac.add(fan2);
    
    // add the AnimationContainer to the Frame
    this.add(ac);
    show();
    
    // create some random values
    for (;;) {
      fan1.setValue(4800+Math.rnd(500));
      fan2.setValue(3400+Math.rnd(200));
      try {
        ThreadExt.sleep(1000);
      } catch (InterruptedException e) {}
    }
  }

  /**
   * Instantiate the VoleFanExample
   */
  public static void main(String[] args) {
    new VoleFanMeterExample();
  }
}
