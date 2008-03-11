/**
 * (C) DOMOLOGIC Home Automation GmbH 2004
 * 
 * @author Marcus Timmermann
 * @version 1.0
 * 
 */
package jcontrol.util;

import jcontrol.system.Management;

/**
 * A queue is a FIFO (<b>F</b>irst-<b>I</b>n-<b>F</b>irst-<b>O</b>ut) buffer.
 * When elements are pushed into the queue they are appended at the end. Popping
 * elements always returns the element at the first index, i.e. the oldest element.  
 */
public class Queue {
    
    private static final int INCREMENT = 2;
    
    private Object[] m_objectList;
    
    private int m_pushIndex;
    private int m_popIndex;    
    
    /**
     * Creates a Queue instance.
     * 
     * @param initialSize the initial size of the internal data array. If the length of the
     *        queue exceeds this initial size, the data array will be enlarged.
     */
    public Queue(int initialSize) {
        m_objectList = new Object[initialSize];
        m_pushIndex = 0;
        m_popIndex = -1; // list is empty
    }
    
    /**
     * Pushes any object into the queue.
     * 
     * @param o an object to push into the queue.
     */
    public synchronized void push(Object o) {
        if (m_pushIndex==m_popIndex) {
            // enlarge data array            
            Object[] newList = new Object[m_objectList.length+INCREMENT];
            if (m_popIndex==0) {
                Management.arraycopy(m_objectList, 0, newList, 0, m_objectList.length);
            } else { // m_popIndex>0 (cannot be <0 here)
                
                // copy from m_popIndex to end of array  
                Management.arraycopy(m_objectList, m_popIndex, newList, 0, m_objectList.length-m_popIndex);
                
                // copy from 0 to m_popIndex
                Management.arraycopy(m_objectList, 0, newList, m_objectList.length-m_popIndex, m_popIndex);                
                m_popIndex = 0;
            }                         
            m_pushIndex = m_objectList.length;           
            m_objectList = newList;            
        }
        if (m_popIndex<0) m_popIndex = m_pushIndex;
        m_objectList[m_pushIndex] = o;
        m_pushIndex++;
        m_pushIndex%=m_objectList.length;       
    }
    
    /**
     * Removes the oldest element from the queue and returns it.
     * 
     * @return the oldest element or <code>null</code> if the queue is empty.
     */
    public synchronized Object pop() {
        if (m_popIndex<0) {
            return null;
        } else {
            Object o = m_objectList[m_popIndex++];
            m_popIndex%=m_objectList.length;
            if (m_popIndex==m_pushIndex) m_popIndex = -1;
            return o;
        }
    }
    
    /**
     * Returns the size of the queue.
     * 
     * @return the number of elements in the queue.
     */
    public synchronized int getSize() {
        if (m_popIndex<0) return 0;
        if (m_popIndex<m_pushIndex) return m_pushIndex-m_popIndex;
        return m_objectList.length-(m_popIndex-m_pushIndex);
    }
}
