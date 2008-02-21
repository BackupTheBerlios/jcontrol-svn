/*
 * Connect4.java
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

import java.io.IOException;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;

/**
 * <CODE>Connect4</CODE> is a simple game for one player versus
 * the computer. You have to try to get four chips in a streak and prevent the
 * computer from doing the same. The current column your chip will be thrown in
 * is marked with an arrow. You can change the current column by moving the
 * up/down button. Throw the chip by pressing the button.
 *
 * @author Marcus Timmermann
 * @version 1.0
 */
public class Connect4 {

    private static final int WIDTH  = 7;                             // width
    private static final int HEIGHT = 6;                             // height
    private static final int XOFFSET = 5;                            // distance from left border
    private static final int YOFFSET = 7;                            // distance from top

    private static final int BEGINNER = 2;
    private static final int ADVANCED = 1;
    private static final int PRO      = 0;

    private static int       level  = BEGINNER;                      // starting level
    private static Display   lcd    = null;                          // init display

    public static Keyboard keys = null;

    private static byte[]  mem    = new byte[WIDTH*HEIGHT];          // save the chips in an array
    private static byte      column = 0;                             // current column for the arrow
    private static boolean   playerwins   = false;                   // true when player wins
    private static boolean   computerwins = false;                   // true when computer win
    private static int       chipNumber   = HEIGHT*WIDTH;            // total number of chips in the game

    /**
     * Init the keyboard and some variables.
     */
    public static void init() {
        playerwins = false;             // nobody has won yet
        computerwins = false;
    }

    /**
     * Returns the number of chips placed around this position relative to a certain direction.
     *
     * @param x         the x-coordinate
     * @param y         the y-coordinate
     * @param direction 0:left, 1:left down, 2:down, 3:right down, 4:right, 5:up right, 7:up left
     * @param player    1:player, 2:computer
     * @return the number of chips
     */
    private static short getChips(int x, int y, int direction, byte player) {
        int ret = 0;
        switch (direction) {                                                                // which direction
            case 0: if (x>0 && y>=0) {                                                      // to the left
                    if (mem[x-1+y*WIDTH]==player) ret=1+getChips(x-1, y, 0, player);
                    else ret=0;
                } else ret=0;
                break;
            case 1: if (x>0 && y<HEIGHT-1) {
                    if (mem[x-1+(y+1)*WIDTH]==player) ret=1+getChips(x-1, y+1, 1, player);  // left down
                    else ret=0;
                } else ret=0;
                break;
            case 2: if (y<HEIGHT-1) {
                    if (mem[x+(y+1)*WIDTH]==player) ret=1+getChips(x, y+1, 2, player);      // down
                    else ret=0;
                } else ret=0;
                break;
            case 3: if (x<WIDTH-1 && y<HEIGHT-1) {
                    if (mem[x+1+(y+1)*WIDTH]==player) ret=1+getChips(x+1, y+1, 3, player);  // right down
                    else ret=0;
                } else ret=0;
                break;
            case 4: if (x<WIDTH-1 && y>=0) {
                    if (mem[x+1+y*WIDTH]==player) ret=1+getChips(x+1, y, 4, player);        // to the right
                    else ret=0;
                } else ret=0;
                break;
            case 5: if (x<WIDTH-1 && y>0) {
                    if (mem[x+1+(y-1)*WIDTH]==player) ret=1+getChips(x+1, y-1, 5, player);  // up right
                    else ret=0;
                } else ret=0;
                break;
            case 7: if (x>0 && y>0) {
                    if (mem[x-1+(y-1)*WIDTH]==player) ret=1+getChips(x-1, y-1, 7, player);  // up left
                    else ret=0;
                } else ret=0;
                break;
        }
        return(short) ret;
    }

    /** Returns the index of the biggest value in the given array.
    */
    private static int getBiggest(short[] array) {
        if (array.length==0) return -1;                             // if the array is empty
        int biggest = -1;                                           // the value to return
        for (int count = 0; count<array.length; count++) {          // for all indices
            if (biggest==-1) {                                      // as long as there was no value bigger than 0
                if (array[count]>0) biggest = count;                // save biggest value
            } else {
                if (array[count]>array[biggest]) biggest = count;   // save biggest value
            }
        }
        return biggest;                                             // return biggest value
    }

