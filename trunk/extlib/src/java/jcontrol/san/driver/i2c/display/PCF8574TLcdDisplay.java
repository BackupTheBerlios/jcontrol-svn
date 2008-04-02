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
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>DB4</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>DB5</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>DB6</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>DB7</td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>R/W</td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>RS</td>
 * </tr>
 * <tr>
 * <td>6</td>
 * <td>EN</td>
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
     * Create a new object.
     * 
     * @param address of the device to open.
     */
    public PCF8574TLcdDisplay(int address) {
        super(address);
    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.actuators.SimpleDisplay#drawString(java.lang.String, int, int)
     */
    public void drawString(String s, int x, int y) {
    // {todo} Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @see jcontrol.san.interfaces.actuators.Actuator#getName()
     */
    public String getName() {
        return "LCD-Display-PCF8574T (0x".concat(Integer.toHexString(addr)).concat(")");
    }

}
