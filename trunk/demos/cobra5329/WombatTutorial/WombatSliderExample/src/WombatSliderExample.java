/*
 * WombatSliderExample.java
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
import jcontrol.ui.wombat.Slider;
import jcontrol.ui.wombat.event.ActionEvent;

/**
 * <p>This example demonstrates how to use the component Slider within
 * the GUI framework JControl/Wombat.</p>
 *
 * @author Marcus Timmermann
 */
public class WombatSliderExample extends Frame implements ActionListener {	

	/**
	 * Constructor WombatSliderExample
	 */
	public WombatSliderExample() {
	    // create a container for the content of this frame
	    Container content = new Container();
	    
	    // create borders and add them to the content
	    Border border = new Border("Slider", 70, 40, 180, 140, Border.STYLE_SIMPLE_BORDER);
		content.add(border);
		
		
		/* create all Sliders
		   add them to the content
		   and define an actionlistener for each component */			
		Slider horizontalSlider = new Slider(90, 57, 140, 18, 0, 100, Slider.ORIENTATION_HORIZONTAL);
		horizontalSlider.setEnabled(false);
		content.add(horizontalSlider);
		horizontalSlider.setActionListener(this);
		Slider verticalSlider = new Slider(150, 80, 18, 90, 0, 100, Slider.ORIENTATION_VERTICAL);
		content.add(verticalSlider);
		verticalSlider.setActionListener(this);	    
		
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
		new WombatSliderExample();
	}
}