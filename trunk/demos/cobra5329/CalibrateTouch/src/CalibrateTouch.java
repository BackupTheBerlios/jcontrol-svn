/*
 * CalibrateTouch.java
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
 * <p>Demonstrate the usage of the JControl/Wombat GUI and especially 
 * how to calibrate the touch screen by getting 4 raw reference point 
 * values, calibrating by these and testing the result.</p>
 * 
 * @author roebbenack
 */
public class CalibrateTouch extends Frame {       

	public CalibrateTouch() {
		setContent(new TouchCalibPage(this));
		setVisible(true);
	}

	/**
	 * The main method.
	 */
	public static void main(String[] args) {
		new CalibrateTouch();
	}
}