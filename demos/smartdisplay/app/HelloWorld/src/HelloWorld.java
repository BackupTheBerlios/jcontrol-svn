/*
 * HelloWorld.java
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

import jcontrol.io.Display;

/**
 * JControl says "Hello World!"
 */
public class HelloWorld {
  /** LCD access */
  static Display lcd;

  /**
   * standard-constructor
   */  
  public HelloWorld() {
    // lights on!
   	jcontrol.io.Backlight.setBrightness(jcontrol.io.Backlight.MAX_BRIGHTNESS);

    lcd = new Display();
    
    lcd.drawString("Hello World!", 42, 30);
    lcd.drawRect(20, 20, 88, 25);
    
    for (;;) {} // sleep well
  }

  /**
   * main method. Program execution starts here.
   */
  public static void main(String[] args) {
    new HelloWorld();
  }
}
