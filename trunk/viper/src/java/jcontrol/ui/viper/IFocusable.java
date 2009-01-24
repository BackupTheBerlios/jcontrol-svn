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

import jcontrol.ui.viper.event.IKeyListener;

/**
 * This interface must be implementes by all classes that should be able to gain the keyboard focus.
 * 
 * @author Marcus Timmermann
 * @see jcontrol.ui.viper.FocusFrame
 * @see jcontrol.ui.viper.AbstractFocusComponent
 * @since Viper 1.0
 * @version $Revision$
 */
public interface IFocusable extends IKeyListener {

    /** Focus transfer direction forward. */
    public final static int TRANSFER_FOCUS_FORWARD = 100;

    /** Focus transfer direction backward. */
    public final static int TRANSFER_FOCUS_BACKWARD = 101;

    /**
     * Forces the component to get the keyboard focus.
     * 
     * @return true if the component has got the keyboard focus.
     */
    public boolean requestFocus();

}
