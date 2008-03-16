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
import java.io.IOException;
import jcontrol.comm.I2C;
import jcontrol.lang.Math;

/**
 * Accesses the TSL2550 ambient light sensor from TAOS Inc. using the SM (I<sup>2</sup>C) bus.
 * Refer to the TSL2550 data sheet for details.
 * 
 * @author RSt, Marcus Timmermann
 * @version $Revision$
 */
public class TSL2550 extends I2C {

    public int count0;
    public int count1;

    private static final int CMD_POWER_DOWN = 0x00;
    private static final int CMD_POWER_UP = 0x03;
    private static final int CMD_STD_RANGE = 0x18;
    private static final int CMD_EXT_RANGE = 0x1D;
    private static final int CMD_READ_CH0 = 0x43;
    private static final int CMD_READ_CH1 = 0x83;

    // Lookup table for channel ratio (i.e. channel1 / channel0)
    private static final int[] RATIO_LOOKUP = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
            100, 100, 100, 100, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 98, 98, 98, 98,
            98, 98, 98, 97, 97, 97, 97, 97, 96, 96, 96, 96, 95, 95, 95, 94, 94, 93, 93, 93, 92, 92,
            91, 91, 90, 89, 89, 88, 87, 87, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 75, 74, 73, 71,
            69, 68, 66, 64, 62, 60, 58, 56, 54, 52, 49, 47, 44, 42, 41, 40, 40, 39, 39, 38, 38, 37,
            37, 37, 36, 36, 36, 35, 35, 35, 35, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32,
            31, 31, 31, 31, 31, 30, 30, 30, 30, 30};

    private static final int COUNT_SATURATION = 32767;
    private static final int[] COUNT_LOOKUP = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 49, 53, 57, 61, 65,
            69, 73, 77, 81, 85, 89, 93, 97, 101, 105, 109, 115, 123, 131, 139, 147, 155, 163, 171,
            179, 187, 195, 203, 211, 219, 227, 235, 247, 263, 279, 295, 311, 327, 343, 359, 375,
            391, 407, 423, 439, 455, 471, 487, 511, 543, 575, 607, 639, 671, 703, 735, 767, 799,
            831, 863, 895, 927, 959, 991, 1039, 1103, 1167, 1231, 1295, 1359, 1423, 1487, 1551,
            1615, 1679, 1743, 1807, 1871, 1935, 1999, 2095, 2223, 2351, 2479, 2607, 2735, 2863,
            2991, 3119, 3247, 3375, 3503, 3631, 3759, 3887, COUNT_SATURATION};

    // maximum measurement value
    private static final int MAX_ILLUMINANCE = 1846;

    // field variable to save current measurement range
    private boolean m_extRange = false;

    /**
     * Opens a connection to the specified TSL2550 device.
     * 
     * @param address of the device to open (0x72 is the only valid address for this device.)
     * @throws IOException if device is not corresponding
     */
    public TSL2550(int address) throws IOException {
        super(address);
        // turn power on
        byte[] command = new byte[1];
        command[0] = (byte) CMD_POWER_UP;
        // initialize with standard range
        this.write(command, 0, 1);
        command[0] = (byte) CMD_STD_RANGE;
        this.write(command, 0, 1);
    }

    /**
     * Simplified lux equation approximation using lookup tables Refer to AN "Simplified TSL2550 Lux
     * Calculation for Embedded Micro Controllers"
     * 
     * @return int illumination value (lux)
     * @throws IOException on communication error
     */
    public int getIllumination() throws IOException {
        count0 = getChannel0();
        count1 = getChannel1();

        int illuminance;
        if (count0 >= COUNT_SATURATION) {
            if (count1 >= COUNT_SATURATION) {
                // if both channels are in saturation, return maximum value
                illuminance = MAX_ILLUMINANCE;
            } else {
                // if one channel is in saturation, return error value
                return -1;
            }
        } else if (count1 >= COUNT_SATURATION) {
            return -1;
        } else {
            // Calculate scaling ratio. Note: the "128" is a scaling factor
            int ratio = 128;
            // avoid division by zero and count1 cannot be greater than count0
            if ((count0 != 0) && (count1 <= count0)) {
                ratio = Math.scale(count1, count0, 128);
            }
            // calculate lux
            illuminance = Math.scale(count0 - count1, 256, RATIO_LOOKUP[ratio]);
        }

        // range check lux
        if (illuminance > MAX_ILLUMINANCE) illuminance = MAX_ILLUMINANCE;
        // if extended range is enabled, measured value is 1/5th of real value
        if (m_extRange)
            return illuminance * 5;
        else
            return illuminance;
    }

    /**
     * Returns the measured color temperature in % ~10%: Fluorescent light; 25..50%: Sunlight; >70%:
     * Incandescent light
     * 
     * @return int color temperature (%)
     * @throws IOException on communication error
     */
    public int getColorTemp() throws IOException {
        count0 = getChannel0();
        count1 = getChannel1();
        // if a channel is in saturation, return without value
        if ((count0 < 0) || (count1 < 0)) { return (-1); }

        int colorTemp;
        // the color temperature is 0 if channel 0 returns 0
        if (count0 > 0)
            colorTemp = Math.scale(count1, count0, 100);
        else
            colorTemp = 0;
        return colorTemp;
    }

    /**
     * Enables or disables the sensor Usefull to reduce overflow values (getCounts(x) = 4015) due to
     * high brightness levels.
     */
    public void setOperationState(boolean state) throws IOException {
        if (state) {
            byte[] command = {(byte) CMD_POWER_UP};
            this.write(command, 0, 1);
        } else {
            byte[] command = {(byte) CMD_POWER_DOWN};
            this.write(command, 0, 1);
        }
    }

    /**
     * Enables 'Extended Range' mode of the TSL2550. Usefull to reduce overflow values (getCounts(x) =
     * 4015) due to high brightness levels.
     */
    public void setExtRange(boolean state) throws IOException {
        if (state) {
            byte[] command = {(byte) CMD_EXT_RANGE};
            this.write(command, 0, 1);
            m_extRange = true;
        } else {
            byte[] command = {(byte) CMD_STD_RANGE};
            this.write(command, 0, 1);
            m_extRange = false;
        }
    }

    /**
     * Reads ADC channel 0. Channel 0 measures both visible and infrared light with a resolution of
     * 12 bits. A value < 0 may be interpreted as an overflow value.
     * 
     * @return int ADC channel 0 count value
     * @throws IOException on communication error
     */
    public int getChannel0() throws IOException {
        byte[] value = {(byte) CMD_READ_CH0};
        this.write(value, 0, 1);
        do {
            this.read(value, 0, 1);
        } while ((value[0] & 0x80) == 0);
        return COUNT_LOOKUP[value[0] & 0x7f];
    }

    /**
     * Reads ADC channel 1 Channel 1 measures primary infrared light with a resolution of 12 bits. A
     * value < 0 may be interpreted as an overflow value.
     * 
     * @return int ADC channel 1 count value
     * @throws IOException on communication error
     */
    public int getChannel1() throws IOException {
        byte[] value = {(byte) CMD_READ_CH1};
        this.write(value, 0, 1);
        do {
            this.read(value, 0, 1);
        } while ((value[0] & 0x80) == 0);
        return COUNT_LOOKUP[value[0] & 0x7f];
    }

}
