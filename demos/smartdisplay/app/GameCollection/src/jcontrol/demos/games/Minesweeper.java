/*
 * Minesweeper.java
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

package jcontrol.demos.games;

import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.Math;
import jcontrol.system.Management;

/**
 * <CODE>Minesweeper.java</CODE> is a simple game.
 * You have to find all mines hidden behind the buttons. You can remove the
 * active button by pressing the select-button on the board.
 * Change the active button with up/down/left/right.
 * But don't press buttons with a mine. When removing some buttons a number
 * would appear. This number indicates the number of mines hidden around this
 * button.<BR>
 * Good luck.
 *
 * @author Marcus Timmermann
 * @version 2.0
 */
public class Minesweeper {
    private static String[]  NUMBERS_IMG;

    private static String[]  BUTTON_IMG;
    private static int       activeX = 0;                           // coordinates for
    private static int       activeY = 0;                           // first active button

    private static final int   WIDTH = 13;
    private static final int   HEIGHT = 9;
    private static       int   MINENUMBER = 10;                     // # mines

    private static int         allBlocks = WIDTH * HEIGHT;          // total number of buttons
    private static boolean     minehit = false;
    private static boolean     won     = false;
    private static boolean[]   buttons;
    private static byte[]      mines;
    private static Display     lcd;       // init display
    private static Keyboard    keys;      // init keyboard
    private static boolean fakeMines=false;

    /** Deletes all mines and opened buttons.
    */
    private static void clearBoard() {
        for (int i=0; i<mines.length; i++) {
            mines[i] = 0;
            buttons[i] = false;
        }
    }

    /** This method is needed to calculate the number of mines around a button.
    */
    private static void incField(int x, int y) {
        if ((x>=0) && (x<WIDTH) && (y>=0) && (y<HEIGHT))
            if (mines[x+y*WIDTH]<(byte)8) mines[x+y*WIDTH]++; // if not a mine inc
    }

    /** Chooses MINNUMBER mines all over the board and calculates the numbers around every mine.
    */
    private static void getMines() {
        clearBoard();                                   // clear the board
        allBlocks = WIDTH * HEIGHT;                     // number of buttons
        won = false;                                    // no winner yet
        minehit = false;                                // no mine yet
        activeX=0;activeY=0;                            // upper left button is active
        for (int count = 0; count<MINENUMBER; count++) {
            int randX = Math.rnd(WIDTH);  // find new mine by random
            int randY = Math.rnd(HEIGHT);
            if (mines[randX+randY*WIDTH]==(byte)9) count--;  // if already mine, find new mine
            else {
                mines[randX+randY*WIDTH]=(byte)9;            // set mine
                incField(randX-1,randY-1);
                incField(randX-1,randY);                // for every field around
                incField(randX-1,randY+1);              // increase the index
                incField(randX,randY-1);
                incField(randX,randY+1);
                incField(randX+1,randY-1);
                incField(randX+1,randY);
                incField(randX+1,randY+1);
            }
        }
        paint(false);                                   // paint all without displaying mines
    }

    /**
     * Paints a field.
     *
     * @param x         the x-coordinate
     * @param y         the y-coordinate
     * @param showMines if true - show the mine if there is one, if false - show the button
     */
    private static void paintBlock(int x, int y, boolean showMines) {

        if (showMines||buttons[x+y*WIDTH])   // if there is no more button or mines are to be shown
            lcd.drawImage(NUMBERS_IMG, (1+x*7), (1+y*7), 7, 7, 0,mines[x+y*WIDTH]*8); // draw numbers or mine
        else {                                              // or show button
            lcd.drawImage(BUTTON_IMG, (1+x*7), (1+y*7), 7, 7,0,0); // draw normal button image
        }
        if (x==activeX && y==activeY) {                   // the active one
            lcd.setDrawMode(Display.XOR);
            lcd.fillRect(2+x*7,2+y*7,5,5);
            lcd.setDrawMode(Display.NORMAL);
        }
        if ((fakeMines)&&(mines[x+y*WIDTH]==9)) {
            lcd.setPixel(2+x*7,6+y*7);
        }
    }


