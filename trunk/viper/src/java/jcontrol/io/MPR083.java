/*
 * $Id$
 * 
 * Copyright (C) 2005-2009 The JControl Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package jcontrol.io;

import java.io.IOException;

import jcontrol.comm.I2C;
import jcontrol.lang.ThreadExt;

/**
 * MPR083 touch sensor implementation.
 * 
 * @author roebbenack
 * @since Viper 1.0
 * @version $Revision$
 */
public class MPR083 extends I2C implements IRotaryTouchDevice {

    class MPR083EventThread extends Thread {

        public static final int STATE_IDLE = 0;
        public static final int STATE_TOUCHED = 2;
        public static final int STATE_TOUCHED_MOVE = 3;
        public static final int STATE_TOUCHED_TIMEOUT = 1;

        boolean alive;

        @Override
        public void run() {

            try {

                int lastPosition = 0;
                int state = 0;

                alive = true;
                while (alive) {

                    try {
                        ThreadExt.sleep(10);
                    } catch (InterruptedException e) {
                    }

                    // check for short to vdd / vss

                    if (0 != getFaultState()) {
                        reset();
                        continue;
                    }

                    // get rotary state

                    int rotaryState = getRotaryState();
                    boolean touched = (0 != (rotaryState & FLAG_STATUS_TOUCHED));
                    int position = rotaryState & FLAG_STATUS_CP_MASK;

                    // trigger state machine

                    switch (state) {

                        case STATE_IDLE: {
                            if (touched) {
                                state = STATE_TOUCHED_TIMEOUT;
                                lastPosition = position;
                                m_listener.pressed(position);
                            }
                            break;
                        }

                        case STATE_TOUCHED_TIMEOUT: {

                            if (touched && lastPosition == position) {
                                break; // switch
                            }
                            state = STATE_TOUCHED;
                            // |
                            // fall trough \|/
                            // '
                        }

                        case STATE_TOUCHED: {

                            if (!touched) {
                                m_listener.select(lastPosition);
                                m_listener.released();
                                state = STATE_IDLE;
                                break; // switch
                            }

                            state = STATE_TOUCHED_MOVE;
                            // |
                            // fall trough \|/
                            // '
                        }

                        case STATE_TOUCHED_MOVE: {

                            if (!touched) {
                                state = STATE_IDLE;
                                m_listener.released();
                                break; // switch
                            }

                            if (lastPosition != position) {

                                if (lastPosition > position) {
                                    lastPosition -= 8;
                                }
                                int diff = position - lastPosition;
                                if (diff < 4) {
                                    m_listener.rightRotate(position, diff);
                                } else if (diff > 4) {
                                    m_listener.leftRotate(position, 8 - diff);
                                }

                                // next time current position is last position
                                lastPosition = position;
                            }

                            break;
                        }

                        default: {
                            state = STATE_IDLE;
                            break;
                        }

                    } // end: case

                } // end: while alive

            } catch (IOException ioe) {
                // I2C Communication Error
                // m_listener.communicationError(ioe);
            }
        } // end: method "run"

    } // end: class MPR083EventThread

