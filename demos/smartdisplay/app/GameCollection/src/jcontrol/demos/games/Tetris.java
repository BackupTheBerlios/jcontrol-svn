/*
 * Tetris.java
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
import jcontrol.lang.ThreadExt;

/**
 * This is a classical Tetris. Try to stack the falling pieces in such a way that they will build a complete row.
 * Completed rows will be deleted. You can move the pieces with left/right. Turn them with up/select. The more rows you
 * kill at once, the more points you will get.
 *
 * @author Marcus Timmermann
 * @version 2.0
 */
public class Tetris {
    private static final String[] BLOCK = new String[]{"\u080E\u0E0F"};
    private static final String[] CHECK = new String[]{"\u050A\u050A"};

    private static final int XOFFSET   = 25;      // distance from the left border
    private static final int YOFFSET   = 1;       // distance from the upper border
    private static final int WIDTH     = 12;      // field width
    private static final int HEIGHT    = 15;      // field height
    private static final int FULLROWS  = 5;       // change level after 5 rows

    public static boolean  pause = false;
    public static int  fastdown = 0;
    private static int   waitTime  = 500;         // current waiting time
    private static int   COUNTDOWN = FULLROWS;    // rows left until level changes
    public  static boolean gameOver  = false;     // the game is not over yet
    private static int   level     = 0;           // initial level
    private static int   score     = 0;           // initial score
    public  static int   X, Y;                    // position of current piece

    public  static boolean     cont  = true;                   // continue when game is over?    
    public  static boolean     moving = false;                 // flag if piece is moving
    private static boolean[][] mem;
    private static boolean[][] nextPiece;
    public  static boolean[][] currPiece;           // current piece and next piece    
    private static Display     lcd  = null;                    // init display
    public  static Keyboard    keys = null;                    // init keyboard


    /** Transforms the blocks defined as Strings into boolean[][]
    */
    private static boolean[][] convertPiece(String block) {
        boolean[][] piece = new boolean[4][4];            // piece init        
        for (int x = 0; x<4; x++) {
            switch (block.charAt(x)) {
                case '1':piece[x][3] = true;          // 0001
                    break;
                case '2':piece[x][2] = true;          // 0010
                    break;
                case '4':piece[x][1] = true;          // 0100
                    break;
                case '6':piece[x][1] = true;  piece[x][2] = true; // 0110
                    break;
                case '7':piece[x][1] = true;  piece[x][2] = true;  piece[x][3] = true; // 0111
                    break;
            }
        }
        boolean[][] helpPiece = new boolean[4][4];
        int turn = Math.rnd(4);
        for (int x = 0 ; x<4 ; x++) {
            for (int y = 0 ; y<4 ; y++) {
                switch (turn) {
                    case 0: helpPiece[x][y] = piece[x][y];break;
                    case 1: helpPiece[x][y] = piece[3-y][x]; break;
                    case 2: helpPiece[x][y] = piece[3-x][3-y]; break;
                    case 3: helpPiece[x][y] = piece[y][3-x]; break;
                }
            }
        }
        return helpPiece;                  // return the converted (turned) piece
    }


    public static boolean[][] getPiece() {
        boolean[][] piece = null;                    // piece init
        switch (Math.rnd(7)) {
            case 0: 
                // ****
                piece = convertPiece("4444");break;
            case 1: 
                //  *
                // ***
                piece = convertPiece("0262");break;
            case 2: 
                // **
                //  **
                piece = convertPiece("0462");break;
            case 3: 
                //  **
                // **
                piece = convertPiece("2640");break;
            case 4: 
                // **
                // **
                piece = convertPiece("0660");break;
            case 5: 
                // ***
                // *
                piece = convertPiece("0710");break;
            case 6: 
                // ***
                //   *
                piece = convertPiece("0170");break;
        }
        return piece;
    }


