/*
 * Copyright (C) 2008 The JControl Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package jcontrol.san.driver.i2c.temperature;

import java.io.IOException;

import jcontrol.lang.Math;
import jcontrol.san.interfaces.sensors.Temperature;

/**
 * Temperature sensor TMP75 (Texas Instruments) using the SM (I<sup>2</sup>C) bus.
 * 
 * <p>
 * The TMP75 is a temperature sensor with a SM (I<sup>2</sup>C) interface. The temperature range
 * goes from -40&deg;C to 125&deg;C. The chip have a resolution between 9 and 12 bits and a LSB unit
 * from 0,0625&deg;C.
 * </p>
 * <p>
 * slave-addresses: 0x90, 0x92, 0x94, 0x96, 0x98, 0x9A, 0x9C, 0x9E
 * </p>
 * 
 * @see <a href="doc-files/TMP75.pdf">TMP75 datasheet</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class TMP75 extends AbstractTempI2CDriver implements Temperature {

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public TMP75(int address) {
        super(address);

        // configure
        // 0x01 Configuration Register (READ/WRITE)
        // 0x60 0110 0000
        // R1=1, R0=1: CONVERTER RESOLUTION -> 12 bits
        byte[] command = {(byte) 0x01, (byte) 0x60};
        try {
            this.write(command, 0, command.length);
        } catch (IOException e) {
            // ignore it, use default values
        }
        updateValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMax()
     */
    public int getMax() {
        return 1250;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMin()
     */
    public int getMin() {
        return -400;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Temperature#getTemp()
     */
    public int getTemp() throws IOException {

        // configure
        // 0x00 TEMPERATURE REGISTER
        byte[] command = {(byte) 0x00};
        this.write(command, 0, command.length);

        // read 16 bit (12 bit for the temperature)
        byte[] buf = new byte[2];
        read(buf, 0, buf.length);

        // calculate the temperature
        temp = Math.scale((buf[0] << 8) | (buf[1] & 0xF0), 128, 5);
        return temp;
    }

}
