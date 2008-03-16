/*
 * I2CPortScan.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This	library	is free	software; you can redistribute it and/or
 * modify it under the terms of	the	GNU	Lesser General Public
 * License as published	by the Free	Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This	library	is distributed in the hope that	it will	be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A	PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received	a copy of the GNU Lesser General Public
 * License along with this library;	if not,	write to the Free Software
 * Foundation, Inc., 51	Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import java.io.IOException;
import jcontrol.lang.ThreadExt;
import jcontrol.comm.RS232;
import jcontrol.comm.I2C;

public class I2CPortScan extends I2C {	  

	public I2CPortScan(int address)	{
		super(address);
	}

  
	public static void main(String[] args) {
  
		int	i;
		int	count =	0;

		RS232 console =	null;		
		// open	RS232 port using default parameters
		try	{
		    console	= new RS232();
		} catch	(IOException e)	{}
	
		// print startup message
		console.println("Scanning I2C Port");
		console.println("=================");
		console.println();

		// scan	complete I2C bus
		for	(i = 0;	i <	255; i+=2) {
			try	{
				I2CTestDevice test = new I2CTestDevice(i);
				test.test();
				count++;
				console.println("Device found at address 0x".concat(Integer.toHexString(i)));
				ThreadExt.sleep(500);
			} catch	(InterruptedException e) {
			} catch(IOException	e){
			}
		}

	    console.println();
	    console.println("Scan finished with address 0x".concat(Integer.toHexString(i-1)));
	    console.println(Integer.toString(count).concat(" devices found."));
	
	    // finish in endless loop
	    for(;;)	{}
	}

}
