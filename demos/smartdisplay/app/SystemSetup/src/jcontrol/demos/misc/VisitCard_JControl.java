/*
 * VisitCard_JControl.java
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

package jcontrol.demos.misc;

import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.system.Management;
import jcontrol.ui.vole.AnimationContainer;
import jcontrol.ui.vole.TextScroller;

/**
 * Displays an electronic business card.
 * @author Marcus Timmermann
 */
public class VisitCard_JControl {

    /**
     * Program entry point.
     * @param args (not used)
     */
    public static void main(String[] args) {
        go(new Display(), new Keyboard());
    }

    /**
     * Main program. Just displays some graphics, scrolls some text and waits
     * for keyboard events.
     * @param lcd  the Display to use
     * @param keys the Keyboard to use
     */
    public static void go(Display lcd, Keyboard keys) {
      lcd.clearDisplay();
      String   TITLE  = "JControl - 8bit Java VM";
      String[] TEXT   = {
        Management.getProperty("profile.name"),
        "...",
        "Domologic",
        "Home-Automation GmbH",
        "...",
        "Department E.I.S.",
        "TU Braunschweig",
        "...",
        "- Developers -",
        "Dipl.Ing. Helge Böhme",
        "Dipl.-Inform. Gerrit Telkamp",
        "Dipl.-Inform. Wolfgang Klingauf",
        "Andreas Wesseler",
        "Ralf Strohmeyer",
        "Marcus Timmermann",
        ""
      };
      try{
        lcd.drawImage(new Resource("jcontrol_s-w_klein.jcif"),0,0);
        lcd.drawImage(new Resource("small_logo.jcif"),105,0);
      } catch(java.io.IOException e) {}
      lcd.fillRect(0, 21, 128, 8);
      lcd.setDrawMode(Display.XOR);
      lcd.drawRect(0, 29, 128, 34);
      lcd.setFont(Display.SYSTEMFONT);
      lcd.drawString(TITLE, 2, 22);
      lcd.setDrawMode(Display.NORMAL);
      char c;
      int speed=50, step = 1;
      TextScroller ts = new TextScroller(TEXT,1,30,126,32,TextScroller.ALIGN_CENTER);
      AnimationContainer ac = new AnimationContainer();
      ac.setGraphics(lcd);
      ac.add(ts);
      ac.setVisible(true);
      do {
        c = keys.read();
        switch (c) {
          case 'L':
          case 'U':
            if (step > 1 || step < 0) {
              if (step < 0 && speed == 0)
                step--;
              else if (speed > 0)
                speed -= 50;
              else if (step > 1)
                step--;
            }
            else if (speed < 300) speed+=50;
            else 
              step = -1;
            if (step < -lcd.getFontHeight())
              step = -lcd.getFontHeight();
            break;
          case 'R':
          case 'D':
            if (step < -1 || step > 0) {
              if (step < -1 && speed == 0)
                step++;
              else if (speed > 0)
                speed -= 50;
              else if (step > 0)
                step++;
            }
            else if (speed < 300) speed+=50;
            else
              step = 1;
            if (step > lcd.getFontHeight())
              step = lcd.getFontHeight();
            break;
        }
        ac.setInterval(speed);
        ts.setStep(step);
      } while (c != 'S');
      
      ac.removeAll();
    }
}
