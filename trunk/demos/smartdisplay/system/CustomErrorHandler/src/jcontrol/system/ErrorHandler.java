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
 * Example for a customized JControl Error-Handler.
 * The method <code>onError()</code> of this class is automatically invoked by the JControl
 * virtual machine if an error occurs.
 *
 * @author Marcus Timmermann, Helge Böhme, Gerrit Telkamp
 * @version 3.0
 */
public class ErrorHandler implements Runnable {

    // table of error messages
    private static String[] ERROR_MESSAGES = new String[]{
        "UnknownError",
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
        "WatchdogError"};

    // error types
    private static final int UNDEFINED_ERROR = 0;
    private static final int FATAL_ERROR     = 1;
    private static final int CODE_ERROR      = 2;
    private static final int RUNTIME_ERROR   = 3;

    private static final byte[] ERROR_TYPES = new byte[] {
        FATAL_ERROR,    // Unknown Error
        FATAL_ERROR,    // HandleError
        FATAL_ERROR,    // NullPointerException
        RUNTIME_ERROR,  // OutOfMemoryError
        CODE_ERROR,     // BytecodeNotAvailableError
        CODE_ERROR,     // BytecodeNotSupportedError
        CODE_ERROR,     // BytecodeNotDefinedError
        FATAL_ERROR,    // ArithmeticException
        RUNTIME_ERROR,  // NegativeArraySizeException
        CODE_ERROR,     // UnsupportedArrayTypeError
        RUNTIME_ERROR,  // ArrayIndexOutOfBoundsException
        FATAL_ERROR,    // ClassCastError
        CODE_ERROR,     // NoCodeError
        RUNTIME_ERROR,  // WaitForMonitorSignal
        FATAL_ERROR,    // ExternalNativeError
        FATAL_ERROR,    // FatalStackFrameOverflowError
        RUNTIME_ERROR,  // InstantiationException
        RUNTIME_ERROR,  // IllegalMonitorStateException
        FATAL_ERROR,    // UnsatisfiedPrelinkError
        CODE_ERROR,     // ClassFormatError
        RUNTIME_ERROR,  // ClassTooBigError
        RUNTIME_ERROR,  // PreLinkError
        FATAL_ERROR,    //PreLinkedUnresolvedError
        CODE_ERROR,     //UnsupportedConstantTypeError
        RUNTIME_ERROR,  // MalformatedDescriptorError
        RUNTIME_ERROR,  // RuntimeRefTableOverrunError
        CODE_ERROR,     // NoSuchFieldError
        RUNTIME_ERROR,  // IllegalAccessError
        CODE_ERROR,     // NoSuchMethodError
        RUNTIME_ERROR,  // TooMuchParametersError
        RUNTIME_ERROR,  // ThrowFinalError
        CODE_ERROR,     // NoClassDefFoundError
        RUNTIME_ERROR,  // IndexOutOfBoundsException
        RUNTIME_ERROR,  // ArrayDimensionError
        RUNTIME_ERROR,  // DeadlockError
        RUNTIME_ERROR,  // IncompatibleClassChangeError
        RUNTIME_ERROR,  // NotImplementedError
        RUNTIME_ERROR}; // WatchdogError


    private static Display  m_display;
    private static Keyboard m_keyboard;


