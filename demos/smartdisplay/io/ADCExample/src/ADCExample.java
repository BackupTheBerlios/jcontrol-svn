/*
 * ADCExample.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import jcontrol.io.ADC;
import jcontrol.io.Display;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Management;

/**
 * This example shows how to read out ADC values on JControl.
 * Furthermore, the usage of multiple threads in one application
 * is demonstrated.
 */
public class ADCExample {
  /** array to store ADCReader instances in */
  ADCReader adcreaders[];
  
  
  /**
   * Inner class <code>ADCReader</code> realizes a thread
   * that continuously reads and stores ADC values.
   */
  class ADCReader extends Thread {
    /** adc channel */    
    int channel;
    /** last read value */
    int value = 0;    
    /** thread instance */
    Thread instance = null;
    
    /**
     * Constructs an <code>ADCReader</code> thread.
     * @param channel the ADC input channel to read values from
     */
    public ADCReader(int channel) {
      // lights on!
      jcontrol.io.Backlight.setBrightness(255);

      instance = this;
      this.channel = channel;
      this.start();
    }
    
    /**
     * This method is invoked when the thread execution starts.
     * @see java.lang.Runnable#run()
     */
    public void run() {
      while (instance == this) {
        // read current adc value
        value = ADC.getValue(channel);
        
        // sleep for 100 millis
        try {
          ThreadExt.sleep(100);
        } catch (InterruptedException e) {}
      }
    }
    
    /**
     * Stop thread execution.
     */
    public void quit() {
      instance = null;
    }
    
    /**
     * Retrieve current adc value 
     */
    public int getValue() {
      return value;
    }
  }


  /**
   * Constructs an <code>ADCExample</code> instance. 
   * We start as many <code>ADCReader</code> threads as
   * adc input channels exist.
   */
  public ADCExample() {
    String s = Management.getProperty("io.adcchannels");
    int numchannels = Integer.parseInt(s);
    
    adcreaders = new ADCReader[numchannels];
    
    // create ADCReader threads and store instances into an array
    for (int i=0; i<numchannels; i++)
      adcreaders[i] = new ADCReader(i);
      
    // draw adc status continuously on lcd    
    drawStatus(); 
  }
  
  
  /**
   * Draw ADC values (read by ADCReader threads) on lcd
   */
  void drawStatus() {
    Display lcd = new Display();
    
    while (true) {
      lcd.clearDisplay();
      
      // show adc values on lcd    
      for (int i=0; i<adcreaders.length; i++) {
        lcd.drawString("ADC channel ".concat(
                        String.valueOf(i)).concat(" value: ").concat(
                        String.valueOf(adcreaders[i].getValue())),
                       0, 8*i);
      }
      
      // sleep for 500 millis
      try {
        ThreadExt.sleep(500);
      } catch (InterruptedException e) {}
    }
  }

  /**
   * Main method. Program execution starts here.
   */
  public static void main(String[] args) {
    new ADCExample();
  }
}
