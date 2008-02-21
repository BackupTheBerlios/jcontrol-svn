/*
 * Checkers.java
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
import jcontrol.io.Backlight;

/**
 * An implementation of the classical board game Draughts or Checkers. Play against
 * a dim witted computer opponent and win!
 *
 * @author Thomas Röbbenack
 * @date 28.12.04 17:17
 */
public class Checkers {

	private final Display m_lcd = new Display();
	private final Keyboard m_keys = new Keyboard();
 	private final Board m_board = new Board(m_lcd, m_keys);
	private final Rules2 m_rules = new Rules2(m_board);
	private final AbstractPlayer m_player1 = new Human(m_rules, m_board, m_keys, Board.WHITE);
	private final AbstractPlayer m_player2 = new Computer(m_rules, m_board, Board.BLACK);
	//new Human(m_rules, m_board, m_keys, Board.BLACK);
	private AbstractPlayer m_player = m_player1;
	
    public Checkers() {
		m_board.drawBoard();
    }
    
    
    private void winner(AbstractPlayer player) {
		if (player.getColor()==Board.WHITE) m_board.drawText("You win :)",2);
		if (player.getColor()==Board.BLACK) m_board.drawText("Computer wins.",2);
		m_board.drawText("press key",4);
		m_board.drawText("to restart..",5);
		m_keys.read();
		m_board.clearText(2);
		m_board.clearText(4);
		m_board.clearText(5);
		m_board.clearBoard();
		m_board.init();
		m_board.drawBoard();
		m_player=m_player1;    	
    }
    
    public void mainLoop() {
    	Move2D move;
    	boolean jumpagain = false;
    	for (;;) {
    		//
    		move = m_player.move(jumpagain);
    		
    		//==Zug ausführen==
    		int x=move.getX(),y=move.getY(),toX=move.getToX(),toY=move.getToY();
    		//zu bewegenden Stein merken
    		byte stone = m_board.getFields()[x][y];
    		boolean jump = (m_rules.findMove(x,y,true)!=null);
    		//Diagonale durchgehen und Steine löschen
    		int mx = ( x>toX ? -1 : 1);
    		int my = ( y>toY ? -1 : 1);
    		while (x!=toX || y!=toY) {
    			m_board.getFields()[x][y] = Board.EMPTY;
    			m_board.clearField(x,y);
    			m_board.drawField(x,y);
    			x+=mx;
    			y+=my;
    		}
    		//bewegten Stein an neue Position setzen
    		m_board.getFields()[toX][toY] = stone;
    		m_board.drawStone(toX,toY,stone);    		
    		
    		//Gibt es einen Gewinner?
			byte winner = m_rules.whoIsTheWinner();
			if (winner!=0) {
				if (winner==Board.WHITE) winner(m_player1);
				if (winner==Board.BLACK) winner(m_player2);
				continue;
			}    		
    		
    		//Dame?
			for (int i=0;i<8; i++) {
	    		if (m_board.getFields()[i][0]==Board.WHITE) {
	    			m_board.getFields()[i][0]=Board.WHITE|Board.QUEEN;
	    			m_board.drawStone(i,0,Board.WHITE|Board.QUEEN);
	    			jump=false;
	    		}
	    		if (m_board.getFields()[i][7]==Board.BLACK) {
	    			m_board.getFields()[i][7]=Board.BLACK|Board.QUEEN;
	    			m_board.drawStone(i,7,Board.BLACK|Board.QUEEN);
	    			jump=false;
	    		}
	    	}
	    	
    		//==Spieler wechseln==
    		//schauen ob es noch ein Sprung auszuführen ist
    		if ( jump && null!=m_rules.findMove(toX,toY,true) ) {
    			m_board.drawText("jump again...",2);
    			jumpagain=true;
    		} else {
    			//Spieler wechseln
    			if (m_player==m_player1) m_player=m_player2; else m_player=m_player1;
    			m_board.drawText("next Player",2);
    			jumpagain=false;
    			//Kann der Spieler noch ziehen?
    			int possiblities = 0;
    			for (int yy=0; yy<8; yy++) {
	    			for (int xx=0; xx<8; xx++) {
	    				if ( m_board.getFields()[xx][yy]==m_player.getColor() ) {
		    				if ( m_rules.findMove(xx,yy,false)!=null || m_rules.findMove(xx,yy,true)!=null ) {
		    					possiblities++;
	    					}
	    				}
    				}
    			}
    			if (possiblities==0) {
    				if (m_player==m_player1) winner(m_player2); else winner(m_player1);
    			}    			
    		}    		
    	}
    }
    public static void main(String args[]) {
    	Backlight.setBrightness(Backlight.MAX_BRIGHTNESS );
    	new Checkers().mainLoop();
    }
}

