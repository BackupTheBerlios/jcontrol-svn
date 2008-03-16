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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */
import jcontrol.comm.RS232;
import java.io.IOException;

/**
 * JControl says "Hello World!"
 */
public class HelloWorld {

  /**
   * standard constructor
   */
  public HelloWorld() {

    // open RS232 communication channel with 19200bps
    try {
	    
	    RS232 rs=new RS232(19200);
	    
	    // print text to console
    	rs.println("Hello World!");
    	
    } catch (IOException e) {}

    // sleep well :-)
    for (;;) {}
  }

  /**
   * main method. Program execution starts here.
   */
  public static void main(String[] args) {
    new HelloWorld();
  }
}
