/**
 * (C) DOMOLOGIC Home Automation GmbH 2004
 *
 * @author Marcus Timmermann
 * @version 1.0
 *
 */
package jcontrol.util;


public interface DataConsumer {

    /**
     * Sets the value of the meter
     *
     * @param value The value to set.
     */
    public void setValue(int value);
    
    /**
     * Sets a data producer to this DataConsumer.
     *
     * @param producer The data producer to set.
     */
    public void setDataProducer(DataProducer producer);
    
    /**
     * Returns the data producer of this DataConsumer.
     * 
     * @return the data producer of this DataConsumer.
     */
    public DataProducer getDataProducer();
    
    /**
     * Sets the DataConsumer's value ranges. The ranges specified here
     * will override the ranges  specified by the DataProducer.
     *
     * @param min Minimum value of the DataConsumer's range
     * @param max Maximum value of the DataConsumer's range
     */
    public void setRange(int min, int max);

}
