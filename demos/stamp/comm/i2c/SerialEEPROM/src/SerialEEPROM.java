/*
 * SerialEEPROM.java
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

import java.io.IOException;

import jcontrol.comm.I2C;
import jcontrol.comm.RS232;

/**
 * Utilizes the I<sup>2</sup>C Bus for access to serial EEproms of the 24Cxx series.
 * A simple command shell is running on the RS232 port at 192000 baud for accessing
 * the EEprom. Make sure that you turn on "auto echo" in the terminal program. 
 */
public class SerialEEPROM {

  /** Atmel EEPROM types */
  public static final int TYPE_C02=2;
  public static final int TYPE_C04=4;
  public static final int TYPE_C08=8;
  public static final int TYPE_C16=16;

  /** EEPROM chip address */
  private int address;
  /** EEPROM size */
  private int size;
  /** EEPROM maximum burst transfer */
  private int maxburst;

  /** Constructs a new SerEPP access object
   * @param type the chip type of the serial EEprom, 
   *         possible values are defined as constants
   * @param address the address of the chip as defined by 
   *         its address input pins, on C04, C08 and C16 
   *         a number of LSBs is ignored.
   */
  public SerialEEPROM(int type, int address){
    this.address = ((16-type) & (address<<1)) + 0xa0;
    this.size = type << 7;
    this.maxburst = (type >= TYPE_C04) ? 16 : 8;
  }

  /** Reads out the serial EEPROM to the destination byte array.
   * @param data the byte array to fill with data
   * @param startindex the index in data to start filling
   * @param length number of bytes to read
   * @param wordaddr the word address of the EEprom to start reading
   * @return the number of bytes read (could be less than array stop-startindex)
   * @throws IOException on communication error (maybe data is partially filled)
   */
  public int read(byte[] data, 
                   int startindex, 
                   int length, 
                   int wordaddr) throws IOException {
    int read = 0,stopindex=startindex+length;
    byte[] sendaddr = new byte[1];
    int i = size-wordaddr+startindex;
    if (stopindex > i) stopindex = i; // check stopindex is inside param boundaries
    
    // read bytes from EEPROM using I2C
    while (startindex < stopindex) {
      sendaddr[0] = (byte)(wordaddr&255);
      int count = stopindex-startindex; // transfer length
      int devaddr = address+((wordaddr>>7)&14); // read address
      i = 256-(wordaddr&255);
      if (count>i) count=i; // page boundry
      
      // I2C access
      I2C i2c = new I2C(devaddr);
      i2c.read(sendaddr,data,startindex,count);
      
      // update variables
      startindex += count;
      wordaddr += count;
      read += count;
    }
    // return number of bytes read
    return read;
  }

  /** Writes a byte array to the serial EEPROM.
   * @param data the byte array to write
   * @param startindex the index in data to start transmitting
   * @param stopindex the index in data to stop transmitting
   * @param wordaddr the word address of the EEprom to start writing
   * @return the number of bytes written (could be less than array stop-startindex)
   * @throws IOException on communication error (maybe data is partially transmitted)
   */
  public int write(byte[] data,
                    int startindex,
                    int length,
                    int wordaddr) throws IOException {
    int written=0,stopindex=startindex+length;
    int i = size-wordaddr+startindex;
    byte[] sendaddr = new byte[1];
    if (stopindex > i) stopindex = i; // check stopindex is inside param boundaries
    while (startindex < stopindex) {
      sendaddr[0] = (byte)(wordaddr&255);
      int count = stopindex-startindex; // transfer length
      int devaddr = address+((wordaddr>>7)&14); // write address
      i = 256-(wordaddr&255);
      if (count > i) count=i; // page boundry
      if (count > maxburst) count = maxburst; // maximum burst transfer

      // I2C access
      I2C i2c = new I2C(devaddr);
      i2c.write(sendaddr,data,startindex,count);
      
      // update variables
      startindex += count;
      wordaddr += count;
      written += count;
    }
    // return number of bytes written
    return written;
  }

  /**
   * Test code for the <code>SerialEEPROM</code> functions. 
   */
  public static void main(String[] args) {
    try{
      RS232 rs=new RS232(19200);
      rs.println("I2C EEprom command shell");
      rs.println("a set address (decimal integer+CR)");
      rs.println("w write a string to set address (String+CR)");
      rs.println("r read a string from set address (decimal integer+CR)");
      rs.println("turn on your local echo");
      int addr=0;
      SerialEEPROM epp=new SerialEEPROM(TYPE_C02,0);
      for(;;){
        try{
          rs.write('>');
          char c=rs.read();
          switch(c){
            case 'a':
              rs.println();
              rs.print("new address: ");
              String line=rs.readLine();
              rs.println();
              addr=Integer.parseInt(line);
              rs.println("set address to: ".concat(String.valueOf(addr)));
              break;
            case 'w':
              rs.println();
              rs.print("data to store: ");
              line=rs.readLine();
              rs.println();
              byte[] buf=line.getBytes();
              epp.write(buf, 0, buf.length, addr);
              rs.print("written ".concat(String.valueOf(buf.length)));
              rs.println("bytes to address: ".concat(String.valueOf(addr)));
              break;
            case 'r':
              rs.println();
              rs.print("amount of bytes to read: ");
              line=rs.readLine();
              rs.println();
              buf=new byte[Integer.parseInt(line)];
              epp.read(buf, 0, buf.length, addr);
              rs.print("read string \"".concat(new String(buf)));
              rs.println("\" from address: ".concat(String.valueOf(addr)));
              break;
          }
        } catch(IOException e){
          rs.println("IOException");
        }
      }
    } catch(IOException e){}
  }
}