class Rules2 {

	private Board m_board;
	
	public Rules2(Board board) {
		m_board = board;
	}
	
	public boolean isMoveOK(int x, int y, int toX, int toY) {
		Move2D moves[];
		//
		moves = findMove(x,y,true);
		if (moves!=null) {
			//m_board.drawText("jump",4);
			for (int i=0; i<moves.length; i++) {
				if (moves[i].getToX()==toX && moves[i].getToY()==toY) return true;
			}
			return false;
		}
		moves = findMove(x,y,false);
		if (moves!=null) {
			//m_board.drawText("move ".concat(Integer.toString(moves.length)),4);
			for (int i=0; i<moves.length; i++) {
				//m_board.drawText(Integer.toString(moves[i].getToX()).concat(",").concat(Integer.toString(moves[i].getToY())),4);
				if (moves[i].getToX()==toX && moves[i].getToY()==toY) return true;
			}
			return false;
		}
		m_board.drawText("nothing",4);
		return false;
	}
	
	public Move2D[] findJumpsFor(int colorStone) {
		Move2D moves[] = null;
		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				if ((m_board.getFields()[x][y]&3)==colorStone) {
					Move2D jump[] = findMove(x,y, true);
					if (jump!=null) moves = add(moves,jump);
				}
			}
		}	
		return moves;
	}
	
	private Move2D[] add(Move2D arg0[], Move2D arg1) {
		if (arg0==null) {
			arg0 = new Move2D[] { arg1 };
		} else {
			Move2D dummy[] = new Move2D[ arg0.length + 1];
			System.arraycopy(arg0,0,dummy,0,arg0.length);
			dummy[dummy.length-1]=arg1;
			arg0=dummy;
		}
		return arg0;
	}
	
	private Move2D[] add(Move2D arg0[], Move2D arg1[]) {
		if (arg0==null) {
			arg0 = arg1;
		} else {
			Move2D dummy[] = new Move2D[ arg0.length + arg1.length];
			System.arraycopy(arg0,0,dummy,0,arg0.length);
			System.arraycopy(arg1,0,dummy,arg0.length,arg1.length);
			arg0=dummy;
		}
		return arg0;
	}
	
	public Move2D[] findMove(int x, int y, boolean jump) {

		byte fields[][] = m_board.getFields();
		byte stone = fields[x][y];
		Move2D moves[] = null;
		
		if (jump) {
			switch (stone) {
				case Board.WHITE: //Zug nach oben
				//oben,links
				if (x>1 && 
				    y>1 && 
				    fields[x-1][y-1]!=Board.EMPTY && 
				    (fields[x-1][y-1]&3)!=(stone&3) &&
				    fields[x-2][y-2]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x-2,y-2));
				}
				//oben,rechts
				if (x<6 && 
				    y>1 && 
				    fields[x+1][y-1]!=Board.EMPTY && 
				    (fields[x+1][y-1]&3)!=(stone&3) &&
				    fields[x+2][y-2]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x+2,y-2));
				}
				break;
				case Board.BLACK: //Zug nach unten
				//unten,links
				if (x>1 && 
				    y<6 && 
				    fields[x-1][y+1]!=Board.EMPTY && 
				    (fields[x-1][y+1]&3)!=(stone&3) &&
				    fields[x-2][y+2]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x-2,y+2));
				}
				//unten,rechts
				if (x<6 && 
				    y<6 && 
				    fields[x+1][y+1]!=Board.EMPTY && 
				    (fields[x+1][y+1]&3)!=(stone&3) &&
				    fields[x+2][y+2]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x+2,y+2));
				}
				break;
				case Board.WHITE | Board.QUEEN:
				case Board.BLACK | Board.QUEEN:
					//Diagonale (oben, links)
					int xx=x, yy=y, stones=0;
					while (xx>0 && yy>0) {
						xx--;
						yy--;
						if (fields[xx][yy]==Board.EMPTY) { 
							if ( stones==1 ) moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							if ( (fields[xx][yy]&3)==(stone&3) ) break;
							if ( stones==0 ) stones++; else break; 
						}
					}
					//Diagonale (oben, rechts)
					xx=x; yy=y; stones=0;
					while (xx<7 && yy>0) {
						xx++;
						yy--;
						if (fields[xx][yy]==Board.EMPTY) { 
							if ( stones==1 ) moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							if ( (fields[xx][yy]&3)==(stone&3) ) break;
							if ( stones==0 ) stones++; else break; 
						}
					}
					//Diagonale (unten, rechts)
					xx=x; yy=y; stones=0;
					while (xx<7 && yy<7) {
						xx++;
						yy++;
						if (fields[xx][yy]==Board.EMPTY) { 
							if ( stones==1 ) moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							if ( (fields[xx][yy]&3)==(stone&3) ) break;
							if ( stones==0 ) stones++; else break; 
						}
					}
					//Diagonale (unten, links)
					xx=x; yy=y; stones=0;
					while (xx>0 && yy<7) {
						xx--;
						yy++;
						if (fields[xx][yy]==Board.EMPTY) { 
							if ( stones==1 ) moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							if ( (fields[xx][yy]&3)==(stone&3) ) break;
							if ( stones==0 ) stones++; else break; 
						}
					}					
				break;
			} //end: case
		} else { //no jump -> only move
			switch (stone) {
				case Board.WHITE: //Zug nach oben
				//oben,links
				if (x>0 && 
				    y>0 && 
				    fields[x-1][y-1]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x-1,y-1));
				}
				//oben,rechts
				if (x<7 && 
				    y>0 && 
				    fields[x+1][y-1]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x+1,y-1));
				}
				break;
				case Board.BLACK: //Zug nach unten
				//unten,links
				if (x>0 && 
				    y<7 && 
				    fields[x-1][y+1]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x-1,y+1));
				}
				//unten,rechts
				if (x<7 && 
				    y<7 && 
				    fields[x+1][y+1]==Board.EMPTY) {
				    moves = add(moves,new Move2D(x,y,x+1,y+1));
				}
				break;
				case Board.WHITE | Board.QUEEN:
				case Board.BLACK | Board.QUEEN:
					//Diagonale (oben, links)
					int xx=x, yy=y;
					while (xx>0 && yy>0) {
						xx--;
						yy--;
						if (fields[xx][yy]==Board.EMPTY) { 
							moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							break;
						}
					}
					//Diagonale (oben, rechts)
					xx=x; yy=y;
					while (xx<7 && yy>0) {
						xx++;
						yy--;
						if (fields[xx][yy]==Board.EMPTY) { 
							moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							break;
						}
					}
					//Diagonale (unten, rechts)
					xx=x; yy=y;
					while (xx<7 && yy<7) {
						xx++;
						yy++;
						if (fields[xx][yy]==Board.EMPTY) { 
							moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							break;
						}
					}
					//Diagonale (unten, links)
					xx=x; yy=y;
					while (xx>0 && yy<7) {
						xx--;
						yy++;
						if (fields[xx][yy]==Board.EMPTY) { 
							moves = add(moves,new Move2D(x,y,xx,yy));
						} else {						
							break;
						}
					}					
				break;
			} //end: case
		}

		return moves;
	}
	
	public byte whoIsTheWinner() {
		//Spiel zu Ende?
    	int white = 0;
    	int black = 0;
    	for (int y=0; y<8; y++) {
    		for (int x=0; x<8; x++) {
    			switch (m_board.getFields()[x][y]) {
    				case Board.WHITE:
    				case Board.WHITE | Board.QUEEN:
    					white++;
    					break;
    				case Board.BLACK:
    				case Board.BLACK | Board.QUEEN:
    					black++;
    					break;
    			}
    		}
    	}
    	if (white==0) return Board.BLACK;
    	if (black==0) return Board.WHITE;
    	return 0;
	}
}

