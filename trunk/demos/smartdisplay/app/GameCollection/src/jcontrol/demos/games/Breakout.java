/*
 * Breakout.java
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
import jcontrol.lang.ThreadExt;

/**
 * In <CODE>Breakout</CODE> the player's job is to shoot up all the blocks on the court
 * by moving his paddle to the left and right.
 * The ball may not pass the bottom line. You may lose your ball at most four times. When you've hit all blocks you
 * will get into the next level.
 *
 * @author Marcus Timmermann
 * @version $Revision$
 */
public class Breakout {

    public static final String[] BALL_IMG = new String[]{"\u001C\u323A\u3E1C\u0000"};

    private static final int      WIDTH  = 98;      // width of board in pixels
    private static final int      HEIGHT = 62;      // height of board in pixels

    public  static final short BALLSPEED   = 10;    // the initial ball speed
    private static final byte  MAXLIVES    = 4;     // max available new balls
    private static final int   BLOCKXCOUNT = 7;     // horizontal blocks
    private static final int   BLOCKYCOUNT = 5;     // vertical blocks
    private static final int   BLOCKWIDTH  = 14;    // block width
    private static final int   BLOCKHEIGHT = 5;     // block height

    private static boolean[] BLOCKS = new boolean[BLOCKXCOUNT*BLOCKYCOUNT];  // blocks to be hit


    public  static int    paddelPos = 20;         // pos of paddel at start
    public  static int    ballX, ballY;           // ball coordinates
    public  static int    dx, dy;                 // ball trajectory
    private static int    blockcount = 0;         // hit blocks
    private static byte   lives  = MAXLIVES;      // live countdown
    private static int    score = 0;              // score
    private static int    level = 0;              // level
    public  static int    ballspeed = BALLSPEED;  // the higher the slower
    private static int      wasOnPaddel = 1;      // counts the blocks hit after the ball was on the paddel
    public  static boolean  active = true;        // flag for the main loop
    public  static boolean  go = true;            // flag for ball loop
    public  static boolean  won = false;          // true if you won
    public  static Display  lcd;
    public  static Keyboard keys;





    /** Calculates the new ball trajectory after a collision.
    */
    public static void reflectBall() {
        int bx = ballX+1,
        by = ballY+1;

        if (ballX>=WIDTH-4) {   // reflect from right border
            dx = (-dx);
            ballX=WIDTH-7;
        } else if (ballX<1) {   // reflect from left border
            dx = (-dx);
            ballX=2;
        } else if (ballY<1) {   // reflect from top border
            dy = (-dy);
            ballY=2;
        } else if (ballY>=HEIGHT-5) {                             // ball goes out
            wasOnPaddel = 1;
            lcd.clearRect(ballX,ballY, 7,7);
            if (lives>-1) {
                paint();
                keys.read();                                      // wait for keypress
                newBall(); return;                                // new ball
            } else go = false;
        } else if (ballX>=paddelPos-5 && ballX<=paddelPos+15 && ballY>=HEIGHT-13 && ballY<=HEIGHT-8) {  // reflect on paddel
            dy = (-dy);
            ballY-=2;
            wasOnPaddel = 1;
            int paddelMid = paddelPos+7;
            if (ballX<paddelMid && (paddelMid-ballX)>4) { // ball is on left corner
                if (dx>0) { // ball comes from left
                    dx--;
                    dy--;
                } else {    // ball comes from right
                    dx--;
                    dy++;
                }
            } else if (ballX>paddelMid && (ballX-paddelMid)>4) { //ball is on right corner
                if (dx>0) { // ball comes from left
                    dx++;
                    dy++;
                } else {    // ball comes from right
                    dx++;
                    dy--;
                }
            }

        } else {
            int hits = collisionWithBlock(true);          // reflect on block
            if (hits>0) {                                 // if collision with  block
                blockcount-=hits;                         // decrease number of blocks left
                score+= wasOnPaddel*(level+1+hits);       // calculate new score
                wasOnPaddel*=2;
                if (blockcount<=0) {                      // if no more block
                    go = false;                           // stop the ball
                    won = true;                           // won!
                }
            }
            if (dy>0) {             // ball comes from top
                dy = (-dy);
                ballY-=2;
            } else {                // ball comes from bottom
                dy = (-dy);
                ballY+=2;
            }

        }
        synchronized (lcd) {
            lcd.clearRect(bx ,by, 5, 5);     // delete ball after collision
        }
        drawBall(ballX, ballY);
        paint();        // repaint all

    }

