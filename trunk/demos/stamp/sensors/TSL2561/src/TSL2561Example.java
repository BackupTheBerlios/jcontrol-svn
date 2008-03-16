import java.io.IOException;

import jcontrol.bus.i2c.TSL2561;
import jcontrol.bus.i2c.TSL2561LuxConversion;
import jcontrol.comm.RS232;


/**
 * <p>TSL2561Test shows how to read the TSL2561 light sensor class</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2006</p>
 */
public class TSL2561Example {

	public static void main(String[] args) throws IOException {
	    // init DisplayConsole
		RS232 console = new RS232();
		
	    // say hello
	    console.println("TSL2561 Test Program.");
	    console.println();
	    
	    // initialize the TSL2561
	    TSL2561 tsl = new TSL2561(0x72);
	    
	    // read data
	    for (;;) { 
	    	int ch0 = tsl.getChannel0();
	    	int ch1 = tsl.getChannel1();
		    console.println("ch0(raw)=".concat(Integer.toHexString(ch0)));
		    console.println("ch1(raw)=".concat(Integer.toHexString(ch1)));
	    	try {
	    		int lux = TSL2561LuxConversion.calculateLux(false, (short)2, (short)ch0, (short)ch1, true);
			    console.println("lux=".concat(Integer.toHexString(lux)));
	    	} catch (IllegalArgumentException e) {
			    console.println("lux=calc error!");
	    	}
		}
	    //tsl2550.powerOff();
	}
}
