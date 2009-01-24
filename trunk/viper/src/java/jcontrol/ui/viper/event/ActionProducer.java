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
 * This interface is implemented by all components that can fire ActionEvents.
 * 
 * @see jcontrol.ui.viper.event.ActionEvent
 * @see jcontrol.ui.viper.event.ActionListener
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public interface ActionProducer {

    /**
     * Returns the ActionListener that has been set by setActionListener.
     * 
     * @return the ActionListener for this class
     */
    public ActionListener getActionListener();

    /**
     * Sets an ActionListener to the ActionProducer.
     * 
     * @param listener The new ActionListener.
     */
    public void setActionListener(ActionListener listener);

}
