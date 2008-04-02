import jcontrol.san.driver.i2c.display.PCF8574TLcdDisplay;

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

/**
 * Demo for the Conrad LCD-Display with PCF8574T (Philips) using the SM (I<sup>2</sup>C) bus.
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class LcdDisplay {

    /**
     * main()
     */
    public static void main(String[] args) {

        PCF8574TLcdDisplay lcd = new PCF8574TLcdDisplay(0x42);

        while (true) {

        }
    }
}