abstract class AbstractPlayer {
	
	protected Board m_board;
	protected Rules2 m_rules;
	protected byte  m_color;
	
	public AbstractPlayer(Rules2 rules, Board board, byte color) {
		m_rules = rules;
		m_board = board;
		m_color = color;
	}
	
	public byte getColor() {
		return m_color;
	}
	
	abstract public Move2D move(boolean jumpagain);	
}

class Human extends AbstractPlayer {
	
	private Keyboard m_keys;
	private int x=0, y=0;

	
	public Human(Rules2 rules, Board board, Keyboard keys, byte color) {
		super(rules, board, color);
		m_keys = keys;
	}
	
	public Move2D move(boolean jumpagain) {
		int keys;
		int markX=-1;
		int markY=-1;
		{
			Move2D jump[] = m_rules.findJumpsFor(m_color);
			if (jump!=null) {
				markX = jump[0].getX();
				markY = jump[0].getY();
				if (!jumpagain) m_board.drawText("please jump...",2);
			}
		}
		do {
   			do {
   				m_board.drawMark(x,y);
   				if (markX!=-1) m_board.drawMark(markX, markY);
   			} while ( (keys=m_keys.getRaw()) == 0 );
   			
			switch (keys) {
            	case 'R':
            		if (x>=7) continue;
            		x++;
	            	break;
                case 'U':                
                case 'u':
            		if (y<=0) continue;
            		y--;
                	break; 
                case 'L':
                	if (x<=0) continue;
            		x--;
                	break;
                case 'D':                
                case 'd':
                	if (y>=7) continue;
            		y++;
                	break; 
                case 'S':
                	//vorherige Markierung wieder aufheben
					if (x==markX && y==markY) {
       	        		//String "move x to ..." loeschen
       	        		m_board.clearText(2);
       	        		markX=-1;
       	        		markY=-1;
       	        		break;
   	            	}
   	            	//Markieren des 1. Steines
   	            	if (markX==-1) {
	   	            	//schauen ob dies überhaupt erlaubt
	   	            	if (m_board.getFields()[x][y]==Board.EMPTY) {
	   	            		m_board.drawText("no stone?",2);
	   	            		break;
	   	            	}
	   	            	if ( (m_board.getFields()[x][y]&3)!=m_color) {
	   	            		m_board.drawText("not your stone!",2);
	   	            		break;
	   	            	}
      	        		//String "move x to y" zusammenbauen & Ausgabe
   	    	        	String first = new String( new byte[] { ((byte)(65+x)) });
               			String second = new String( new byte[] { ((byte)(49+y)) });
						String wait = "move ".concat(first).concat(second).concat(" to ...");
                		m_board.drawText(wait,2);
                		//
                		markX=x;
                		markY=y;               			
                	} else {
                	//Auswahl komplett
                		//schauen ob dies überhaupt erlaubt
	   	            	if (m_board.getFields()[x][y]!=Board.EMPTY) {
	   	            		m_board.drawText("not empty!",2);
	   	            		try { jcontrol.lang.ThreadExt.sleep(1000); } catch (InterruptedException e) {}
	   	            		String first = new String( new byte[] { ((byte)(65+markX)) });
               				String second = new String( new byte[] { ((byte)(49+markY)) });
							String wait = "move ".concat(first).concat(second).concat(" to ...");
                			m_board.drawText(wait,2);
	   	            		break;
	   	            	}
	   	            	//schauen ob dies überhaupt erlaubt (anhand der Regeln)
	   	            	if (!m_rules.isMoveOK(markX,markY,x,y)) {
	   	            		m_board.drawText("incorrect move!",2);
	   	            		try { jcontrol.lang.ThreadExt.sleep(1000); } catch (InterruptedException e) {}
	   	            		String first = new String( new byte[] { ((byte)(65+markX)) });
               				String second = new String( new byte[] { ((byte)(49+markY)) });
							String wait = "move ".concat(first).concat(second).concat(" to ...");
                			m_board.drawText(wait,2);
                			break;	   	            		
	   	            	}
                		//String "move x to y" zusammenbauen & Ausgabe
   	    	        	String first = new String( new byte[] { ((byte)(65+markX)) });
               			String second = new String( new byte[] { ((byte)(49+markY)) });
						String wait = "move ".concat(first).concat(second).concat(" to ");
						first = new String( new byte[] { ((byte)(65+x)) });
						second = new String( new byte[] { ((byte)(49+y)) });
						wait=wait.concat(first).concat(second);
                		m_board.drawText(wait,2);
                		//
               			try { jcontrol.lang.ThreadExt.sleep(500); } catch (InterruptedException e) {}
               			m_board.clearText(2);
               			//
               			return new Move2D(markX,markY,x,y);
                	}
					break;
			}
			try { jcontrol.lang.ThreadExt.sleep(150); } catch (InterruptedException e) {}
		} while (true);
		//return null;
	}	
}

