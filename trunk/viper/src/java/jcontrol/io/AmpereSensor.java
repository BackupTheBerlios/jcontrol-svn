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

import jcontrol.lang.Math;
import jcontrol.util.DataProducer;

/**
 * This is a wrapper class to read ampere values from the ADC that implements a DataConsumer.
 * 
 * @author Marcus Timmermann
 * @author Alexander Schade
 * @since Viper 1.0
 * @version $Revision$
 */
public class AmpereSensor implements DataProducer {

    /** The adc channel. */
    private int m_channel;

    /** The max. value of the sensor in mA. */
    private int m_max;

    /** The value of the ampere sensor. */
    private int m_value;

    /**
     * Creates a new ampere sensor for the specified ADC channel.
     * 
     * @param channel an ADC channel
     * @param max_mA the maximum value for this AmpereSensor in mA.
     */
    public AmpereSensor(int channel, int max_mA) {
        m_channel = channel;
        m_max = max_mA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getExponent()
     */
    public int getExponent() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getMax()
     */
    public int getMax() {
        return m_max;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getMin()
     */
    public int getMin() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getUnit()
     */
    public String getUnit() {
        return "mA";
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getValue()
     */
    public int getValue() {
        return m_value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#updateValue()
     */
    public void updateValue() {
        m_value = Math.scale(ADC.getValue(m_channel), 255, getMax());
    }
}