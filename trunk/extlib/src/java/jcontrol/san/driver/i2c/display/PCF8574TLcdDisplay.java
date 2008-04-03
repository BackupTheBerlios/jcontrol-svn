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

package jcontrol.san.driver.i2c.display;

import java.io.IOException;

import jcontrol.lang.ThreadExt;
import jcontrol.san.interfaces.actuators.SimpleDisplay;

/**
 * LCD-Display (Conrad-198330) with PCF8574T (Philips) using the SM (I<sup>2</sup>C) bus and the
 * Display MC1602Q.
 * 
 * <p>
 * Ports mapped from PCF8574 to LCD:
 * <p>
 * <table border="1">
 * <tr>
 * <td><b>PCF8574 port</b></td>
 * <td><b>LCD</b></td>
 * <td><b>description</b></td>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>DB4</td>
 * <td>bit4</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>DB5</td>
 * <td>bit5</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>DB6</td>
 * <td>bit6</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>DB7</td>
 * <td>bit7</td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>R/W</td>
 * <td>1=<b>R</b>ead / 0=<b>W</b>rite (display RAM)</td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>RS</td>
 * <td><b>R</b>egister <b>S</b>elect, 1=write data / 0=read data.</td>
 * </tr>
 * <tr>
 * <td>6</td>
 * <td>EN</td>
 * <td>(<b>En</b>able) trailing edge - transmit commands or data from/to the display.</td>
 * </tr>
 * </table>
 * 
 * @see <a href="doc-files/PCF8574T.pdf">PCF8574T datasheet</a>
 * @see <a href="doc-files/MC1602Q.pdf">MC1602Q datasheet</a>
 * @see <a href="doc-files/198330.pdf">Conrad i2c-Bus-LCD-Display</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class PCF8574TLcdDisplay extends AbstractDisplayI2CDriver implements SimpleDisplay {

    /**
     * The byte for the output.
     */
    private int OUTBYTE = 0;

    /**
     * Create a new object.
     * 
     * @param address of the device to open.
     * @throws IOException if an error occurred.
     */
    public PCF8574TLcdDisplay(int address) throws IOException {
        super(address);
        init();
        clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.actuators.SimpleDisplay#clear()
     */
    public void clear() throws IOException {
        lcd_cmd(0x02);
        pause(5);
        lcd_cmd(0x01);
        pause(50);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.actuators.SimpleDisplay#drawString(java.lang.String, int)
     */
    public void drawString(String s, int row) throws IOException {
        if (s != null) {
            lcd_selectLine(row);

            for (int i = 0, n = s.length() > 16 ? 16 : s.length(); i < n; i++) {
                lcd_writechar(s.charAt(i));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.actuators.Actuator#getName()
     */
    public String getName() {
        return "LCD-Display-PCF8574T (0x".concat(Integer.toHexString(addr)).concat(")");
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.actuators.SimpleDisplay#init()
     */
    public void init() throws IOException {
        OUTBYTE = 0x2;
        write((char) OUTBYTE);
        lcd_ena();
        pause(10);
        lcd_cmd(0x28);
        pause(10);
        lcd_cmd(0x0c);
        pause(50);
    }

    /**
     * Send a command to the display.
     * 
     * @param cmd the command.
     * @throws IOException if an error occurred.
     */
    private void lcd_cmd(int cmd) throws IOException {

        OUTBYTE = 0 | (cmd >> 4);
        write((char) OUTBYTE);
        lcd_ena();
        OUTBYTE = 0 | (cmd & 0x0f);
        write((char) OUTBYTE);
        lcd_ena();
    }

    /**
     * Toggle the EN pin.
     * 
     * @throws IOException if an error occurred.
     */
    private void lcd_ena() throws IOException {
        // 01000000 -> 0x40
        write((char) (OUTBYTE | 0x40));
        // 10111111 -> 0xbf
        write((char) (OUTBYTE & 0xbf));
    }

    /**
     * Select the display line.
     * 
     * @param line the line (1 or 2).
     * @throws IOException if an error occurred.
     */
    private void lcd_selectLine(int line) throws IOException {
        if (line == 1) {
            lcd_cmd(0x80);
        } else {
            lcd_cmd(0xc0);
        }
    }

    /**
     * Write a character.
     * 
     * @param ch the char to display.
     * @throws IOException if an error occurred.
     */
    private void lcd_writechar(int ch) throws IOException {

        OUTBYTE = 0x20 | (ch >> 4);
        write((char) OUTBYTE);
        pause(5);
        lcd_ena();
        OUTBYTE = 0x20 | (ch & 0x0f);
        write((char) OUTBYTE);
        pause(5);
        lcd_ena();
    }

    /**
     * Wait.
     * 
     * @param i time in ms.
     */
    private void pause(int i) {
        try {
            ThreadExt.sleep(i);
        } catch (InterruptedException e) {
        }
    }
}