   /**
    * Display the error message.
    *
    * @param code    the error code.
    * @param message the error message (textual error description).
    * @param jpc     points to the last bytecode to be executed when the error occured.
    * @param npc     points to the machine instruction that was producing the error.
    * @param jpcext  set if jpc points to external memory (e.g. flash).
    * @see onError(int code, int jpc, int npc, boolean jpcext).
    */
    public static void onError(int code, String message, int jpc, int npc, boolean jpcext){
	    // lights on!
    	jcontrol.io.Backlight.setBrightness(255);

        // set current flash bank where error occured
        String jpcbank;
        if (jpcext) {
            jpcbank=Management.getProperty("system.userbank");
            if (jpcbank==null) jpcbank="0";
        } else {
            // when jpcext is set, error was caused by ROM code
            jpcbank="f";
        }

        // Print the error message to the RS232 port, using standard communication parameters. 
        // When the RS232-Terminal of the JControl/IDE is opened, this message is used to
        // back-trace the error automatically.
        try {
            RS232 rs232 = new RS232();
            String errorString = "\nJControl/ErrorHandler: Code=".concat(String.valueOf(code)).concat(" NPC=0xf").concat(Integer.toHexString(npc)).concat(" JPC=0x").concat(jpcbank).concat(Integer.toHexString(jpc)).concat("\n");
            rs232.write(errorString.getBytes(), 0, errorString.length());
            rs232.close();
        } catch (IOException e) {            
        }

        // create display and keyboard instance
        m_display = new Display();
        m_keyboard = new Keyboard();

        // start timing thread to control buzzer signals and reboot
        Thread t = new Thread(new ErrorHandler());
        t.setDaemon(true);
        t.start();

        // draw error screen
        m_display.clearDisplay();
        m_display.drawImage(new String[] { // draw the main headline
            "\u0000\u80C0\uE0E0\uF0F8\uF8FC\uFAF1\uE1E1\uC182\u0408\u7080\u0000\u8000\u0040\u0000\u0080\u00C0\uC0C0\uC0C0\uC0C0\uC0C0\uC000\uC0C0\uC0C0\uC0C0\uC0C0\uC080\u0000\u00C0\uC0C0\uC0C0\uC0C0\uC0C0\u8000\u0000\u0080\uC0C0\uC0C0\uC0C0\u8000\u0000\uC0C0\uC0C0\uC0C0\uC0C0\uC080\u0000\u00C0\uC0C0\uC000\u0000\u80C0\uE0E0\uF0F8\uF8FC\uFAF1\uE1E1\uC182\u0408\u7080\u0000\u8000\u0040\u0000\u0080",
            "\uFCFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\u7F9F\uFFFF\uFFFC\u0003\u0408\u9012\u4411\u4450\u0210\u00FF\uFFFF\uFF39\u3939\u3939\u0100\uFFFF\uFFFF\u3939\uF9FF\uDFDF\u0F00\u00FF\uFFFF\uFF39\u39F9\uFFDF\uDF0F\u007C\uFFFF\uFF83\u0101\u83FF\uFFFF\u7C00\uFFFF\uFFFF\u3939\uF9FF\uDFDF\u0F00\u000F\u7F7F\u0F00\uFCFF\uFFFF\uFFFF\uFFFF\uFFFF\uFFFF\u7F9F\uFFFF\uFFFC\u0003\u0408\u9012\u4411\u4450\u0110",
            "\u0003\u070F\u1F1F\u3F3F\u3F3D\u3D3E\u1E1F\u0F07\u0300\u0000\u0100\u0000\u0401\u0000\u0104\u0007\u0707\u0707\u0707\u0707\u0700\u0707\u0707\u0000\u0007\u0707\u0704\u0007\u0707\u0700\u0000\u0707\u0707\u0400\u0103\u0707\u0707\u0707\u0301\u0000\u0707\u0707\u0000\u0007\u0707\u0704\u0002\u0707\u0200\u0003\u070F\u1F1F\u3F3F\u3F3D\u3D3E\u1E1F\u0F07\u0300\u0000\u0100\u0000\u0401\u0000\u0104"}, 0,0,128,22,0,0);
        m_display.drawRect(0, 25, 128, 39); // the error text rectangle

        // generate error message string when not passed by caller
        if (message == null) {
            if ((code < 0) || (code >= ERROR_MESSAGES.length)) {
                message = "ErrorCode 0x".concat(Integer.toHexString(code));
            } else {
                message = ERROR_MESSAGES[code];
            }
        }

        // draw error message
        m_display.drawString(message, 2, 28);
        // draw native PC
        m_display.drawString("NPC:",2, 44);
        m_display.drawString("0xf".concat(Integer.toHexString(npc)), 24, 44);
        // draw java PC
        m_display.drawString("JPC:", 2, 36);
        m_display.drawString("0x".concat(jpcbank).concat(Integer.toHexString(jpc)), 24, 36);

        m_display.drawString("Press any key to restart...",2,54);

        // get error type
        int type = UNDEFINED_ERROR;
        if ((code < 0) || (code >= ERROR_TYPES.length)) {
            type = ERROR_TYPES[code];
        }
        // display error type image
        switch (type) {
            case FATAL_ERROR:
                m_display.drawImage(new String[] { // :P
                    "\u0080\u6010\u2864\u4482\uC261\u2101\u0131\u6142\u82C4\u6408\u1060\u8000",
                    "\uFE01\u0000\u0406\u0301\u0182\u8480\u8084\u0603\u0103\u0604\u0000\u01FE",
                    "\u0003\u0C10\u2446\u4281\u830C\u1126\u2841\u47B9\u8246\u4420\u100C\u0300",
                    "\u0000\u0000\u0000\u0000\u0001\u0101\u0101\u0100\u0000\u0000\u0000\u0000"}, 101, 32, 24, 25, 0,0);
                break;            
            case CODE_ERROR:
                m_display.drawImage(new String[] { // ?
                    "\u0000\u8040\u2010\u080C\u0A0A\u0909\u0909\u0911\u1121\u4282\uA448\uB0C0\u0000",
                    "\uF886\u8180\u8080\u80F0\u582C\u1C04\u8870\u0000\u0000\u0001\uFE55\u3A0F\u0000",
                    "\u0000\u0000\u0000\u0000\uF00C\u0201\u0000\u00E0\uD8F4\u9EDD\uDCFC\uFCF8\u7800",
                    "\u0000\u0000\u0000\u0000\uFD87\u8585\u8585\u85FD\u562B\u1F07\u0301\u0000\u0000"}, 101, 28, 25, 32,0,0);
                break;
            default:
                m_display.drawImage(new String[] { // /!\
                    "\u0000\u0000\u0000\u00C0\u300C\u0281\u8103\u0E3C\uF0C0\u0000\u0000\u0000\u0000",
                    "\u0000\u00C0\u300C\u0300\u0000\u7FFF\uFF7F\u0000\u0003\u0F3C\uF0C0\u0000\u0000",
                    "\u304C\u43C0\uC0C0\uC0C0\uC0C0\uC8DD\uDDC8\uC0C0\uC0C0\uC0C0\uC0C3\uCFFC\u7000"}, 101, 32, 25, 24,0,0);
                break;
        }

        // wait for keypress
        m_keyboard.read();
        // dispose display instance
        m_display=null;
    }


   /**
    * Run method is used for acoustic signals and reboot after 10s
    */
    public void run(){
        // generate acoustic error signal if property is enabled
        if("true".equals(Management.getProperty("buzzer.systembeep"))){
            // we use PWM to bypass the property "buzzer.enable"
            PWM.setFrequency(400);
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
            // wait for 10 sek.
            ThreadExt.sleep(10000);
            // reboot device
            Management.reboot(true);
        } catch (InterruptedException e) {}
    }

}