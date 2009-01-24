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

import java.util.EventListener;

/**
 * <p>
 * The interface <code>TouchListener</code> must be implemented by components that want to receive
 * touch events when the screen was touched.
 * </p>
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.event.TouchEvent
 * @since Viper 1.0
 * @version $Revision$
 */
public interface ITouchListener extends EventListener {

    public static final int RESULT_NONE = 0;

    public static final int RESULT_ACCEPTED = 1 << 0;

    public static final int RESULT_EXECUTED = 1 << 1 | RESULT_ACCEPTED;

    /**
     * Every time a touch event is fired, this method will be called.
     * 
     * @param event The touch event.
     * @return <code>RESULT_ACCEPTED</code> if the TouchEvent has been consumed by the
     *         TouchListener,<br>
     *         <code>RESULT_EXECUTED</code> if the TouchEvent has been consumed by the TouchListener
     *         and some important action has been performed, e.g. an action event has been invoked,<br>
     *         <code>RESULT_NONE</code> if the TouchEvent should be passed on to the next
     *         TouchListener.
     */
    public int onTouchEvent(TouchEvent event);

}
