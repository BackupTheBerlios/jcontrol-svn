/*
 * VoleDigitalMeterExample.java
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

import java.io.IOException;

import jcontrol.io.Resource;
import jcontrol.lang.Math;
import jcontrol.lang.ThreadExt;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.meter.DigitalMeter;
import jcontrol.ui.vole.meter.SevenSegmentMeter;

/**
 * <p>This example demonstrates how to use the
 * component DigitalMeter within the GUI framework
 * JControl/Vole.
 * This example needs the font resource
 * "arial24.jcfd".</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleDigitalMeterExample extends Frame {
  
  public VoleDigitalMeterExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create the DigitalMeter
    DigitalMeter dm = new DigitalMeter(13, 20, 3, 1);

    // set the Font of the DigitalMeter
    try {
      dm.setFont(new Resource("arial24.jcfd"));
    } catch (IOException e) {}

    // add the DigitalMeter to the Frame
    this.add(dm);
    
    // create the LCDMeter
    SevenSegmentMeter sm = new SevenSegmentMeter(70, 10, 50,
                                                 50, 2, 0);
    this.add(sm);
    
    // make some pretty borders
    this.add(new Border("DigitalMeter", 0, 0, 63, 60));
    this.add(new Border("SevenSegment", 64, 0, 63, 60));
    
    // make the frame visible
    show();
    
    
    // produce some random values
    for (;;) {
      dm.setValue(Math.rnd(2000)-1000);
      sm.setValue(Math.rnd(150)-50);
      try{ ThreadExt.sleep(1000); }catch(InterruptedException e){}
    }
  }

  // start the demonstration
  public static void main(String[] args) {
    new VoleDigitalMeterExample();
  }
}
