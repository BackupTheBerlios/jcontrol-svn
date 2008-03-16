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

/**
 * Accesses the MAX6655 integrated dual remote/local temperature sensors and four channel voltage
 * monitors using the SM (I<sup>2</sup>C) bus. Refer to the MAX6655 data sheet for details.
 * 
 * @author boehme. mgn
 * @version $Revision$
 * @jcontrol.devices lib
 * @jcontrol.aml available="true" icon="chip.gif"
 */
public class Max6655 extends I2C {

    private byte[] buf, cmd;

    /**
     * Opens a connection to the specified MAX6655 device.
     * 
     * @param address of the device to open
     * @throws IOException if no device replies
     * @throws IllegalArgumentException if the address is invalid
     * @jcontrol.aml available="true" {@Parameter name="address" format="Hex"
     *               {@Choice value="0x30" text="0x30"} {@Choice value="0x32" text="0x32"}
     *               {@Choice value="0x34" text="0x34"} {@Choice value="0x52" text="0x52"}
     *               {@Choice value="0x54" text="0x54"} {@Choice value="0x56" text="0x56"}
     *               {@Choice value="0x98" text="0x98"} {@Choice value="0x9a" text="0x9a"}
     *               {@Choice value="0x9c" text="0x9c"} }
     */
    public Max6655(int address) throws IOException {
        super(address);
        buf = new byte[2];
        cmd = new byte[1];
    }

    private static final byte temph[] = {0x00, 0x01, 0x13};
    private static final byte templ[] = {0x12, 0x10, 0x11};

    /**
     * Reads a specified temperature channel of the MAX6655. The temperature range is -65 to 127
     * degrees (°C) and the accuracy is 0.125 degrees.
     * 
     * @param channel to use, typically 0: internal, 1 and 2: external sensors
     * @return int temperature value multiplied by 256 so in the highbyte is the integer value and
     *         in the highest bits of the lowbyte the fractional value.
     * @throws IOException on communication error
     * @throws IllegalArgumentException if <code>channel</code> is out of range
     * @jcontrol.aml applicable="true"
     */
    public int getTemp(int channel) throws IOException, IllegalArgumentException {
        if (channel < 0 || channel >= temph.length) throw new IllegalArgumentException();
        cmd[0] = temph[channel];
        read(cmd, buf, 0, 1);
        cmd[0] = templ[channel];
        read(cmd, buf, 1, 1);
        return (buf[0] << 8) + (buf[1] & 255);
    }

    private static final byte volt[] = {0x2e, 0x2f, 0x30, 0x31};

    /**
     * Reads a specified voltage channel of the MAX6655. The voltage range is 0 to 14 V (depends on
     * channel).
     * 
     * @param channel to use, typically 0: supply, 1 to 3: external measures
     * @return int voltage proportional value (depends on channel)
     * @throws IOException on communication error
     * @throws IllegalArgumentException if <code>channel</code> is out of range
     * @jcontrol.aml applicable="true"
     */
    public int getVoltage(int channel) throws IOException, IllegalArgumentException {
        if (channel < 0 || channel >= volt.length) throw new IllegalArgumentException();
        cmd[0] = volt[channel];
        read(cmd, buf, 0, 1);
        return buf[0] & 255;
    }
}
