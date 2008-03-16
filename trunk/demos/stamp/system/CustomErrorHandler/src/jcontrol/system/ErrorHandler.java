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
import jcontrol.io.Keyboard;
import jcontrol.io.PWM;
import jcontrol.lang.ThreadExt;

/**
 * Example for a customized JControl Error-Handler. The method
 * <code>onError()</code> of this class is automatically invoked by the
 * JControl virtual machine if an error occurs.
 * 
 * @author Marcus Timmermann, Helge Böhme, Gerrit Telkamp
 * @version 3.0
 */
public class ErrorHandler implements Runnable {

	// table of error messages
	private static String[] ERROR_MESSAGES = new String[] { "UnknownError",
			"HandleError", "NullPointerException", "OutOfMemoryError",
			"BytecodeNotAvailableError", "BytecodeNotSupportedError",
			"BytecodeNotDefinedError", "ArithmeticException",
			"NegativeArraySizeException", "UnsupportedArrayTypeError",
			"ArrayIndexOutOfBoundsException", "ClassCastError", "NoCodeError",
			"WaitForMonitorSignal", "ExternalNativeError",
			"FatalStackFrameOverflowError", "InstantiationException",
			"IllegalMonitorStateException", "UnsatisfiedPrelinkError",
			"ClassFormatError", "ClassTooBigError", "PreLinkError",
			"PreLinkedUnresolvedError", "UnsupportedConstantTypeError",
			"MalformatedDescriptorError", "RuntimeRefTableOverrunError",
			"NoSuchFieldError", "IllegalAccessError", "NoSuchMethodError",
			"TooMuchParametersError", "ThrowFinalError",
			"NoClassDefFoundError", "IndexOutOfBoundsException",
			"ArrayDimensionError", "DeadlockError",
			"IncompatibleClassChangeError", "NotImplementedError",
			"WatchdogError" };

	// error types
	private static final int UNDEFINED_ERROR = 0;

	private static final int FATAL_ERROR = 1;

	private static final int CODE_ERROR = 2;

	private static final int RUNTIME_ERROR = 3;

	private static final byte[] ERROR_TYPES = new byte[] { FATAL_ERROR, // Unknown
																		// Error
			FATAL_ERROR, // HandleError
			FATAL_ERROR, // NullPointerException
			RUNTIME_ERROR, // OutOfMemoryError
			CODE_ERROR, // BytecodeNotAvailableError
			CODE_ERROR, // BytecodeNotSupportedError
			CODE_ERROR, // BytecodeNotDefinedError
			FATAL_ERROR, // ArithmeticException
			RUNTIME_ERROR, // NegativeArraySizeException
			CODE_ERROR, // UnsupportedArrayTypeError
			RUNTIME_ERROR, // ArrayIndexOutOfBoundsException
			FATAL_ERROR, // ClassCastError
			CODE_ERROR, // NoCodeError
			RUNTIME_ERROR, // WaitForMonitorSignal
			FATAL_ERROR, // ExternalNativeError
			FATAL_ERROR, // FatalStackFrameOverflowError
			RUNTIME_ERROR, // InstantiationException
			RUNTIME_ERROR, // IllegalMonitorStateException
			FATAL_ERROR, // UnsatisfiedPrelinkError
			CODE_ERROR, // ClassFormatError
			RUNTIME_ERROR, // ClassTooBigError
			RUNTIME_ERROR, // PreLinkError
			FATAL_ERROR, // PreLinkedUnresolvedError
			CODE_ERROR, // UnsupportedConstantTypeError
			RUNTIME_ERROR, // MalformatedDescriptorError
			RUNTIME_ERROR, // RuntimeRefTableOverrunError
			CODE_ERROR, // NoSuchFieldError
			RUNTIME_ERROR, // IllegalAccessError
			CODE_ERROR, // NoSuchMethodError
			RUNTIME_ERROR, // TooMuchParametersError
			RUNTIME_ERROR, // ThrowFinalError
			CODE_ERROR, // NoClassDefFoundError
			RUNTIME_ERROR, // IndexOutOfBoundsException
			RUNTIME_ERROR, // ArrayDimensionError
			RUNTIME_ERROR, // DeadlockError
			RUNTIME_ERROR, // IncompatibleClassChangeError
			RUNTIME_ERROR, // NotImplementedError
			RUNTIME_ERROR }; // WatchdogError

