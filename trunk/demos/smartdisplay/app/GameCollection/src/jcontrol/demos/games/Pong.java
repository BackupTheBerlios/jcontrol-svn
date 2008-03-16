/*
 * Pong.java
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

import java.io.IOException;

import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.Math;
import jcontrol.lang.ThreadExt;

/**
 * This controllet <CODE>Pong</CODE> is a simple game for one player versus
 * the computer. The player's job is to keep the ball inside the court by moving
 * his paddle up and down. The computer player does the same.
 * When the ball goes out on the player's side, he will get a penalty score
 * and lose one ball. When the computer player loses the ball the player will
 * get this ball including a bonus score. After having lost all balls,
 * the player loses the game. After winning all balls, the player will enter the
 * next level.
 *
 * @author Marcus Timmermann
 * @version $Revision$
 */
public class Pong {

    private static final int      WIDTH  = 101;       // width of board
    public  static final int      HEIGHT = 62;        // height of board

    public  static final short MAXLIVES  = 7;         // the maximum number of lives
    public  static final short BALLSPEED = 10;        // the initial ball speed
    public  static int       ballX, ballY;            // ball coordinates
    public  static int       dx, dy;                  // ball trajectory

    public  static short     ballspeed = BALLSPEED;   // the higher the slower
    public  static boolean   gameOver  = false;       // flag for game status
    public  static boolean   go;                      // flag for secondary loops
    public  static int       score, lives, level,     // score, lives, level
    yHit;                    // position where the ball will cross the computer's paddle line
    private static final int USERPADDLE_X = WIDTH-10; // x-coordinate of player's paddle
    public  static final int COMPPADDLE_X = 5;        // x-coordinate of computer's paddle
    public  static int       userPaddle   = HEIGHT/2, // y-coordinate of player's paddle
    compPaddle   = HEIGHT/2; // y-coordinate of computer's paddle

    public static int lastreflect = -1;

    public  static Display   lcd    = null;
    public  static Keyboard   keys   = null;; // init keyboard

    /**
     * Returns if the ball collides with the computer's paddle.
     *
     */
    public static boolean onCompPaddle() {
        boolean result = (ballX>=COMPPADDLE_X && ballX<=COMPPADDLE_X+5 && ballY>=compPaddle-4 && ballY<=compPaddle+19 && lastreflect!=4);
        if (result) lastreflect = 4;
        return result;
    }

    /**
     * Returns if the ball collides with the user's paddle.
     *
     */
    public static boolean onUserPaddle() {
        boolean result = (ballX>=USERPADDLE_X-5 && ballX<=USERPADDLE_X && ballY>=userPaddle-4 && ballY<=userPaddle+19 && lastreflect!=2);
        if (result) lastreflect = 2;
        return result;
    }

