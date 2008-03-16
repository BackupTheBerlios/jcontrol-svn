/*
 * IntroScreen.java
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
import jcontrol.io.Backlight;

import jcontrol.comm.RS232;
import jcontrol.lang.ThreadExt;

/**
 * <p>
 * Intro screen for the LightAndTemperatureDisplay.
 * 
 * This application runs on a "JControl/SmartDisplay" device. It will show a simple "Splash Screen" 
 * with some text notes and waits for the first characters coming from the serial interface.
 * </p>
 */
public class IntroScreen{

  /** Baud rate used for serial communication */
  private static final int BAUD_RATE = 19200;

  private RS232 m_rs232;

  public IntroScreen() {
    /** Initialize display and draw a welcome message */
    Display d = new Display();
    d.drawString("Welcome!", 2, 5);

    /** Initialize RS232 interface and give a simple message to the user */
    try {
      m_rs232 = new RS232(BAUD_RATE);
      d.drawString("Waiting for RS232 input!", 2, 25);
    } catch(IOException e) {
      d.drawString("Error while opening RS232!", 2, 25);
    }

    /** Draw a simple JControl logo on the screen */
    try {
      d.drawImage(new Resource("jcontrol_small.jcif"), 100, 0);
    } catch (IOException e) {}

    /** Turn on the backlight */
    Backlight.setBrightness(Backlight.MAX_BRIGHTNESS);

    /** Wait for a character, received by the serial interface */
    if (m_rs232 != null) {
      while (m_rs232.available() == 0) {
        try {
          ThreadExt.sleep(1);
        } catch (InterruptedException e) {
        }
      };
      d.clearDisplay();

      if (m_rs232 != null) {
        m_rs232.close();
      }
    }
  }

}
