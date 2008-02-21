/*
 * VoleMultiImageMenuExample.java
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
import jcontrol.ui.vole.menu.MultiImageMenu;

/**
 * <p>This example demonstrates how to use the
 * component MultiImageMenu within the GUI framework
 * JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleMultiImageMenuExample extends Frame {
  
  /**
   * Create and show a BigImageMenu
   */
  public VoleMultiImageMenuExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create a big BigImageMenu
    MultiImageMenu menu = new MultiImageMenu(0, 10, 128, 38, 6);
    
    // add some menu items
    menu.addMenuItem("menuimage1.jcif");
    menu.addMenuItem("menuimage2.jcif");
    menu.addMenuItem("menuimage3.jcif");
    menu.addMenuItem("menuimage4.jcif");
    menu.addMenuItem("menuimage5.jcif");
    menu.addMenuItem("menuimage6.jcif");
    menu.addMenuItem("menuimage7.jcif");
    menu.addMenuItem("menuimage8.jcif");
    menu.addMenuItem("menuimage9.jcif");
    
    // show the menu
    setMenu(menu);
    show();
  }

  /**
   * Instantiate the VoleBigImageMenuExample
   */
  public static void main(String[] args) {
    new VoleMultiImageMenuExample();
  }
}
