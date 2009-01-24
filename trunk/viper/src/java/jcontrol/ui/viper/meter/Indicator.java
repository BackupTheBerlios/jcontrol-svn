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
package jcontrol.ui.viper.meter;

import jcontrol.io.Graphics;
import jcontrol.ui.viper.Component;

/**
 * This class <code>Indicator</code> provides a simple LED indicator. The LED can be turned on or
 * off to visualize a digital state.
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public class Indicator extends Component {

    /**
     * Creates a new Indicator at the specified position.
     * 
     * @param x The upper left x-coordinate.
     * @param y The upper left y-coordinate.
     */
    public Indicator(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 7;
        this.height = 7;
        state |= STATE_SIZE_FIXED;
    }

    /**
     * Returns the state of this indicator.
     * 
     * @return the state of this indicator.
     */
    public boolean getState() {
        return (state & STATE_SELECTED) == STATE_SELECTED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        if ((state & STATE_DIRTY_MASK) == STATE_DIRTY_PAINT_ALL) {
            g.clearRect(x, y, width, height);
        }
        if ((state & STATE_SELECTED) == STATE_SELECTED) {
            // draw on image
            g.drawImage(new String[]{"\u1C32\u7D7D\u7F3E\u1C00"}, x, y, width, height, 0, 0);
        } else {
            // draw off image
            g.drawImage(new String[]{"\u1C22\u4141\u4122\u1C00"}, x, y, width, height, 0, 0);
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Turns the indicator on or off.
     * 
     * @param on on or off
     */
    public void setState(boolean on) {
        if (on) {
            state |= STATE_SELECTED;
        } else {
            state &= ~STATE_SELECTED;
        }
        redrawInternalAndParent();
    }

}
