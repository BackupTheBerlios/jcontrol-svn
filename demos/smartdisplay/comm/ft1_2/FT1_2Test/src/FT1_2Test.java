/*
 * FT1_2Test.java
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

import jcontrol.comm.DisplayConsole;
import jcontrol.comm.FT1_2;
import jcontrol.comm.FT1_2EventListener;
import jcontrol.comm.RS232;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.lang.Math;
import jcontrol.io.Buzzer;

/**
 * Sends different FT1.2 telegrams on key press.
 * @version 1.0
 * @author  boehme
 */
public class FT1_2Test implements FT1_2EventListener {

    public static void main(String[] args) {
        try {
            RS232 rs=new RS232(); rs.close();   // Würgaround für static{}-GC Problem
            FT1_2 ft=new FT1_2();
            new FT1_2Test(ft);
        } catch (IOException e) {}
    }
    
    FT1_2 ft;
    DisplayConsole out;
    Display d;
    Keyboard k;
    
    private static Buzzer buzzer=new Buzzer();
    
    FT1_2Test(FT1_2 ft){
        ft.setListener(this);
        this.ft=ft;
        d=new Display();
        k=new Keyboard();
        out=new DisplayConsole(d);
        for(;;) try{
//            TimedThread.wait(null,(short)(1000+Math.rnd(1000)));
//            switch(Math.rnd(3)){
            switch(k.read()){
                case 0:
                case 'U':
                case 'u':
                    byte[] buf=new byte[Math.rnd(6)+5];
                    fill(buf);
                    writeSent("sent UDAT: ".concat(new String(buf)));
                    if(ft.sendUDAT(buf)) {
                        buzzer.on((short)2000);
                        writeRecvd("ACK");
                    } else {
                        buzzer.on((short)500);
                        writeRecvd("NACK");
                    }
                    buzzer.off();
                    break;
                case 1:
                case 'S':
                    writeSent("req STATUS");
                    if(ft.reqSTATUS()) {
                        buzzer.on((short)2000);
                        writeRecvd("OK");
                    } else {
                        buzzer.on((short)500);
                        writeRecvd("NOK");
                    }
                    buzzer.off();
                    break;
                case 2:case 'D':
                case 'd':
                    writeSent("req CLASS1");
                    buf=ft.reqCLASS1();
                    if(buf!=null) {
                        buzzer.on((short)2000);
                        writeRecvd("CLS1: ".concat(new String(buf)));
                    } else {
                        buzzer.on((short)500);
                        writeRecvd("NO CLS1");
                    }
                    buzzer.off();
                    break;
            }
        } catch(IOException e){
            try {
                writeSent("### IOException in main thread");
                buzzer.on((short)250,(short)250);
            } catch (IOException ex) {}
        }
    }

    static void fill(byte[] buf){
        for(int i=0;i<buf.length;i++) buf[i]=(byte)(Math.rnd('Z'-'!')+'!');
    }

    synchronized void writeSent(String message) throws IOException {
        if(out.pos>0) out.println();
        out.print(message);
    }

    synchronized void writeRecvd(String message) throws IOException {
        out.pos=128-d.getTextWidth(message);
        out.println(message);
    }
    
    /* (non-Javadoc)
     * @see jcontrol.comm.FT1_2EventListener#onIndication(byte[], int)
     */
    public void onIndication(byte[] udat, int control) {
        try {
            switch(control){
                case FT1_2.CF_PRM_SEND_UDAT:
                    ft.sendACK(udat);
                    writeSent("*recvd UDAT: ".concat(new String(udat)));
                    writeRecvd("sent ACK");
                    break;
                case FT1_2.CF_PRM_REQ_STATUS:
                    ft.sendResponse(null);
                    writeSent("*recvd req STATUS");
                    writeRecvd("sent STATUS");
                    break;
                case FT1_2.CF_PRM_REQ_CLASS1:
                    byte[] buf=new byte[Math.rnd(6)+5];
                    fill(buf);
                    ft.sendResponse(buf);
                    writeSent("*recvd req CLS1");
                    writeRecvd("sent CLS1: ".concat(new String(buf)));
                    break;
                default:
                    writeRecvd("raw: ".concat(Integer.toHexString(control)));
            }
            buzzer.on((short)1000);
        } catch(IOException e){
            try {
                writeSent("### IOException in event listener");
            } catch (IOException ex) {}
            ft.errorCode();
        }
        buzzer.off();
    }

}
