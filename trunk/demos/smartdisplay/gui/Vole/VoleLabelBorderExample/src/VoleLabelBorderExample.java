/*
 * VoleLabelBorderExample.java
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
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;

/**
 * <p>This example demonstrates how to use the
 * components List and TextArea within the GUI
 * framework JControl/Vole.
 * This program needs the image resource
 * 'mouse.jcif'.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleLabelBorderExample extends Frame {
  Label imageLabel;
  
  /**
   * Create a few Labels and Borders.
   */
  public VoleLabelBorderExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create an image Label
    try {
      Resource img = new Resource("mouse.jcif");
      imageLabel = new Label(img, 5, 10);
    } catch (IOException e) {}
    
    Border b = new Border("Mouse", 0, 0, 50, 60);

    // add mouse and border to the frame
    this.add(imageLabel);
    this.add(b);
        
    // now create some text labels with different alignments
    this.add(new Label("Programming", 60, 15));
    this.add(new Label("with VOLE", 60, 25, 60, 10,
                       Label.ALIGN_CENTER));
    this.add(new Label("is really", 60, 35));
    this.add(new Label("simple!", 60, 45, 60, 10,
                       Label.ALIGN_RIGHT));
    
    // at last, add a 2nd border
    this.add(new Border("Information", 52, 0, 75, 60));
  }


  /**
   * Instantiate and show the VoleLabelBorderExample
   */
  public static void main(String[] args) {
    new VoleLabelBorderExample().show();
  }
}
