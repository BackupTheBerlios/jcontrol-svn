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
import jcontrol.io.Resource;
import jcontrol.toolkit.ImageUtils;
import jcontrol.ui.viper.event.ActionEvent;
import jcontrol.ui.viper.event.KeyEvent;
import jcontrol.ui.viper.event.RotaryTouchEvent;
import jcontrol.ui.viper.event.TouchEvent;

/**
 * <p>
 * Instances of this class represent a keyboard selectable user interface object that issues
 * notification when pressed and released.
 * </p>
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class Button extends AbstractFocusComponent {

    /** The caption of the button weather an image or an string resource. */
    private Object m_caption;

    /**
     * Creates a button. The size will be calculated as big as is fits the given label.
     * 
     * @param text the label to display on the button.
     * @param x the x-coordinate of the button's top-left corner
     * @param y the y-coordinate of the button's top-left corner
     */
    public Button(String text, int x, int y) {
        this.m_caption = text;
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a button with fixed size.
     * 
     * @param text the label to display on the button.
     * @param x the x-coordinate of the button's top-left corner
     * @param y the y-coordinate of the button's top-left corner
     * @param width the width of the button
     * @param height the height of the button
     */
    public Button(String text, int x, int y, int width, int height) {
        this(text, x, y);
        this.width = width;
        this.height = height;
        if (width > 0) state |= STATE_WIDTH_FIXED;
        if (height > 0) state |= STATE_HEIGHT_FIXED;
    }

    /**
     * Draw the graphical elements of this <code>Button</code>.
     * 
     * @param g the global graphics object
     * @param x
     * @param y
     * @param width
     * @param height
     * @param m_pressed
     */
    private void drawButton(Graphics g, int x, int y, int width, int height, boolean m_pressed) {
        g.drawLine(x + 1, y, x + width - 2, y);
        g.drawLine(x, y + 1, x, y + height - 2);
        g.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1);
        g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2);
        if (m_pressed) {
            g.drawLine(x + 1, y + 1, x + width - 2, y + 1);
            g.drawLine(x + 1, y + 2, x + 1, y + height - 1);
            g.clearRect(x + 2, y + 2, width - 3, height - 3);
        } else {
            g.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 3);
            g.drawLine(x + 2, y + height - 2, x + width - 2, y + height - 2);
            g.clearRect(x + 1, y + 1, width - 3, height - 3);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.AbstractFocusComponent#onKeyEvent(jcontrol.ui.viper.event.KeyEvent)
     */
    @Override
    public KeyEvent onKeyEvent(KeyEvent e) {
        switch (e.m_key) {
            case KeyEvent.KEY_SELECT_PRESSED:
                if ((state & STATE_SELECTED) == 0) {
                    state |= STATE_SELECTED | STATE_DIRTY_REPAINT;
                    if (listener != null) {
                        if (m_caption instanceof String)
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_PRESSED,
                                    (String) m_caption));
                        else
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_PRESSED, null));
                    }
                }
                return null;
            case KeyEvent.KEY_SELECT_RELEASED:
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    state &= ~STATE_SELECTED;
                    state |= STATE_DIRTY_REPAINT;
                    if (listener != null) {
                        if (m_caption instanceof String)
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED,
                                    (String) m_caption));
                        else
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED, null));
                    }
                }
                return null;
        }
        return e; // let the Container handle this KeyEvent
    }

    /*
     * (non-Javadoc)
     * 
     * @seejcontrol.ui.viper.AbstractFocusComponent#onRotaryTouchEvent(jcontrol.ui.viper.event.
     * RotaryTouchEvent)
     */
    @Override
    public RotaryTouchEvent onRotaryTouchEvent(RotaryTouchEvent e) {
        switch (e.getRotaryTouchState()) {
            case RotaryTouchEvent.STATE_TOUCHED:
                if ((state & STATE_SELECTED) == 0) {
                    state |= STATE_SELECTED | STATE_DIRTY_REPAINT;
                    if (listener != null) {
                        if (m_caption instanceof String)
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_PRESSED,
                                    (String) m_caption));
                        else
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_PRESSED, null));
                    }
                }
                return null;
            case RotaryTouchEvent.STATE_TOUCHED_MOVE: {
                // if a touched & move is performed the focus is lost and the button is released.
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    state &= ~STATE_SELECTED;
                    state |= STATE_DIRTY_REPAINT;
                    if (listener != null) {
                        if (m_caption instanceof String)
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED,
                                    (String) m_caption));
                        else
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED, null));
                    }
                }
                return e;
            }
            case RotaryTouchEvent.STATE_IDLE:
            case RotaryTouchEvent.STATE_SELECTED:
                if ((state & STATE_SELECTED) == STATE_SELECTED) {
                    state &= ~STATE_SELECTED;
                    state |= STATE_DIRTY_REPAINT;
                    if (listener != null) {
                        if (m_caption instanceof String)
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED,
                                    (String) m_caption));
                        else
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED, null));
                    }
                }
                return null;
        }
        return e; // let the Container handle this KeyEvent
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * jcontrol.ui.viper.AbstractFocusComponent#onTouchEvent(jcontrol.ui.viper.event.TouchEvent)
     */
    @Override
    public int onTouchEvent(TouchEvent e) {
        if ((state & STATE_SELECTED) == 0) {
            if (e.type == TouchEvent.TYPE_TOUCH_PRESSED) {
                if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) {
                    state |= STATE_SELECTED | STATE_DIRTY_REPAINT;
                    if (listener != null) {
                        if (m_caption instanceof String)
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_PRESSED,
                                    (String) m_caption));
                        else
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_PRESSED, null));
                    }
                    return RESULT_EXECUTED;
                }
            }
        } else { // button is selected
            if (e.type == TouchEvent.TYPE_TOUCH_RELEASED) {
                state &= ~STATE_SELECTED;
                state |= STATE_DIRTY_REPAINT;
                if (e.x >= x && e.x < x + width && e.y >= y && e.y < y + height) {
                    // touch released inside button area
                    if (listener != null) {
                        if (m_caption instanceof String)
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED,
                                    (String) m_caption));
                        else
                            onActionEvent(new ActionEvent(this, ActionEvent.BUTTON_RELEASED, null));
                    }
                    return RESULT_EXECUTED;
                }
                return RESULT_ACCEPTED;
            } else { // dragged
                if (e.x < x || e.x >= x + width || e.y < y || e.y >= y + height) { // dragged out
                    state &= ~STATE_SELECTED;
                    state |= STATE_DIRTY_REPAINT;
                    return RESULT_ACCEPTED;
                }
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
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) g.clearRect(x, y, width, height);

        // when no size is specified, compute preferred size
        if ((state & STATE_REVALIDATE) != 0 || width <= 0 || height <= 0) {
            if (m_caption instanceof String) {
                g.setFont(font);
                if ((state & STATE_WIDTH_FIXED) == 0) {
                    width = g.getTextWidth((String) m_caption) + 4;
                }
                if ((state & STATE_HEIGHT_FIXED) == 0) {
                    height = g.getFontHeight() + 3;
                }
                g.setFont(null);
            } else if (m_caption instanceof Resource) {
                if ((state & STATE_WIDTH_FIXED) == 0) {
                    width = ImageUtils.getWidth((Resource) m_caption);
                }
                if ((state & STATE_HEIGHT_FIXED) == 0) {
                    height = ImageUtils.getHeight((Resource) m_caption);
                }
            }
            state &= ~STATE_REVALIDATE;
        }

        // the caption is a String
        if (m_caption instanceof String) {
            g.setFont(font);
            int textWidth = g.getTextWidth((String) m_caption);
            if (textWidth > width - 3) textWidth = width - 3;
            int theight = g.getFontHeight();
            if (theight > height - 3) theight = height - 3;
            int xoffset = ((width - textWidth) >> 1) + (((state & STATE_SELECTED) != 0) ? 1 : 0);
            if (xoffset < 1) xoffset = 1;
            int yoffset = ((height - theight) >> 1) + (((state & STATE_SELECTED) != 0) ? 1 : 0);
            if (yoffset < 1) yoffset = 1;

            drawButton(g, x, y, width, height, ((state & STATE_SELECTED) != 0));
            g.drawString((String) m_caption, x + xoffset, y + yoffset, textWidth, theight, 0, 0);

            if (((state & STATE_FOCUS) == STATE_FOCUS) && ((state & STATE_SELECTED) == 0)) {
                g.setDrawMode(Display.XOR);
                if (yoffset - 2 <= 0) {
                    drawDottedRect(g, x + 1, y + 1, width - 3, height - 3);
                } else {
                    drawDottedRect(g, x + 1, y + yoffset - 2, width - 3, theight + 3);
                }
                g.setDrawMode(Display.NORMAL);
            }
            g.setFont(null);

            // the caption as image resource
        } else if (m_caption instanceof Resource) {
            int resourceWidth = ImageUtils.getWidth((Resource) m_caption);
            if (resourceWidth > width - 3) resourceWidth = width - 3;
            int rheight = ImageUtils.getHeight((Resource) m_caption);
            if (rheight > height - 3) rheight = height - 3;
            int xoffset = ((width - resourceWidth) >> 1)
                    + (((state & STATE_SELECTED) != 0) ? 1 : 0);
            if (xoffset < 1) xoffset = 1;
            int yoffset = ((height - rheight) >> 1) + (((state & STATE_SELECTED) != 0) ? 1 : 0);
            if (yoffset < 1) yoffset = 1;

            drawButton(g, x, y, width, height, ((state & STATE_SELECTED) != 0));
            g.drawImage(m_caption, x + xoffset, y + yoffset, resourceWidth, rheight, 0, 0);

            if (((state & STATE_FOCUS) == STATE_FOCUS) && ((state & STATE_SELECTED) == 0)) {
                g.setDrawMode(Display.XOR);
                drawDottedRect(g, x + 1, y + 1, width - 3, height - 3);
                g.setDrawMode(Display.NORMAL);
            }
        } else {
            if (width > 0 && height > 0)
                drawButton(g, x, y, width, height, ((state & STATE_SELECTED) != 0));
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Press/release the button. An ActionEvent is fired.
     * 
     * @param on press or release.
     */
    public void press(boolean on) {
        if (on) {
            state |= STATE_SELECTED;
        } else {
            state &= ~STATE_SELECTED;
        }
        redrawInternalAndParent();
    }

    /**
     * Replaces the current text or image on this button by the specified image. The size will be
     * re-calculated if it has not been specified with the <code>setBounds()</code>-method or in the
     * constructor.
     * 
     * @param image the new image to set.
     */
    public void setImage(Resource image) {
        m_caption = image;
        if ((state & STATE_WIDTH_FIXED) == 0) state |= STATE_REVALIDATE;
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

    /**
     * Replaces the current text or image on this button by the specified text. The size will be
     * re-calculated if it has not been specified with the <code>setBounds()</code>-method or in the
     * constructor.
     * 
     * @param text the new label to set. This can either be a string or an image resource.
     */
    public void setText(String text) {
        m_caption = text;
        if ((state & STATE_WIDTH_FIXED) == 0) state |= STATE_REVALIDATE;
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

}