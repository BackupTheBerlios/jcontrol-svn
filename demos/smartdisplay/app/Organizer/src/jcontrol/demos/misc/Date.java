/*
 * Date.java
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

package jcontrol.demos.misc;

import java.io.IOException;
import jcontrol.storage.FlashTlv;
import jcontrol.storage.TlvFile;
import jcontrol.system.Time;

/** 
 * Encapsulates an appointment composed of a time and a description text.
 * The appointments could be stored to and restored from persistent memory.
 * 
 * @version 1.0
 * @author  Marcus Timmermann
 */
public class Date {

    public static final byte DATETAG = (byte)0x01;
    private static final int tlvBank = -1;                  // flash bank to use    
    private static FlashTlv tlv;
    static{
        try {
            tlv = new FlashTlv(tlvBank);
            if (!tlv.isFormated()) {           // format tlv system if necessary
                tlv.format(-1);
            }
        } catch (IOException e) {
            tlv=null;
        }
    }

    public jcontrol.system.Time time;
    public String text;
    private TlvFile file=null;

    /**
     * Constructs a new Date.
     * @param time the appointments point of time
     * @param text the appointments description
     */
    public Date(jcontrol.system.Time time, String text) {
        this.time = time;
        this.text = text;
    }

    /**
     * Removes an appointment from persistent storage.
     */
    public void remove() throws IOException {
        if(file==null) getNextDate(time);
        if(file==null) throw new IOException(); 
        // if there is a TLV-file
        tlv.open(file); // open
        file=null;
        //byte[] data =
        tlv.read();   // get the tlv entry's data
        tlv.delete();               // delete the entry (it should be overwritten by creating a new one)
    }

    /**
     * Stores this appointment to persistent strage.
     */
    public void store() throws IOException {
        if(file!=null) remove();
        byte[] data;            // the data as byte[]
        if (text==null) {  // if there is no subject in here
            data = new byte[6]; 
    
        } else {                // date with subject
            data = new byte[5+text.length()];
        }
        data[0] = (byte)(time.year-2000);  // write the time data
        data[1] = (byte)(time.month);
        data[2] = (byte)(time.day);
        data[3] = (byte)(time.hour);
        data[4] = (byte)(time.minute);
        if (text!=null) {                  // if there is a subject
            byte[] buf = text.getBytes();
            for (int c=0; c<buf.length; c++) {  // write the subject data bytes
                data[c+5] = buf[c];
            }
        } else {    // no subject -> write a $ to indicate an empty subject message
            data[5] = (byte)'$';
        }
        tlv.create(DATETAG);     // create a new entry
        tlv.write(data);         // write the data into the tlv entry
        tlv.close();             // close the entry (now, writing into this tlv entry is no longer possible)
    }

    /**
     * Returns a date from persistent storage.
     * 
     * @param time the starting time. If time is null, the current time will be taken
     * @return the next date available in persistent storage or <code>null</code> if not found
     */
    public static Date getNextDate(Time time) {
        if (time==null) time = new Time();
        int syear = 2101;      // the "smallest" initial time
        int smonth = 13;       // these values will be replaced by smaller ones if there are
        int sday = 32;         // finally, the date indicated by these values will be returned 
        int shour = 24;
        int smin = 60;
        TlvFile sfile=null;
        String message="";
        try {
            TlvFile file = tlv.findNext(null, DATETAG); // find the fist date entry
            while (file!=null) {        // while there are more entries
                tlv.open(file);         // open the current entry for reading
                byte[] data = tlv.read();       // get the entry's date
                int year  = (int)data[0]+2000;
                int month = (int)data[1];
                int day   = (int)data[2];
                int hour  = (int)data[3];
                int min   = (int)data[4];
    //            boolean del = false;
                boolean found = false;
                if (year<syear) {       // check if the current date is before or equals the earliest known date
                    found = true; 
                } else if (year==syear) {
                    if (month<smonth) {
                        found = true; 
                    } else if (month==smonth) {
                        if (day<sday) {
                            found = true; 
                        } else if (day==sday) {
                            if (hour<shour) {
                                found = true;
                            } else if (hour==shour) {
                                if (min<=smin) {
                                    found = true;
                                }
                            }
                        }
                    }
                }
                if (found) {        // check if the current date is before the specified date
                                    // if yes, it must be skipped
                    if (year<time.year) {
                        found = false;
    //                    del = true;
                    } else if (year==time.year) {
                        if (month<time.month) {
                            found = false;
    //                        del = true;
                        } else if (month==time.month) {
                            if (day<time.day) {
                                found = false;
    //                            del = true;
                            } else if (day==time.day) {
                                if (hour<time.hour) {
                                    found = false;
    //                                del = true;
                                } else if (hour==time.hour) {
                                    if (min<time.minute) {
                                        found = false;
    //                                    del = true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (found) {  // if a date was found
                    syear = year;       // set this date the "smallest"
                    smonth = month;
                    sday = day;
                    shour = hour;
                    smin = min;                
                    message=new String(data, 5, data.length-5);
                    sfile = file;
    //            } else {
    //                if (del && !now) {
    //                    tlv.delete();    // delete the entry that was before the specified date
    //                }
                }
                file = tlv.findNext(file, DATETAG);        // get the next entry
            }
            if (syear<2101) {   // if the earliest found date if before the intial date
                time.year = syear;      
                time.month = smonth;
                time.day = sday;
                time.hour = shour;
                time.minute = smin;
                Date sdate=new Date(time,message);
                sdate.file=sfile;
                return sdate;                          // return the date object
            }
        } catch (Exception e) {}
        return null;
    }       

}