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
 * This is a wrapper class to read voltage values from the ADC that implements a DataConsumer.
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public class VoltageSensor implements DataProducer {

    /** Type 3,3 volt. */
    public static final int TYPE_3V3 = 33;

    /** Type 5 volt. */
    public static final int TYPE_5V = 50;

    /** The adc channel. */
    int m_channel;

    /** The type of the voltage sensor. Value: TYPE_5V. */
    int m_type = TYPE_5V;

    /** The value of the voltage sensor. */
    int m_value;

    /**
     * Creates a new VoltageSensor.
     * 
     * @param channel the ADC channel
     * @param type the type, either TYPE_3V3 or TYPE_5V
     */
    public VoltageSensor(int channel, int type) {
        m_type = type;
        m_channel = channel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getExponent()
     */
    public int getExponent() {
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataProducer#getMax()
     */
    public int getMax() {
        return m_type;
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
        return "V";
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
        m_value = Math.scale(ADC.getValue(m_channel), 255, m_type);
    }
}