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

import jcontrol.io.Graphics;
import jcontrol.io.Resource;
import jcontrol.toolkit.ImageUtils;

/**
 * <p>
 * A label is a very simple component which displays a single line of read-only text or an image
 * resource. The text can be changed by the application, but a user cannot edit it directly.<br>
 * Labels are passive components, they can neither be selected nor fire any events.
 * </p>
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public class Label extends Component {

    /** Align text to the left */
    public static final int STYLE_ALIGN_LEFT = 1;

    /** Align text to the middle */
    public static final int STYLE_ALIGN_CENTER = 2;

    /** Align text to the right */
    public static final int STYLE_ALIGN_RIGHT = 4;

    /** Inverse mode */
    public static final int STYLE_DRAW_INVERSE = 8;

    /** Paint a border around the label */
    public static final int STYLE_SHOW_BORDER = 16;

    /** Normal (non-inverse) mode */
    public static final int STYLE_DRAW_NORMAL = 0;

    private static final int ALIGN_MASK = STYLE_ALIGN_CENTER | STYLE_ALIGN_LEFT | STYLE_ALIGN_RIGHT;

    /** String or image ressource */
    private Object m_label;

    /** Text alignment and style */
    private int m_style = STYLE_ALIGN_LEFT;

    /**
     * Constructs a new label. The size will be calculated as big as is fits the given label.
     * 
     * @param image an image to print on the label.
     * @param x the x-coordinate of the label's top-left corner
     * @param y the y-coordinate of the label's top-left corner
     */
    public Label(Resource image, int x, int y) {
        m_label = image;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new label with specified bounds and alignment.
     * 
     * @param image the image to display on the label
     * @param x the x-coordinate of the label's top-left corner
     * @param y the y-coordinate of the label's top-left corner
     * @param width the width of the label
     * @param height the height of the label
     * @param style the alignment and style of the label. Possible values are one of the styles
     *        <code>STYLE_ALIGN_LEFT</code>, <code>STYLE_ALIGN_CENTER</code> or
     *        <code>STYLE_ALIGN_RIGHT</code> and optional <code>STYLE_DRAW_INVERSE</code> and
     *        <code>STYLE_SHOW_BORDER</code>
     */
    public Label(Resource image, int x, int y, int width, int height, int style) {
        m_label = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        state |= STATE_WIDTH_FIXED;
        state &= ~STATE_REVALIDATE;
        if ((style & ALIGN_MASK) == 0) {
            m_style &= ALIGN_MASK;
            m_style |= style;
        } else {
            m_style = style;
        }
    }

    /**
     * Constructs a new label. The size will be calculated as big as is fits the given label.
     * 
     * @param text a text to print on the label.
     * @param x the x-coordinate of the label's top-left corner
     * @param y the y-coordinate of the label's top-left corner
     */
    public Label(String text, int x, int y) {
        m_label = text;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new label with specified bounds and alignment.
     * 
     * @param text the label to display on the label
     * @param x the x-coordinate of the label's top-left corner
     * @param y the y-coordinate of the label's top-left corner
     * @param width the width of the label
     * @param height the height of the label
     * @param style the alignment and style of the label. Possible values are one of the styles
     *        <code>STYLE_ALIGN_LEFT</code>, <code>STYLE_ALIGN_CENTER</code> or
     *        <code>STYLE_ALIGN_RIGHT</code> and optional <code>STYLE_DRAW_INVERSE</code> and
     *        <code>STYLE_SHOW_BORDER</code>
     */
    public Label(String text, int x, int y, int width, int height, int style) {
        m_label = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        state |= STATE_WIDTH_FIXED;
        state &= ~STATE_REVALIDATE;
        if ((style & ALIGN_MASK) == 0) {
            m_style &= ALIGN_MASK;
            m_style |= style;
        } else {
            m_style = style;
        }
    }

    /**
     * Returns the caption of this label.
     * 
     * @return the caption of this label.
     */
    public Object getLabel() {
        return m_label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public synchronized void paint(Graphics g) {
        int borderSize;
        int marginWidth;
        if ((m_style & STYLE_SHOW_BORDER) != 0) {
            borderSize = 1;
            marginWidth = 2;
        } else {
            borderSize = 0;
            marginWidth = 0;
        }
        {// when no size is specified, compute preferred size
            boolean revalidate = ((state & STATE_REVALIDATE) != 0 && (state & STATE_WIDTH_FIXED) == 0);
            boolean revalidateWidth = revalidate || width <= 0;
            boolean revalidateHeight = revalidate || height <= 0;
            if (revalidate || revalidateWidth || revalidateHeight) {
                if (m_label instanceof String) {
                    g.setFont(font);
                    if (revalidateWidth)
                        this.width = g.getTextWidth((String) m_label) + (marginWidth << 1);
                    if (revalidateHeight) this.height = g.getFontHeight() + (borderSize << 1);
                } else if (m_label instanceof Resource) {
                    if (revalidateWidth)
                        this.width = ImageUtils.getWidth((Resource) m_label) + (marginWidth << 1);
                    if (revalidateHeight)
                        this.height = ImageUtils.getHeight((Resource) m_label) + (borderSize << 1);
                }
                state &= ~STATE_REVALIDATE;
            }
        }
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
            g.clearRect(x + borderSize, y + borderSize, width - (borderSize << 1), height
                    - (borderSize << 1));
        }
        if ((m_style & STYLE_SHOW_BORDER) == STYLE_SHOW_BORDER) {
            g.drawRect(x, y, width, height);
        }
        if ((m_style & STYLE_DRAW_INVERSE) == STYLE_DRAW_INVERSE) {
            g.setDrawMode(Graphics.INVERSE);
        }
        int labelWidth = 0;
        int labelHeight = 0;

        if (m_label instanceof String) {
            // the label is a string
            g.setFont(font);
            labelWidth = g.getTextWidth((String) m_label);
            labelHeight = g.getFontHeight();
        } else if (m_label instanceof Resource) {
            // the label is an image
            labelWidth = ImageUtils.getWidth((Resource) m_label);
            labelHeight = ImageUtils.getHeight((Resource) m_label);
        }
        int xoffset = marginWidth;
        int yoffset = borderSize;
        if (labelWidth > width - (marginWidth << 1)) {
            labelWidth = width - (marginWidth << 1); // cut the width if it doesn't fit
        }
        if (labelHeight > height - (borderSize << 1)) {
            labelHeight = height - (borderSize << 1); // cut the height if it doesn't fit
        } else {
            yoffset = ((height - labelHeight) >> 1);
        }
        switch (m_style & ALIGN_MASK) {
            case STYLE_ALIGN_CENTER:
                xoffset = ((width - labelWidth) >> 1);
                break;
            case STYLE_ALIGN_RIGHT:
                xoffset = width - marginWidth - labelWidth;
                break;
        }
        g.clearRect(x + xoffset, y + borderSize, labelWidth, yoffset - borderSize); // top
        g.clearRect(x + borderSize, y + borderSize, xoffset - borderSize, height
                - (borderSize << 1)); // left
        g.clearRect(x + xoffset + labelWidth, y + borderSize, width - xoffset - labelWidth
                - borderSize, height - (borderSize << 1)); // right
        g.clearRect(x + xoffset, y + labelHeight + yoffset, labelWidth, height
                - (yoffset + labelHeight) - borderSize); // below
        if (m_label instanceof String) {
            g.drawString((String) m_label, x + xoffset, y + yoffset, labelWidth, labelHeight, 0, 0);
        } else if (m_label instanceof Resource) {
            g.drawImage(m_label, x + xoffset, y + yoffset, labelWidth, labelHeight, 0, 0);
        }
        g.setFont(null);
        if ((m_style & STYLE_DRAW_INVERSE) == STYLE_DRAW_INVERSE) {
            g.setDrawMode(Graphics.NORMAL);
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Replaces the current text or image on this label by the specified image. The size will be
     * re-calculated if it has not been specified with the <code>setBounds()</code>-method or in the
     * constructor.
     * 
     * @param image the new image to set.
     */
    public void setImage(Resource image) {
        m_label = image;
        if ((state & STATE_WIDTH_FIXED) == 0) state |= STATE_REVALIDATE;
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

    /**
     * Sets the style of the label.
     * 
     * @param style the alignment and style of the label. Possible values are one of the styles
     *        <code>STYLE_ALIGN_LEFT</code>, <code>STYLE_ALIGN_CENTER</code> or
     *        <code>STYLE_ALIGN_RIGHT</code> and optional <code>STYLE_DRAW_INVERSE</code>
     */
    public void setStyle(int style) {
        if ((style & ALIGN_MASK) == 0) {
            m_style &= ALIGN_MASK;
            m_style |= style;
        } else {
            m_style = style;
        }
        if ((state & STATE_WIDTH_FIXED) == 0) state |= STATE_REVALIDATE;
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

    /**
     * Replaces the current text or image on this label by the specified text. The size will be
     * re-calculated if it has not been specified with the <code>setBounds()</code>-method or in the
     * constructor.
     * 
     * @param text the new text
     */
    public void setText(String text) {
        m_label = text;
        if ((state & STATE_WIDTH_FIXED) == 0) state |= STATE_REVALIDATE;
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

}
