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
import jcontrol.util.DataProducer;

/**
 * Accesses the LM80 using the SM (I<sup>2</sup>C) bus. Refer to the LM80 data sheet for details.
 * 
 * @author boehme, mgn
 * @version $Revision$
 * @jcontrol.devices lib
 * @jcontrol.aml available="true" icon="chip.gif"
 */
public class LM80 extends I2C implements DataProducer {

    private byte[] buf, cmd;

    private static final int CR = 0; // configuration register
    private static final int CR_Start = 0x01; // start flag
    private static final int CR_Int_Clear = 0x08; // int_clear flag
    private static final int FDR = 5; // fan divisor register
    private static final int TRR = 6; // temperature resolution register
    private static final int TRR_Control = 0x08; // temperature resolution control flag

    private static final byte TEMPH = 0x27; // temperature reading

    private int m_value;

    /**
     * Opens a connection to the specified LM80 device.
     * 
     * @param address of the device to open
     * @param init initialize the device (set to <code>true</code> if JControl is a single master,
     *        <code>false</code> if the device was initialized by another master)
     * @throws IOException if no device replies
     * @throws IllegalArgumentException if the address is invalid
     * @jcontrol.aml available="true" {@Parameter name="address" format="Hex"
     *               {@Choice value="0x50" text="0x50"} {@Choice value="0x52" text="0x52"}
     *               {@Choice value="0x54" text="0x54"} {@Choice value="0x56" text="0x56"}
     *               {@Choice value="0x58" text="0x58"} {@Choice value="0x5a" text="0x5a"}
     *               {@Choice value="0x5c" text="0x5c"} {@Choice value="0x5e" text="0x5e"} }
     *               {@Parameter name="init" default="true" text="lm80.init.tooltip"
     *               {@Choice value="false" text="no"} {@Choice value="true" text="yes"} }
     */
    public LM80(int address, boolean init) throws IOException {
        super(address);
        buf = new byte[2];
        cmd = new byte[1];
        if (init) {
            cmd[0] = CR; // configuration register
            read(cmd, buf, 0, 1);
            buf[0] |= CR_Start; // start monitoring
            buf[0] &= ~CR_Int_Clear; // enable
            write(cmd, buf, 0, 1);

            cmd[0] = TRR; // temperature resolution register
            read(cmd, buf, 0, 1);
            buf[0] |= TRR_Control; // high resolution
            write(cmd, buf, 0, 1);
            updateValue();
        }
    }

    /**
     * Reads a specified temperature channel of the LM80. The temperature range is -65 to 127
     * degrees (°C) and the accuracy is 0.125 degrees. NOTE: When the device is polled to often, it
     * will not update the values.
     * 
     * @return int temperature value multiplied by 256 so in the highbyte is the integer value and
     *         in the highest bits of the lowbyte the fractional value.
     * @throws IOException on communication error
     * @jcontrol.aml applicable="true"
     */
    public int getTemp() throws IOException {
        cmd[0] = TEMPH;
        read(cmd, buf, 0, 1);
        cmd[0] = TRR;
        read(cmd, buf, 1, 1);
        return (buf[0] * 10) + (((buf[1] & 0xf0) * 5) >> 7);
    }

    private static final byte fans[] = {0x28, 0x29}; // fan addresses
    private static final int fanms[] = {0x01, 0x02}; // fan mode select flags
    private static final int fansc[] = {2, 4}; // fan speed control divider lsb

    /**
     * Reads a specified counter channel (used for fan RPMs etc).
     * 
     * @param channel to use
     * @return int counter value
     * @throws IOException on communication error
     * @throws IllegalArgumentException if <code>channel</code> is out of range
     * @jcontrol.aml applicable="true"
     */
    public int getCount(int channel) throws IOException, IllegalArgumentException {
        if (channel < 0 || channel >= fans.length) throw new IllegalArgumentException();
        cmd[0] = FDR;
        read(cmd, buf, 0, 1);
        int trr = buf[0] & 255;
        if ((trr & fanms[channel]) != 0) return 0; // level sensitive input mode
        trr = 1 << ((trr >> fansc[channel]) & 3); // divisor
        cmd[0] = fans[channel];
        read(cmd, buf, 1, 1);
        if ((buf[1] & 255) == 255) return 0; // below limit
        trr *= buf[1] & 255; // count
        return trr;
    }

    private static final int ADCl = 0x20; // gpio lower bound
    private static final int ADCu = 0x27; // gpio upper bound

    /**
     * Reads a specified ADC channel (temperatures, voltages etc., depends on application circuit).
     * 
     * @param channel to use
     * @return int ADC value
     * @throws IOException on communication error
     * @throws IllegalArgumentException if <code>channel</code> is out of range
     * @jcontrol.aml applicable="true"
     */
    public int getADC(int channel) throws IOException, IllegalArgumentException {
        if (channel < 0 || channel >= (ADCu - ADCl)) throw new IllegalArgumentException();
        cmd[0] = (byte) (ADCl + channel);
        read(cmd, buf, 0, 1);
        return buf[0] & 255;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getMin()
     */
    public int getMin() {
        return -6500;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getMax()
     */
    public int getMax() {
        return 12700;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getValue()
     */
    public int getValue() {
        return m_value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getUnit()
     */
    public String getUnit() {
        // TODO Auto-generated method stub
        return "°C";
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getExponent()
     */
    public int getExponent() {
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#updateValue()
     */
    public void updateValue() {
        try {
            m_value = getTemp();
        } catch (IOException e) {
            m_value = getMin();
        }

    }
}
