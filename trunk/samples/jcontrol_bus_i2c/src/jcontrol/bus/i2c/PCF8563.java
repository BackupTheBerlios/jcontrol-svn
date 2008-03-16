/*
 * Copyright (C) 2004-2008 DOMOLOGIC Home Automation GmbH This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jcontrol.bus.i2c;

import java.io.IOException;

import jcontrol.comm.I2C;
import jcontrol.system.Time;

/**
 * Accesses the PCF8563 Real Time Clock (RTC) using the SM (I<sup></sup>C) bus. Refer to the
 * PCF8563 data sheet for details.
 * 
 * @author telkamp, boehme, mgn
 * @version $Revision$
 * @jcontrol.devices lib
 * @see jcontrol.system.RTC
 * @see jcontrol.system.Time
 */
public class PCF8563 extends I2C {

    /** Fixed address */
    private static final int ADDRESS_8563 = 0xA2;

    /**
     * Opens a connection to the specified PCF8563 device.
     */
    public PCF8563() {
        super(ADDRESS_8563);
    }

    /**
     * Initializes the RTC by enabling the running clk and disabling extended test mode, doesn't
     * affect simple test mode by {@link #test(boolean)}.
     * 
     * @throws IOException
     */
    public void reset() throws IOException {
        byte[] buf = new byte[]{0, 0};
        write(buf, 0, 2); // activate clk, disable test mode
    }

    /**
     * Sets test mode of the RTC.
     * 
     * @param enable turns on 32768Hz at <code>CLKOUT</code> pin
     * @throws IOException
     */
    public void test(boolean enable) throws IOException {
        byte[] buf = new byte[]{0x0d, (byte) (enable ? 0x80 : 0)};
        write(buf, 0, 2); // set test mode

    }

    /**
     * Sets the current time. From the point of setting the value, the PCF8563 is counting seconds.
     * This doesn't affect {@link Time time} object in the parameter.
     * 
     * @param time contains the specification of the point of time the RTC should run from.
     * @see jcontrol.system.RT#CsetTime(Time)
     */
    public void setTime(Time time) throws IOException {
        byte[] buf = new byte[7];
        byte[] cmd = new byte[]{2}; // start transaction at register 2
        buf[0] = (byte) setBCD(time.second);
        buf[1] = (byte) setBCD(time.minute);
        buf[2] = (byte) setBCD(time.hour);
        buf[3] = (byte) setBCD(time.day);
        buf[5] = (byte) (setBCD(time.month) + ((time.year < 2000) ? 0x80 : 0));
        buf[6] = (byte) setBCD(time.year % 100);
        write(cmd, buf, 0, 7);
    }

    /**
     * Gets the current time from the PCF8563.
     * 
     * @param time: this object is filled with the values of <i>now</i> (takes a snapshot).
     * @see jcontrol.system.RTC#getTime(Time)
     */
    public void getTime(Time time) throws IOException {
        byte[] buf = new byte[7];
        byte[] cmd = new byte[]{2}; // start transaction at register 2
        read(cmd, buf, 0, 7);
        if ((buf[0] & 0x80) == 0x80) throw new TimeIntegrityLostException(); // low voltage
                                                                                // detect
        time.second = getBCD(buf[0] & 0x7f);
        time.minute = getBCD(buf[1] & 0x7f);
        time.hour = getBCD(buf[2] & 0x3f);
        time.day = getBCD(buf[3] & 0x3f);
        time.month = getBCD(buf[5] & 0x1f);
        time.year = getBCD(buf[6]) + (((buf[5] & 0x80) == 0x80) ? 1900 : 2000);
    }

    /**
     * Converts a BCD value to integer.
     * 
     * @param value the BCD coded value (byte range)
     * @return the integer value
     */
    public static int getBCD(int value) {
        return (value & 0xf) + (((value & 0xf0) >> 3) * 5);
    }

    /**
     * Converts an integer value to BCD.
     * 
     * @param value the integer value (range 0..99)
     * @return the BCD value
     */
    public static int setBCD(int value) {
        return ((value / 10) << 4) + (value % 10);
    }

    /**
     * special IOException: used by getTime
     */
    public class TimeIntegrityLostException extends IOException {
        public TimeIntegrityLostException() {
            super();
        }

    }
}
