/*
 * VoleAnalogClockExample.java
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

import jcontrol.lang.ThreadExt;
import jcontrol.system.Time;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.meter.AnalogClock;

/**
 * <p>This example demonstrates how to use the
 * component AnalogClock within the GUI framework
 * JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleAnalogClockExample extends Frame {
  
  /**
   * Create and continuosly update an AnalogClock
   */
  public VoleAnalogClockExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create a new AnalogClock
    AnalogClock ac = new AnalogClock(35, 7, 26, true);
    this.add(ac);
    
    // add a border
    this.add(new Border("Analog Clock", 26, 0, 70, 64));
    
    // make us visible
    show();
    
    // update the AnalogClock's time once a second 
    for (;;) {
      Time t = new Time();
      ac.setValue(t.hour, t.minute, t.second);
      try { 
        ThreadExt.sleep(1000); 
      } catch (InterruptedException e) {}
    }      
  }

  /**
   * Instantiate the example
   */
  public static void main(String[] args) {
    new VoleAnalogClockExample();
  }
}