    /**
     * Calculates the new ball trajectory after a collision.
     */
    public static void reflectBall() {
        int x = ballX, y = ballY;           // save old ball coordinates to delete ball image
        if (ballX>=WIDTH-4) {               // ball is out, player fail
            score-=((level+1)*6);           // penalty score
            lives--;                        // decrease lives
            if (lives<-1)                   // if no more lives
                gameOver = true;            // game over
            else
                newBall();                  // new ball

            lcd.clearRect(x,y, 9, 9);
            paint();
        } else if (ballX<1) {               // ball is out, computer fail
            score+=((level+1)*5);           // bonus score
            lives++;                        // get one more live
            if (lives>=MAXLIVES) {          // if computer has no more live
                level++;                    // next level
                lives = 3;                  // initial lives
                ballspeed = (short)((ballspeed*95)/100); // make the ball go faster
            }
            lcd.clearRect(x,y, 9, 9);
            paint();
            newBall();                         // new ball

        } else if (lastreflect==4) {           // computer hit
            int dist = ((compPaddle+10)-(ballY+2)); // distance from paddle center
            if (ballY<=1) ballY+=2;
            if (ballY>=HEIGHT-4) ballY-=2;
            dx=-dx;                            // reverse trajectory
            if (dy>0) {                        // ball comes from top
                dx = ((dx*(100+dist)/100)==0?dx:(dx*(100+dist)/100));
                dy = (dy*(100-dist)/100);  // change angle
            } else if (dy<0) {             // ball comes from bottom
                dx = ((dx*(100-dist)/100)==0?dx:(dx*(100-dist)/100));
                dy = (dy*(100+dist)/100);  // change angle
            } else dy=-dist;               // don't move vertically

        } else if (lastreflect==2) {           // player hit
            score+=(level+1);                  // increase player score
            if (ballY<=1) ballY+=2;
            if (ballY>=HEIGHT-4) ballY-=2;
            int dist = ((compPaddle+10)-(ballY+2)); // distance from paddle middle
            dx=-dx;                                 // reverse trajectory
            if (dy>0) {                             // ball comes from top
                dx = ((dx*(100+dist)/100)==0?dx:(dx*(100+dist)/100));
                dy = (dy*(100-dist)/100);       // change angle
            } else if (dy<0) {                  // ball comes from bottom
                dx = ((dx*(100-dist)/100)==0?dx:(dx*(100-dist)/100));
                dy = (dy*(100+dist)/100);       // change angle
            } else dy=-dist;                    // don't move vertically

            int l = lcd.drawString(String.valueOf(score), (WIDTH+4), 33); // draw score
            lcd.drawString("  ", l+(WIDTH+4), 33);

        } else if (lastreflect==1) {                // reflect on top
            if ((dy/dx)>3 || (dy/dx)<-3) dx*=2;     // dx must be big enough
            dy=-dy;                                 // reverse trajectory
        } else if (lastreflect==3) {                // reflect on bottom
            if ((dy/dx)>3 || (dy/dx)<-3) dx*=2;     // dx must be big enough
            dy=-dy;                                 // reverse trajectory
        }

        if (dx<0) {                                 // if ball is on the way to computer's paddle
            yHit = (ballY+(dy*(COMPPADDLE_X+5-ballX))/dx)-9; // calculate the position the ball will cross the
                                                             // computer's paddle line
        }
        // delete ball after collision
        drawBall(ballX ,ballY);          // redraw ball
    }

    /** Makes a new ball coming from the top line hitting the player's paddle
    */
    public static void newBall() {
        ballX = WIDTH/2;             // the position
        ballY = 1;
        yHit=HEIGHT/2;
        dx = (USERPADDLE_X-ballX);   // the angle
        dy = (userPaddle-ballY);

        lastreflect=-1;

    }

    /** Checks if the ball has collided with any paddle or the board's bounds.
    */
    public static boolean collision() {
        boolean up = (ballY<1 && lastreflect!=1);
        if (up) lastreflect=1;
        boolean down = (ballY>=HEIGHT-4 && lastreflect!=3);
        if (down) lastreflect = 3;
        boolean onBounds = (ballX>=WIDTH-4 || ballX<1 || up || down); // check bounds
        boolean collision = onBounds || onCompPaddle() || onUserPaddle() || !go;
        return collision;
    }

    private static String[] PADDLE_IMG= {"\uF008\u08E8\uF000",
                                         "\uFF00\u00FF\uFF00",
                                         "\u0F10\u181F\u0F00"};
    /** Draws the paddle on the board.
    */
    public synchronized static void drawPaddles(boolean computer, boolean user) {
        if (user) {
            int yoff = userPaddle<=3?4-userPaddle:0;
            int height= yoff>0?24-yoff:userPaddle>HEIGHT-20?HEIGHT+4-userPaddle:24;
            lcd.drawImage(PADDLE_IMG, USERPADDLE_X, userPaddle+yoff-3, 5, height, 0, yoff); // player's paddle
        }
        if (computer) {
            int yoff = compPaddle<=3?4-compPaddle:0;
            int height= yoff>0?24-yoff:compPaddle>HEIGHT-20?HEIGHT+4-compPaddle:24;
            lcd.drawImage(PADDLE_IMG, COMPPADDLE_X, compPaddle+yoff-3, 5, height, 0, yoff); // computer's paddle
        }
    }

