/*
 * MachineGui.java
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

/**
 * <p>This demo shows a simple ui, based on components of the Wombat-GUI 
 * framework. It is a simple user panel for a machine.</p>
 * 
 * Java file created by JControl/IDE
 * 
 * @author Marcus Timmermann
 * @created on 25.04.07 16:00
 */
public class MachineGui extends Frame {
	public static final int MAIN_PAGE = 0;

	/**
	 * Constructor MachineGui
	 */
	public MachineGui() {
		showPage(MAIN_PAGE);
		setVisible(true);
	}

	/**
	 * This method is used to perform a page switch by replacing the conent
	 * of this frame.
	 * 
	 * @param page
	 *        The page to show, represented by a final field in this class.
	 */
	public void showPage(int page) {
		if (page == MAIN_PAGE) {
			setContent(new MainPage(this));
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *        The main arguments
	 */
	public static void main(String[] args) {
		new MachineGui();
	}
}