/*
 * LightTemperatureIntro.java
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

import jcontrol.comm.RS232;
import jcontrol.lang.ThreadExt;

/**
 * Intro text for the LightTemperatureDemo.
 * 
 * @author Remi Seiler, Thomas Röbbenack
 */
public class LightTemperatureIntro {

	public LightTemperatureIntro() {
		try {
			RS232 rs232 = new RS232();
			rs232.println("Welcome!");
			rs232.println("Please connect one or both of the");
			rs232.println("sensors that came with your evaluation kit");
			rs232.println("to the evaluation board's I2C connectors");

			TMP75 tmp = null;
			TSL2561 tsl = null;

			intro: for (;;) {
				if (tmp == null) {
					tmp = new TMP75(0x9e);
				}
				if (tmp != null) {
					try {
						tmp.getTemp();
					} catch (IOException e1) {
						tmp = null;
					}
				}
				if (tsl == null) {
					try {
						tsl = new TSL2561(0x72);
					} catch (IOException e2) {
					}
				}
				if (tsl != null) {
					try {
						tsl.getChannel0();
					} catch (IOException e) {
						tsl = null;
					}
				}

				if (tmp != null || tsl != null) {
					break intro;
				}

				try {
					ThreadExt.sleep(1);
				} catch (InterruptedException e) {
				}
			}
			if (tmp != null) {
				rs232.println("TMP75 detected.");
			}
			if (tsl != null) {
				rs232.println("TSL2651 detected.");
			}
		} catch (IOException e) {
		}
	}
}
