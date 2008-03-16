/*
 * WombatLabelBorderExample.java
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
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.graphics.Color;

/**
 * <p>This example demonstrates how to use the
 * components Label and Border within the GUI
 * framework JControl/Wombat.
 * This program needs the image resource
 * 'smile.jcif'.</p>
 *
 * @author Marcus Timmermann
 */
public class WombatLabelBorderExample extends Frame  {	

	/**
	 * Constructor WombatLabelBorderExample
	 */
	public WombatLabelBorderExample() {
		// create a container for the content of this frame
	    Container content = new Container();
	    
	    // create some borders of different kind
	    Border simpleBorder = new Border("Simple Border", 70, 30, 160, 50, Border.STYLE_SIMPLE_BORDER);
		content.add(simpleBorder);
		Border roundBorder = new Border("Round Border", 70, 80, 160, 50, Border.STYLE_ROUND_BORDER);
		content.add(roundBorder);
		Border etchedBorder = new Border("Etched Border", 70, 130, 160, 50, Border.STYLE_ETCHED_BORDER);
		content.add(etchedBorder);
		
		// create some labels of different kind
		try {
		    Label label1 = new Label(new Resource("smile.jcif"), 140, 47, 0, 0, Label.STYLE_ALIGN_LEFT);				
		    content.add(label1);
		} catch(IOException ioe) {
			// must be caught as the Resource-constructor may throw this exception
		}	    
		Label label2 = new Label("Hello World!", 120, 97, 60, 20, Label.STYLE_ALIGN_CENTER);
		label2.setForegroundColor(Color.WHITE);
		label2.setBackgroundColor(Color.RED);
		content.add(label2);
		Label label3 = new Label("Hello World!", 120, 147, 60, 20, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		label3.setBackgroundColor(Color.WHITE);		
		content.add(label3);
		
		// set the content to this frame
        setContent(content);
        // finally, make the frame visible		
		setVisible(true);
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *        The main arguments
	 */
	public static void main(String[] args) {
		new WombatLabelBorderExample();
	}
}