    /**
     * Moves the paddle up or down.
     *
     * @param up     move up when true, down when false
     * @param which  user paddle when true, computer paddle when false.
     */
    public static synchronized void movePaddles(boolean up, boolean which) {
        if (up) {                                                           // move up
            if (which)
                userPaddle=(userPaddle<=1?1:userPaddle-1);                  // move player's paddle
            else
                compPaddle=(compPaddle<=1?1:compPaddle-1);                  // move computer's paddle
        } else {                                                            // move down
            if (which)
                userPaddle=(userPaddle>=HEIGHT-17?HEIGHT-17:userPaddle+1);  // move player's paddle
            else
                compPaddle=(compPaddle>=HEIGHT-17?HEIGHT-17:compPaddle+1);  // move computer's paddle
        }
        drawPaddles(!which, which);                                                      // draw paddles
    }

    private static String[] BALLIMG=new String[]{"\u0000\u3864\u747C\u3800\u0000",
                                                 "\u0000\u0000\u0000\u0000\u0000"};
    /**
     * Draws the ball to (x,y).
     *
     * @param x      the x-coordinate of the ball's upper left
     * @param y      the y-coordinate of the ball's upper left
     */
    public static void drawBall(int x, int y) {

        //int xoff = x<=COMPPADDLE_X+7?2:0;
        int yoff = y<=2?3-y:0;
        int width = x>USERPADDLE_X-7?7:9;
        int height= yoff>0?9-yoff:y>HEIGHT-7?HEIGHT+3-y:9;
        lcd.drawImage(BALLIMG, x-2, y+yoff-2, width, height, 0, yoff);  // draw the ball
        drawPaddles(x<=COMPPADDLE_X+7, false);
        ballX = x; ballY = y;
    }

    /**
     * Paints all images on the board.
     * This method is called before every new game.
     */
    public static void repaint() {
        lcd.drawString("LEVEL", (WIDTH+3), 5);
        lcd.drawString("SCORE", (WIDTH+3), 25);
        lcd.drawString("BALLS", (WIDTH+3), 43);

    }

    /** Repaints the border, the live indication balls, level, score and the paddle.
    */
    public static void paint() {
        lcd.drawRect(0, 0, (WIDTH+2),(HEIGHT+2)); // draw big rectangle
        drawPaddles(true, true);                                                        // draw paddles
        for (int count = 0; count<MAXLIVES; count++) {                        // for all lives
            if (lives>=count)                                                 // draw ball
                lcd.drawImage(new String[]{"\u0E19\u1D1F\u0E00"}, ((count%4)*6+WIDTH+3), (52+6*(count/4)), 5,5,0,0);
            else {                                                            // delete ball (don't draw any ball)
                lcd.clearRect(((count%4)*6+WIDTH+3), (52+6*(count/4)), 5, 5);
            }
        }
        lcd.drawString("     ",(WIDTH+3), 14);         // clear old values
        lcd.drawString("     ",(WIDTH+3), 33);
        lcd.drawString(String.valueOf(level), (WIDTH+4), 14);          // draw level
        lcd.drawString(String.valueOf(score), (WIDTH+4), 33);          // draw score
    }

    /** Init the keyboard and some variables.
    */
    public static void init() {
        go = true;                      // flag for the ball loop
        gameOver = false;               // new game
        score = 0;                      // zero points
        level = 0;                      // first level
        lives = 3;                      // 4 lives (0, 1, 2, 3)

    }

    /** The main method. */
    public static void main(String[] args) {
        lcd = new Display();
        keys = new Keyboard();
        go(lcd, keys);
    }



