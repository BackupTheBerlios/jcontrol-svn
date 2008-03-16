/*
 * ImageExample.java
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
 * This example loads an image from the flash memory
 * and paints it onto the screen.
 */
public class ImageExample {
  Display lcd;
  
  public ImageExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    lcd = new Display();
    
    try {
      lcd.drawImage(new Resource("Malerei.jcif"), 0, 0);
    } catch (IOException e) {}
    
    for (;;) {}
  }

  public static void main(String[] args) {
    new ImageExample();
  }
}
