/*
 * RS232HexDumpExample.java
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
import jcontrol.comm.RS232;
import jcontrol.io.Display;
import jcontrol.io.Buzzer;

/**
 * <p>
 * RS232HexDumpExample shows how to write raw RS232 data to a <code>DisplayConsole</code>.
 * </p>
 * 
 * (C) DOMOLOGIC Home Automation GmbH 2005
 */
public class RS232HexDumpExample {

    private static final int BAUDRATE = 19200;

    /**
     * Init and start the RS232 console.
     */
    public RS232HexDumpExample() {
        Display lcd = new Display();
        
        // some nice little output on the display
        lcd.drawString("JControl RS232 Hex Dump Example", 0,0);                
        
        RS232 rs232;
        try {
            rs232 = new RS232(BAUDRATE); // init RS232 access
        } catch (IOException e) {
            lcd.drawString("RS3232 ERROR!", 0, 12);
            return; // nothing to do because RS232 initialization failed
        }
        
        lcd.drawString("RS232 ready", 0,12);
        // print the current baudrate on the display
        lcd.drawString("Baudrate at ".concat(String.valueOf(BAUDRATE)), 0,20);
        
        lcd.drawString("Waiting for RS232 input...", 0,34);
        
        // init console
        TerminalConsole console = new TerminalConsole(lcd);
                
        // continuously receive data from RS232 and write it to the console
        for (;;) {
             try {
                char c = rs232.read(); // read a single character if available                           
                console.append(c);               
             } catch (IOException e) {                
                Buzzer buzzer = new Buzzer();
             	buzzer.on(1000, 200);
             	buzzer.off();
             	rs232.close();
             	try {
            		rs232 = new RS232(BAUDRATE); // init RS232 access
		        } catch (IOException e1) {
        		    lcd.drawString("RS3232 ERROR!", 0, 12);
		            return; // nothing to do because RS232 initialization failed
		        }
             }
        }
    }
      

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new RS232HexDumpExample();
    }
}