    public static void go(Display display, Keyboard key) {
        lcd=display;
        keys=key;

        try {
            lcd.drawImage(new Resource("Pong.jcif"), 0,0);
        } catch (IOException e) {}
        keys.read();
        lcd.clearDisplay();
        lcd.setFont(Display.SYSTEMFONT);
        init();
        lcd.clearDisplay();
        newBall();                      // get a new ball
        paint();                        // paint everything
        repaint();                      // paint images
        ballspeed = BALLSPEED;          // speed in level 0

        lcd.drawString("Press a key to begin", 2,20); // draw text
        lcd.setFont(Display.SYSTEMFONT);
        keys.read();                 // wait for keypress
        lcd.clearRect(2, 20, WIDTH-2, 10);
        go = true;
        (new Pong$ComputerPlayer()).start();      // create a computer player
        (new Pong$Ball()).start();            // start the ball flying

        mainloop:while (go) {                  // the main loop
            try {
                ThreadExt.sleep(10); // slow down the paddle
            } catch (InterruptedException e) {}
            switch (keys.getRaw()) {
                case 'u':
                case 'U':movePaddles(true, true); break; // move paddle up
                case 'd':
                case 'D':movePaddles(false, true);break; // move paddle down
                case 'S'://go = false; break mainloop;
            }
            if (gameOver) {   // if game is lost but still running
                if (GameOver()) {   // game over
                    go = false;
                    break mainloop;
                } else {
                    paint();      // paint all for new game
                    repaint();
                }
            }

        }
    }

    /** This method shows that you've lost the game.
    */
    public static boolean GameOver() {
        try {
            ThreadExt.sleep(100);
        } catch (InterruptedException e) {}
//        lcd.setFont(Display.LARGEFONT);
        lcd.setDrawMode(Display.INVERSE);
        lcd.drawString("  Game Over!  ", 17,30);  // draw text
        lcd.setDrawMode(Display.NORMAL);
        lcd.drawString("Press a key !", 20,40);   // draw text
        lcd.setFont(Display.SYSTEMFONT);
        char c = 0;
        c = keys.read();         // wait for keypress
        if (c!='S') {
            return true;
        }
        newBall();                  // get a new ball
        gameOver = false;           // new game
        lcd.clearDisplay();         // clear display
        level = 0;                  // first level
        score = 0;                  // no score
        ballspeed = BALLSPEED;      // ínitial speed
        lives = 3;                  // four lives (0,1,2,3)
        return false;
    }

}

/**
 * This class moves the ball.
 * It uses Bresenham's Algorithm for drawing lines to calculate the new ball position.
 * This algorithm is designed only for the first octant.
 * All other seven octants do it in the same way but with some plusses and minuses changed.
 */
class Pong$Ball extends Thread {

    int c1, c2, error;