	private static Keyboard m_keyboard;

	/**
	 * Display the error message.
	 * 
	 * @param code
	 *            the error code.
	 * @param message
	 *            the error message (textual error description).
	 * @param jpc
	 *            points to the last bytecode to be executed when the error
	 *            occured.
	 * @param npc
	 *            points to the machine instruction that was producing the
	 *            error.
	 * @param jpcext
	 *            set if jpc points to external memory (e.g. flash).
	 * @see onError(int code, int jpc, int npc, boolean jpcext).
	 */
	public static void onError(int code, String message, int jpc, int npc,
			boolean jpcext) {

		// set current flash bank where error occured
		String jpcbank;
		if (jpcext) {
			jpcbank = Management.getProperty("system.userbank");
			if (jpcbank == null)
				jpcbank = "0";
		} else {
			// when jpcext is set, error was caused by ROM code
			jpcbank = "f";
		}

		// Print the error message to the RS232 port, using standard
		// communication parameters.
		// When the RS232-Terminal of the JControl/IDE is opened, this message
		// is used to
		// back-trace the error automatically.
		try {
			RS232 rs232 = new RS232();
			String errorString = "\nJControl/ErrorHandler: Code=".concat(
					String.valueOf(code)).concat(" NPC=0xf").concat(
					Integer.toHexString(npc)).concat(" JPC=0x").concat(jpcbank)
					.concat(Integer.toHexString(jpc)).concat("\n");
			rs232.write(errorString.getBytes(), 0, errorString.length());
			rs232.close();
		} catch (IOException e) {
		}

		// create keyboard instance
		m_keyboard = new Keyboard();

		// start timing thread to control buzzer signals and reboot
		Thread t = new Thread(new ErrorHandler());
		t.setDaemon(true);
		t.start();

		// generate error message string when not passed by caller
		if (message == null) {
			if ((code < 0) || (code >= ERROR_MESSAGES.length)) {
				message = "ErrorCode 0x".concat(Integer.toHexString(code));
			} else {
				message = ERROR_MESSAGES[code];
			}
		}

		// Print the error message to the RS232 port, using standard
		// communication parameters.
		// When the RS232-Terminal of the JControl/IDE is opened, this message
		// is used to
		// back-trace the error automatically.
		try {
			RS232 rs232 = new RS232();
			String errorString = "\nJControl/ErrorHandler: Code=".concat(
					String.valueOf(code)).concat(" NPC=0xf").concat(
					Integer.toHexString(npc)).concat(" JPC=0x").concat(jpcbank)
					.concat(Integer.toHexString(jpc)).concat("\n");
			rs232.write(errorString.getBytes(), 0, errorString.length());
			rs232.close();
		} catch (IOException e) {
		}

		// wait for keypress
		m_keyboard.read();
	}

	/**
	 * Run method is used for acoustic signals and reboot after 10s
	 */
	public void run() {
		// generate acoustic error signal if property is enabled
		if ("true".equals(Management.getProperty("buzzer.systembeep"))) {
			// we use PWM to bypass the property "buzzer.enable"
			PWM.setFrequency(400);
			PWM.setDuty(Buzzer.BUZZERCHANNEL, 128);
			PWM.setActive(Buzzer.BUZZERCHANNEL, true);
			try {
				ThreadExt.sleep(500);
				PWM.setFrequency(200);
				ThreadExt.sleep(250);
			} catch (InterruptedException e) {
			}
			PWM.setActive(Buzzer.BUZZERCHANNEL, false);
		}
		try {
			// wait for 10 sek.
			ThreadExt.sleep(10000);
			// reboot device
			Management.reboot(true);
		} catch (InterruptedException e) {
		}
	}

}