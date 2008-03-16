/*
 * VoleMenuDemo.java
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
import jcontrol.ui.vole.event.ActionEvent;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.menu.MenuBar;

/**
 * (C) DOMOLOGIC Home Automation GmbH 2003
 *
 * @author Marcus Timmermann
 * @version 1.0
 *
 */
public class VoleMenuDemo extends Frame {

    int multiindex = 0;
    int bigindex = 0;
    int textindex = 0;

    public VoleMenuDemo() {
	    // lights on!
	    jcontrol.io.Backlight.setBrightness(255);
	
        MenuBar menu = new MenuBar(0, 0, 128, 64, MenuBar.ALIGN_BOTTOM);

        try {
            menu.setFont(new Resource("arial14.jcfd"));
        } catch (IOException e) {
        }

        menu.addMenuItem("Punkt 1");
        menu.addMenuItem("Punkt 2");
        menu.addMenuItem("Punkt 3");
        menu.addMenuItem("Punkt 4");
        menu.addMenuItem("Punkt 5");
        menu.addMenuItem("Punkt 6");
        menu.addMenuItem("Punkt 7");
        menu.addMenuItem("Punkt 8");
        menu.addMenuItem("Punkt 9");
        menu.addMenuItem("Punkt 10");
        menu.addMenuItem("Punkt 11");
        menu.addMenuItem("Punkt 12");
        menu.addMenuItem("Punkt 13");
        menu.addMenuItem("Punkt 14");
        menu.addMenuItem("Punkt 15");
        this.setMenu(menu);
        menu.enableMenuItem("Punkt 3", false);

        setVisible(true);

    }

   /**
   * Handle ActionEvents.
   * @param event the ActionEvent
   */
    public void onActionEvent(ActionEvent event) {
        // check whether a menu item was selected
        Object o = event.getSource();
    }

    public static void main(String[] args) {
        (new VoleMenuDemo()).setVisible(true);
    }

}
