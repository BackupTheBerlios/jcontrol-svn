/*
 * RS232Example.java
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

import jcontrol.comm.DisplayConsole;
import jcontrol.comm.RS232;
import jcontrol.io.Display;

/**
 * <p>RS232Example shows how to write a RS232 terminal
 * for JControl using the API classes <code>RS232</code>
 * and <code>DisplayConsole</code>.</p>
 * 
 * (C) DOMOLOGIC Home Automation GmbH 2003
 */
public class RS232Example {
  /** RS232 access */
  RS232 rs232;
  /** DisplayConsole */
  DisplayConsole console;
  
  /** baud rate */
  final static int BAUDRATE = 19200;   

  /**
   * Init and start the RS232 console.
   */
  public RS232Example() {    
    // init RS232 access
    try {
      rs232 = new RS232(BAUDRATE);
    } catch (IOException e) {
      new Display().drawString("IOError!",0,0);
    }
    
    // init DisplayConsole
    console = new DisplayConsole();
    
    // say hello
    console.println("JControl RS232 Terminal ready.");
    
    // start RS232 terminal
    terminal();
  }

  /**
   * The RS232 terminal implementation
   */  
  void terminal() {
    String s;
    
    // continuously receive from RS232 and write to lcd
    for (;;) {
      try {
        s = rs232.readLine();
        console.println(s);
      } catch (IOException e) {
        // reset errorflag
        rs232.errorCode();
      }      
    }
  }

  /**
   * Main method. Program execution starts here.
   */
  public static void main(String[] args) {
    new RS232Example();
  }
}
