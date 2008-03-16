/*
 * VoleListTextareaExample.java
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
import jcontrol.ui.vole.List;
import jcontrol.ui.vole.TextArea;

/**
 * <p>This example demonstrates how to use the
 * components List and TextArea within the GUI
 * framework JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleListTextareaExample extends Frame {
  
  /**
   * Create a List and a TextArea.
   */
  public VoleListTextareaExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create a list with some entries
    List l = new List(0, 0, 128, 31, true);
    l.add("North");
    l.add("South");
    l.add("East");
    l.add("West");
    l.add("North-East");
    l.add("North-West");
    l.add("South-East");
    l.add("South-West");
    this.add(l);
    
    // create a TextArea
    TextArea ta = new TextArea(new String[]{"The TextArea", 
                                             "can be filled",
                                             "up with nonsense",
                                             "and nobody really",
                                             "cares about it."},
                               0, 32, 128, 32, true);
    this.add(ta);
  }

  /**
   * Instantiate the VoleListTextareaExample
   */
  public static void main(String[] args) {
    new VoleListTextareaExample().show();
  }
}
