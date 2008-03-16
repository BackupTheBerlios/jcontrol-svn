/*
 * UIDemo.java
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

import jcontrol.io.Buzzer;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;

/**
 * UIDemo shows how to create a simple graphical user interface
 * with JControl.
 */
public class UIDemo {
  static Keyboard keys;
  static Display lcd;
  static Buzzer buzzer;
  
  /**
   * Inits the UIDemo user interface. Calls UIDemo$MainMenu
   * to draw the main menu and calls the submenu classes
   * on user's command.
   */
  public UIDemo() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    int select = 0;
    
    // init
    init();

    UIDemo$MainMenu mm = new UIDemo$MainMenu();          
    
    // mainloop
    for (;;) {
      mm.draw();
      select = mm.choose(select);
      switch (select) {
        case 0: 
          new UIDemo$ExamplesMenu();
          break;
        case 1: 
          new UIDemo$SystemMenu();
          break;
        default: break;
      }
    }        
  }
  
  
  /**
   * Inits class variables, shows splashscreen
   */
  private void init() {    
    keys =   new Keyboard();      // get keyboard
    lcd =    new Display();       // get display
    buzzer = new Buzzer();        // get buzzer

    // show splashscreen
    try { 
      lcd.drawImage(new Resource("splashscreen.jcif"), 0,0);
    } catch (java.io.IOException e) {}
    
    // wait a while
    try {
      ThreadExt.sleep(1000);
    } catch (InterruptedException e) {}

    lcd.clearDisplay();
  }
  
    
  /**
   * Main method. Program execution starts here.
   */
  public static void main(String[] args) {
    new UIDemo();
  }
  
  
  // subclasses ////////////////////////////////////////////////////////////////
  
  /**
   * Subclass that draws the main menu. 
   */
  class UIDemo$MainMenu {
    /**
     * Draws the background image
     */
    public void draw() {
      try { 
        UIDemo.lcd.drawImage(new Resource("background.jcif"), 0,0);
        UIDemo.lcd.drawString("Main menu", 0, 10);
      } catch (IOException e) {}          
    }
    
    /**
     * Draws the main menu items and lets the user choose. 
     * @param select Item to draw
     * @return int Selected item
     */
    public int choose(int select) {
      boolean selected = false;     
      int entryCount = 2; // number of menu entries
                      
      // trace user choices
      menuloop:for (;;) {
                    
        switch (select) {
          case 0: // examples 
            try {
              UIDemo.lcd.clearRect(54,18,15,5);
              UIDemo.lcd.drawImage(new Resource("examples.jcif"),
                                   14, 24);
              UIDemo.lcd.drawImage(new Resource("big_right.jcif"),
                                   122, 32);
              UIDemo.lcd.drawImage(new Resource("down.jcif"),
                                   54, 58);
            } catch (IOException e) {}
            break;            
          case 1: // system settings
            try {
              UIDemo.lcd.clearRect(54,58,15,5);
              UIDemo.lcd.drawImage(new Resource("syssettings.jcif"),
                                   14, 24);
              UIDemo.lcd.drawImage(new Resource("big_right.jcif"),
                                   122, 32);
              UIDemo.lcd.drawImage(new Resource("up.jcif"),
                                   54, 18);
            } catch (IOException e) {}
            break;            
        }
        
        // wait for keypress
        switch (UIDemo.keys.read()) {
          case 'R': selected = true; break;
          case 'U':
          case 'u': if (select > 0) select--; break;
          case 'L': break;
          case 'D':
          case 'd': if (select < entryCount-1) select++; break;
          case 'S': selected = true; break;
        }
                
        if (selected) 
          break menuloop;                
      }
      return select;      
    }
  }
  
  
  /**
   * Subclass that draws the examples submenu. 
   */
  class UIDemo$ExamplesMenu {
    public UIDemo$ExamplesMenu() {
      int select = 0;
      
      for (;;) {
  
        UIDemo.lcd.clearRect(0,10,90,7);
        UIDemo.lcd.clearRect(1,18,126,45);
        UIDemo.lcd.drawString("Examples", 0, 10);

        select = choose(select);
        switch (select) {
          case 0: // insert code for menu item 1 here
          case 1: // insert code for menu item 2 here
          case 2: // insert code for menu item 3 here
          case 3: // insert code for menu item 4 here
          case 4: // insert code for menu item 5 here
          case 5: // insert code for menu item 6 here
          case 6: // insert code for menu item 7 here
          case 7: // insert code for menu item 8 here
            UIDemo.lcd.clearRect(1,18,126,45);
            UIDemo.lcd.drawString("Selected menu item: ".concat(
                                   String.valueOf(select+1)),
                                  10,20);
            try {
              ThreadExt.sleep(1000);
            } catch (InterruptedException e) {}
          case 8: return;
          default: break;
        }
      }
    }
    

    /**
     * Trace user choices in network menu
     */  
    int choose(int select) {
      int shift = 0, oldselect = -1, oldshift = -1;
      final int images = 8;
      Display lcd = UIDemo.lcd;
          
      mainloop:for (;;) {
        if (select-shift>5) shift = select-5;
        if (select-shift<0) shift = select;
        if (shift!=oldshift) {
          // the icons to choose
          try {
            lcd.drawImage(new Resource("exampleschooser.jcif"),
                          4, 23, 120, 40, 20*shift,0);
          } catch (java.io.IOException e) {}
          if (oldshift==2) {
            // delete obsolete pixels
            lcd.clearRect(1,44,4,19);
            lcd.clearRect(123,44,4,19);
          }
          // left arrow
          try {
            lcd.drawImage(new Resource("left.jcif"), 1, 31);
          } catch (IOException e) {}
          if (shift<(images-6)) {
            // right arrow
            try {
              lcd.drawImage(new Resource("right.jcif"), 124, 31);
            } catch (IOException e) {}
          } else {
            // no arrow
            lcd.clearRect(124,30,3,9);
          }
        }
        if (select!=oldselect) {
          // set selection
          lcd.setDrawMode(Display.XOR);
          if (oldselect!=-1 && oldshift==shift)
            lcd.fillRect(4+(oldselect-shift)*20, 23, 20, 22);
          lcd.fillRect(4+(select-shift)*20, 23, 20, 22);
          oldselect = select;
          lcd.setDrawMode(Display.NORMAL);
          oldshift = shift;
        }
  
        switch (UIDemo.keys.read()) {
          case 'u':
          case 'U':
          case 'L':
            if (select == 0) {
              select = images;
              break mainloop;
            }
            else select--;
            break;
          case 'd':
          case 'D':
          case 'R':
            if (select<images-1) select++;
            break;
          case 'S':
            break mainloop;
        }
      }
      return select;
    }      
  }
  
  
  /**
   * Subclass that draws the system settings submenu. 
   */
  class UIDemo$SystemMenu {
    public UIDemo$SystemMenu() {
      int select = 0;
      
      for (;;) {
  
        UIDemo.lcd.clearRect(0,10,90,7);
        UIDemo.lcd.clearRect(1,18,126,45);
        UIDemo.lcd.drawString("System Settings", 0, 10);                            
          
        select = choose(select);
        switch (select) {
          case 0: // insert code for menu item 1 here
          case 1: // insert code for menu item 2 here
          case 2: // insert code for menu item 3 here
          case 3: // insert code for menu item 4 here
          case 4: // insert code for menu item 5 here
            UIDemo.lcd.clearRect(1,18,126,45);
            UIDemo.lcd.drawString("Selected menu item: ".concat(
                                   String.valueOf(select+1)), 10,20);
            try {
              ThreadExt.sleep(1000);
            } catch (InterruptedException e) {}
          case 5: return;
          default: break;
        }
      }
    }
  
    /**
     * Draws system settings menu and highlights user's choice.
     * @param select preselection
     * @return int selected menu item
     */  
    int choose(int select) {
      int oldselect = -1;
      final int images = 5;
      Display lcd = UIDemo.lcd;

      // draw menu
      try {
        lcd.drawImage(new Resource("syssettingschooser.jcif"),
                      14, 23);
      } catch (java.io.IOException e) {}
      // left arrow
      try {
        lcd.drawImage(new Resource("left.jcif"), 1, 31);
      } catch (IOException e) {}
          
      mainloop:for (;;) {
        if (select!=oldselect) {
          // set selection
          lcd.setDrawMode(Display.XOR);
          if (oldselect!=-1)
            lcd.fillRect(14+oldselect*20, 23, 20, 22);
          lcd.fillRect(14+select*20, 23, 20, 22);
          oldselect = select;
          lcd.setDrawMode(Display.NORMAL);
        }
  
        switch (UIDemo.keys.read()) {
          case 'u':
          case 'U':
          case 'L':
            if (select == 0) {
              select = images;
              break mainloop;
            }
            else select--;
            break;
          case 'd':
          case 'D':
          case 'R':
            if (select<images-1) select++;
            break;
          case 'S':
            break mainloop;
        }
      }
      return select;
    }
  }
}
