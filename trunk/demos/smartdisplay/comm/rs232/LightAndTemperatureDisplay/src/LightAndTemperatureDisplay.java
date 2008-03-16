/*
 * LightAndTemperatureDisplay.java
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

/** import classes used for graphical user interface */
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.TextArea;
import jcontrol.ui.vole.meter.AnalogMeter;
import jcontrol.ui.vole.meter.Thermometer;

/** import I/O classes */
import jcontrol.io.Buzzer;
import jcontrol.comm.RS232;

/**
 * <p>
 * This application runs on a "JControl/SmartDisplay" device. It will display temperature and light
 * sensor information obtained from a RS232 connection. For this, it uses a very simple ASCII-based
 * "protocol" with the communication parameters 19200bps, 8N1.
 *
 * The application expects the sensor value in the following format:
 *   AT#Temp:<TempValue>
 *   AT#Light:<LightValue>
 * Notes:
 * - <TempValue> and <LightValue> will be replaced by the real values from the
 *   sensor measurement.
 * - The temperature will be given in centigrades (10°C = 100 centigrades).
 * - The light will be given in Lux.
 * </p>
 */
public class LightAndTemperatureDisplay extends Frame {

  /** Baud rate used for serial communication */
  private static final int BAUD_RATE = 19200;

  /** String constants used for communication */
  private static final String AT_TEMPERATURE = "Temp";
  private static final String AT_LIGHT = "Light";

  /** Other local variables */
  private Buzzer m_buzzer;
  private RS232 m_rs232;
  private boolean m_lightOverflow;

  public LightAndTemperatureDisplay() {

    /** Draw some borders on the display, used to border the meters */
    Border lmBorder = new Border("Temp.", 0, 0, 40, 54);
    lmBorder.setVisible(false);
    add(lmBorder);
    Border tslBorder = new Border("Light", 42, 0, 86, 54);
    tslBorder.setVisible(false);
    add(tslBorder);

    /** Create a graphical Thermometer */
    Thermometer thermometer = new Thermometer(3, 10, 35, 40, -100, 500);
    thermometer.setCaption("-10", "+50");
    thermometer.setNumericDisplay(3, 1, "\u00b0C");
    thermometer.setValue(0);
    thermometer.setVisible(false);
    add(thermometer);

    /** Create a graphical AnalogMeter, used to display the light value */
    AnalogMeter luxmeter = new AnalogMeter(57, 5, 50, 40, 0, 10000, 130, AnalogMeter.ORIENTATION_CENTER, 20);
    luxmeter.setCaption("Dark", "Bright");
    luxmeter.setNumericDisplay(5, 0, " lx");
    luxmeter.setValue(0);
    luxmeter.setVisible(false);
    add(luxmeter);

    /** Open buzzer */
    m_buzzer = new Buzzer();

    /** Create a TextArea, used to display status information */
    TextArea status = new TextArea(0, 54, 128, 10, false);
    /** Open serial communication interface and display result */
    try {
      m_rs232 = new RS232(BAUD_RATE);
      status.insert(0, "Reading RS232-Data...");
    } catch(IOException e) {
      status.insert(0, "Error while opening RS232!");
      m_buzzer.on(440, 500);
    }
    add(status);

    /** Show all graphical objects */
    show();

    /**
     * The following endless-loop will read the data from the serial interface and
     * display the values by the meters
     */
    for (;;) {
      try {
        /** Read one complete line, terminated by <CR> */
        String line = m_rs232.readLine();

        /** Separates the line into command and value */
        String[] command = separateATCommands(line);
        if (command != null) {
          if (command[0].equals(AT_TEMPERATURE)) {
            /** Display temperature information */
            int temperature = Integer.parseInt(command[1]);
            thermometer.setValue(temperature);
          } else if (command[0].equals(AT_LIGHT)) {
            /** Display light information */
            int light = Integer.parseInt(command[1]);
            luxmeter.setValue(light);
            /** Check if light value overflows */
            if (light > 10000) {
              if (!m_lightOverflow) {
                m_lightOverflow = true;
                status.remove(0);
                status.insert(0, "LUX-value is greater than 10000!");
              }
            } else {
              if (m_lightOverflow) {
                m_lightOverflow = false;
                status.remove(0);
                status.insert(0, "Reading RS232-Data...");
              }
            }
          } else {
            /** Command not understood, so ignore it */
            m_buzzer.on(880, 100);
          }
        }
      } catch (Exception e) {
        /** Exception occured - e.g. caused by an I/O problem. */
        m_buzzer.on(880, 100);
      }
    } // for(;;)
  }

  /**
   * This method separates a received line into command and value, using
   * the following format:
   *   AT#<Command>:<Value>
   * <Command> and <Value> are returned by a String Array, where
   * string[0] is <Command> and string[1] is <Value>.
   */
  private String[] separateATCommands(String line) {
    int colon = line.indexOf(":", 3);
    if (colon > 3) {
      return new String[]{
        /** separate command, from position 3 to colon */
        line.substring(3, colon),
        /** seÃ¼arate value, from colon to end of string */
        line.length()>colon+1?line.substring(colon+1, line.length()):""};
      } else {
        /** return null otherwise */
        return null;
      }
  }

  /**
   * Main method of the application
   */
  public static void main(String[] args) {
    new IntroScreen();
    new LightAndTemperatureDisplay();
  }

}
