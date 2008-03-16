/*
 * VoleSliderScrollbarExample.java
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

import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.ScrollBar;
import jcontrol.ui.vole.Slider;

/**
 * <p>This example demonstrates how to use the
 * components Slider and ScrollBar within the
 * GUI framework JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleSliderScrollbarExample extends Frame {
  
  /**
   * Create a Slider and a ScrollBar 
   */
  public VoleSliderScrollbarExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // Create and add the Slider 
    Slider slider = new Slider(5, 30, 80, 0, 20, 1);
    this.add(slider);
    
    // Create and add the ScrollBar
    ScrollBar sb = new ScrollBar(110, 0, 64, 
                                 ScrollBar.ORIENTATION_VERTICAL);
    sb.setValue(30);
    this.add(sb);
    
    // Add a text label
    Label l = new Label("Slider Demo", 5, 10);
    this.add(l); 
  }


  /**
   * Instantiate the VoleSliderScrollbarExample
   */
  public static void main(String[] args) {
    new VoleSliderScrollbarExample().show();
  }
}
