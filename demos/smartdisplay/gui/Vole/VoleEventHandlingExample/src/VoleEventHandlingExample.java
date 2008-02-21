/*
 * VoleEventHandlingExample.java
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

import jcontrol.ui.vole.Button;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.event.ActionEvent;
import jcontrol.ui.vole.event.ActionListener;

/**
 * <p>This example demonstrates how to handle
 * events within the GUI framework JControl/Vole.
 * This program needs the image resource
 * 'mouse.jcif'.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleEventHandlingExample 
             extends Frame implements ActionListener {
  // the Label
  Label label;
  // the right Button
  Button button_right;

  /**
   * Create two buttons and a label and add an ActionListener.
   */
  public VoleEventHandlingExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create the Buttons
    Button b1 = new Button("Left Button", 2, 10, 60, 13);
    button_right = new Button("Right Button", 66, 10, 60, 13);
    
    // add the ActionListener
    b1.setActionListener(this);
    button_right.setActionListener(this);
    
    // add the Buttons to the Frame
    this.add(b1);
    this.add(button_right);
    
    // create the Label
    label = new Label("Please press a button!", 0, 30, 128, 10,
                      Label.ALIGN_CENTER);
    this.add(label);
  }
  
  
  /**
   * This is the event handler. When a component fires an 
   * ActionEvent for us, this method is invoked.
   */
  public void onActionEvent(ActionEvent event) {
    // check whether this is a BUTTON_PRESSED event
    if (event.getType() == ActionEvent.BUTTON_PRESSED) {
      
      // recognize event's source by using getActionCommand()
      if (event.getActionCommand().equals("Left Button"))
        label.setLabel("You pressed the left button!", true);
        
      // recognize event's source by using getSource()
      if (event.getSource() == button_right)
        label.setLabel("The right button was hit!", true);
    }
  }
  

  /**
   * Instantiate and show the main frame.
   */
  public static void main(String[] args) {
    new VoleEventHandlingExample().show();
  }
}