    /** Checks if the ball has collided with something.
    */
    public static boolean collision() {
        boolean onBounds = (ballX>=WIDTH-4 || ballX<1 || ballY<1 || ballY>=HEIGHT); // check bounds
        boolean onPaddel = (ballX>=paddelPos-5 && ballX<=paddelPos+15 && ballY>=HEIGHT-13 && ballY<=HEIGHT-8); // check paddel
        boolean onBlock = collisionWithBlock(false)>0;
        return onBounds || onPaddel || onBlock || !go;
    }

    public static int getBlockAt(int x, int y) {
        if (x<0 || x>=BLOCKXCOUNT) return 0;
        if (y<0 || y>=BLOCKYCOUNT) return 0;
        return BLOCKS[x+y*BLOCKXCOUNT]?1:0;
    }

    /**
     * Checks if the ball would collide with a certain block.
     *
     * @param delete if true, the block that is hit will be deleted
     * @return 1 on collision, 0 otherwise
     */
    private static int collisionWithBlock(boolean delete) {
        int x1 = (ballX-1)/BLOCKWIDTH;
        int x2 = (ballX+6)/BLOCKWIDTH;
        int y1 = (ballY-5)/BLOCKHEIGHT;
        int y2 = (ballY+2)/BLOCKHEIGHT;
        int result = 0;
        if (x1==x2) {
            if (y1==y2) {
                // one block
                result = getBlockAt(x1,y1);
                if (delete) drawBlock(x1, y1, false);

            } else {
                // two blocks
                result = getBlockAt(x1,y1) + getBlockAt(x1,y2);
                if (delete) {
                    drawBlock(x1, y1, false);
                    drawBlock(x1, y2, false);
                }
            }
        } else {
            if (y1==y2) {
                // two blocks
                result = getBlockAt(x1,y1) + getBlockAt(x2,y1);
                if (delete) {
                    drawBlock(x1, y1, false);
                    drawBlock(x2, y1, false);
                }
            } else {
                // three (four) blocks
                result = getBlockAt(x1,y1) + getBlockAt(x1,y2) + getBlockAt(x2,y1) + getBlockAt(x2,y2);
                if (delete) {
                    drawBlock(x1, y1, false);
                    drawBlock(x1, y2, false);
                    drawBlock(x2, y1, false);
                    drawBlock(x2, y2, false);
                }
            }

        }

        return result;
    }



    /** Makes a new ball falling out from the left border with 45 degrees downwards.
    */
    public static void newBall() {
        ballX = 1;      // the position
        ballY = 40;
        dx = (paddelPos+7);
        dy = ((HEIGHT-8)-ballY);        // the angle
        lives--;        // decrease lives
        if (lives<-1) go = false; // if no more live, stop

    }

    /** Moves the paddel about one pixel.
    */
    public static void movePaddel(boolean right) {
        if (right) {                    // move to the right
            paddelPos++;
            if (paddelPos>(WIDTH-16)) { // don't move any further
                paddelPos = (WIDTH-16);
            }
        } else {                        // move to the left
            paddelPos--;
            if (paddelPos<1) {          // don't move any further
                paddelPos=1;
            }
        }
        drawPaddel();   // draw the paddel
    }

    /**
     * Paints all blocks and images on the board.
     * This method is called before every new game.
     */
    public synchronized static void paintBlocks() {
        blockcount = 0;                             // no blocks hit
        for (int x = 0; x<BLOCKXCOUNT; x++) {       // for all blocks
            for (int y = 0; y<BLOCKYCOUNT; y++) {
                drawBlock(x,y,true);                // draw block
                BLOCKS[x+y*BLOCKXCOUNT] = true;     // block is not hit yet
                blockcount++;
            }
        }
        lcd.drawString("level", (WIDTH+5), 5);  // level
        lcd.drawString("score", (WIDTH+5), 27); // score
        lcd.drawString("lives", (WIDTH+5), 49); // lives
    }

    private static final String[] BLOCKIMG=new String[]{"\u1F11\u1919\u1919\u1919\u1919\u1919\u1D1F"};
    /**
     * Draws a certain block on the board.
     *
     * @param x      the x-position of block
     * @param y      the y-position of block
     * @param onoff  paint or delete it
     */
    private static void drawBlock(int x, int y, boolean onoff) {
        if (x<0 || x>=BLOCKXCOUNT) return;
        if (y<0 || y>=BLOCKYCOUNT) return;
        synchronized (lcd) {
            BLOCKS[x+y*BLOCKXCOUNT] = onoff; // set the block
            if (onoff) {          // if paint
                lcd.drawImage(BLOCKIMG, (x*BLOCKWIDTH+1), (y*BLOCKHEIGHT+5), BLOCKWIDTH, BLOCKHEIGHT, 0,0);
            } else {              // if delete
                lcd.clearRect((x*BLOCKWIDTH+1), (y*BLOCKHEIGHT+5), BLOCKWIDTH, BLOCKHEIGHT);
            }
        }
    }