    public static final int FLAG_CONFIG_IRQEN = (1 << 1); // Interrupt Enable (0 disabled, 1
    // enabled)
    public static final int FLAG_CONFIG_IRQR_LSHIFT = 5; // Interrupt Rate Left Shift
    public static final int FLAG_CONFIG_IRQR_MASK = (7 << FLAG_CONFIG_IRQR_LSHIFT); // Interrupt
    // Rate 000-111
    // (set 1 to
    public static final int FLAG_CONFIG_nDCE = (1 << 2); // Duty Cycle Enable
    // 8)
    public static final int FLAG_CONFIG_nRST = (1 << 4); // Reset
    public static final int FLAG_CONFIG_RUNE = (1 << 0); // Run Mode Enable (0 disabled, 1 enabled)
    public static final int FLAG_FAULT_MASK = (1 << 3); // only
    public static final int FLAG_FAULT_NONE = (0); // no fault detected
    public static final int FLAG_FAULT_VDD = (2); // short to VDD detected
    public static final int FLAG_FAULT_VSS = (1); // short to VSS detected
    public static final int FLAG_FIFO_CP_MASK = (0xF); // Current Buffered Position
    public static final int FLAG_FIFO_MDF = (1 << 7); // More Data Flag (0=no data remaining, 1=data
    // remaining)
    public static final int FLAG_FIFO_NDF = (1 << 6); // No Data Flag (0=buffer has data, 1=buffer
    // empty)
    public static final int FLAG_FIFO_OF = (1 << 5); // Overflow Flag (0=no overflow, 1=overflow has
    // occurred)
    public static final int FLAG_FIFO_TRF = (1 << 4); // Touch Release Flag (0=pad is released,
    // 1=pad is touched)
    public static final int FLAG_ROTARY_ACE = (1 << 4); // AUTO CALIBRATION ENABLED
    public static final int FLAG_ROTARY_RE = (1 << 0); // ROTARY ENABLED
    public static final int FLAG_ROTARY_RRBE = (1 << 3); // ROTARY RELEASE BUFFER ENABLED
    public static final int FLAG_ROTARY_RSE = (1 << 7); // ROTARY SOUNDER_ENABLED
    public static final int FLAG_ROTARY_RTBE = (1 << 2); // ROTARY TOUCH BUFFER ENABLED
    public static final int FLAG_SOUNDER_CP = (1 << 2); // click period (0=10ms, 1=20ms)
    public static final int FLAG_SOUNDER_freq = (1 << 1); // frequency (0=1khz, 1=2khz)
    public static final int FLAG_SOUNDER_sen = (1 << 0); // sounder enabled (0=disable, 1=enable)
    public static final int FLAG_STATUS_CP_MASK = (0xF); // Current Position Touched
    public static final int FLAG_STATUS_TOUCHED = (1 << 4); // TOUCH DETECTION (0=not detected,
    // 1=touch detected)
    public static final int REGISTER_CONFIG = 0x0A;
    public static final int REGISTER_FAULT = 0x01; // can only occurre if an electrode is shorted to
    // VDD or VSS
    public static final int REGISTER_FIFO = 0x00;
    public static final int REGISTER_LOW_POWER = 0x08;
    public static final int REGISTER_MASTER_TICK = 0x05;
    public static final int REGISTER_ROTARY_CONFIG = 0x03;
    public static final int REGISTER_ROTARY_STATUS = 0x02;
    public static final int REGISTER_SENSITIVITY = 0x04;
    public static final int REGISTER_SENSOR_INFO = 0x0B;
    public static final int REGISTER_SOUNDER = 0x07; // sounds are enabled by default!!!
    public static final int REGISTER_STUCK_KEY = 0x09;
    public static final int REGISTER_TOUCH_ACQUISITION = 0x06;
    public static final int SENSITIVITY_MAX = 63; // sets sensitivity to 64

    public static final int SENSITIVITY_MIN = 0; // sets sensitivity to 1
    // don't allocate each time byte arrays!
    private byte cmd[] = new byte[1];

    /*******************************************************************************************************************
     * EVENT MECHANISM IMPLEMENTATION
     ******************************************************************************************************************/

    private MPR083EventListener m_listener;

    MPR083EventThread m_thread;

    private byte value[] = new byte[1];

    public MPR083(int address) throws IOException {
        super(address); // 0x9A
        init();
    }

