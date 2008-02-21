/*
 * TSL2561.java
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
import jcontrol.comm.I2C;
import jcontrol.lang.ThreadExt;

/**
 * Accesses the TSL2561 ambient light sensor from TAOS Inc. using the SM (I<sup>2</sup>C) bus.
 * Refer to the TSL2561 data sheet for details.
 *
 * @version 1.1
 * @author Thomas Roebbenack
 */
public class TSL2561 extends I2C {

    private static final int CMD_POWER_DOWN   	= 0x00; // power down command
    private static final int CMD_POWER_UP   	 	= 0x03; // power up command
    private static final int CMD_TIMING_FAST  	= 0x00; // integrate-time = 13,7ms
    private static final int CMD_TIMING_SLOW  	= 0x10; // integrate-time = 402ms
    private static final int CMD_GAIN_1  		= 0x00; // gain = 1x
    private static final int CMD_GAIN_16  		= 0x08; // gain = 16x
    private static final int CMDnWORD       		= 0xA0; // 0x1010 <- command- and word-flag 
    private static final int CHANNEL_0      		= 0x0C; // address of first byte of channel 0
    private static final int CHANNEL_1      		= 0x0E; // address of first byte of channel 1

    // time period between command and read data
    private int wait = 402 /*ms*/;
    
    /**
     * Initialize the TSL2561-ALS, the Power-On method is implicit called. 
     * 
     * @param address
     * @throws IOException
     */
    public TSL2561(int address) throws IOException {
    	super(address);
    	powerOn();
    }

    /**
     * Reads ADC channel 0.
     * 
     * @return int ADC channel 0 count value
     * @throws IOException on communication error
     */
    public int getChannel0() throws IOException{
    	byte cmd[] = new byte[] { (byte)(CMDnWORD|CHANNEL_0) };
    	write( cmd, 0 , cmd.length );
    	try { ThreadExt.sleep(wait); } catch (InterruptedException e) {} // sleep 400ms
    	byte dat[] = new byte[2];
    	read(dat, 0, dat.length);
		return ( ((0xff&dat[1])<<8) | (0xff&dat[0]) );
    }
    
    /**
     * Reads ADC channel 1
     * 
     * @return int ADC channel 1 count value
     * @throws IOException on communication error
     */
    public int getChannel1() throws IOException {
    	byte cmd[] = new byte[] { (byte)(CMDnWORD|CHANNEL_1) };
    	write( cmd, 0 , cmd.length );
    	try { ThreadExt.sleep(wait); } catch (InterruptedException e) {} // sleep 400ms
    	byte dat[] = new byte[2];
    	read(dat, 0, dat.length);
		return ( ((0xff&dat[1])<<8) | (0xff&dat[0]) );
    }

    /**
     * Switch power of TSL2561-ALS on. This function is implicit called by the constructor. 
     * 
     * @return true if the chip answered success state (should only work one time)
     * @throws IOException on communication error
     */
    public boolean powerOn() throws IOException {
    	byte cmd[] = new byte[] { 0, CMD_POWER_UP };
    	write( cmd, 0 , cmd.length );
    	byte data[] = new byte[1];
    	read( data, 0, 1);
    	if ( data[0] == CMD_POWER_UP ) { return true; }
    	return false;
    }

    /**
     * Switch power of TSL2561-ALS off. 
     * 
     * @throws IOException on communication error
     */
    public void powerOff() throws IOException {
    	byte cmd[] = new byte[] { 0, CMD_POWER_DOWN };
    	write( cmd, 0 , cmd.length );
    }

    /**
     * Set the time response to fastest possible of 13,7ms between send command and read data.
     *  
     * @throws IOException on communication error
     */
    public void setTimingFast() throws IOException {
    	byte cmd[] = new byte[] { 0x01, CMD_TIMING_FAST };
    	write( cmd, 0 , cmd.length );
    	wait = 14;
    }

    /**
     * Set the time response to slowest possible of 402ms between send command and read data.
     * That's the default settings after powered on.
     *  
     * @throws IOException on communication error
     */
    public void setTimingSlow() throws IOException {
    	byte cmd[] = new byte[] { 0x01, CMD_TIMING_SLOW };
    	write( cmd, 0 , cmd.length );
    	wait = 402;
    }

    /**
     * Set the gain (precision) of read data to 1x.
     * That's the default settings after powered on.
     *  
     * @throws IOException on communication error
     */
    public void setGain1x() throws IOException {
    	byte cmd[] = new byte[] { 0x01, CMD_GAIN_1 };
    	write( cmd, 0 , cmd.length );
    }

    /**
     * Set the gain (precision) of read data to 16x.
     *  
     * @throws IOException on communication error
     */
    public void setGain16x() throws IOException {
    	byte cmd[] = new byte[] { 0x01, CMD_GAIN_16 };
    	write( cmd, 0 , cmd.length );
    }

}
