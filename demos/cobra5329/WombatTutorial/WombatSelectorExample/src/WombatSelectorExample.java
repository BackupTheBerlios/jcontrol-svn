/*
 * WombatSelectorExample.java
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
import jcontrol.ui.wombat.Frame;
import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.ComboBox;
import jcontrol.ui.wombat.NumberChooser;
import jcontrol.ui.wombat.menu.MultiImageMenu;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.event.ActionEvent;

/**
 * <p>This example demonstrates how to use the
 * components ComboBox, NumberChooser and MultiImageMenu within
 * the GUI framework JControl/Wombat.</p>
 *
 * @author Marcus Timmermann
 */
public class WombatSelectorExample extends Frame implements ActionListener {	

	/**
	 * Constructor WombatSelectorExample
	 */
	public WombatSelectorExample() {
		// create a container for the content of this frame
	    Container content = new Container();
	    
	    // create borders and add them to the content
	    Border border1 = new Border("ComboBox", 60, 40, 180, 40, Border.STYLE_ETCHED_BORDER);
		content.add(border1);
		Border border2 = new Border("NumberChooser", 60, 80, 180, 40, Border.STYLE_ETCHED_BORDER);
		content.add(border2);
		Border border3 = new Border("MultiImageMenu", 60, 120, 180, 70, Border.STYLE_ETCHED_BORDER);
		content.add(border3);
		
		/* create all components
		   add them to the content
		   and define an actionlistener for each component */
		
		// ComboBox
		ComboBox comboBox = new ComboBox(new String[]{"Entry 1", "Entry 2", "Entry 3"}, 110, 56, 80);
		content.add(comboBox);
		comboBox.setActionListener(this);
		
		// NumberChooser
		NumberChooser numberChooserDay = new NumberChooser(112, 96, 1, 31);
		numberChooserDay.setValue(1);
		content.add(numberChooserDay);
		numberChooserDay.setActionListener(this);
		NumberChooser numberChooserMonth = new NumberChooser(136, 96, 1, 12);
		numberChooserMonth.setValue(1);
		content.add(numberChooserMonth);
		numberChooserMonth.setActionListener(this);
		NumberChooser numberChooserYear = new NumberChooser(160, 96, 2000, 2100);
		numberChooserYear.setValue(1);
		content.add(numberChooserYear);
		numberChooserYear.setActionListener(this);
		
		// MultiImageMenu
		MultiImageMenu multiImageMenu = new MultiImageMenu(70, 136, 160, 44, 3, 1, MultiImageMenu.STYLE_SHOW_BORDER);
		try {
			multiImageMenu.setImageItems(new Resource[]{new Resource("item1.jcif"), new Resource("item2.jcif"), new Resource("item3.jcif")});
		} catch(IOException ioe) {}
		multiImageMenu.setBackgroundColor(Color.WHITE);
		content.add(multiImageMenu);
		multiImageMenu.setActionListener(this);
		
		// set the content to this frame
        setContent(content);
        // finally, make the frame visible		
		setVisible(true);
	}

	/**
	 * This method is called every time any component declared above fires an
	 * action event.
	 * 
	 * @param e the ActionEvent
	 */
	public void onActionEvent(ActionEvent e) {
		// add some code if you want to 
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *        The main arguments
	 */
	public static void main(String[] args) {
		new WombatSelectorExample();
	}
}