    public void clearFaultState() throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_FAULT;
        value[0] = 0;
        write(cmd, value, 0, value.length);
    }

    public int getConfig() throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_CONFIG;
        read(cmd, value, 0, value.length);
        return value[0];
    }

    public int getFaultState() throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_FAULT;
        read(cmd, value, 0, value.length);
        return value[0];
    }

    public int getRotaryState() throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_ROTARY_STATUS;
        read(cmd, value, 0, value.length);
        return value[0];
    }

    public String getSensorInformationString() throws IOException {
        // byte cmd[] = new byte[1];
        byte value[] = new byte[128];
        cmd[0] = REGISTER_SENSOR_INFO;
        int length = value.length;
        for (int i = 0; i < length; i++) {
            read(cmd, value, i, 1);
            if (value[i] == 0) {
                length = i;
                break;
            }
        }
        String s = new String(value, 0, length);
        return s;
    }

    public int getTouchedRotaryField() throws IOException {
        int state = getRotaryState();
        if (0 != (state & FLAG_STATUS_TOUCHED)) { return (state & FLAG_STATUS_CP_MASK); }
        return -1;
    }

    public void init() throws IOException {

        // - ROTARY CONFIG -
        // setRotaryConfig(FLAG_ROTARY_RSE | FLAG_ROTARY_ACE | FLAG_ROTARY_RRBE | FLAG_ROTARY_RTBE |
        // FLAG_ROTARY_RE); //
        // buzzer enabled
        setRotaryConfig(FLAG_ROTARY_ACE | FLAG_ROTARY_RRBE | FLAG_ROTARY_RTBE | FLAG_ROTARY_RE); // buzzer
        // disabled

        // - SENSITIVITY THRESHOLD -
        setSensitivyThreshold(0xA);

        // - MASTER TICK PERIOD -
        setMasterTickPeriod(0);

        // - TOUCH ACQ -
        setTouchAcquisition(0);

        // - SOUNDER -
        setSounder(0x0); // no buzzer connected

        // - LOW POWER -
        setLowPower(0);

        // - STUCK KEY TIMEOUT -
        setStuckKeyTimeout(0);

        // CONFIGURATION : set to "Run1"
        // 
        // Mode | RUNE | nDCE
        // -------+-----------+---------
        // Run1 | 1 | 1
        // Run2 | 1 | 0
        // Stop1 | 0 | 1
        // Stop2 | 0 | 0
        //
        int config = (6 << FLAG_CONFIG_IRQR_LSHIFT) | FLAG_CONFIG_nRST | FLAG_CONFIG_nDCE
                | FLAG_CONFIG_RUNE; // ==
        // 0xD5
        // (1101_0101)
        setConfig(config);

        // check fault state
        if (0 != getFaultState()) {
            reset();
        }
    }

    public void reset() throws IOException {
        setConfig(getConfig() & ~FLAG_CONFIG_RUNE); // stop touch sensor
        clearFaultState(); // reset fault state
        setConfig(getConfig() | FLAG_CONFIG_RUNE); // restart touch sensor
    }

    public void setConfig(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_CONFIG;
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }

    public void setListener(MPR083EventListener listener) {
        if (null != m_thread) {
            m_thread.alive = false;
            m_thread = null;
        }
        m_listener = listener;
        if (null != listener) {
            m_thread = new MPR083EventThread();
            m_thread.start();
        }
    }

    public void setLowPower(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_LOW_POWER;
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }

    public void setMasterTickPeriod(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_MASTER_TICK;
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }

    public void setRotaryConfig(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_ROTARY_CONFIG;
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }

    public void setSensitivyThreshold(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_SENSITIVITY;
        if (val < 0 || val > 63) {
            // throw new IllegalArgumentException("value must be between 0 - 63");
        }
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }

    public void setSounder(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_SOUNDER;
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }

    public void setStuckKeyTimeout(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_STUCK_KEY;
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }

    public void setTouchAcquisition(int val) throws IOException {
        // byte cmd[] = new byte[1];
        // byte value[] = new byte[1];
        cmd[0] = REGISTER_TOUCH_ACQUISITION;
        value[0] = (byte) val;
        write(cmd, value, 0, value.length);
    }
}
