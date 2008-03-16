/**
 * (C) DOMOLOGIC Home Automation GmbH 2004
 * 
 * @author Marcus Timmermann
 * @version 1.0
 * 
 */
package jcontrol.util;


public interface DataProducer {
    
    /**
     * Returns the minimum value of this value producer.
     * 
     * @return the minimum value of this value producer.
     */
    public int getMin();
    
    /**
     * Returns the maximum value of this value producer.
     * 
     * @return the maximum value of this value producer.
     */
    public int getMax();
    
    /**
     * Returns the last measured value.
     * 
     * @return the last measured value.
     */
    public int getValue();
    
    /**
     * Returns a unit string for the graphical representation of the value.
     * @return a unit string.
     */
    public String getUnit();
    
    /**
     * Returns the exponent of the value to show.
     * 
     * @return the exponent of the value to show.
     */
    public int getExponent();
    
    /**
     * Updates the measured value.
     *
     */
    public void updateValue();
}
