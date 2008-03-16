/*
 * ErrorHandler.java
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

package jcontrol.system;
import java.io.IOException;
import jcontrol.comm.RS232;
import jcontrol.io.Buzzer;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.PWM;
import jcontrol.lang.ThreadExt;

/**
 * The JControl Error-Handler.
 * The method <code>onError()</code> of this class is automatically invoked by the JControl
 * virtual machine if an error occurs.
 *
 * @author Marcus Timmermann
 * @author Helge Böhme
 * @version 2.0
 * @since Yogi1
 */
public class ErrorHandler implements Runnable {

    private static Display  lcd;
    private static Keyboard keys;

   /**
    * Just display the error message.
    *
    * @param code    the error code.
    * @param message the error message (textual error description).
    * @param jpc     points to the last bytecode to be executed when the error occured.
    * @param npc     points to the machine instruction that was producing the error.
    * @param jpcext  set if jpc points to external memory (e.g. flash).
    * @see onError(int code, int jpc, int npc, boolean jpcext).
    */
    public static void onError(int code, String message, int jpc, int npc, boolean jpcext){
/*        message=null;
        jcontrol.system.TimedThread.wait(null,(short)100);*/
        if(message==null){
            message=(new String[] {
                "Unknown Error",
                "HandleError",
                "NullPointerException",
                "OutOfMemoryError",
                "BytecodeNotAvailableError",
                "BytecodeNotSupportedError",
                "BytecodeNotDefinedError",
                "ArithmeticException",
                "NegativeArraySizeException",
                "UnsupportedArrayTypeError",
                "ArrayIndexOutOfBoundsException",
                "ClassCastError",
                "NoCodeError",
                "WaitForMonitorSignal",
                "ExternalNativeError",
                "FatalStackFrameOverflowError",
                "InstantiationException",
                "IllegalMonitorStateException",
                "UnsatisfiedPrelinkError",
                "ClassFormatError",
                "ClassTooBigError",
                "PreLinkError",
                "PreLinkedUnresolvedError",
                "UnsupportedConstantTypeError",
                "MalformatedDescriptorError",
                "RuntimeRefTableOverrunError",
                "NoSuchFieldError",
                "IllegalAccessError",
                "NoSuchMethodError",
                "TooMuchParametersError",
                "ThrowFinalError",
                "NoClassDefFoundError",
                "IndexOutOfBoundsException",
                "ArrayDimensionError",
                "DeadlockError",
                "IncompatibleClassChangeError",
                "NotImplementedError",
                "WatchdogError"
            })[code];
        } else {
            message=" ".concat(message);
        }
        
        try {
            RS232 rs232 = new RS232();
            String errorString = "\nJControl/ErrorHandler: Code=".concat(String.valueOf(code)).concat(" JPC=0x").concat(toHex(jpc)).concat("\n");
            rs232.write(errorString.getBytes(),0,errorString.length());
            rs232.close();
        } catch (IOException e) {            
        }
        lcd = new Display();
        keys = new Keyboard();

        Thread t=new Thread(new ErrorHandler());
        t.setDaemon(true);
        t.start();
        lcd.clearDisplay();
        lcd.drawImage(new String[] {                              // draw the main headline
                          "\u0000\u80C0\uE0E0\uF0F8\uF8FC\uFAF1\uE1E1\uC182\u0408\u7080\u0000\u8000\u0040\u0000\u0080\u00C0\uC0C0\uC0C0\uC0C0\uC0C0\uC000\uC0C0\uC0C0\uC0C0\uC0C0\uC080\u0000\u00C0\uC0C0\uC0C0\uC0C0\uC0C0\u8000\u0000\u0080\uC0C0\uC0C0\uC0C0\u8000\u0000\uC0C0\uC0C0\uC0C0\uC0C0\uC080\u0000\u00C0\uC0C0\uC000\u0000\u80C0\uE0E0\uF0F8\uF8FC\uFAF1\uE1E1\uC182\u0408\u7080\u0000\u8000\u0040\u0000\u0080",
                          "\uFCFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\u7F9F\uFFFF\uFFFC\u0003\u0408\u9012\u4411\u4450\u0210\u00FF\uFFFF\uFF39\u3939\u3939\u0100\uFFFF\uFFFF\u3939\uF9FF\uDFDF\u0F00\u00FF\uFFFF\uFF39\u39F9\uFFDF\uDF0F\u007C\uFFFF\uFF83\u0101\u83FF\uFFFF\u7C00\uFFFF\uFFFF\u3939\uF9FF\uDFDF\u0F00\u000F\u7F7F\u0F00\uFCFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\u7F9F\uFFFF\uFFFC\u0003\u0408\u9012\u4411\u4450\u0110",
                          "\u0003\u070F\u1F1F\u3F3F\u3F3D\u3D3E\u1E1F\u0F07\u0300\u0000\u0100\u0000\u0401\u0000\u0104\u0007\u0707\u0707\u0707\u0707\u0700\u0707\u0707\u0000\u0007\u0707\u0704\u0007\u0707\u0700\u0000\u0707\u0707\u0400\u0103\u0707\u0707\u0707\u0301\u0000\u0707\u0707\u0000\u0007\u0707\u0704\u0002\u0707\u0200\u0003\u070F\u1F1F\u3F3F\u3F3D\u3D3E\u1E1F\u0F07\u0300\u0000\u0100\u0000\u0401\u0000\u0104"}, 0,0,128,22,0,0);
        lcd.drawRect(0, 25, 128, 39);                             // the error text rectangle
//        lcd.setFont(Display.SYSTEMFONT);
        if (code>33 || code<0) code = 0;
//        lcd.drawString("Error code: ".concat(String.valueOf(code)), 2, 28);
        lcd.drawString(message, 2, 28);
        lcd.drawString("JPC:", 2, 36);
        int pos = drawHex(jpc, 18, 36);                           // draw the jpc hex value
        if (jpcext) lcd.drawString("external memory", pos+1, 36);
        lcd.drawString("NPC:",2, 44);
        drawHex(npc, 18, 44);                                     // draw the npc hex value
        lcd.drawString("Press any key to restart...",2,54);
        switch (code) {                                           // decide which image to paint
            case 0x01:        // Handle
            case 0x02:        // NullPointer
            case 0x07:        // Arithmetic
            case 0x0b:        // ClassCast
            case 0x0e:        // ExternalNative
            case 0x0f:        // FatalStackFrameOverflow
            case 0x12:        // UnsatisfiedPrelink
            case 0x16:        // ExternalUnresolved
                lcd.drawImage(new String[] {       // :P
                                  "\u0080\u6010\u2864\u4482\uC261\u2101\u0131\u6142\u82C4\u6408\u1060\u8000",
                                  "\uFE01\u0000\u0406\u0301\u0182\u8480\u8084\u0603\u0103\u0604\u0000\u01FE",
                                  "\u0003\u0C10\u2446\u4281\u830C\u1126\u2841\u47B9\u8246\u4420\u100C\u0300",
                                  "\u0000\u0000\u0000\u0000\u0001\u0101\u0101\u0100\u0000\u0000\u0000\u0000"}, 101, 32, 24, 25, 0,0);
                break;
            case 0x04:        // BytecodeNA
            case 0x05:        // BytecodeNS
            case 0x06:        // BytecodeND
            case 0x09:        // UnsupportedArrayType
            case 0x0c:        // NoCode
            case 0x13:        // ClassFormat
            case 0x17:        // UnsupportedConstantType
            case 0x1a:        // NoSuchField
            case 0x1c:        // NoSuchMethod
            case 0x1f:        // NoClassDefFound
                lcd.drawImage(new String[] {       // ?
                                  "\u0000\u8040\u2010\u080C\u0A0A\u0909\u0909\u0911\u1121\u4282\uA448\uB0C0\u0000",
                                  "\uF886\u8180\u8080\u80F0\u582C\u1C04\u8870\u0000\u0000\u0001\uFE55\u3A0F\u0000",
                                  "\u0000\u0000\u0000\u0000\uF00C\u0201\u0000\u00E0\uD8F4\u9EDD\uDCFC\uFCF8\u7800",
                                  "\u0000\u0000\u0000\u0000\uFD87\u8585\u8585\u85FD\u562B\u1F07\u0301\u0000\u0000"}, 101, 28, 25, 32,0,0);
                break;
            default:
                lcd.drawImage(new String[] {       // /!\
                                  "\u0000\u0000\u0000\u00C0\u300C\u0281\u8103\u0E3C\uF0C0\u0000\u0000\u0000\u0000",
                                  "\u0000\u00C0\u300C\u0300\u0000\u7FFF\uFF7F\u0000\u0003\u0F3C\uF0C0\u0000\u0000",
                                  "\u304C\u43C0\uC0C0\uC0C0\uC0C0\uC8DD\uDDC8\uC0C0\uC0C0\uC0C0\uC0C3\uCFFC\u7000"}, 101, 32, 25, 24,0,0);
                break;
        }
        keys.read();
        lcd=null;
    }

