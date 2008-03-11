/*
 * (c) Copyright 2004 Domologic Home-Automation GmbH, TU Braunschweig. All Rights Reserved.
 */
package jcontrol.bus.i2c;

import java.io.IOException;
import jcontrol.comm.I2C;

/**
 * Accesses the TSL2550 ambient light sensor using the SM (I<sup>2</sup>C) bus. Refer to the
 * TSL2550 data sheet for details.
 * 
 * @author RSt
 * @version $Revision$
 */

public class TSL2550 extends I2C {

    /**
     * Opens a connection to the specified TSL2550 device.
     * 
     * @param address of the device to open (0x72 is the only valid address for this device.)
     * @throws IOException if no device replies
     */
    public TSL2550(int address) throws IOException {
        super(address);
        byte[] command = {(byte) 0x18};
        this.write(command, 0, 1);
    }

    /**
     * Reads the 'Count Value' of the specified ADC channel of the TSL2550. A value of 4015 may be
     * interpreted as an overflow value.
     * 
     * @return int temperature value multiplied by 10 (in °C)
     * @throws IOException on communication error
     */
    public int getCounts(int channel) throws IOException {
        byte[] command = new byte[1];
        if ((channel != 0) && (channel != 1)) { throw new IOException(); }
        if (channel == 0) {
            command[0] = (byte) 0x43;
        } else {
            command[0] = (byte) 0x83;
        }
        this.write(command, 0, 1);
        command = null;
        byte[] value = new byte[1];
        this.read(value, 0, 1);
        if ((value[0] & 0x80) == 0) { return -1; }
        int c = (value[0] & 0x70) >>> 4;
        /*
         * counts = CHORD_VALUE + STEP_SIZE * STEPS CHORD_VALUE[c] = ((0xFF >>> (8-c)) * 16.5) =
         * (((0xFF >>> (8-c)) * 33) / 2) STEP_SIZE[c] = (0x01 << c) STEPS = (value[0] & 0x0F)
         */
        int counts = (((0xFF >>> (8 - c)) * 33) / 2) + (0x01 << c) * (value[0] & 0x0F);
        return counts;
    }

    /**
     * Enables 'Extended Range' mode of the TSL2550. Usefull to reduce overflow values (getCounts(x) =
     * 4015) due to high brightness levels.
     */
    public void enableExtendedRange() throws IOException {
        byte[] command = {(byte) 0x1D};
        this.write(command, 0, 1);
    }

    /**
     * Switches the TSL2550 back to 'Standard Range' mode.
     */
    public void disableExtendedRange() throws IOException {
        byte[] command = {(byte) 0x18};
        this.write(command, 0, 1);
    }
}
