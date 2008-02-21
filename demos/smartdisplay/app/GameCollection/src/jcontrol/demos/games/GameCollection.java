/*
 * GameCollection.java
 * Copyright (C) 2000-2008 DOMOLOGIC Home Automation GmbH
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
package jcontrol.demos.games;

import jcontrol.io.Backlight;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.system.Management;

/**
 * Games launcher. Just displays a little graphical menu and starts some games.
 * 
 * @author  Marcus Timmermann
 * @version $Revision$
 */
public class GameCollection {

    /**
     * Program entry point. Calls the menu and the game applications.
     * @param args (not used)
     */
    public static void main(String[] args) {
		// turn on backlight
	    Backlight.setBrightness(Backlight.MAX_BRIGHTNESS);        
	    //
        Display lcd   = new Display();                 // init display
        Keyboard keys = new Keyboard();
        {
            String poweroff = Management.getProperty("system.standbytimer");;
            if (poweroff!=null) Management.powerOff(Integer.parseInt(poweroff));
        }
        int select=0;
        for (;;) {
            select = menu(lcd,keys, select);
            switch (select) {
                case 0: 
                	jcontrol.system.Management.setProperty("buzzer.keyboardbeep","false");
	                Tetris.go(lcd,keys);      // Tetris 48x13 1,20
	                jcontrol.system.Management.setProperty("buzzer.keyboardbeep","true");
                    break ;
                case 1: Minesweeper.go(lcd,keys); // Minesweeper 62x15 63,20
                    break ;
                case 2: Pong.go(lcd,keys);        // Pong 44x12 77,37
                    break ;
                case 3: Breakout.go(lcd,keys);    // Breakout 65x11 36,51
                    break ;
                case 4: Connect4.go(lcd,keys);    // Connect4 61x19 2,32
                    break ;
            }
        }
    }

    /**
     * Displays a graphical menu.
     * @param lcd    Display to use
     * @param keys   Keyboard to use
     * @param select preselection
     * @return int   selection
     */
    static int menu(Display lcd,Keyboard keys, int select){
        int[] X = new int[]{  1, 63, 77, 36,  2};
        int[] Y = new int[]{ 20, 20, 37, 51, 32};
        int[] W = new int[]{ 48, 62, 44, 65, 61};
        int[] H = new int[]{ 13, 15, 12, 11, 19};
        int oldselect = -1;
        try{
            lcd.drawImage(new Resource("GameCollection.jcif"),0,0);
        } catch(java.io.IOException e){}
        lcd.setFont(Display.SYSTEMFONT);
        mainloop:for (;;) {
            if (select!=oldselect) {
                // set selection
                lcd.setDrawMode(Display.XOR);
                if (oldselect!=-1)
                    lcd.fillRect(X[oldselect], Y[oldselect], W[oldselect], H[oldselect]);
                oldselect = select;
                lcd.fillRect(X[oldselect], Y[oldselect], W[oldselect], H[oldselect]);
                lcd.setDrawMode(Display.NORMAL);
            }
            switch (keys.read()) {
                case 'R':
                case 'U':
                case 'u': select++; select%=5; break;
                case 'L':
                case 'D':
                case 'd': select+=4; select%=5; break;
                case 'S': break mainloop;
            }

        }
        return select;
    }

}