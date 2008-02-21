/*
 * AnimationExample.java
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
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;

/**
 * <p>Draws an animated picture on the lcd.</p>
 */
public class AnimationExample extends Thread {
  /** number of animation images */  
  final int IMAGE_COUNT = 34;
  /** lcd access */
  Display lcd =  new Display();
    
  /**
   * Thread that continuously draws pictures on the lcd
   * to achieve an animation effect. 
   * @see java.lang.Runnable#run()
   */  
  public void run() {  
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    for (;;) {
      try {
          for (int i=0; i<IMAGE_COUNT; i++) {                                    
              String name = "anim00";
              if (i<10) {
                  name = name.concat("0").concat(String.valueOf(i));
              } else {
                  name = name.concat(String.valueOf(i));
              }
              lcd.drawImage(new Resource(name.concat(".jcif")),32,0);              
              ThreadExt.sleep(100); // wait 100 millis            
          }
        } catch (Exception e) {}                
    }    
  }

  /**
   * Main Method. Program execution starts here.
   */
  public static void main(String[] args) {
    // create and start animation thread
    new AnimationExample().start();    
  }
}