class Computer extends AbstractPlayer {

	public Computer(Rules2 rules, Board board, byte color) {
		super(rules, board, color);
	}

	public Move2D move(boolean jumpagain) {
		m_board.drawText("i'am thinking.",2);
		m_board.drawText("please wait...",3);
		//
		//Backlight.setBrightness(Backlight.MAX_BRIGHTNESS/2);
		//		
		Move2D moves[];
		Move2D target;		
		//Jump
		moves = m_rules.findJumpsFor(m_color);
		if (moves!=null) {
			target = moves[ jcontrol.lang.Math.rnd(moves.length) ];
		} else {
			//Move
			do {
				int x = jcontrol.lang.Math.rnd(8);
				int y = jcontrol.lang.Math.rnd(8);
				if ( (m_board.getFields()[x][y]&3)!=m_color ) continue;
				moves = m_rules.findMove(x,y,false);
				if (moves!=null) break;
			} while (true);
			//
			target = moves[ jcontrol.lang.Math.rnd(moves.length) ];
		}
		//		
		m_board.clearText(2);
		m_board.clearText(3);
		//
		//Backlight.setBrightness(Backlight.MAX_BRIGHTNESS );
		//Computerssprung dem Spieler zeigen
		for (int i=0; i<5; i++){
   			m_board.drawMark(target.getX(),target.getY());
   			m_board.drawMark(target.getToX(), target.getToY());
   		}   			
		return target;
	}	
}

