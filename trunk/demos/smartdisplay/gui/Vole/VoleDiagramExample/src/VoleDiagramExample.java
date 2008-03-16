/*
 * VoleDiagramExample.java
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
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.graph.Diagram;

/**
 * <p>This example demonstrates how to use the
 * component Diagram within the GUI framework
 * JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleDiagramExample extends Frame {
  
  /**
   * Create a Diagram and fill it with some random values.
   */
  public VoleDiagramExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create the Diagram
    Diagram dia = new Diagram(0, 10, 128, 40, 0, 20, 30);
    dia.setCaption("0", "20", Diagram.ALIGN_LEFT);
    add(dia);
    
    // add a Label
    add(new Label("Diagram", 0, 52, 128, 10, Label.ALIGN_CENTER));
    
    // show the frame
    show();
    
    // generate random values and pass them to the Diagram
    for (;;) {
      dia.setValue(Math.rnd(20));
      // sleep 
      try {
        ThreadExt.sleep(500);
      } catch (InterruptedException e) {}
    }    
  }

  /**
   * Create an instance of the VoleDiagramExample
   */
  public static void main(String[] args) {
    new VoleDiagramExample();
  }
}
