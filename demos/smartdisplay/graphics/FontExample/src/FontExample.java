/*
 * FontExample.java
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

/**
 * This example loads a font from the flash memory and 
 * uses it to draw some text onto the screen.
 */
public class FontExample {
  Display lcd;
  
  public FontExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    lcd = new Display();
    
    try {
      // switch to arial 30 point font
      lcd.setFont(new Resource("arial30.jcfd"));
      lcd.drawString("That's big!", 0, 10);
    } catch (IOException e) {}    
      
    // switch back to system font
    lcd.setFont(Display.SYSTEMFONT);
    lcd.drawString("Switched back to system font", 0, 50);
    
    for (;;) {} // sleep well
  }

  public static void main(String[] args) {
    new FontExample();
  }
}