class Move2D {

	private final int m_x;
	private final int m_y;
	private final int m_toX;
	private final int m_toY;

	public Move2D(int x, int y, int toX, int toY) {
		m_x = x;
		m_y = y;
		m_toX = toX;
		m_toY = toY;
	}
	
	public final int getX() {
		return m_x;
	}
	
	public final int getY() {
		return m_y;
	}

	public final int getToX() {
		return m_toX;
	}
	
	public final int getToY() {
		return m_toY;
	}

}

class Board {
	public final static byte EMPTY 	= 0;  //b00000000
 	public final static byte WHITE 	= 1;  //b00000001
 	public final static byte BLACK 	= 2;  //b00000010
 	public final static byte QUEEN 	= 4;  //b00000100
	// 	
	private final byte m_fields[][] = new byte[8][8];
	private Display m_lcd;
	private Keyboard m_keys;
	
	public Board(Display lcd, Keyboard keys) {
		m_lcd = lcd;
		m_keys = keys;
		init();
	}

	public final byte[][] getFields() {
		return m_fields;
	}
	
	public final void drawText(String text, int line) {
	 	m_lcd.clearRect(70,line*7,58,7);
        m_lcd.drawString(text,70,line*7);
	}

	public final void clearText(int line) {
	 	m_lcd.clearRect(70,line*7,58,7);
	}

