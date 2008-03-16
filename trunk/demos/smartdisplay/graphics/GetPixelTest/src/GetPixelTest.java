/*
 * GetPixelTest.java
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

import jcontrol.io.Display;
import jcontrol.io.Resource;
import jcontrol.comm.RS232;

/**
 * A simple program which draws an image to the display, then reads in the
 * pixel data and sends it as ASCII stream over the RS232 port.
 *  
 * @author telkamp
 */
public class GetPixelTest {

	/** baud rate */
	final static int BAUDRATE = 19200;

	public GetPixelTest() {
		RS232 rs232;
		Display d = new Display();
		// init RS232 access
		try {
			rs232 = new RS232(BAUDRATE);
		} catch (IOException e) {
			rs232 = null;
		}
		try {
			d.drawImage(new Resource("jcontrol_logo.jcif"), 0, 0);
		} catch (IOException e) {
		}

		rs232.println();
		int color;
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				color = d.getPixel(x, y);
				if (rs232 != null)
					rs232.print(String.valueOf(color));
				d.setColor(d.getPixel(x, y));
				d.setPixel(x + 30, y);
			}
			rs232.println();
		}

		for (;;) {
		}
	}

	public static void main(String[] args) {
		new GetPixelTest();
	}
}
