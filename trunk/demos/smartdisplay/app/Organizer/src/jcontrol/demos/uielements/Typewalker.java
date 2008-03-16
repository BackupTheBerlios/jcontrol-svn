/*
 * Typewalker.java
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

package jcontrol.demos.uielements;

import jcontrol.io.*;

/**
 * Virtual keyboard using one large area scrolling over the screen.
 * 
 * @version 1.0
 * @author  Marcus Timmermann
 */
public class Typewalker {

    private static final int MINLETTER = 32;
    private static final int MAXLETTER = 97;
    private static Display lcd;
    private static Keyboard keys;
    private static char currentChar = (char)97;  // ENTER   
    private static int letter = -1;
           
    /**
     * Displays the virtual keyboard and returns the edited text.
     * @param word text to edit or empty for a new text
     * @param d the Display to display the keyboard
     * @param k the (physical) keyboard to read user commands
     * @return String the edited text
     */
    public static String getText(String word, Display d, Keyboard k) {
        if (word==null) {
            word = "";
        }
        Typewalker.lcd = d;
        Typewalker.keys = k;
        try {
            lcd.setFont(new Resource("Times13.jcfd"));
        } catch (java.io.IOException e) {
        }
        int ypos = 0;  // Selektierte Zeile
        int xpos = 0;  // Verschiebung des Bildes aus der Mitte -> - , <- +
        int oldxpos = -1;
        int oldypos = -1;  
        lcd.setDrawMode(Display.NORMAL);
        lcd.drawRect(0,0,127,16);
        boolean isSticker=false;
        mainloop:for (;;) {
            {
                int l = lcd.getTextWidth(word);                
                if (l>124) {
                    lcd.clearRect(1,1,125, 13);
                    lcd.drawString(word, 2,1,124,13,l-124,0);
                } else {
                    lcd.drawString(word, 2, 1);
                }                
            }
            ypos = ((currentChar-MINLETTER)%3); // Selektierte Zeile
            xpos = ((currentChar-MINLETTER)/3); // Verschiebung des Bildes aus der Mitte -> - , <- +
            if (xpos!=oldxpos) {
                // Hintergrundbild malen
                if (xpos<4) { // wenn links noch Platz ist                                 
                    try {
                        Resource image = new Resource("typewalker.jcif");
                        int xoff = (4-xpos)*14; 
                        lcd.drawImage(image, xoff, 17, 127-xoff, 46, 0,0);
                        lcd.drawImage(image, 0, 17, xoff, 46, 308-xoff,0);
                    } catch (java.io.IOException e) {
                    } 
                } else if (xpos>=4 && xpos<=17) {
                    try {
                        Resource image = new Resource("typewalker.jcif");
                        int xoff = (xpos-4)*14;
                        lcd.drawImage(image, 0, 17, 127, 46, xoff,0);                    
                    } catch (java.io.IOException e) {
                    }
                } else { // wenn rechts noch Platz ist
                    try {
                        Resource image = new Resource("typewalker.jcif");
                        int xoff = (xpos-4)*14;
                        lcd.drawImage(image, 0, 17, 308-xoff, 46, xoff,0);                    
                        lcd.drawImage(image, 308-xoff, 17, 127-(308-xoff), 46, 0,0);
                    } catch (java.io.IOException e) {
                    }
                }           
                lcd.drawRect(55,16,17,48);
                oldxpos = xpos;
                oldypos = -1; // damit das Auswahlrechteck wieder hingemalt wird
            }
            if (ypos!=oldypos) {
                lcd.setDrawMode(Display.XOR);
                if (oldypos>-1) {
                    lcd.fillRect(57,19+(oldypos*14),13,14); 
                }
                lcd.fillRect(57,19+(ypos*14),13,14);
                oldypos = ypos;
                lcd.setDrawMode(Display.NORMAL);                
            }
            char key = keys.read();
            switch (key) {
                case 'L':
                    isSticker=false;
                    currentChar-=3;
                    break;
                case 'U':
                    currentChar-=(isSticker)?3:1;
                    break;
                case 'u':
                    isSticker=true;
                    currentChar--;
                    break;
                case 'R':
                    isSticker=false;
                    currentChar+=3;
                    break;
                case 'D':
                    currentChar+=(isSticker)?3:1;
                    break;
                case 'd':                
                    isSticker=true;
                    currentChar++;
                    break;
                case 'S':
                    if (currentChar==MAXLETTER) {
                        return word;
                    } else if (currentChar==MAXLETTER-1) {
                        if (word.length()>1) {
                            word = word.substring(0, word.length()-1);                            
                        } else {
                            word = "";
                        }
                        lcd.clearRect(1,1,125, 13);
                    } else {
                        word = word.concat(new String (new byte[]{(byte)currentChar}));
                    }


            }
            if (currentChar<MINLETTER) currentChar = MAXLETTER;
            if (currentChar>MAXLETTER) currentChar = MINLETTER;
        } // mainloop


    }

}