	public final void clearBoard() {
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				clearField(x,y);
			}
		}
	}	
		
    public final void clearField(int x, int y) {
    	m_lcd.clearRect(x*7+7,y*7+7,7,7);
    	drawField(x,y);
    }
    
    public final void drawField(int x, int y) {
    	x=x*7+7;
    	y=y*7+7;
    	m_lcd.drawLine(x,y,x+6,y);	//wagerechte Linie
    	m_lcd.drawLine(x,y,x,y+6); //senkrechte Linie
    	if (x%2!=y%2) { //dunkle Spielfelder
    		//Eckpunkte
    		m_lcd.setPixel(x+1,y+1);
    		m_lcd.setPixel(x+1,y+6);
    		m_lcd.setPixel(x+6,y+1);
    		m_lcd.setPixel(x+6,y+6);
    		//Mittelpunkte
    		m_lcd.setPixel(x+4,y+2);
    		m_lcd.setPixel(x+5,y+4);
    		m_lcd.setPixel(x+3,y+5);
    		m_lcd.setPixel(x+2,y+3);
    	}
    }
    	
 	public final void drawBoard() {
		//Überschrift
		{
			String head = "CHECKERS";
			m_lcd.drawString(head,70,0);
			m_lcd.drawLine(70,7,70+m_lcd.getTextWidth(head)-2,7);
		}
		//Bitte warten
		m_lcd.drawString("Please wait...",70,20);
		//Schachbrett zeichnen
		m_lcd.drawLine(7,63,7+8*7,63);
		m_lcd.drawLine(7+8*7,7,7+8*7,63);
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				drawField(x,y);
			}
		}
		//Beschriftung oben (A...H)
		for (int x=0; x<8; x++) {
			m_lcd.drawString(new String( new byte[] { ((byte)(65+x)) }),9+x*7,0);
		}
		//Beschriftung Seite (1..8)
		for (int y=0; y<8; y++) {
			m_lcd.drawString(new String( new byte[] { ((byte)(49+y)) }),2,8+y*7);
		}
		//Steine zeichnen
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				drawStone(x,y,m_fields[x][y]);
			}
		}
		//Bitte warten Ende :)
		m_lcd.setDrawMode(Display.XOR);
		m_lcd.drawString("Please wait...",70,20);
		m_lcd.setDrawMode(Display.NORMAL);
    }
    
 	public final void drawStone(int x, int y, int stone) {
    	x=x*7+9;
    	y=y*7+9;
    	switch (stone&3) {
    		case WHITE:
    			m_lcd.drawRect(x,y, 4, 4) ;
    			break;
    		case BLACK:
    			m_lcd.fillRect(x,y, 4, 4) ;
    			break;    		
    	}    	
    	if ((stone&QUEEN)>0) {
    		m_lcd.drawLine(x+1,y-1,x+2,y-1);
    		m_lcd.drawLine(x+1,y+4,x+2,y+4);
    		m_lcd.drawLine(x-1,y+1,x-1,y+2);
    		m_lcd.drawLine(x+4,y+1,x+4,y+2);
    	}
    }
    
	public final void drawMark(int x, int y) {
		x=x*7+7;
    	y=y*7+7;
		m_lcd.setDrawMode(Display.XOR);
		m_lcd.drawRect(x,y,8,8);
		try {
			for (int i=0; i<5; i++) if (m_keys.getRaw()==0) jcontrol.lang.ThreadExt.sleep(30); 
		} catch (InterruptedException e) {}    	
		m_lcd.drawRect(x,y,8,8);
		try {
			for (int i=0; i<5; i++) if (m_keys.getRaw()==0) jcontrol.lang.ThreadExt.sleep(30); 
		} catch (InterruptedException e) {}    	
    	m_lcd.setDrawMode(Display.NORMAL);
	}
	
	public final void init() {
		//alle Felder/Steine loeschen
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				m_fields[x][y] = EMPTY;
			}
		}
		//Steine setzen
		for (int i=0; i<8; i+=2) {
			m_fields[i][1] = BLACK;
			m_fields[i][5] = WHITE;
			m_fields[i][7] = WHITE;
			if (i<7) {
				m_fields[i+1][0] = BLACK;
				m_fields[i+1][2] = BLACK;
				m_fields[i+1][6] = WHITE;
			}
		}		
	}	
}