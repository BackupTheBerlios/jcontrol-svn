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

package jcontrol.san.driver.i2c.key;

import java.io.IOException;

import jcontrol.san.interfaces.sensors.Sensor;

/**
 * Keyboard (Conrad-198356) with PCF8574T (Philips) using the SM (I<sup>2</sup>C) bus.
 * 
 * <p>
 * The keys and the key code:
 * </p>
 * <table border="1">
 * <tr>
 * <td><b>key code</b></td>
 * <td><b>key string</b></td>
 * <td><b>key label</b></td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>1</td>
 * <td>2</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>1</td>
 * <td>3</td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>1</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>1</td>
 * <td>5</td>
 * </tr>
 * <tr>
 * <td>6</td>
 * <td>1</td>
 * <td>6</td>
 * </tr>
 * <tr>
 * <td>7</td>
 * <td>1</td>
 * <td>7</td>
 * </tr>
 * <tr>
 * <td>8</td>
 * <td>1</td>
 * <td>8</td>
 * </tr>
 * <tr>
 * <td>9</td>
 * <td>1</td>
 * <td>9</td>
 * </tr>
 * <tr>
 * <td>10</td>
 * <td>0</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>11</td>
 * <td>a</td>
 * <td>C*</td>
 * </tr>
 * <tr>
 * <td>12</td>
 * <td>b</td>
 * <td>E#</td>
 * </tr>
 * <tr>
 * <td>13</td>
 * <td>c</td>
 * <td>F3</td>
 * </tr>
 * <tr>
 * <td>14</td>
 * <td>d</td>
 * <td>F4</td>
 * </tr>
 * <tr>
 * <td>15</td>
 * <td>e</td>
 * <td>F1</td>
 * </tr>
 * <tr>
 * <td>16</td>
 * <td>f</td>
 * <td>F2</td>
 * </tr>
 * </table>
 * 
 * @see <a href="doc-files/PCF8574T.pdf">PCF8574T datasheet</a>
 * @see <a href="doc-files/198356.pdf">Conrad i2c-Bus-Tastatur</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class PCF8574TKeyBoard extends AbstractKeyI2CDriver implements Sensor {

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public PCF8574TKeyBoard(int address) {
        super(address);
        updateValue();
    }

    /**
     * Read the data from the matrix keyboard and decode it. If no key in the column is pressed, -1
     * is returned.
     * 
     * @param writeData the data to write (select the column)
     * @param column the selected column
     * @return the key value, or -1 if no key in the column is pressed.
     * @throws IOException
     */
    private int decode(int writeData, int column) throws IOException {

        int colKey = -1;

        // select column
        write((char) writeData);

        // read row
        int readData = getInput() & 0x0f;

        if (readData == 1) {
            // row 1
            colKey = 1 + column;
        } else if (readData == 2) {
            // row 2
            colKey = 5 + column;
        } else if (readData == 4) {
            // row 3
            colKey = 9 + column;
        } else if (readData == 8) {
            // row 4
            colKey = 13 + column;
        }
        return colKey;
    }

    /**
     * Returns the actual input from the i2c.
     * 
     * @return the actual input.
     * @throws IOException if a IO-error occurred.
     */
    private int getInput() throws IOException {
        byte[] buf = new byte[1];
        read(buf, 0, 1);
        return (((buf[0] & 255) - 255) * (-1));
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getKey()
     */
    public int getKey() {

        try {
            key = -1;

            // COLUMN 0: keys 1-5-9-C: 11101111 = 0xef
            key = decode(0xef, 0);
            if (key != -1) { return key; }

            // COLUMN 1: keys 2-6-0-D: 11011111 = 0xdf
            key = decode(0xdf, 1);
            if (key != -1) { return key; }

            // COLUMN 2: keys 3-7-A-E: 10111111 = 0xbf
            key = decode(0xbf, 2);
            if (key != -1) { return key; }

            // COLUMN 3: keys 4-8-B-F: 01111111 = 0x7f
            key = decode(0x7f, 3);
            if (key != -1) { return key; }

        } catch (IOException e) {
            // ignore it, use default values
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#getKeyString()
     */
    public String getKeyString() {

        if (key == -1) { return ""; }
        if (key >= 1 && key <= 9) { return String.valueOf(key); }
        if (key == 10) { return "0"; }
        return Integer.toHexString(key - 1);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMax()
     */
    public int getMax() {
        return 16;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getMin()
     */
    public int getMin() {
        return -1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.Sensor#getName()
     */
    public String getName() {
        return "KeyBoard-PCF8574T (0x".concat(Integer.toHexString(addr)).concat(")");
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.sensors.KeyBoard#waitForKey()
     */
    public int waitForKey() {
        int key = -1;
        while (key == -1) {
            key = getKey();
        }
        return key;
    }

}
