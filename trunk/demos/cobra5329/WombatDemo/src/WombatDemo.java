/*
 * WombatDemo.java
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
 * <p>This demo includes all existing ui components of the Wombat-GUI 
 * and also demonstrate the usage of them all.</p>
 * 
 * Java file created by JControl/IDE
 * 
 * @author Marcus Timmermann
 * @created on 28.02.07 16:38
 */
public class WombatDemo extends Frame {
	
	public static final int BUTTON_PAGE = 0;
	public static final int CHECKBOX_PAGE = 1;
	public static final int RADIOBUTTON_PAGE = 2;
	public static final int SLIDER_PAGE = 3;
	public static final int TOGGLESWITCH_PAGE = 4;
	public static final int KEYPAD_PAGE = 5;
	public static final int LABEL_PAGE = 6;
	public static final int BORDER_PAGE = 7;
	public static final int NUMBERCHOOSER_PAGE = 8;
	public static final int LISTBOX_PAGE = 9;
	public static final int COMBOBOX_PAGE = 10;
	public static final int MULTIIMAGEMENU_PAGE = 11;
	

	/**
	 * Constructor WombatDemo
	 */
	public WombatDemo() {
		setOutline(new MenuPage(this));
		showPage(BUTTON_PAGE);
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
		if (page == BORDER_PAGE) {
			setContent(new BorderPage(this));
		} else if (page == LABEL_PAGE) {
			setContent(new LabelPage(this));
		} else if (page == NUMBERCHOOSER_PAGE) {
			setContent(new NumberChooserPage(this));
		} else if (page == LISTBOX_PAGE) {
			setContent(new ListBoxPage(this));
		} else if (page == COMBOBOX_PAGE) {
			setContent(new ComboBoxPage(this));
		} else if (page == KEYPAD_PAGE) {
			setContent(new KeyPadPage(this));
		} else if (page == TOGGLESWITCH_PAGE) {
			setContent(new ToggleSwitchPage(this));
		} else if (page == SLIDER_PAGE) {
			setContent(new SliderPage(this));
		} else if (page == RADIOBUTTON_PAGE) {
			setContent(new RadioButtonPage(this));
		} else if (page == CHECKBOX_PAGE) {
			setContent(new CheckboxPage(this));
		} else if (page == BUTTON_PAGE) {
			setContent(new ButtonPage(this));
		} else if (page == MULTIIMAGEMENU_PAGE) {
            setContent(new MultiImageMenuPage(this));
        }
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *        The main arguments
	 */
	public static void main(String[] args) {
		new WombatDemo();
	}
}