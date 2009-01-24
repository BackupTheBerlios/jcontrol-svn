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

/**
 * <p>
 * Instances of this class provide a border with an optional title.<br>
 * Possible styles are: <i>simple</i>, <i>round</i> and <i>etched</i>.
 * </p>
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public class Border extends Component {

    /** Rectangular etched border with round corners */
    public final static int STYLE_ETCHED_BORDER = 2;

    /** Rectangular border with round corners */
    public final static int STYLE_ROUND_BORDER = 1;

    /** Simple rectangular border */
    public final static int STYLE_SIMPLE_BORDER = 0;

    private String m_label;

    private int m_style = STYLE_SIMPLE_BORDER;

    /**
     * Creates a border.
     * 
     * @param label The string label of the border or <code>null</code>.
     * @param x The x-coordinate of the border's top-left corner.
     * @param y The y-coordinate of the border's top-left corner.
     * @param width The width of the border.
     * @param height The height of the border.
     * @param style The new style of the border. Possible values are
     *        <code>STYLE_SIMPLE_BORDER</code>, <code>STYLE_ROUND_BORDER</code> or
     *        <code>STYLE_ETCHED_BORDER</code>.
     */
    public Border(String label, int x, int y, int width, int height, int style) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.m_label = label;
        this.m_style = style;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public synchronized void paint(Graphics g) {
        // draw label
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
            g.clearRect(x, y, width, height);
            if (parent != null) {
                parent.setDirty(this, x, y, width, height, STATE_DIRTY_REPAINT, true);
            }
        }
        int yoff = 0;
        int xoff = 4;
        if (m_label != null && m_label.length() > 0) {
            g.setFont(font);
            yoff = g.getFontHeight() >> 1;
            int l = g.getTextWidth(m_label);
            if (l > width - 10) l = width - 10;
            g.drawString(m_label, x + xoff + 1, y, l, g.getFontHeight(), 0, 0);
            xoff += l + 1;
            g.setFont(null);
        }

        switch (m_style) {
            case STYLE_ETCHED_BORDER:
                g.drawLine(x + 2, y + height - 2, x + width - 2, y + height - 2); // lower inner
                // line
                g.drawLine(x + width - 2, y + yoff + 2, x + width - 2, y + height - 3); // right
                // inner
                // line
            case STYLE_ROUND_BORDER:
                g.drawLine(x, y + yoff + 1, x, y + height - 2); // left line
                g.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1); // lower line
                g.drawLine(x + width - 1, y + yoff + 1, x + width - 1, y + height - 2); // right
                // line

                g.drawLine(x + 1, y + yoff, x + 3, y + yoff); // upper line left of text
                g.drawLine(x + xoff, y + yoff, x + width - 2, y + yoff); // upper line right of text
                break;

            default:
                g.drawLine(x, y + yoff, x, y + height - 1); // left line
                g.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1); // lower line
                g.drawLine(x + width - 1, y + yoff, x + width - 1, y + height - 1); // right line

                g.drawLine(x + 1, y + yoff, x + 3, y + yoff); // upper line left of text
                g.drawLine(x + xoff, y + yoff, x + width - 2, y + yoff); // upper line right of text
                break;
        }
        state &= ~(STATE_REVALIDATE | STATE_DIRTY_MASK);
    }

    /**
     * Sets the label of this border.
     * 
     * @param text The new label of the border.
     */
    public void setText(String text) {
        this.m_label = text;
        setDirty(STATE_DIRTY_PAINT_ALL, true);
    }

}
