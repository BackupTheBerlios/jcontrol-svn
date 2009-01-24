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
package jcontrol.ui.viper.event;

/**
 * An interface for a touch sensor. (e.g. MPR083EJ).
 * 
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public interface IRotaryTouchListener {

    /**
     * Every time a key event is fired, this method will be called.
     * 
     * @param e a rotary touch event
     * @return <code>null</code> if the event has been consumed by a rotary touch listener or the
     *         the specified event if it should be passed on to the next rotary touch listener.
     */
    public RotaryTouchEvent onRotaryTouchEvent(RotaryTouchEvent e);

}
