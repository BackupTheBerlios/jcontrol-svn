package jcontrol.bus.i2c;

/*
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH This library is free software; you can
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
import jcontrol.lang.ThreadExt;

/**
 * Accesses the BH1710 ambient light sensor from ROHM Inc. using the SM (I<sup>2</sup>C) bus.
 * Refer to the BH1710FVC data sheet for details.
 * 
 * @author Thomas Roebbenack
 * @version $Revision$
 */
public class BH1710 extends I2C {

    // commands
    private static final int CMD_POWER_DOWN = 0x00; // power down command
    private static final int CMD_POWER_UP = 0x01; // power up command
    private static final int CMD_RESET = 0x07; // reset command
    private static final int CMD_CONTINUOUSLY_MEASUREMENT_H = 0x10;
    private static final int CMD_CONTINUOUSLY_MEASUREMENT_M = 0x13;
    private static final int CMD_CONTINUOUSLY_MEASUREMENT_L = 0x16;
    private static final int CMD_ONE_TIME_H = 0x20;
    private static final int CMD_ONE_TIME_M = 0x23;
    private static final int CMD_ONE_TIME_L = 0x26;
    // time to sample
    private static final int TIME_H_RES_MODE = 180;
    private static final int TIME_M_RES_MODE = 24;
    private static final int TIME_L_RES_MODE = 5;

    /**
     * Initialize the BH1710, the Power-On method is implicit called.
     * 
     * @param address
     * @throws IOException
     */
    public BH1710(int address) throws IOException {
        super(address);
        powerOn();
    }

    /**
     * Switch power of BH1710 on. This function is implicit called by the constructor.
     * 
     * @throws IOException on communication error
     */
    public void powerOn() throws IOException {
        byte cmd[] = new byte[]{CMD_POWER_UP};
        write(cmd, 0, cmd.length);
    }

    /**
     * Switch power of BH1710 off.
     * 
     * @throws IOException on communication error
     */
    public void powerOff() throws IOException {
        byte cmd[] = new byte[]{CMD_POWER_DOWN};
        write(cmd, 0, cmd.length);
    }

    /**
     * Switch power of BH1710 off.
     * 
     * @throws IOException on communication error
     */
    public void reset() throws IOException {
        byte cmd[] = new byte[]{CMD_RESET};
        write(cmd, 0, cmd.length);
    }

    /**
     * Get the CONTINUOUSLY_MEASUREMENT_H value.
     * 
     * @return CONTINUOUSLY_MEASUREMENT_H value
     */
    public int getContinuouslyMeasurementH() throws IOException {
        byte data[] = new byte[2];
        byte cmd[] = new byte[]{CMD_CONTINUOUSLY_MEASUREMENT_H};
        write(cmd, 0, cmd.length);
        try {
            ThreadExt.sleep(TIME_H_RES_MODE);
        } catch (Exception e) {
        }
        read(data, 0, data.length);
        return (((0xff & data[0]) << 8) | (0xff & data[1])) * 10 / 12;
    }

    /**
     * Get the CONTINUOUSLY_MEASUREMENT_M value.
     * 
     * @return CONTINUOUSLY_MEASUREMENT_M value
     */
    public int getContinuouslyMeasurementM() throws IOException {
        byte data[] = new byte[2];
        byte cmd[] = new byte[]{CMD_CONTINUOUSLY_MEASUREMENT_M};
        write(cmd, 0, cmd.length);
        try {
            ThreadExt.sleep(TIME_M_RES_MODE);
        } catch (Exception e) {
        }
        read(data, 0, data.length);
        return (((0xff & data[0]) << 8) | (0xff & data[1])) * 10 / 12;
    }

    /**
     * Get the CONTINUOUSLY_MEASUREMENT_L value.
     * 
     * @return CONTINUOUSLY_MEASUREMENT_L value
     */
    public int getContinuouslyMeasurementL() throws IOException {
        byte data[] = new byte[2];
        byte cmd[] = new byte[]{CMD_CONTINUOUSLY_MEASUREMENT_L};
        write(cmd, 0, cmd.length);
        try {
            ThreadExt.sleep(TIME_L_RES_MODE);
        } catch (Exception e) {
        }
        read(data, 0, data.length);
        return (((0xff & data[0]) << 8) | (0xff & data[1])) * 10 / 12;
    }

    /**
     * Get the ONE_TIME_H value.
     * 
     * @return ONE_TIME_H value
     */
    public int getOneTimeH() throws IOException {
        byte data[] = new byte[2];
        byte cmd[] = new byte[]{CMD_ONE_TIME_H};
        write(cmd, 0, cmd.length);
        try {
            ThreadExt.sleep(TIME_H_RES_MODE);
        } catch (Exception e) {
        }
        read(data, 0, data.length);
        return (((0xff & data[0]) << 8) | (0xff & data[1])) * 10 / 12;
    }

    /**
     * Get the ONE_TIME_M value.
     * 
     * @return ONE_TIME_M value
     */
    public int getOneTimeM() throws IOException {
        byte data[] = new byte[2];
        byte cmd[] = new byte[]{CMD_ONE_TIME_M};
        write(cmd, 0, cmd.length);
        try {
            ThreadExt.sleep(TIME_M_RES_MODE);
        } catch (Exception e) {
        }
        read(data, 0, data.length);
        return (((0xff & data[0]) << 8) | (0xff & data[1])) * 10 / 12;
    }

    /**
     * Get the ONE_TIME_L value.
     * 
     * @return ONE_TIME_L value
     */
    public int getOneTimeL() throws IOException {
        byte data[] = new byte[2];
        byte cmd[] = new byte[]{CMD_ONE_TIME_L};
        write(cmd, 0, cmd.length);
        try {
            ThreadExt.sleep(TIME_L_RES_MODE);
        } catch (Exception e) {
        }
        read(data, 0, data.length);
        return (((0xff & data[0]) << 8) | (0xff & data[1])) * 10 / 12;
    }

}
