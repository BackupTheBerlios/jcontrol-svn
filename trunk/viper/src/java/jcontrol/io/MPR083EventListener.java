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
package jcontrol.io;

import java.io.IOException;

/**
 * Event listener for MPR083 rotary sensor. <br>
 * not used in the current viper implementation.
 * 
 * @author roebbenack
 * @since Viper 1.0
 * @version $Revision$
 */
public interface MPR083EventListener {

    public void communicationError(IOException e);

    public void leftRotate(int position, int count);

    public void pressed(int position);

    public void released();

    public void rightRotate(int position, int count);

    public void select(int position);

}