    public void run() {
        while (Pong.go) {                            // the secondary loop
            if (Pong.dx>=0) {                           // 1st half
                if (Pong.dy>=0) {                        // 1st quadrant
                    if (Pong.dx>=Pong.dy) {             // 1st octant
                        c1= 2*Pong.dy;
                        error = c1 - Pong.dx;
                        c2 = error - Pong.dx;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall((Pong.ballX+1), Pong.ballY); // one to the right
                                error = error+c1;
                            } else {
                                Pong.drawBall((Pong.ballX+1),  (Pong.ballY+1)); // one to the right, one down
                                error = error+c2;
                            }
                        }
                    } else {                            // 2nd octant
                        c1= 2*Pong.dx;
                        error = c1 - Pong.dy;
                        c2 = error - Pong.dy;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall(Pong.ballX, (Pong.ballY+1)); // one down
                                error = error+c1;
                            } else {
                                Pong.drawBall((Pong.ballX+1),  (Pong.ballY+1)); // one to the right, one down
                                error = error+c2;
                            }
                        }
                    }
                } else {                                // 4th quadrant
                    if (Pong.dx>=(-Pong.dy)) {          // 8th octant
                        c1= -2*Pong.dy;
                        error = c1 - Pong.dx;
                        c2 = error - Pong.dx;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall((Pong.ballX+1), Pong.ballY);   // one to the right
                                error = error+c1;
                            } else {
                                Pong.drawBall((Pong.ballX+1), (Pong.ballY-1)); // one to the right, one up
                                error = error+c2;
                            }
                        }
                    } else {                            // 7th octant
                        c1= 2*Pong.dx;
                        error = c1 + Pong.dy;
                        c2 = error + Pong.dy;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall(Pong.ballX, (Pong.ballY-1));   // one up
                                error = error+c1;
                            } else {
                                Pong.drawBall((Pong.ballX+1), (Pong.ballY-1)); // upe to the right, one up
                                error = error+c2;
                            }
                        }
                    }

                }
            } else {                                    // 2nd half
                if (Pong.dy>=0) {                        // 2st quadrant
                    if ((-Pong.dx)>=Pong.dy) {          // 4th octant
                        c1= 2*Pong.dy;
                        error = c1 + Pong.dx;
                        c2 = error + Pong.dx;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall((Pong.ballX-1), Pong.ballY); // one to the left
                                error = error+c1;
                            } else {
                                Pong.drawBall((Pong.ballX-1),  (Pong.ballY+1)); // one to the left, one down
                                error = error+c2;
                            }
                        }
                    } else {                            // 3rd octant
                        c1= 2*Pong.dx;
                        error = c1 + Pong.dy;
                        c2 = error + Pong.dy;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall(Pong.ballX, (Pong.ballY+1)); // one down
                                error = error-c1;
                            } else {
                                Pong.drawBall((Pong.ballX-1),  (Pong.ballY+1)); // one to the left, one down
                                error = error-c2;
                            }
                        }
                    }
                } else {                                // 3rd quadrant
                    if (Pong.dx<=Pong.dy) {             // 5th octant
                        c1= -2*Pong.dy;
                        error = c1 + Pong.dx;
                        c2 = error + Pong.dx;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall((Pong.ballX-1), Pong.ballY); // one to the left
                                error = error+c1;
                            } else {
                                Pong.drawBall((Pong.ballX-1), (Pong.ballY-1)); // one to the left, one up
                                error = error+c2;
                            }
                        }
                    } else {                            // 6th octant
                        c1= 2*Pong.dx;
                        error = c1 - Pong.dy;
                        c2 = error - Pong.dy;
                        while (!Pong.collision()) {     // while no collision, move ball
                            try {
                                ThreadExt.sleep(Pong.ballspeed); // slow down the ball
                            } catch (InterruptedException e) {}
                            if (error < 0) {
                                Pong.drawBall(Pong.ballX, (Pong.ballY-1)); // one up
                                error = error-c1;
                            } else {
                                Pong.drawBall((Pong.ballX-1), (Pong.ballY-1)); // one to the left, one up
                                error = error-c2;
                            }
                        }
                    }

                }
            }
            if (Pong.go && !Pong.gameOver) Pong.reflectBall(); // if still running reflect the ball
        }
    }
}


/**
 * This class simulates a computer player.
 * When the ball moves to the left, the computer player will try to move his paddle to the calculated position where it would hit the ball.
 * Like a human player, the computer will make mistakes and have a reaction time delay.
 * In higher levels, mistakes will decrease.
 */
class Pong$ComputerPlayer extends Thread {
    public void run() {
        while (Pong.go) {
            short reactionTime = (short)(6000-(Pong.level>10?10:Pong.level)*(Math.rnd(100)));
            int errorFac = 70-Pong.level; if (errorFac<20) errorFac=20;       // the higher the level, the smaller the error
            int error = (int)(Math.rnd(errorFac))-(errorFac/2);              // don't hit exactly
            try {
                ThreadExt.sleep(reactionTime+10); // reaction time delay
            } catch (InterruptedException e) {}
            while (Pong.go && Pong.compPaddle!=Pong.yHit+error) {             // while running and paddle is not in place
                try {
                    ThreadExt.sleep(20);                                   // slow down the paddle
                } catch (InterruptedException e) {}
                if (Pong.compPaddle<Pong.yHit && (Pong.compPaddle<=Pong.HEIGHT-17)) { // move down
                    Pong.movePaddles(false, false);
                } else if (Pong.compPaddle>Pong.yHit && (Pong.compPaddle>=1)) {       // move up
                    Pong.movePaddles(true, false);
                }
            }
        }
    }
}