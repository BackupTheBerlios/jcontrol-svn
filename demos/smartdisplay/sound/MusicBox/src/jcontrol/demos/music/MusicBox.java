/*
 * MusicBox.java
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

package jcontrol.demos.music;

import java.io.IOException;

import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;
import jcontrol.toolkit.iMelody;
import jcontrol.io.Backlight;

/**
 * This class demonstrates the use of iMelody clips. A graphical menu shows and
 * plays all resources in imelody format (<code>.imy</code>) residenting on the
 * device.
 * 
 * @version 1.0
 * @author  Timmermann
 */
public class MusicBox {

    /** Counts the songs available on the device */
    static int maxSongs = 0;
    /** Y position of the menu buttons */
    private static final int Y = 48;
    /** Height of the menu buttons */
    private static int H = 15;
    /** X positions of the menu buttons */
    private static final int[] X = new int[]{  2, 16, 48, 64};
    /** Widths of the menu buttons */
    private static final int[] W = new int[]{ 15, 15, 16, 16};
    /** Autostart flag, set to <code>true</code> for playing the first song automatically on startup. */
    static boolean autostart = false;
    /** Stores the song names */
    static String[] songs;

    /**
     * Program entry point. Just initializes display and keyboard and calls {@link #go(Display, Keyboard) go}.
     * @param args (not used)
     */
    public static void main(String[] args) throws Exception {
        Backlight.setBrightness(255);
        go(new Display(),new Keyboard());
    }    

    /**
     * Main program. Displays the menu, polls the keyboard and runs the iMelody playing threads.
     * @param lcd the display to use
     * @param keys the keyboard to use
     */
    public static void go(Display lcd, Keyboard keys) throws Exception {
        try {
            lcd.setFont(new Resource("Times13.jcfd"));
        } catch (IOException e) {}
        try {
            lcd.drawImage(new Resource("MusicBox.jcif"),0,0);
        } catch (IOException e) {}
        try {
            Resource in = new Resource(Resource.FLASHACCESS);
            while (in!=null) {                // count the songs in all archives
                Resource bak=in;
                while (in!=null) {
                    if (in.getName().indexOf(".imy",0)>0) maxSongs++;
                    in=in.next();
                }
                in=bak.nextArchive();
            }
            songs = new String [maxSongs];

            in = new Resource(Resource.FLASHACCESS);
            int count = 0;
            while (in!=null) {                             // get all song names
                Resource bak=in;
                while (in!=null) {
                    if (in.getName().indexOf(".imy",0)>0) {
                        songs[count++] = in.getName();
                    }
                    in=in.next();
                }
                in=bak.nextArchive();
            }

            int songNum = 0;                             // selected song number
            int lastSong=-1;                        // lase selected song number
            int select = 0;                              // selected menu button
            int oldselect = -1;                     // last selected menu button
            char c = autostart?'S':0;
            iMelody imy=null;

  mainloop: for (;;) {
                switch (c) {
                    case 'L':
                    case 'U':
                    case 'u': select+=3;select%=4; break;       // cursor left
                    case 'R':
                    case 'D':
                    case 'd': select++; select%=4; break;       // cursor right
                    case 'S':                                   // select key
                        switch (select) {
                            case 1: if(imy!=null) imy.stop();   // stop button
                                break;
                            case 2:                             // back button
                                songNum+=maxSongs-1;
                                songNum%=maxSongs;              // no break here
                            case 3:                             // fwd button
                                if (select==3) {                // catch case 2:
                                    songNum++;
                                    songNum%=maxSongs;
                                }                               // no break here
                            case 0:                             // play button
                                if(imy!=null) imy.stop();
                                Resource song = new Resource(songs[songNum]);
                                try {
                                    while ((imy!=null) && (imy.isPlaying())) {
                                        ThreadExt.sleep(10);
                                    }            // wait for playing termination
                                } catch (InterruptedException e) {}
                                imy=new iMelody(song);
                                (new Thread(imy)).start();      // play the song
                                break;
                        }
                }

                lcd.setDrawMode(Display.XOR);
                if (select!=oldselect) {               // display menu highlight
                    if (oldselect>=0) lcd.fillRect(X[oldselect], Y, W[oldselect], H);
                    oldselect=select;
                    lcd.fillRect(X[oldselect], Y, W[oldselect], H);
                }
                lcd.setDrawMode(Display.NORMAL);

                if(songNum!=lastSong) {                     // display somg name
                    lastSong=songNum;
                    lcd.clearRect( 1, 24, 126, 20);
                    lcd.drawString(String.valueOf(songNum+1).concat(". ").
                                   concat(songs[songNum].substring(0,songs[songNum].length()-4)), 5, 27);
                                       // display song name w/o .imy
                }

                c = keys.read();                                // read next key
            }
        } catch (IOException e) { // emergency sequence (should never be called)
            lcd.drawString("Exception ", 5, 26);
            try {
                ThreadExt.sleep(5000);
            } catch (InterruptedException ie) {}
        }
    }

}
