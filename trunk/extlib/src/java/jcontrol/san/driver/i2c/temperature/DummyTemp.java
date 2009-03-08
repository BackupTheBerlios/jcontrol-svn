/*
 * Copyright (C) 2008-2009 The JControl Group and individual authors listed
 * below.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package jcontrol.san.driver.i2c.temperature;

import java.io.IOException;

import jcontrol.lang.Math;
import jcontrol.san.interfaces.sensors.Temperature;

/**
 * Dummy temperature sensor.
 * <p>
 * This is a dummy sensor, which use Math.rnd() for the temperature.
 * </p>
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision$
 */
public class DummyTemp extends AbstractTempI2CDriver implements Temperature {

   /**
    * Create a new object.
    */
   public DummyTemp() {
      super(0);
      updateValue();
   }

   /**
    * {@inheritDoc}
    * 
    * @see jcontrol.san.interfaces.sensors.Sensor#getMax()
    */
   public int getMax() {
      return Integer.MAX_VALUE;
   }

   /**
    * {@inheritDoc}
    * 
    * @see jcontrol.san.interfaces.sensors.Sensor#getMin()
    */
   public int getMin() {
      return Integer.MIN_VALUE;
   }

   /**
    * {@inheritDoc}
    * 
    * @see jcontrol.san.interfaces.sensors.Sensor#getName()
    */
   public String getName() {
      return "dummy";
   }

   /**
    * {@inheritDoc}
    * 
    * @see jcontrol.san.interfaces.sensors.Temperature#getTemp()
    */
   public int getTemp() throws IOException {

      // use random
      temp = Math.rnd(0);
      return temp;
   }
}
