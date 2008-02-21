/*
 * WombatListTextviewerExample.java
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
import jcontrol.ui.wombat.TextViewer;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.ListBox;
import jcontrol.ui.wombat.event.ActionEvent;


/**
 * <p>This example demonstrates how to use the
 * components ListBox and TextViewer within the GUI
 * framework JControl/Wombat.</p>
 *
 * @author Marcus Timmermann
 */
public class WombatListTextviewerExample extends Frame implements ActionListener  {
	/**
	 * Constructor WombatListTextviewerExample
	 */
	public WombatListTextviewerExample() {
		// create a container for the content of this frame
	    Container content = new Container();
	    try {
	        // set a larger Font to the content, all components in the content will
	        // get this font, too
			content.setFont(new Resource("Arial.jcfd"));
		} catch(IOException ioe) {}		
		
		// create a TextViewer
	    TextViewer textViewer = new TextViewer("The TextViewer can show any text you want.\nLine-breaks are performed autmatically.", 70, 40, 180, 70, TextViewer.STYLE_SHOW_SCROLLBAR);
		content.add(textViewer);
		
		// create a ListBox
		ListBox listBox = new ListBox(new String[]{"North", "Eath", "South", "West", "North-Eath", "South-West", "South-Eath", "North-West"}, 70, 110, 180, 70, ListBox.STYLE_SHOW_SCROLLBAR);
		content.add(listBox);
		listBox.setActionListener(this);
		
		// set the content to this frame
        setContent(content);
        // finally, make the frame visible		
		setVisible(true);
	}

	/**
	 * This method is called every time an item in the listbox declared above is selected
	 * be the user.
	 * 
	 * @param e the ActionEvent fired by the listbox
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
		new WombatListTextviewerExample();
	}
}