    /** Repaints the whole board by calling paintBlock for every field.
    */
    private static void paint(boolean showMines) {
        if (!showMines) lcd.clearDisplay();
        lcd.drawString("Mines", 100,0);              // draw number of mines
        lcd.drawString(String.valueOf(MINENUMBER), 104, 7);
        for (int x=0; x<WIDTH; x++) {                               // for every field
            for ( int y=0; y<HEIGHT; y++) {
                paintBlock(x,y, showMines);                         // paint
            }
        }
    }



    /**
     * Increases the number by 10.
     */
    private static boolean openBlock(int x, int y) {
        boolean r = false;
        if ((x>=0) && (x<WIDTH) && (y>=0) && (y<HEIGHT) && !buttons[x+y*WIDTH]) {
            if (mines[x+y*WIDTH] == 0) {
                r = true;
                mines[x+y*WIDTH] = 10;
            } else if (mines[x+y*WIDTH]<9) {
                r = false;
                buttons[x+y*WIDTH] = true;                                 // open field
                paintBlock(x,y, false);                            // paint this field
            }
        }
        return r;
    }

    /**
     * This method is called when a field was clicked. It is called recursively until all available fields are open.
     *
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     */
    private static void clickBlock(int x, int y) {
        // Wert des aktuelles Feldes feststellen
        int current = mines[x+y*WIDTH];
        if (current == 9) {
            buttons[x+y*WIDTH] = true;                         // open this field
            paint(true);                                       // paint it
            minehit = true;                                    // you hit a mine!
        }
        if ((current > 0) & (current<9)) {
            buttons[x+y*WIDTH] = true;                         // open only this
            paintBlock(x,y,false);                             // paint it
        }
        if (current == 0) {
            buttons[x+y*WIDTH] = true;                         // open field
            paintBlock(x,y, false);                            // paint this field
            mines[x+y*WIDTH] = 10;


            boolean goon = true;
            while (goon) {
                goon = false;
                for (int x1=0; x1<WIDTH; x1++) {
                    for (int y1=0; y1<HEIGHT; y1++) {
                        if (mines[x1+y1*WIDTH] == 10) {
                            mines[x1+y1*WIDTH] = 0;
                            buttons[x1+y1*WIDTH] = true;       // open field
                            paintBlock(x1,y1, false);          // paint this field
                            goon |= openBlock(x1-1,y1-1);      // open all fields around this
                            goon |= openBlock(x1-1,y1);
                            goon |= openBlock(x1-1,y1+1);
                            goon |= openBlock(x1,y1-1);
                            goon |= openBlock(x1,y1+1);
                            goon |= openBlock(x1+1,y1-1);
                            goon |= openBlock(x1+1,y1);
                            goon |= openBlock(x1+1,y1+1);
                        } else if (mines[x1+y1*WIDTH] > 10) {
                            mines[x1+y1*WIDTH] -= 10;
                            buttons[x1+y1*WIDTH] = true;        // open field
                            paintBlock(x1,y1, false);           // paint this field
                        }

                    }
                }
            }
        }

        won = true;
        for (int x1=0; x1<WIDTH; x1++) {
            for (int y1=0; y1<HEIGHT; y1++) {
                if ((mines[x1+y1*WIDTH] != 9) & (buttons[x1+y1*WIDTH]==false)) {
                    won = false;
                    return;
                }
            }
        }
    }

    /** Moves the active button up or down on available fields.
    */
    private static void moveUpDown(boolean up){
        int x = activeX;                        // save old coordinates
        int y = activeY;
        int oldX = activeX;
        int oldY = activeY;
        if (up) {                               // move up
            y--; if (y<0) y=HEIGHT-1;
        } else {                                // move down
            y++; if (y>=HEIGHT) y=0;
        }
        activeX=x;                              // new coordinates
        activeY=y;
        paintBlock(oldX,oldY,false);            // paint
        paintBlock(activeX,activeY,false);
    }