    /** Draws the ball to (x,y).
    */
    public synchronized static void drawBall(int x, int y) {
        synchronized (lcd) {
            if (y<HEIGHT-4) {
                lcd.drawImage(BALL_IMG, x, y, 7, 7,0,0);

            } else if (y==HEIGHT-4) lcd.clearRect(ballX,ballY, 7,7);
            ballX = x; ballY = y;
        }

    }

    private static final String[] PADDLEIMG=new String[]{"\u000E\u1119\u1919\u1919\u1919\u1919\u1919\u1D0E\u0000"};
    /** Draws the paddel to paddelPos.
    */
    public synchronized static void drawPaddel() {
        synchronized (lcd) {
            lcd.drawImage(PADDLEIMG, paddelPos, (HEIGHT-8), 17, 5,0,0);
        }
    }

    /** Repaints the border, the live indication balls, level, score and the paddel.
    */
    public synchronized static void paint() {
        drawPaddel(); // draw the paddel
        synchronized (lcd) {
            lcd.drawRect(0, 0, (WIDTH+2),(HEIGHT+2)); // draw big rectangle
            for (int count = 0; count<MAXLIVES; count++) {  // for all lives
                if (lives>=count)   // draw ball
                    lcd.drawImage(BALL_IMG, (count*6+WIDTH+2), 58, 7, 7,0,0);
                else {              // delete ball
                    lcd.clearRect((count*6+WIDTH+2), 58, 7, 7);
                }
            }
            lcd.drawString("     ",(WIDTH+4), 14); // clear old values
            lcd.drawString("     ",(WIDTH+4), 35);
            lcd.drawString(String.valueOf(level), (WIDTH+5), 14); // draw level
            lcd.drawString(String.valueOf(score), (WIDTH+5), 35); // draw score
        }
    }

    /** Init for the keyboard and some variables.
    */
    public static void init() {
        active = true;                  // flag for the main loop
        go = true;                      // flag for the ball loop
        won = false;                    // not won yet
        score = 0;
        lives = MAXLIVES;               // live countdown

    }

    public static void main(String[] args) {
        lcd = new Display();
        keys = new Keyboard();
        go(lcd, keys);
    }


    /** The main method. */
    public static void go(Display gfx, Keyboard key) {
        lcd = gfx;
        keys = key;
        try {
            lcd.drawImage(new Resource("Breakout.jcif"), 0,0);
        } catch (IOException e) {}
        keys.read();
        lcd.clearDisplay();

        lcd.setFont(Display.SYSTEMFONT);
        init();                 // init
        paint();                // paint all
        paintBlocks();          // paint all blocks

        lcd.drawString("Press a key", 22,35);      // draw text
        lcd.setFont(Display.SYSTEMFONT);
        keys.read();         // wait for keypress
        lcd.clearRect(5, 35, WIDTH-10, 10);
        newBall();              // make a ball
        (new Breakout$Ball()).start();   // start the ball

        while (active) {             // the main loop
            try {
                ThreadExt.sleep(10); // not so fast!
            } catch (InterruptedException e) {}
            switch (keys.getRaw()) {
                case 'R':
                case 'd':
                case 'D': movePaddel(true);break; // move paddel to the right
                case 'L':
                case 'u':
                case 'U': movePaddel(false) ;break; // move paddel to the left
                case 'S': //go=false; active = false;
            }
            if (!go && active) {
                try {
                    ThreadExt.sleep(100);
                } catch (InterruptedException e) {}
                lcd.setDrawMode(Display.INVERSE);                              
                if (won) {
                    GameWon();            // if won
                } else {
                    GameOver();           // if lost
                }
                // prepare for new game or quit
                lives = MAXLIVES;
                char c = keys.read();  // wait for keypress
                lcd.clearDisplay();       // clear display
                won = false;                
                if (c=='S') {
                    go = true; 
                    lcd.setFont(Display.SYSTEMFONT);
                    paint();
                    paintBlocks();
                    newBall();                // new game
                    (new Breakout$Ball()).start();
                } else {
                    go = false;
                    active = false;
                }

            }

        }
    }

    /** This method shows that you've lost the game.
    */
    private static void GameOver() {
        lcd.drawString("  Game Over!  ", 15,30);     // draw text
        lcd.setDrawMode(Display.NORMAL);
        lcd.drawString("Press to restart,", 18,40);      // draw text
        lcd.drawString("move to quit.", 25,48);      // draw text
        level = 0;
        score = 0;
        ballspeed = BALLSPEED;
    }