    /** Turns the current piece.
    */
    public synchronized static void turnCurrPiece(boolean right) {
        synchronized (currPiece) {
            {
                boolean[][] piece = new boolean[4][4];           // define new piece            
                for (int x = 0 ; x<4 ; x++) {
                    for (int y = 0 ; y<4 ; y++) {

                        if (right) piece[x][y] = currPiece[3-y][x];     // turn right
                        else       piece[x][y] = currPiece[y][3-x];     // turn left
                        if (piece[x][y] && ((X+x)>=WIDTH || (y+Y)>=HEIGHT || (X+x)<0)) {
                            return;                        
                        }
                        if ((Y+y)>0)
                            if (piece[x][y] && (mem[X+x][Y+y])) return; // do not turn!
                    }
                }                      
                drawPiece(false);
                currPiece = piece;
            }
            drawPiece(true);
            // current piece was turned
        }
    }


    /** This method is called when the 'down button' was pressed.
    */
    public synchronized static void moveDown() {
        while (movePiece(X, Y+1)) { // move down as far as possible
            moving = true;
            try {
                ThreadExt.sleep(10);
            } catch (InterruptedException e) {}
        }
        moving = false;
    }



    /**
     * This method moves a piece to the given position when possible.
     *
     * @param x      the x-coordinate of destination position
     * @param y      the y-coordinate of destination position
     * @return true if moving was successfull, false otherwise.
     */
    public synchronized static boolean movePiece(int x, int y) {
        {
            for (int x1 = 0; x1<4; x1++) {
                for (int y1 = 0; y1<4; y1++) {
                    if ((((y+y1)>=HEIGHT) ||           // do not move if piece would be moved
                         ((x+x1)>=WIDTH)  ||           // out of bounds
                         ((x+x1)<0) )  && (currPiece[x1][y1]) ) {
                        return false;
                    }
                    if ((y+y1)>=0) {
                        if (currPiece[x1][y1] && mem[x+x1][y+y1]) {
                            return false;
                        } // do not move when collision happens
                    }
                }
            }
        }
        drawPiece(false);
        X = x;
        Y = y;
        drawPiece(true);
        return true;
    }

    public static boolean isAtTop() {        
        for (int x1 = 0; x1<4; x1++) {
            for (int y1 = 0; y1<4; y1++) {
                if ((Y+y1)>=0) {
                    if (currPiece[x1][y1] && mem[X+x1][Y+y1]) return true; // do not move when collision happens
                }
            }
        }
        return false;
    }



    /** Paints the preview piece.
    */
    public static void paintNextPiece() {
        lcd.clearRect(XOFFSET+61,YOFFSET+41,16,16);
        for (int x1 = 0; x1<4; x1++) {                      // draw a 4x4 field
            for (int y1 = 0; y1<4; y1++) {
                if (nextPiece[x1][y1]) lcd.drawImage(BLOCK, XOFFSET+1+(x1<<2)+60,YOFFSET+1+(y1<<2)+40,4,4,0,0); // draw block
            }
        }

    }

    /** Draws a simple rectangle as basic part of piece.
    */
    private synchronized static void drawBlock(int x, int y, boolean onoff) {
        if (x<0 || y<0 || x>(WIDTH<<2)) return;
        synchronized (lcd) {
            if (onoff) lcd.drawImage(BLOCK,(XOFFSET+1+x),(YOFFSET+1+y),4,4,0,0);
            else lcd.clearRect((XOFFSET+1+x),(YOFFSET+1+y),4,4);
        }
    }


