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
package jcontrol.bus.i2c;

import java.io.IOException;

import jcontrol.comm.I2C;

/**
 * Accesses the PCF2116C lcd driver using the SM (I<sup>2</sup>C) bus. Refer to the PCF2116C data
 * sheet for details.
 * 
 * @author rst
 * @version $Revision$
 */

public class PCF2116C extends I2C {

    public static String debugMessage = "";
    private int cursorPosition = 0;

    public PCF2116C(int address) throws IOException {
        super(address);
        init();
    }

    public void init() throws IOException {
        debugMessage = "Sende Kommandos an Display...";
        byte[] command = new byte[2];
        command[0] = (byte) 0x80;// Controlbyte follows (Co=1 ; RS=0 ; R/!W=0)
        command[1] = (byte) 0x00;// Control Byte (Co=1 ; RS=0 ; R/!W=0 (W active))
        write(command, 0, command.length);
        command[1] = (byte) 0x3E;// FunctionSet
        write(command, 0, command.length);
        command[1] = (byte) 0x14;// Cursor/Display shift
        write(command, 0, command.length);
        command[1] = (byte) 0x0C;// DisplayControl
        write(command, 0, command.length);
        command[1] = (byte) 0x06;// Entry mode set
        write(command, 0, command.length);
        command[1] = (byte) 0x01;// Clear Display
        write(command, 0, command.length);
    }

    public void print(String s) throws IOException {
        byte[] cb = {(byte) 0x40};
        byte[] wb = s.getBytes();
        write(cb, wb, 0, wb.length);
    }

    public void print(byte b) throws IOException {
        byte[] cb = {(byte) 0x40};
        byte[] wb = new byte[1];
        wb[0] = b;
        write(cb, wb, 0, wb.length);
    }

    public void setPosition(byte line, byte cursor) {
        byte[] cb = {(byte) 0x80};// Controlbyte follows
        byte[] wb = {(byte) 0x00};// set DDRAM address
        switch (line) {
            case 0:
                wb[0] = (byte) 0x80;
                break;
            case 1:
                wb[0] = (byte) 0xA0;
                break;
            case 2:
                wb[0] = (byte) 0xC0;
                break;
            default:
                wb[0] = (byte) 0x80;
        }
        wb[0] |= cursor;
        try {
            write(cb, wb, 0, 1);
        } catch (IOException e) {
            debugMessage = "FEHLER: Cursor Position nicht ändern!";
        }
    }

    public boolean clearDisplay() {
        boolean success = false;
        byte[] cb = {(byte) 0x80};
        byte[] wb = {(byte) 0x02};
        try {
            write(cb, wb, 0, 1);
            success = true;
        } catch (IOException e) {
            success = false;
            debugMessage = "FEHLER: Konnte Display nicht löschen!";
        }
        return success;
    }
}
