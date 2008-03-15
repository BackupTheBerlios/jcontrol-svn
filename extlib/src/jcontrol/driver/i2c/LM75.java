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

package jcontrol.driver.i2c;

import java.io.IOException;

import jcontrol.comm.I2C;
import jcontrol.driver.Temperature;

/**
 * Temperature sensor LM75 using the SM (I<sup>2</sup>C) bus.
 * 
 * <p>
 * The LM75 is a temperature sensor with a SM (I<sup>2</sup>C) interface. The temperature range
 * goes from -55&deg;C to 125&deg;C. The chip have a resolution of 9 bits and a LSB unit from
 * 0,5&deg;C.
 * </p>
 * <p>
 * slave-addresses: 0x90, 0x92, 0x94, 0x96, 0x98, 0x9A, 0x9C, 0x9E
 * </p>
 * <p>
 * The temperature value consists of a total of 9 bits and therefore has to be written into two
 * bytes. The first byte contains the integer part of the temperature (in &deg;C). The most
 * significant bit (MSB = bit 7) acts as a signum bit (a logical "1" represents a negative
 * temperature value). The remaining decimals are contained within the second byte. In case of the
 * LM75, only the MSB of this byte is used and resolves to 0,5&deg;C. So if the second byte equals
 * 0x00, the remainder results in 0,0&deg;C and if its value equals 0x80, the remainder results in
 * 0,5&deg;C. The formula, which converts the two bytes into the temperature value is:
 * 
 * <pre>([first byte]*10) + ((([second byte] & 0x80) >> 7)*5)</pre>
 * 
 * As can be seen, the integer part is multiplied by 10 and the decimal by 5 (actually by 10/2).
 * This is done, because the small JControl versions do not support floating-point numbers. In case
 * of an actual temperature of 20,5&deg;C, the value 205 will be returned.
 * </p>
 * 
 * @see <a href="doc-files/LM75.pdf">LM75 datasheet</a>
 * @see <a href="http://www.national.com/mpf/LM/LM75.html">datasheet from www.national.com</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class LM75 extends I2C implements Temperature {

    /**
     * The temperature.
     */
    private int temp;

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public LM75(int address) {
        super(address);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Sensor#getExponent()
     */
    public int getExponent() {
        return -1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Sensor#getMax()
     */
    public int getMax() {
        return 1250;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Sensor#getMin()
     */
    public int getMin() {
        return -550;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Temperature#getTemp()
     */
    public int getTemp() throws IOException {

        // allocate buffer for temperature
        byte[] buf = new byte[2];

        // read content of 16-bit register #0x00
        read(new byte[]{0x00}, buf, 0, 2);

        // calculate the temperature
        temp = (buf[0] * 10) + (((buf[1] & 0x80) * 5) >> 7);
        return temp;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Sensor#getUnit()
     */
    public String getUnit() {
        return "\u00b0C";
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Sensor#getValue()
     */
    public int getValue() {
        return temp;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Sensor#toString()
     */
    public String toString() {
        // temperature resolution is 1/10 degree celsius
        int whole = temp / 10;
        int parts = temp % 10;

        return Integer.toString(whole).concat(".").concat(Integer.toString(parts))
                .concat(getUnit());
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.driver.Sensor#updateValue()
     */
    public void updateValue() {
        try {
            temp = getTemp();
        } catch (IOException e) {
            temp = 0;
        }
    }
}
