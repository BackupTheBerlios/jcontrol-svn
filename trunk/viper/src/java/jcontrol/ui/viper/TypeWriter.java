/*
 * $Id$
 * 
 * Copyright (C) 2005-2009 The JControl Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package jcontrol.ui.viper;

import jcontrol.io.Display;
import jcontrol.io.Graphics;
import jcontrol.system.Management;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * Virtual keyboard using one small QWERTY area fitting on the screen.
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class TypeWriter extends AbstractFocusComponent {

    /* Key codes . */
    private static final int SPACE = 46;
    private static final int BACKSPACE = 47;
    private static final int OK = 48;
    private static final int SHIFT = 49;

    private static final int STATE_SHIFT = RESERVED1;
    private static final int STATE_CURSOR = RESERVED2;

    private static final int CHAR_WIDTH = 10;
    private static final int LINE_HEIGHT = 9;
    private static final int FIRST_LINE_X_OFFSET = 1;
    private static final int FIRST_LINE_Y_OFFSET = 14;
    private static final int FIRST_LINE_X2 = 121;
    private static final int SECOND_LINE_X_OFFSET = 4;
    private static final int SECOND_LINE_Y_OFFSET = 24;
    private static final int SECOND_LINE_X2 = 125;
    private static final int THIRD_LINE_X_OFFSET = 6;
    private static final int THIRD_LINE_Y_OFFSET = 34;
    private static final int THIRD_LINE_X2 = 126;
    private static final int FOURTH_LINE_X_OFFSET = 11;
    private static final int FOURTH_LINE_Y_OFFSET = 44;
    private static final int FOURTH_LINE_X2 = 110;
    private static final int FOURTH_LINE_END = 123;
    private static final int SPACE_X = 34;
    private static final int SPACE_WIDTH = 65;
    private static final String LETTERS1 = "!@<>%&/\\()=?QWERTZUIOPÜ+ASDFGHJKLÖÄ#YXCVBNM;.: ";
    private static final String LETTERS2 = "1234567890ß\'qwertzuiopü*asdfghjklöä~yxcvbnm,.- ";

    /** The typed text. */
    private String text = "";

    /** The keyboard image as an string array. */
    private static final String[] KEYBOARD_IMAGE = new String[]{
            "\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE01\u0101\u0101\u0101\u0101\uFE00\u0000\u0000",
            "\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0304\u04FC\u0404\u0404\u0404\u0704\u04F8\u0000",
            "\u0000\u000F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10F0\u1010\u1010\u1010\u100F\u10E0",
            "\u0000\u0000\u003F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u4040\uC040\u4040\u403F\u4040\u40C0\u4040\u4040\u403F\u4040\u4040\u4040\uC040\u403F",
            "\u0000\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u0000\uFF00\u0000\u0000\u0000\u00FF\u0008\u1422\u7714\u1414\u1414\u1C00\uFF00\u0000",
            "\u0000\u0000\u0000\u0000\u0000\u0001\u0101\u0101\u0101\u0101\u0001\u0101\u0101\u0101\u0101\u0001\u01FF\u0101\u0101\u0101\u0001\u0101\u0101\u0101\u0101\u0001\u0101\u0101\u0101\u0101\u0001\u0101\u0101\u0101\u0101\u0001\u0101\u0101\u0101\u0101\u0001\u0101\u0101\u0101\u0101\u0001\u0101\u0101\u01FF\u0101\u0001\u0101\u0101\u0101\u0100\u0101\u0101\u0101\u0101\u0101\u0101\u0000\u0000",
            "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0202\u0201\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000"};

    /** The last time. */
    private int lastTime = 0;

    /** The current character. */
    private int currChar = 0;

    /** The last character. */
    private int lastChar = -1;

    /**
     * Creates a type writer with the given start text.
     * 
     * @param initialText the start text
     */
    public TypeWriter(String initialText) {
        if (initialText != null) text = initialText;
        x = 0;
        y = 0;
        width = 128;
        height = 64;
        state |= STATE_ANIMATED;
    }

    /**
     * Draws an rectangle.
     * 
     * @param g the global graphics device
     * @param charSelect the selected char
     */
    private void drawRect(Graphics g, int charSelect) {
        g.setDrawMode(Graphics.XOR);
        if (charSelect > -1) {
            if (charSelect >= 0 && charSelect < 12) {
                int xoff = FIRST_LINE_X_OFFSET + charSelect * CHAR_WIDTH;
                g.fillRect(xoff + 1, FIRST_LINE_Y_OFFSET, 9, LINE_HEIGHT);
            } else if (charSelect >= 12 && charSelect < 24) {
                int xoff = SECOND_LINE_X_OFFSET + (charSelect - 12) * CHAR_WIDTH;
                g.fillRect(xoff + 1, SECOND_LINE_Y_OFFSET, 9, LINE_HEIGHT);
            } else if (charSelect >= 24 && charSelect < 36) {
                int xoff = THIRD_LINE_X_OFFSET + (charSelect - 24) * CHAR_WIDTH;
                g.fillRect(xoff + 1, THIRD_LINE_Y_OFFSET, 9, LINE_HEIGHT);
            } else if (charSelect >= 36 && charSelect < SPACE) {
                int xoff = FOURTH_LINE_X_OFFSET + (charSelect - 36) * CHAR_WIDTH;
                g.fillRect(xoff + 1, FOURTH_LINE_Y_OFFSET, charSelect == 45 ? 8 : 9, LINE_HEIGHT);
            } else if (charSelect == SPACE) {
                // SPACE
                g.fillRect(SPACE_X + 1, FOURTH_LINE_Y_OFFSET + LINE_HEIGHT + 1, SPACE_WIDTH - 2,
                        LINE_HEIGHT - 1);
            } else if (charSelect == BACKSPACE) {
                // BACKSPACE
                g.fillRect(FOURTH_LINE_X2 + 1, FOURTH_LINE_Y_OFFSET, FOURTH_LINE_END
                        - (FOURTH_LINE_X2 + 1), LINE_HEIGHT);
            } else if (charSelect == OK) {
                // OK
                g.fillRect(116, 55, 12, 8);
            } else if (charSelect == SHIFT) {
                // SHIFT
                g.fillRect(2, 55, 26, 8);
            }
        }
        g.setDrawMode(Graphics.NORMAL);
    }

    /**
     * Returns the typed text.
     * 
     * @return
     */
    public String getText() {
        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent event) {
        switch (event.m_key) {
            case KeyEvent.KEY_SELECT_PRESSED:
                if (currChar > -1) {
                    if (currChar < LETTERS1.length()) {
                        text = text.concat(((state & STATE_SHIFT) != 0 ? LETTERS1 : LETTERS2)
                                .substring(currChar, currChar + 1));
                        if ((state & STATE_SHIFT) == STATE_SHIFT) {
                            state &= ~STATE_SHIFT;
                            state |= STATE_DIRTY_REPAINT;
                        } else {
                            setDirty(STATE_DIRTY_UPDATE, true);
                        }
                    } else if (currChar == BACKSPACE) {
                        // BACKSPACE
                        if (text.length() > 1) {
                            text = text.substring(0, text.length() - 1);
                            setDirty(STATE_DIRTY_UPDATE, true);
                        } else if (text.length() == 1) {
                            text = "";
                            setDirty(STATE_DIRTY_UPDATE, true);
                        }
                    } else if (currChar == OK) {
                        // OK
                        onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, text));
                    } else if (currChar == SHIFT) {
                        // SHIFT
                        if ((state & STATE_SHIFT) == 0) {
                            state |= STATE_SHIFT | STATE_DIRTY_REPAINT;
                        } else {
                            state &= ~STATE_SHIFT;
                            state |= STATE_DIRTY_REPAINT;
                        }
                    }
                }
                return null;
            case KeyEvent.KEY_UP_PRESSED:
                switch (currChar) {
                    case 0:
                    case 1:
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case 10:
                    case 11:
                        currChar = OK; // goto OK
                        break;
                    case SHIFT: // SHIFT
                        currChar = 36; // goto 'y'
                        break;
                    case SPACE: // SPACE
                        currChar = 41; // goto 'n'
                        break;
                    case OK: // OK
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    default:
                        if (currChar < 10) {
                            currChar = SPACE; // goto SPACE
                        } else {
                            currChar += 38;
                        }
                        break;
                }
                break;
            case KeyEvent.KEY_DOWN_PRESSED:
                switch (currChar) {
                    case 45: // '-'
                    case BACKSPACE: // BACKSPACE
                        currChar = OK; // goto OK
                        break;
                    case SPACE: // SPACE
                        currChar = 5; // goto '6'
                        break;
                    case SHIFT: // SHIFT
                        currChar = 0; // goto '1'
                        break;
                    case 36: // 'y'
                    case 37: // 'x'
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case 34: // 'ä'
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    default:
                        if (currChar >= 38 && currChar < SPACE - 1) {
                            currChar = SPACE; // goto space
                        } else {
                            currChar += 12;
                        }
                        break;
                }
                break;
            case KeyEvent.KEY_LEFT_PRESSED:
                switch (currChar) {
                    case 0: // '1'
                        currChar = OK; // goto OK
                        break;
                    case OK: // OK
                        currChar = SPACE; // goto SPACE
                        break;
                    case SPACE: // SPACE
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case SHIFT: // SHIFT
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    case BACKSPACE: // BACKSPACE
                        currChar = 45; // goto - or :
                        break;
                    default:
                        currChar += 49;
                }
                break;
            case KeyEvent.KEY_RIGHT_PRESSED:
                switch (currChar) {
                    case 45: // - or :
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    case BACKSPACE: // BACKSPACE
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case SHIFT: // SHIFT
                        currChar = SPACE; // goto SPACE
                        break;
                    case SPACE: // SPACE
                        currChar = OK; // goto OK
                        break;
                    case OK: // OK
                        currChar = 0; // goto '1'
                        break;
                    default:
                        currChar++;
                        break;
                }
                break;
            default:

        }
        currChar %= 50;
        setDirty(STATE_DIRTY_UPDATE, true);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seejcontrol.ui.viper.AbstractFocusComponent#onRotaryTouchEvent(jcontrol.ui.viper.event.
     * RotaryTouchEvent) TODO: Modify
     */
    @Override
    public RotaryTouchEvent onRotaryTouchEvent(RotaryTouchEvent e) {
        switch (e.getRotaryTouchState()) {
            case KeyEvent.KEY_SELECT_PRESSED:
                if (currChar > -1) {
                    if (currChar < LETTERS1.length()) {
                        text = text.concat(((state & STATE_SHIFT) != 0 ? LETTERS1 : LETTERS2)
                                .substring(currChar, currChar + 1));
                        if ((state & STATE_SHIFT) == STATE_SHIFT) {
                            state &= ~STATE_SHIFT;
                            state |= STATE_DIRTY_REPAINT;
                        } else {
                            setDirty(STATE_DIRTY_UPDATE, true);
                        }
                    } else if (currChar == BACKSPACE) {
                        // BACKSPACE
                        if (text.length() > 1) {
                            text = text.substring(0, text.length() - 1);
                            setDirty(STATE_DIRTY_UPDATE, true);
                        } else if (text.length() == 1) {
                            text = "";
                            setDirty(STATE_DIRTY_UPDATE, true);
                        }
                    } else if (currChar == OK) {
                        // OK
                        onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, text));
                    } else if (currChar == SHIFT) {
                        // SHIFT
                        if ((state & STATE_SHIFT) == 0) {
                            state |= STATE_SHIFT | STATE_DIRTY_REPAINT;
                        } else {
                            state &= ~STATE_SHIFT;
                            state |= STATE_DIRTY_REPAINT;
                        }
                    }
                }
                return null;
            case KeyEvent.KEY_UP_PRESSED:
                switch (currChar) {
                    case 0:
                    case 1:
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case 10:
                    case 11:
                        currChar = OK; // goto OK
                        break;
                    case SHIFT: // SHIFT
                        currChar = 36; // goto 'y'
                        break;
                    case SPACE: // SPACE
                        currChar = 41; // goto 'n'
                        break;
                    case OK: // OK
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    default:
                        if (currChar < 10) {
                            currChar = SPACE; // goto SPACE
                        } else {
                            currChar += 38;
                        }
                        break;
                }
                break;
            case KeyEvent.KEY_DOWN_PRESSED:
                switch (currChar) {
                    case 45: // '-'
                    case BACKSPACE: // BACKSPACE
                        currChar = OK; // goto OK
                        break;
                    case SPACE: // SPACE
                        currChar = 5; // goto '6'
                        break;
                    case SHIFT: // SHIFT
                        currChar = 0; // goto '1'
                        break;
                    case 36: // 'y'
                    case 37: // 'x'
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case 34: // 'ä'
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    default:
                        if (currChar >= 38 && currChar < SPACE - 1) {
                            currChar = SPACE; // goto space
                        } else {
                            currChar += 12;
                        }
                        break;
                }
                break;
            case KeyEvent.KEY_LEFT_PRESSED:
                switch (currChar) {
                    case 0: // '1'
                        currChar = OK; // goto OK
                        break;
                    case OK: // OK
                        currChar = SPACE; // goto SPACE
                        break;
                    case SPACE: // SPACE
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case SHIFT: // SHIFT
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    case BACKSPACE: // BACKSPACE
                        currChar = 45; // goto - or :
                        break;
                    default:
                        currChar += 49;
                }
                break;
            case KeyEvent.KEY_RIGHT_PRESSED:
                switch (currChar) {
                    case 45: // - or :
                        currChar = BACKSPACE; // goto BACKSPACE
                        break;
                    case BACKSPACE: // BACKSPACE
                        currChar = SHIFT; // goto SHIFT
                        break;
                    case SHIFT: // SHIFT
                        currChar = SPACE; // goto SPACE
                        break;
                    case SPACE: // SPACE
                        currChar = OK; // goto OK
                        break;
                    case OK: // OK
                        currChar = 0; // goto '1'
                        break;
                    default:
                        currChar++;
                        break;
                }
                break;
            default:

        }
        currChar %= 50;
        setDirty(STATE_DIRTY_UPDATE, true);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * jcontrol.ui.viper.touch.AbstractTouchComponent#onTouchEvent(jcontrol.ui.viper.event.TouchEvent
     * )
     */
    @Override
    public int onTouchEvent(TouchEvent e) {
        if (e.type == TouchEvent.TYPE_TOUCH_PRESSED) {
            int index = -1;
            if (e.y > FIRST_LINE_Y_OFFSET && e.y < FIRST_LINE_Y_OFFSET + LINE_HEIGHT) {
                if (e.x > FIRST_LINE_X_OFFSET && e.x < FIRST_LINE_X2) {
                    // first line
                    index = (e.x - FIRST_LINE_X_OFFSET) / CHAR_WIDTH;
                }
            } else if (e.y > SECOND_LINE_Y_OFFSET && e.y < SECOND_LINE_Y_OFFSET + LINE_HEIGHT) {
                if (e.x > SECOND_LINE_X_OFFSET && e.x < SECOND_LINE_X2) {
                    // second line
                    index = 12 + (e.x - SECOND_LINE_X_OFFSET) / CHAR_WIDTH;
                }
            } else if (e.y > THIRD_LINE_Y_OFFSET && e.y < THIRD_LINE_Y_OFFSET + LINE_HEIGHT) {
                if (e.x > THIRD_LINE_X_OFFSET && e.x < THIRD_LINE_X2) {
                    // third line
                    index = 24 + (e.x - THIRD_LINE_X_OFFSET) / CHAR_WIDTH;
                }
            } else if (e.y > FOURTH_LINE_Y_OFFSET && e.y < FOURTH_LINE_Y_OFFSET + LINE_HEIGHT) {
                if (e.x > FOURTH_LINE_X_OFFSET && e.x < FOURTH_LINE_X2) {
                    // fourth line
                    index = 36 + (e.x - FOURTH_LINE_X_OFFSET) / CHAR_WIDTH;
                } else if (e.x > FOURTH_LINE_X2 && e.x < FOURTH_LINE_END) {
                    // BACKSPACE
                    currChar = BACKSPACE;
                    if (text.length() > 1) {
                        text = text.substring(0, text.length() - 1);
                        setDirty(STATE_DIRTY_UPDATE, true);
                    } else if (text.length() == 1) {
                        text = "";
                        setDirty(STATE_DIRTY_UPDATE, true);
                    }
                    return RESULT_ACCEPTED;
                }

            } else if (e.y > FOURTH_LINE_Y_OFFSET + LINE_HEIGHT
                    && e.y < FOURTH_LINE_Y_OFFSET + LINE_HEIGHT + LINE_HEIGHT) {
                if (e.x > SPACE_X && e.x < SPACE_X + SPACE_WIDTH) {
                    // SPACE
                    index = SPACE;
                }
            }
            if (e.x >= 117 && e.y >= 54) {
                // OK
                currChar = OK;
                onActionEvent(new ActionEvent(this, ActionEvent.VALUE_CHANGED, text));
                return RESULT_EXECUTED;
            } else if (e.x < 30 && e.y >= 54) {
                // Shift
                currChar = SHIFT;
                if ((state & STATE_SHIFT) == 0) {
                    state |= STATE_SHIFT | STATE_DIRTY_REPAINT;
                } else {
                    state &= ~STATE_SHIFT;
                    state |= STATE_DIRTY_REPAINT;
                }
                return RESULT_ACCEPTED;
            }
            if (index > -1) {
                text = text.concat(((state & STATE_SHIFT) != 0 ? LETTERS1 : LETTERS2).substring(
                        index, index + 1));
                currChar = index;
                if ((state & STATE_SHIFT) == STATE_SHIFT) {
                    state &= ~STATE_SHIFT;
                    state |= STATE_DIRTY_REPAINT;
                } else {
                    setDirty(STATE_DIRTY_UPDATE, true);
                }
                return RESULT_ACCEPTED;
            }
        }
        return RESULT_NONE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public synchronized void paint(Graphics g) {
        switch (state & STATE_DIRTY_MASK) {
            case STATE_DIRTY_PAINT_ALL:
                g.clearRect(x, y, width, height);
            case STATE_DIRTY_REPAINT:
                lastChar = -1;
                g.drawRect(0, 0, 128, 12); // text field
                g.drawImage(KEYBOARD_IMAGE, FIRST_LINE_X_OFFSET, FIRST_LINE_Y_OFFSET - 1, 128, 50,
                        0, 0);
                for (int c = 0; c < 12; c++) {
                    ((Display) g).drawChar(((state & STATE_SHIFT) != 0 ? LETTERS1 : LETTERS2)
                            .charAt(c), 3 + FIRST_LINE_X_OFFSET + CHAR_WIDTH * c,
                            FIRST_LINE_Y_OFFSET + 1);
                }
                for (int c = 0; c < 12; c++) {
                    ((Display) g).drawChar(((state & STATE_SHIFT) != 0 ? LETTERS1 : LETTERS2)
                            .charAt(c + 12), 2 + SECOND_LINE_X_OFFSET + CHAR_WIDTH * c,
                            SECOND_LINE_Y_OFFSET + 1);
                }
                for (int c = 0; c < 12; c++) {
                    ((Display) g).drawChar(((state & STATE_SHIFT) != 0 ? LETTERS1 : LETTERS2)
                            .charAt(c + 24), 3 + THIRD_LINE_X_OFFSET + CHAR_WIDTH * c,
                            THIRD_LINE_Y_OFFSET + 1);
                }
                for (int c = 0; c < 10; c++) {
                    ((Display) g).drawChar(((state & STATE_SHIFT) != 0 ? LETTERS1 : LETTERS2)
                            .charAt(c + 36), 2 + FOURTH_LINE_X_OFFSET + CHAR_WIDTH * c,
                            FOURTH_LINE_Y_OFFSET + 1);
                }
                g.drawString("\u0018Shift", 2, 56);
                g.drawString("OK", 118, 56);
            case STATE_DIRTY_UPDATE:
                if (lastChar > -1 && currChar != lastChar) {
                    drawRect(g, lastChar);
                }
                if (currChar > -1 && currChar != lastChar) {
                    drawRect(g, currChar);
                    lastChar = currChar;
                }
                int time = Management.currentTimeMillis() & 0xffff;
                if (time - lastTime > 500) {
                    if ((state & STATE_CURSOR) == STATE_CURSOR) {
                        state &= ~STATE_CURSOR;
                    } else {
                        state |= STATE_CURSOR;
                    }
                    lastTime = time;
                }
                int textwidth = g.getTextWidth(text);
                if (textwidth < 122) {
                    g.drawString(text, 2, 2);
                    if ((state & STATE_CURSOR) == STATE_CURSOR) {
                        g.drawLine(2 + textwidth, 2, 2 + textwidth, 8);
                        g.clearRect(3 + textwidth, 1, 123 - textwidth, 10);
                    } else {
                        g.clearRect(2 + textwidth, 1, 124 - textwidth, 10);
                    }
                } else {
                    g.drawString(text, 2, 2, 122, -1, textwidth - 122, 0);
                    if ((state & STATE_CURSOR) == STATE_CURSOR) {
                        g.drawLine(124, 2, 124, 8);
                    } else {
                        g.clearRect(124, 2, 1, 7);
                    }
                }
        }
        state &= ~(STATE_DIRTY_PAINT_ALL & ~STATE_ANIMATED);
    }
}