    /**
     * Returns the maximum number of chips placed in a streak around a chip.
     *
     * @param chips  an array including all the values returned by <code>getChips</code>.
     * @return the longest streak around this position
     */
    private static int howManyInAStreak(short[] chips) {
        if (chips.length==0) return 0;          // if the array is empty
        int biggest = 0;                        // the value t return
        for (int count = 0; count<4; count++) { // for all indices in the array
            if (chips[count]+chips[count+4]>biggest) biggest = chips[count]+chips[count+4];
            // sum up all chips that are standing opposite
        }
        return biggest;                         // return longest streak
    }

    /**
     * This method is called when the human player has thrown a chip.
     * It figures out the best position for the computer's chip.
     */
    private static void computersTurn() {
        int x = 0;                                  // current column
        short[] priority      = new short[WIDTH];   // longest player streak in every column
        short[] compStreaks   = new short[WIDTH];   // longest computer streak in every column
        short[] playerChips   = new short[8];       // player chips placed around every position
        short[] computerChips = new short[8];       // computer chips placed around every position

        for (x = 0; x<WIDTH; x++) {                 // for all columns
            int y = HEIGHT-1;
            while (y>=0 && mem[x+y*WIDTH]>0) {         // search free space in this column
                y--;
            }
            if (y<HEIGHT-1 && mem[x+(y+1)*WIDTH]==1) { //  if upper chip in this column is a player's chip
                for (int chipcount = 0; chipcount<playerChips.length; chipcount++) {
                    playerChips[chipcount] = getChips(x, y+1, chipcount, (byte)1);
                }
                if (howManyInAStreak(playerChips)>2) {              // if there are at least three chips around this chip
                    playerwins = true;                              // player wins
                    return;                                         // go back
                }
            }
            if (y>=0) {                                             // when row is not full
                for (int chipcount = 0; chipcount<playerChips.length; chipcount++) {
                    playerChips[chipcount] = getChips(x, y, chipcount, (byte)1);    // player chips around x,y
                    computerChips[chipcount] = getChips(x,y, chipcount,(byte)2);    // computer chips aroud x,y
                }
                priority[x]  = (short) howManyInAStreak(playerChips);       // longest player streak in column x
                compStreaks[x] = (short) howManyInAStreak(computerChips);   // longest computer streak in column x
            }
        }                                       // end for all columns
        int biggestPlayerStreak = getBiggest(priority);             // get longest player streak
        int biggestComputerStreak = getBiggest(compStreaks);        // get longest computer streak

        if (biggestComputerStreak!=-1 && compStreaks[biggestComputerStreak]>2) { // if computer has three chips in a streak
            throwChip(biggestComputerStreak, false);                // complete streak to four chips
            computerwins = true;                                    // computer wins
            return;                                                 // go back to main()
        }
        if (priority[biggestPlayerStreak]>2) {                      // if player has three chips in a streak
            throwChip(biggestPlayerStreak, false);                  // block it
            return;                                                 // go back to main()

        } else {                                                    // computer and player have at most two chips in a streak
            x = (priority[biggestPlayerStreak]>level?biggestPlayerStreak:-1); //if player has too many chips in a streak, save column else -1
            int col = (x==-1?(biggestComputerStreak==-1?findNextFreeCol(-1):biggestComputerStreak):x); // possible column
            int cols = getFreeCols();                               // number of free columns
            for (int count = 0; count<cols; count++) {              // for all free columns
                int y = HEIGHT-1;
                while (y>0 && mem[col+y*WIDTH]>0) {                      // search free space in this column
                    y--;
                }
                if (y>0) {                                          // if free space in this column is greater than 1
                    for (int chipcount = 0; chipcount<playerChips.length; chipcount++) {
                        playerChips[chipcount] = getChips(col, y-1, chipcount, (byte)1);
                        // get player chips around this position, if computer would throw the chip here
                    }
                    if (howManyInAStreak(playerChips)<3 || (count==cols-1)) { // no danger or no more free column
                        throwChip(col, false);                      // throw chip
                        return;                                     // go back to main()
                    } else {
                        col = findNextFreeCol(col);                 // try next free column
                    }
                } else {                                            // no danger
                    throwChip(col, false);                          // throw chip
                    return;                                         // go back to main()
                }
            }
        }
    }

