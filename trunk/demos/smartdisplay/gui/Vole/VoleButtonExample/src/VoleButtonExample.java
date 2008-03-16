/*
 * VoleButtonExample.java
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

import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Button;
import jcontrol.ui.vole.CheckBox;
import jcontrol.ui.vole.Container;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.RadioButton;

/**
 * <p>This example demonstrates how to use buttons
 * within the GUI framework JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleButtonExample extends Frame {
  
  /**
   * Create different kinds of buttons.
   */
  public VoleButtonExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create a simple button and add it to the frame
    Button simpleButton = new Button("Press me!", 30, 45, 65, 12);
    this.add(simpleButton);
    
    // create a Container with three RadioButtons 
    // and a Border around it
    Container c1 = new Container();
        
    RadioButton rb1 = new RadioButton("Radio 1", 5, 8);
    RadioButton rb2 = new RadioButton("Radio 2", 5, 18);
    RadioButton rb3 = new RadioButton("Radio 3", 5, 28);
    
    // add the RadioButtons to the Container    
    c1.add(rb1);
    c1.add(rb2);
    c1.add(rb3);
    
    // add a Border
    c1.add(new Border("RadioButtons", 0, 0, 60, 40));  
    
    // add the Container to the Frame
    this.add(c1); 
    
    
    // create a Container with two CheckBoxes and a Border around it
    Container c2 = new Container();

    CheckBox cb1 = new CheckBox("Check 1", 69, 10);
    CheckBox cb2 = new CheckBox("Check 2", 69, 23);

    // add the CheckBoxes to the Container    
    c2.add(cb1);
    c2.add(cb2);
    
    // add a Border
    c2.add(new Border("CheckBoxes", 64, 0, 60, 40));
    
    // add the second Container to the Frame
    this.add(c2); 
  }

  /**
   * Instantiate the VoleButtonExample.
   */
  public static void main(String[] args) {
    VoleButtonExample vbe = new VoleButtonExample();
    // make the Frame visible
    vbe.show();
  }
}
