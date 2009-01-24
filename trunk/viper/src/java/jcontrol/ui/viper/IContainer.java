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

/**
 * IContainer is an interface for Frame and Container classes.
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public interface IContainer {

    /**
     * Adds a component to this IContainer
     * 
     * @param component the component to add.
     */
    public void add(Component component);

    /**
     * Removes a component from this IContainer.
     * 
     * @param component the component to remove
     */
    public void remove(Component component);

    /**
     * Removes all components from this IContainer.
     */
    public void removeAll();

    /**
     * Marks all components inside this container that intersect the given bounds to be repainted.
     * This method should not be used by any application.
     * 
     * @param Object source the calling object that must be a child of this container or null
     * @param x
     * @param y
     * @param width
     * @param height
     * @param state the state to set
     * @return
     */
    boolean setDirty(Object source, int x, int y, int width, int height, int state, boolean b);
}