    /** Returns the number columns with space for at least one chip.
    */
    private static int getFreeCols() {
        int count = 0;                  // the value to return
        for (int c=0; c<WIDTH; c++) {   // for all columns
            if (mem[c+0*WIDTH]==0) {    // if there is space
                count++;                // one more column
            }
        }
        return count;                   // return value
    }

    /**
     * Returns the next free column.
     *
     * @param col    the column the search starts
     * @return
     */
    private static int findNextFreeCol(int col) {
        int search = (col+1>WIDTH-1?0:col+1);      // start here
        while (mem[search+0*WIDTH]>0) {            // while column is full
            search = (search+1)%WIDTH;             // next colum
        }
        return search;                             // return result
    }

    /** Paint the board.
    */
    private static void paint() {
        String[] circle={"\uFFC7\u8301\u0101\u83C7"};
        lcd.drawLine((WIDTH*8+XOFFSET), YOFFSET, (WIDTH*8+XOFFSET), (YOFFSET+8*HEIGHT));    // draw vertical line
        lcd.drawLine((XOFFSET), (HEIGHT*8+YOFFSET), (XOFFSET+8*WIDTH), (HEIGHT*8+YOFFSET)); // draw horizontal line
        for (int x = 0; x<WIDTH*HEIGHT; x++) {
            lcd.drawImage(circle,((x%WIDTH)*8+XOFFSET), ((x/WIDTH)*8+YOFFSET), 8,8,0,0);                        // draw holes
        }
        repaint();    // repaint the chips and the arrow
        paintLevel(); // paint the level

    }

    /** Writes the level in the lower right corner.
    */
    private static void paintLevel() {
        lcd.setFont(Display.SYSTEMFONT);
        lcd.drawString("LEVEL", 65, 35);
//        lcd.setFont(Display.LARGEFONT);
        switch (level) {
            case BEGINNER: lcd.drawString("beginner", 65, 45);break;
            case ADVANCED: lcd.drawString("advanced", 65, 45);break;
            case PRO     : lcd.drawString("pro", 65, 45);     break;
        }
    }

    /** Remove the chips.
    */
    private static void clearMem() {
        for (int x = 0; x<WIDTH*HEIGHT; x++) {
            mem[x] = 0;
        }
        chipNumber   = HEIGHT*WIDTH; // all available chips
    }

    /**
     * Throws a chip in a specified column.
     *
     * @param col    the column
     * @param player true:player's chip, false:computer's chip
     * @return true if the chip has been thrown successfully.
     */
    private static boolean throwChip(int col, boolean player) {
        int row = HEIGHT-1;
        while (mem[col+row*WIDTH]>0) {  // search the first free space in this column
            if (row==0) return false;   // no free space
            row--;
        }
        chipNumber--;                   // one chip less available
        drawChip(col, row, player);     // draw the chip
        if (player) {                   // if player
            mem[col+row*WIDTH] = 1;     // draw player's chip
        } else {                        // if computer
            mem[col+row*WIDTH] = 2;     // draw computer's chip
        }
        return true;                    // chip thrown
    }

    /** Repaint the arrow and the chips.
    */
    private static void repaint() {
        drawArrow();                        // draw the arrow
        for (int x = 0; x<WIDTH; x++) {
            for (int y = 0; y<HEIGHT; y++) {
                switch (mem[x+y*WIDTH]) {
                    case 0: ;break;                     // no chip
                    case 1: drawChip(x,y,true); break;  // player chip
                    case 2: drawChip(x,y,false);break;  // computer chip
                }
            }
        }
    }

    /** Draw the arrow to the current column.
    */
    private static void drawArrow() {
        lcd.clearRect(XOFFSET, 0, (WIDTH*8), 7);                 // clear arrow background
        lcd.drawImage(new String[]{"\u0818\u3F7F\u3F18\u0800"}, (XOFFSET+1+column*8), 0, 7, 7,0,0); // draw the arrow image
    }