    /** This method shows that you've won the game.
    */
    private static void GameWon() {
        lcd.drawString("  Congratulations!  ", 8,30);  // draw text
        lcd.setDrawMode(Display.NORMAL);
        lcd.drawString("Press to continue,", 18,40);      // draw text
        lcd.drawString("move to quit.", 25,48);      // draw text

        level++;
        ballspeed = ((ballspeed*90)/100);
    }


}

/**
 * This class moves the ball.
 * It uses Bresenham's Algorithm for drawing lines to calculate the new ball position.
 * This algorithm is designed only for the first octant.
 * All other seven octants do it in the same way but with some plusses and minuses changed.
 */
class Breakout$Ball extends Thread {

    int c1, c2, error;

    public void run() {
        while (Breakout.go) {                            // the secondary loop
            if (Breakout.dx>=0) {                        // 1st half
                if (Breakout.dy>=0) {                    // 1st quadrant
                    if (Breakout.dx>=Breakout.dy) {      // 1st octant
                        c1= 2*Breakout.dy;
                        error = c1 - Breakout.dx;
                        c2 = error - Breakout.dx;
                        while (!Breakout.collision()) {  // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall((Breakout.ballX+1), Breakout.ballY); // one to the right
                                error = error+c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX+1),  (Breakout.ballY+1)); // one to the right, one down
                                error = error+c2;
                            }
                        }
                    } else {                              // 2nd octant
                        c1= 2*Breakout.dx;
                        error = c1 - Breakout.dy;
                        c2 = error - Breakout.dy;
                        while (!Breakout.collision()) {   // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall(Breakout.ballX, (Breakout.ballY+1)); // one down
                                error = error+c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX+1),  (Breakout.ballY+1)); // one to the right, one down
                                error = error+c2;
                            }
                        }
                    }
                } else {                                  // 4th quadrant
                    if (Breakout.dx>=(-Breakout.dy)) {    // 8th octant
                        c1= -2*Breakout.dy;
                        error = c1 - Breakout.dx;
                        c2 = error - Breakout.dx;
                        while (!Breakout.collision()) {   // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall((Breakout.ballX+1), Breakout.ballY);   // one to the right
                                error = error+c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX+1), (Breakout.ballY-1)); // one to the right, one up
                                error = error+c2;
                            }
                        }
                    } else {                              // 7th octant
                        c1= 2*Breakout.dx;
                        error = c1 + Breakout.dy;
                        c2 = error + Breakout.dy;
                        while (!Breakout.collision()) {   // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall(Breakout.ballX, (Breakout.ballY-1));   // one up
                                error = error+c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX+1), (Breakout.ballY-1)); // upe to the right, one up
                                error = error+c2;
                            }
                        }
                    }

                }
            } else {                                      // 2nd half
                if (Breakout.dy>=0) {                     // 2st quadrant
                    if ((-Breakout.dx)>=Breakout.dy) {    // 4th octant
                        c1= 2*Breakout.dy;
                        error = c1 + Breakout.dx;
                        c2 = error + Breakout.dx;
                        while (!Breakout.collision()) {   // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall((Breakout.ballX-1), Breakout.ballY); // one to the left
                                error = error+c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX-1),  (Breakout.ballY+1)); // one to the left, one down
                                error = error+c2;
                            }
                        }
                    } else {                              // 3rd octant
                        c1= 2*Breakout.dx;
                        error = c1 + Breakout.dy;
                        c2 = error + Breakout.dy;
                        while (!Breakout.collision()) {   // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall(Breakout.ballX, (Breakout.ballY+1)); // one down
                                error = error-c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX-1),  (Breakout.ballY+1)); // one to the left, one down
                                error = error-c2;
                            }
                        }
                    }
                } else {                                  // 3rd quadrant
                    if (Breakout.dx<=Breakout.dy) {       // 5th octant
                        c1= -2*Breakout.dy;
                        error = c1 + Breakout.dx;
                        c2 = error + Breakout.dx;
                        while (!Breakout.collision()) {   // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall((Breakout.ballX-1), Breakout.ballY); // one to the left
                                error = error+c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX-1), (Breakout.ballY-1)); // one to the left, one up
                                error = error+c2;
                            }
                        }
                    } else {                              // 6th octant
                        c1= 2*Breakout.dx;
                        error = c1 - Breakout.dy;
                        c2 = error - Breakout.dy;
                        while (!Breakout.collision()) {   // while no collision, move ball
                            try {
                                ThreadExt.sleep(Breakout.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Breakout.drawBall(Breakout.ballX, (Breakout.ballY-1)); // one up
                                error = error-c1;
                            } else {
                                Breakout.drawBall((Breakout.ballX-1), (Breakout.ballY-1)); // one to the left, one up
                                error = error-c2;
                            }
                        }
                    }

                }
            }
            if (Breakout.go) Breakout.reflectBall();      // if still running reflect the ball
        }
    }
}