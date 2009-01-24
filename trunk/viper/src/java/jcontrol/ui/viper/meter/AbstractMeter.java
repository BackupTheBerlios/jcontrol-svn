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
package jcontrol.ui.viper.meter;

import jcontrol.ui.viper.Component;
import jcontrol.util.DataConsumer;
import jcontrol.util.DataProducer;

/**
 * Abstract superclass for meter components.
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public abstract class AbstractMeter extends Component implements DataConsumer {

    protected int m_style;

    protected int m_digits = 1;

    protected int m_min = 0;

    protected int m_max = 0;

    /** The current value of the numeric display */
    protected int m_value = 0;

    protected DataProducer m_dataProducer;

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataConsumer#getDataProducer()
     */
    public DataProducer getDataProducer() {
        return m_dataProducer;
    }

    /**
     * @param value
     * @return
     */
    protected int log10(int value) {
        int count = value < 0 ? 1 : 0;
        do {
            value /= 10;
            count++;
        } while (value != 0);
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataConsumer#setDataProducer(jcontrol.util.DataProducer)
     */
    public void setDataProducer(DataProducer producer) {
        if (producer != null && m_dataProducer != producer) {
            m_dataProducer = producer;
            // set the meter's range
            setRange(producer.getMin(), producer.getMax());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataConsumer#setRange(int, int)
     */
    public void setRange(int min, int max) {
        if (min < max) {
            if (m_dataProducer != null) {
                if (m_dataProducer.getMin() < m_dataProducer.getMax()) {
                    if (min >= m_dataProducer.getMin() && min < m_dataProducer.getMax()) {
                        m_min = min;
                    }
                    if (max <= m_dataProducer.getMax() && max > m_dataProducer.getMin()) {
                        m_max = max;
                    }
                } else {
                    return;
                }
            } else {
                m_min = min;
                m_max = max;
            }
            if (m_value > m_max) m_value = m_max;
            if (m_value < m_min) m_value = m_min;
        } else if (min > max) {
            if (m_dataProducer != null) {
                if (m_dataProducer.getMin() > m_dataProducer.getMax()) {
                    if (min <= m_dataProducer.getMin() && min > m_dataProducer.getMax()) {
                        m_min = min;
                    }
                    if (max >= m_dataProducer.getMax() && max < m_dataProducer.getMin()) {
                        m_max = max;
                    }
                } else {
                    return;
                }
            } else {
                m_min = min;
                m_max = max;
            }
            if (m_value < m_max) m_value = m_max;
            if (m_value > m_min) m_value = m_min;
        } else {
            return;
        }
        m_digits = log10(m_max);
        int minLog10 = log10(m_min);
        if (m_digits < minLog10) m_digits = minLog10;
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.util.DataConsumer#setValue(int)
     */
    public void setValue(int value) {
        if (value != m_value) {
            if (m_min < m_max) {
                if (value < m_min) {
                    m_value = m_min;
                } else if (value > m_max) {
                    m_value = m_max;
                } else {
                    m_value = value;
                }
            } else {
                if (value < m_max) {
                    m_value = m_max;
                } else if (value > m_min) {
                    m_value = m_min;
                } else {
                    m_value = value;
                }
            }
            setDirty(STATE_DIRTY_UPDATE, true);
        }
    }

}