    public void run(){
        if("true".equals(Management.getProperty("buzzer.systembeep"))){     // if sound is enabled
            PWM.setFrequency(400); // we use PWM to bypass the "buzzer.enable" property
            PWM.setDuty(Buzzer.BUZZERCHANNEL,128);
            PWM.setActive(Buzzer.BUZZERCHANNEL, true);
            try {
                ThreadExt.sleep(500);
                PWM.setFrequency(200);
                ThreadExt.sleep(250);
            } catch (InterruptedException e) {}
            PWM.setActive(Buzzer.BUZZERCHANNEL, false);
        }
        try {
            ThreadExt.sleep(10000);
            Management.reboot(true);
        } catch (InterruptedException e) {}
    }

    /**
     * Draws an integer value as a hex string.
     *
     * @param value  the int value to paint.
     * @param x      the x coordinate.
     * @param y      the y coordinate.
     * @return the next x position to paint.
     */
    private static int drawHex(int value, int x, int y) {
        for (int i=12;i>=0;i-=4) {
            int digit=(value>>i)&0x0f;
            String[] hexdigit = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
            x+=lcd.drawString(hexdigit[digit], x, y);
//            if (digit<10) x+=lcd.drawChar((char)('0'+digit),x,y,(byte)0xff);
//            else          x+=lcd.drawChar((char)('A'-10+digit),x,y,(byte)0xff);
        }
        return x;
    }
    
    private static String toHex(int value) {
        byte[] buf=new byte[4];
        for (int i=3;i>=0;i--) {
            int digit=value & 15;
            if (digit<10) buf[i]=(byte)('0'+digit);
            else         buf[i]=(byte)('a'-10+digit);
            value>>=4;
        }
        return new String(buf);
    }
}