/*
 * VoleComboNumberExample.java
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

import jcontrol.system.Time;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.ComboBox;
import jcontrol.ui.vole.Container;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.NumberChooser;

/**
 * <p>This example demonstrates how to use the
 * components ComboBox and NumberChooser within
 * the GUI framework JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleComboNumberExample extends Frame {
  
  /**
   * Create Vole ComboBox and NumberChooser UI elements.
   */
  public VoleComboNumberExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create a new ComboBox
    ComboBox cb = new ComboBox(10,10);

    // add some entries
    cb.add("Vole");
    cb.add("Mole");
    cb.add("Tiger");
    cb.add("Lion");
    cb.add("Elephant");
    cb.add("Tyrannosaurus Rex");
    
    // add the box to our Frame
    this.add(cb);
    
    
    // create some NumberChoosers and a Container to hold them
    Container c = new Container();
    
    NumberChooser nc1 = new NumberChooser(10,40,1,31);
    NumberChooser nc2 = new NumberChooser(35,40,1,12);
    NumberChooser nc3 = new NumberChooser(60,40,2000,2099);
    
    // set current date
    Time t = new Time();
    nc1.setValue(t.day);
    nc2.setValue(t.month);
    nc3.setValue(t.year);    
    
    // add the NumberChoosers to the Container
    c.add(nc1);
    c.add(nc2);
    c.add(nc3);
    
    // add the Container to the frame
    this.add(c);
    
    
    // nice borders
    this.add(new Border("ComboBox", 0,0,128,25));
    this.add(new Border("Current Date", 0,30,128,25));
  }


  public static void main(String[] args) {
    new VoleComboNumberExample().show();
  }
}