    public synchronized static void drawPiece(boolean onoff) {
        synchronized (lcd) {
            lcd.setDrawMode(Display.NORMAL);
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    if (currPiece[x][y]) drawBlock((x+X)<<2, (y+Y)<<2, onoff);

                }
            }
        }

    }


    /** Finds full lines and calculates new score and level.
    */
    public static void findFullLine() {
        int count = 0;                      // counts full lines
        {
            boolean full;
            for (int y = HEIGHT-1; y>=0; y--) { // from lower line to upper line
                full = true;                    // assert line is full
                for (int x = 0; x<WIDTH; x++) {
                    if (!mem[x][y]) {
                        full = false;           // oh, line is not full
                        break;                  // go away from loop
                    }
                }
                if (full) {                     // if current line is full
                    for (int xl=0; xl<WIDTH; xl++) {
                        lcd.drawImage(CHECK, XOFFSET+1+(xl<<2),YOFFSET+1+((y-count)<<2), 4,4,0,0);
                    }
                    count++;                    // one more line
                    for (int yc = y-1; yc>=0; yc--) { // delete this line from background memory
                        for (int xc = 0; xc<WIDTH; xc++) {
                            mem[xc][yc+1] = mem[xc][yc];
                        }
                    }
                    y++;                        // try the same line again

                }
            }
        }
        if (count>0) {                      // if some full lines were found
            try {
                ThreadExt.sleep(300);
            } catch (InterruptedException e) {}
            lcd.clearRect(26, 2,    // draw big rectangle
                          (48),
                          (60));

            for (int xc = 0; xc < WIDTH; xc++) {
                for (int yc = 0; yc < HEIGHT; yc++) {
                    if (mem[xc][yc] )drawBlock(xc<<2,yc<<2, mem[xc][yc]);    // draw visible block
                }
            }
            score = (score+(count * ((WIDTH<<1) + level))); // new score
            COUNTDOWN-=count;                    // next level will come soon
            if (COUNTDOWN<1) {
                level++;                     // increase level
                COUNTDOWN = FULLROWS;        // new countdown
                waitTime = waitTime/2*90/50; // speed of pieces increases
            }
        } else {
            score = (score + level +1);      // no full line but a few points indeed
        }

    }

    public static void main(String[] args) {
        go(new Display(),new Keyboard());
    }

    public static void go(Display lcd, Keyboard keys){

        Tetris.lcd    = lcd;                 // init display
        Tetris.keys = keys;

        lcd.setFont(Display.SYSTEMFONT);        
        try {
            lcd.drawImage(new Resource("Tetris.jcif"), 0,0);
        } catch (java.io.IOException e) {
        }
        keys.read();        
        paintBackground();                   // paint background
        X=Math.rnd(WIDTH-4);                 // position of piece when appearing
        Y=-2;
        mem = new boolean[WIDTH][HEIGHT];    // background memory (stack pieces)        
        currPiece = getPiece();        // current piece
        nextPiece = getPiece();        // next piece
        paintNextPiece();                               // paint preview for next piece
       	boolean statePause = false;
        lcd.drawString(String.valueOf(level+1), 105, 5); // draw level
        lcd.drawString(String.valueOf(score), 105, 15);  // draw score
        (new Tetris$Keyrequest()).start();                      // start keyboard request
        while (cont) {
            try {
            	if (fastdown>0) {
	                fastdown=0;
	                ThreadExt.sleep(50);   // wait between actions (speed)
	            } else {
	            	ThreadExt.sleep(waitTime);
	            }
            } catch (InterruptedException e) {}
            if (!pause && !moving) {
                if (!Tetris.movePiece(X, Y+1)) {                       // move one down if possible
                    lcd.setDrawMode(Display.XOR);
                    lcd.drawString(String.valueOf(level+1), 105, 5);   // delete level
                    lcd.drawString(String.valueOf(score), 105, 15);    // delete score
                    lcd.setDrawMode(Display.NORMAL);
                    for (int x1 = 0; x1<4; x1++) {
                        for (int y1 = 0; y1<4; y1++) {
                            if (Y+y1>=0) {
                                if (currPiece[x1][y1]) mem[X+x1][Y+y1] = true;     // copy into background mem
                            }
                        }
                    }
                    findFullLine();                     // delete full lines if possible                              // position of piece when it appears
                    lcd.drawString(String.valueOf(level+1), 105, 5);   // draw level
                    lcd.drawString(String.valueOf(score), 105, 15);  // draw score
                    currPiece= nextPiece;// next piece becomes current piece
                    nextPiece = getPiece();       // get new next piece
                    paintNextPiece();                   // next piece preview
                    X=Math.rnd(WIDTH-4);
                    Y=-2;

                    if (isAtTop()) {
                        waitTime = 500;                 // level 0 speed
                        level = 0;                      // level 0
                        score = 0;                      // score 0
                        GameOver();                     // draw splash screen
                        lcd.drawString(String.valueOf(level+1), 105, 5);   // draw level
                        lcd.drawString(String.valueOf(score), 105, 15);    // draw score
                    }
                }
            }
            if (pause || statePause) {
				if (!statePause) {
            			lcd.drawString("PAUSE", 95, 23);
            			statePause=true;
            	} else {
	            		lcd.setDrawMode(Display.XOR);
	           			lcd.drawString("PAUSE", 95, 23);
	           			lcd.setDrawMode(Display.NORMAL);
            			//if (!pause) statePause=2; else statePause=0;
            			statePause=false;
            	}
            }
            
            
        }
        lcd.clearDisplay();
    }

    public static void paintBackground() {
        lcd.clearDisplay();
        lcd.drawRect(25, 1, 50, 62); // draw big rectangle
        try {
            lcd.drawImage(new Resource("JControl_logo.jcif"), 105, 38); // draw JControl image
            lcd.drawImage(new Resource("Tetris_logo.jcif"), 0, 0);   // draw Tetris image
        } catch (java.io.IOException e) {
        }
        lcd.drawString("Level  ", 80, 5);     // draw level text
        lcd.drawString("Score  ", 80, 15);    // draw score text        
        lcd.drawString("Next", 80,  30);      // draw next piece text        
    }

    /** Draws a splash screen to tell the player that the game is over.
    */
    public static void GameOver() {
        gameOver = true;
        int x = 10;
        int y = 10;                        // position of splash screen
        lcd.drawRect(x, y, 110, 35);
        lcd.clearRect((x+1),(y+1), 108, 33);
        lcd.setDrawMode(Display.INVERSE);
        lcd.drawString(" Game over! ", (x+25), (y+5));  // draw text
        lcd.setDrawMode(Display.NORMAL);
        lcd.drawString("Press up/down to continue,", (x+5), (y+15));// draw text
        lcd.drawString("any other key to quit.", (x+10), (y+23));// draw text
        try {
            while (gameOver) {
                ThreadExt.sleep(10);
            }
        } catch (InterruptedException e) {}
        if (cont) {
            paintBackground();                         // draw new background
            for (int x1 = 0; x1 < WIDTH; x1++) {
                for (int y1 = 0; y1 < HEIGHT; y1++) {
                    mem[x1][y1] = false;         // all old blocks
                }
            }
        }

    }


}

