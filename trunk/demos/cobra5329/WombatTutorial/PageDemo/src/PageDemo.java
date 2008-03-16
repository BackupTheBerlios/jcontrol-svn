/*
 * PageDemo.java
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
 * <p>Contains the base of all following WombatTutorials that constructive 
 * on it. This demo creates one frame with an outline and 2 possible contents. 
 * Please look at the tutorials for detailed informations.</p>
 * 
 * Java file created by JControl/IDE
 * 
 * @author Marcus Timmermann
 * @created on 01.03.07 15:44
 */
public class PageDemo extends Frame {
	
	public static final int CONTENT_1 = 0;
	public static final int CONTENT_2 = 1;

	/**
	 * Constructor LayoutProject
	 */
	public PageDemo() {
		setOutline(new Outline(this)); // specify the outline
		showPage(CONTENT_1); // the initial page
		setVisible(true);    // show the frame
	}

	/**
	 * This method is used to perform a page switch by replacing the conent
	 * of this frame.
	 * 
	 * @param page
	 *        The page to show, represented by a final field in this class.
	 */
	public void showPage(int page) {
		if (page == CONTENT_1) {
			setContent(new Content1(this));
		} else if (page == CONTENT_2) {
			setContent(new Content2(this));
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *        The main arguments
	 */
	public static void main(String[] args) {
		new PageDemo();
	}
}