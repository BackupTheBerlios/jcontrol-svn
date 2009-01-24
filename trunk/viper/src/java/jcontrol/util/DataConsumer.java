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
package jcontrol.util;

/**
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public interface DataConsumer {

    /**
     * Returns the data producer of this DataConsumer.
     * 
     * @return the data producer of this DataConsumer.
     */
    public DataProducer getDataProducer();

    /**
     * Sets a data producer to this DataConsumer.
     * 
     * @param producer The data producer to set.
     */
    public void setDataProducer(DataProducer producer);

    /**
     * Sets the DataConsumer's value ranges. The ranges specified here will override the ranges
     * specified by the DataProducer.
     * 
     * @param min Minimum value of the DataConsumer's range
     * @param max Maximum value of the DataConsumer's range
     */
    public void setRange(int min, int max);

    /**
     * Sets the value of the meter
     * 
     * @param value The value to set.
     */
    public void setValue(int value);

}
