/*
 * TerminalConsole.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import jcontrol.io.Display;
import jcontrol.system.Management;

/**
 * TerminalConsole is a simple full screen hex dump viewer.
 * Strings or characters written to the console are displayed both
 * as hex and ASCII representation.
 * 
 * @author Marcus Timmermann
 */
public class TerminalConsole {

    /** Used Display instance */
    private Display lcd;
    /** Contains the current line (pixels) */
    private int line = 0, h;
    /** Contains the current pixel position in the current line */
    public int  pos  = 0;
    
    int width;
    int height;


    /** Constructs a new TerminalConsole using the default Display. */
    public TerminalConsole() {
        this(new Display());
        initDimensions();
    }
    
    void initDimensions() {
        String str = Management.getProperty("display.dimensions");
        if (str == null) {
            width = 128; // jcvm8 default
            height = 64; // jcvm8 default
        } else {
            int firstx = str.indexOf("x", 0);
            width = Integer.parseInt(str.substring(0, firstx));
            height = Integer.parseInt(str.substring(firstx + 1, str.indexOf("x", firstx + 1)));
        }
    }

    /**
     * Constructs a new TerminalConsole using a specified Display.
     * 
     * @param lcd Display to use (full screen)
     */
    public TerminalConsole(Display lcd) {
        this.lcd = lcd;
        lcd.setFont(null);
        h = lcd.getFontHeight();
        initDimensions();
    }

    private static String toHex(int value) {
        byte[] buf = new byte[2];
        for (int i = 1; i >= 0; i--) {
            int digit = value & 15;
            if (digit < 10) buf[i] = (byte) ('0' + digit);
            else buf[i] = (byte) ('a' - 10 + digit);
            value >>= 4;
        }
        return new String(buf);
    }

    /**
     * Appends a single charater to the console.
     * 
     * @param c the character to write (8859-1 encoding)
     */
    public synchronized void append(char c) {
        if (lcd == null) return;
        scroll();
        lcd.drawString(toHex(c), pos*11, line);
        String s = new String(new byte[]{(byte)c});
        int w = lcd.getTextWidth(s);        
        lcd.drawString(s, 97+pos*4-(w/2), line); // draw ASCII char
        pos++;
    }

    /**
     * Writes a String to the console.
     * 
     * @param buffer the String to write
     */
    public synchronized void print(String buffer) {
        if (lcd == null) return;
        for (int i=0; i<buffer.length(); i++) {
            append(buffer.charAt(i));
        }
    }
   

    /** Scrolls the Display by one line. */
    private void scroll() {
        if (pos==0 && line==0) {
            lcd.clearDisplay();            
        }
        if (pos >= 8) {
            line += h;
            pos = 0;
        }
        if (pos == 0) {
            int s = line + h - height;
            if (s > 0) {
                lcd.clearRect(0, 0, width, h);
                lcd.scroll(0, -s);
                line -= s;
            }
        }
    }
    
}