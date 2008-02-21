/*
 * LightTemperatureDemo.java
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
import jcontrol.lang.ThreadExt;

/**
 * This demo prints out temperature and light sensor information obtained from a 
 * TMP75 and a TSL2561 sensor connected via the JControl's I<sup>2</sup>C bus to
 * the RS232 console.
 * 
 * @author Remi Seiler, Thomas Röbbenack
 */
public class LightTemperatureDemo {
   
    private static final String ERROR_NO_SENSORS = "No sensors(LM75/TSL2561) found!";
   	private static final String ERROR_NO_TSL = "No TSL2561 found!";
  	private static final String ERROR_NO_LM = "No LM75 found!";
  	private static final String NO_ERROR = "Reading LM75 and TSL2561...";
   
   	private static final int ERROR_NO_SENSORS_STATE = 0;
   	private static final int ERROR_NO_TSL_STATE = 1;
   	private static final int ERROR_NO_LM_STATE = 2;
   	private static final int NO_ERROR_STATE = 3;
   	   	   	   	
   	private int m_state = ERROR_NO_SENSORS_STATE;
   	private int m_oldState = ERROR_NO_SENSORS_STATE;
   
    public LightTemperatureDemo() {
        TMP75 tmp = null;
        TSL2561 tsl = null;
        
        for (;;) {
        	String message = "";
        	if (tmp == null) {
        		tmp = new TMP75(0x9e);
        	}
            if (tmp != null) {
                try {
                	int temp = tmp.getTemp();
                	message = message.concat( "temperature: ").concat( Integer.toString(temp/10)).concat(".").concat(Integer.toString(temp%10)).concat(" degrees");
                } catch (IOException e1) {
                	message = message.concat( "temperature: N/A");
                    tmp = null;
                }
            }
            if (tsl == null) {
            	try {
                    tsl = new TSL2561(0x72);
                    //tsl.setTimingFast();
                } catch (IOException e2) {}
            }
            if (tsl != null) {
                try {
                	int brightness = 0;
        	    	int ch0 = tsl.getChannel0();
        	    	int ch1 = tsl.getChannel1();
        	    	try {
        	    		int l = TSL2561LuxConversion.calculateLux(false, (short)2, (short)ch0, (short)ch1, true);
        	    		if (l < 0 || l > 1000) {
        	    			brightness = 1000;
        	    		} else {
        	    			brightness = l;
	        	    	}
        	    	} catch (IllegalArgumentException e) {
    	    			brightness = 1000;
        	    	}
        	    	message = message.concat( ", brightness: ").concat( Integer.toString(brightness)).concat( " lux");
                } catch (IOException e) { 
                    tsl = null;
        	    	message = message.concat( ", brightness: N/A");
                }
            } else {
    	    	message = message.concat( ", brightness: N/A");
            }

            try {
            	RS232 rs232 = new RS232();
            	rs232.println( message);
            } catch (IOException e) {
            }
            
            try {
                ThreadExt.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }
    
    public static void main(String[] args) {
        new LightTemperatureIntro();
      	new LightTemperatureDemo();
    }
}