/** This thread is the loop for continuous keyboard request.
*/
class Tetris$Keyrequest extends Thread {

    public void run() {
        while (Tetris.cont) {                           // the main thread loop
			char c;
			do {
				c = (Tetris.keys).getKey();	// wait for keypress
				if (c==0) try { ThreadExt.sleep(10); } catch (InterruptedException e) {}
			} while (c==0);

            this.setPriority(Thread.MAX_PRIORITY);            
            if (Tetris.gameOver) {                 // if splash screen
                Tetris.gameOver = false;           // new game
                switch (c) {
                    case 'u':
                    case 'd':
                    case 'U':
                    case 'D': Tetris.cont = true; break;
                    default : Tetris.cont = false; break;
                }
            } else {
                Tetris.moving = true;
                switch (c) {
                    case 'M':
                        if (!Tetris.pause) Tetris.turnCurrPiece(false);
                        break;
                    case 'S': 
                    	if (!Tetris.pause) Tetris.turnCurrPiece(true);
                        break;
                    case 'R':
                    	if (!Tetris.pause) Tetris.movePiece(Tetris.X+1, Tetris.Y);
                    	break;
                    case 'D':
                    case 'd':
                    	Tetris.fastdown=1; 
                        break;
                    case 'L':
	                    if (!Tetris.pause) Tetris.movePiece(Tetris.X-1, Tetris.Y);
	                    break;
                    case 'U':
                    case 'u':
                    	Tetris.pause=!Tetris.pause;                     	
                        break;
                }
                Tetris.moving = false;
                this.setPriority(Thread.NORM_PRIORITY);
            }

        }
    }
}
