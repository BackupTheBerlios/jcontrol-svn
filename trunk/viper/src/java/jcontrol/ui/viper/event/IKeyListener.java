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
 * The interface <code>KeyListener</code> must be implemented by components that want to receive key
 * events when a key was pressed.
 * </p>
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.event.KeyEvent
 * @since Viper 1.0
 * @version $Revision$
 */
public interface IKeyListener extends EventListener {

    /**
     * Every time a key event is fired, this method will be called.
     * 
     * @param e The key event.
     * @return <code>null</code> if the KeyEvent has been consumed by the KeyListener,<br>
     *         or the the specified KeyEvent if it should be passed on to the next KeyListener.
     */
    public KeyEvent onKeyEvent(KeyEvent e);

}