    /**
     * Draw a chip to the specified position.
     *
     * @param x      the x-coordinate
     * @param y      the y-coordinate
     * @param player true:player chip, false:computer chip
     */
    private static void drawChip(int x, int y, boolean player) {
        if (player) lcd.drawImage(new String[]{"\u7F63\u5D5D\u5D63\u7F00"}, (x*8+XOFFSET+1), (y*8+YOFFSET+1), 7, 7,0,0);   // draw player chip image
        else        lcd.drawImage(new String[]{"\u7F6B\u556B\u556B\u7F00"}, (x*8+XOFFSET+1), (y*8+YOFFSET+1), 7, 7,0,0); // draw computer chip image
    }

    /** The main method. */
    public static void main(String[] args) {
        lcd    = new Display();                 // init display
        keys = new Keyboard();
        go(lcd,keys);
    }

    /**
     * This method can be run from a global menu context, using the global Keyboard and Display objects.
     *
     * @param lcd    the Display
     * @param keys   the Keyboard
     */
    public static void go(Display lcd,Keyboard keys){
        Connect4.lcd=lcd;
        Connect4.keys=keys;
        try {
            lcd.drawImage(new Resource("Connect4.jcif"),1,0);
        } catch (IOException e) {}
        keys.read();
        lcd.clearDisplay();
        lcd.drawImage(new String[]{
                          "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u8060\u304C\u9E31\u61C1\u8181\u830D\u1933\uFF00\u0000",
                          "\uE0F8\u7C1E\u0707\u033F\u1E00\u0080\uC060\u40C0\u8000\u00C0\uC080\u4040\uC080\u0000\uC0C0\u8040\u40C0\u8000\u8080\u8040\u40C0\uA018\u048F\uD9D2\u4458\u8807\u6160\uFEFC\u414C\u1830\uFF00\u0000",
                          "\u070F\u1818\u1818\u1C0C\u0603\u070F\u1818\u1C0F\u0700\u001F\u1F01\u0000\u1F1F\u0000\u1F1F\u0100\u001F\u1F0F\u0F19\u1919\u59D9\uCD40\u4F5F\u5958\uD8D8\u4C44\uC800\u1F1F\u10C0\u8061\uC345\u497F",
                          "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u090C\u0A09\u0008\u0007\u0808\u0700\u0F0C\u0808\u0808\u090B\u0F00\u0000"},
                      64,4,64,28,0,0);
        init();                     // init
        clearMem();                 // delete all chips
        paint();                    // paint all
        char c = 0;
        Ende:
        for (;;) {                  // the main loop
            drawArrow();            // draw the arrow
            c = keys.read();        // wait for keypress
            switch (c) {
                case 'L':
                case 'U':
                case 'u': column = (column<=0?(byte)(WIDTH-1):(byte)(column-1)); // shift arrow left
                    break;
                case 'R':
                case 'D':
                case 'd': column = (column>=WIDTH-1?(byte)0:(byte)(column+1));   // shift arrow right
                    break;
                case 'M':
                case 'S': if (throwChip(column, true)) {        // if chip was thrown
                        if (chipNumber<=0) {                    // if no more space
                            gameOver();                         // game over (draw)
                        } else {
                            computersTurn();                    // computer's turn
                        }
                        if (playerwins || computerwins || chipNumber<=0) { // if anybody wins or no more chip
                            if (gameOver()=='S') {
                                init();                     // init
                                clearMem();                 // delete all chips
                                paint();
                            } else break Ende;
                        }
                    }
                    break;
            }
        }
    }

    /** This method tells you that the game is over.
    */
    private static char gameOver() {
        if (playerwins) {                         // if player is winner
            lcd.drawString("You won!", 64, 34);   // write it down
            level=(level<=0?0:level-1);           // next level
        } else if (computerwins) {                     // if computer is winner
            lcd.drawString("JControl wins", 64, 34);   // write it down
        } else {                                       // if no one wins
            lcd.drawString(" Draw! ", 64, 34);           // draw game
        }
        lcd.setFont(Display.SYSTEMFONT);
        lcd.drawString("Press to continue", 64,43);
        lcd.drawString("Move to quit   ", 64, 50);
        char c = keys.read();        // wait for keypress
        playerwins = false;
        computerwins = false;
        lcd.clearRect(59,34,68,30);  // clear display
        return c;
    }
}