    /** Moves the active button to the left or to the right on available fields.
    */
    private static void moveLeftRight(boolean left) {
        int x = activeX;                        // save old coordinates
        int y = activeY;
        int oldX = activeX;
        int oldY = activeY;
        if (left) {                             // move left
            x--; if (x<0) x=WIDTH-1;
        } else {                                // move right
            x++; if (x>=WIDTH) x=0;
        }
        activeX=x;                              // new coordinates
        activeY=y;
        paintBlock(oldX,oldY,false);            // paint
        paintBlock(activeX,activeY,false);
    }


    /** The main method. */
    public static void main(String[] args) {
        Keyboard keys=new Keyboard();
        Display lcd=new Display();
        go(lcd,keys);
    }

    /**
     * This method can be run from a global menu context, using the global Keyboard and Display objects.
     *
     * @param lcd    the Display
     * @param keys   the Keyboard
     */
    public static void go(Display lcd, Keyboard keys){
        Minesweeper.lcd=lcd;
        lcd.setFont(Display.SYSTEMFONT);
        Minesweeper.keys=keys;
        NUMBERS_IMG=new String[]{
            "\u0000\u0000\u0000\u0000",  // empty
            "\u0000\u043E\u0000\u0000",  // one
            "\u0000\u3A2A\u2E00\u0000",  // two
            "\u0000\u2A2A\u3E00\u0000",  // three
            "\u0000\u0E08\u3E00\u0000",  // four
            "\u0000\u2E2A\u3A00\u0000",  // five
            "\u0000\u3E2A\u3A00\u0000",  // six
            "\u0000\u0202\u3E00\u0000",  // seven
            "\u0000\u3E2A\u3E00\u0000",  // eigth
            "\u082A\u1C7F\u1C2A\u0800"}; // bomb
        BUTTON_IMG=new String[] { "\u7F41\u6161\u617D\u7F00"}; // normal button
        buttons = new boolean[WIDTH*HEIGHT];
        mines   = new byte[WIDTH*HEIGHT];

        String profile = Management.getProperty("profile.name");

        try {
            lcd.drawImage(new Resource("Minesweeper.jcif"),0,0);
        } catch (java.io.IOException e) {}
        char c=keys.read();

        all:for (;;) {
            boolean movedWhit = false;
            if (c=='H') fakeMines=true;
            getMines();
            lcd.drawLine(92,0,92,63);
            game:for (;;) {                                 // the main loop
                c = keys.read();

                switch (c) {
                    case 'u': movedWhit = true;
                              moveUpDown(false); 
                              break;
                    case 'd': movedWhit = true;
                              moveLeftRight(false);
                              break;
                    case 'R': movedWhit = false;
                              moveLeftRight(false);
                              break;
                    case 'L': movedWhit = false;
                              moveLeftRight(true);
                              break;
                    case 'U': moveUpDown(true);
                              break;
                    case 'D': if(movedWhit) 
                                  moveLeftRight(true);
                              else
                                  moveUpDown(false);
                              break;
                    case 'M':
                    case 'S': clickBlock(activeX, activeY); // open field
                        if (won) {
                            lcd.drawImage(new String[]{"\uE018\u0402\u0231\u3101\u0131\u3102\u0204\u18E0",
                                                       "\u0718\u2042\u4488\u98B8\uB898\u8844\u4220\u1807"},
                                          101,15);
                            MINENUMBER++;
                            lcd.drawString("Press to", 95, 33);
                            lcd.drawString("continue.", 94, 40);
                            lcd.drawString("Move to", 96, 49);
                            lcd.drawString("quit.", 101, 56);
                            c=keys.read();
                            if (c=='S' || c=='M') break game;
                            else                  break all;
                        } else if (minehit) {
                            lcd.drawImage( new String[]{"\uE018\u0402\u0231\u3101\u0131\u3102\u0204\u18E0",
                                                        "\u0718\u2040\u5088\u8484\u8484\u8850\u4020\u1807"},
                                           101,15);
                            lcd.drawString("Press to", 95, 33);
                            lcd.drawString("continue.", 94, 40);
                            lcd.drawString("Move to", 96, 49);
                            lcd.drawString("quit.", 101, 56);
                            c=keys.read();
                            if (c=='S' || c=='M') break game;
                            else                  break all;
                        }
                }

            } // end
        } // all
    }

}
