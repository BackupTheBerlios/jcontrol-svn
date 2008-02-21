/*
 * WombatButtonExample.java
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
import jcontrol.ui.wombat.RadioButton;
import jcontrol.ui.wombat.CheckBox;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.ToggleSwitch;
import jcontrol.ui.wombat.event.ActionEvent;

/**
 * <p>This example demonstrates how to use buttons and similar components
 * within the GUI framework JControl/Wombat.</p>
 *
 * @author Marcus Timmermann
 */
public class WombatButtonExample extends Frame implements ActionListener {
	

	/**
	 * Constructor WombatButtonExample
	 */
	public WombatButtonExample() {
	    // create a container for the content of this frame
	    Container content = new Container();
	    
	    // create borders and add them to the content
	    Border border1 = new Border("Radiobuttons", 40, 40, 120, 80, Border.STYLE_ETCHED_BORDER);
		content.add(border1);
		Border border2 = new Border("Checkboxes", 160, 40, 120, 80, Border.STYLE_ETCHED_BORDER);
		content.add(border2);
		Border border3 = new Border("Buttons", 40, 120, 120, 80, Border.STYLE_ETCHED_BORDER);
		content.add(border3);
		Border border4 = new Border("Toggleswitches", 160, 120, 120, 80, Border.STYLE_ETCHED_BORDER);
		content.add(border4);
		
		/* create all components
		   add them to the content
		   and define an actionlistener for each component */
		
		// Radiobuttons
		RadioButton radioButton1 = new RadioButton("RadioButton 1", 65, 56, 0, 0);
		content.add(radioButton1);
		radioButton1.setActionListener(this);
		RadioButton radioButton2 = new RadioButton("RadioButton 2", 65, 76, 0, 0);
		content.add(radioButton2);
		radioButton2.setActionListener(this);
		RadioButton radioButton3 = new RadioButton("RadioButton 3", 65, 96, 0, 0);
		content.add(radioButton3);		
		radioButton3.setActionListener(this);
		
		// Checkboxes
		CheckBox checkBox1 = new CheckBox("CheckBox 1", 190, 56, 0, 0);
		content.add(checkBox1);
		checkBox1.setActionListener(this);
		CheckBox checkBox2 = new CheckBox("CheckBox 2", 190, 76, 0, 0);
		content.add(checkBox2);
		checkBox2.setActionListener(this);
		CheckBox checkBox3 = new CheckBox("CheckBox 3", 190, 96, 0, 0);
		content.add(checkBox3);
		checkBox3.setActionListener(this);
		
		// Buttons
		Button button1 = new Button("Press me!", 57, 142, 90, 20);
		content.add(button1);
		button1.setActionListener(this);
		Button button2 = new Button("Push the button", 57, 162, 90, 20);
		content.add(button2);
		button2.setActionListener(this);
		
		// Toggleswitches
		ToggleSwitch toggleSwitch1 = new ToggleSwitch(190, 140);
		toggleSwitch1.setText("On", "Off");
		content.add(toggleSwitch1);
		toggleSwitch1.setActionListener(this);
		ToggleSwitch toggleSwitch2 = new ToggleSwitch(220, 140);
		toggleSwitch2.setText("On", "Off");
		content.add(toggleSwitch2);
		toggleSwitch2.setActionListener(this);
		/* */
		
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
		new WombatButtonExample();
	}
}