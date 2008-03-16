/*
 * FT1_2Monitor.java
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
import jcontrol.comm.RS232;
import jcontrol.comm.RS232Selector;
import jcontrol.io.Buzzer;
import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.PWM;
//import jcontrol.io.Portpins;

/**
 * Monitors incoming FT1.2 telegrams and sends simple telegrams on key press. For more
 * complex telegram types, see FT1_2Test.java.
 */
public class FT1_2Monitor extends RS232 implements Runnable {

  /** Default baudrate for communication */
  public static final int BAUDRATE=19200;

  static Display lcd = null;
  static Keyboard keys = null;
  static DisplayConsole d = null;

  /* Some protocol constants */
  private static final byte FIXED_LENGTH_START=0x10;
  private static final byte FIXED_LENGTH_END=0x16;
  private static final byte VARIABLE_LENGTH_START=0x68;
  private static final byte VARIABLE_LENGTH_END=0x16;
  private static final char SINGLE_CHARACTER_ACK=0xe5;
  private static final int FRAME_MAX_LENGTH=23;

  /* Some control field constants */
  private static final int CF_SNDDIR=0;
  private static final int CF_RCVDIR=0x80;
//    private static final int CF_SNDDIR=0x80; // for testing the other side
//    private static final int CF_RCVDIR=0;
  public static final boolean isBCU=(CF_RCVDIR==0);
  private static final int CF_PRM=0x40;
  private static final int CF_FCB=0x20;
  private static final int CF_FCV=0x10;
  private static final int CF_PRM_SEND_RESET=0;
  /** for use in the FT1_2 event loop
   * @see FT1_2EventListener#onIndication(byte[], int)
   */
  public static final int CF_PRM_SEND_UDAT=0x03;
  /** for use in the FT1_2 event loop
   * @see FT1_2EventListener#onIndication(byte[], int)
   */
  public static final int CF_PRM_REQ_STATUS=0x09;
  /** for use in the FT1_2 event loop
   * @see FT1_2EventListener#onIndication(byte[], int)
   */
  public static final int CF_PRM_REQ_CLASS1=0x0a;
  private static final int CF_PRM_REQ_CLASS2=0x0b;
  private static final int CF_SEC_CONFIRM_ACK=0;
  private static final int CF_SEC_CONFIRM_NAK=0x01;
  private static final int CF_SEC_RESPOND_UDAT=0x08;
  private static final int CF_SEC_RESPOND_NAK=0x09;
  private static final int CF_SEC_RESPOND_STATUS=0x0b;

  /** Frame toggle bit */
  private int fcb=0;

  // Link Layer
  /** data request */
  static final byte L_DATA_req       = 0x11;
  /** data indication */
  static final byte L_DATA_ind       = 0x49;
  /** data confirm */
  static final byte L_DATA_con       = 0x4E;
  /** unkown code */
  static final byte UNKNOWN          = 0x00;

  // PEI-Services
  /** write value to BCU memory */
  static final byte PC_SET_VAL_req   = (byte)0x46;
  /** get value from BCU memory */
  static final byte PC_GET_VAL_req   = (byte)0x4C;
  /** PC_GET_VAL_req confirm */
  static final byte PC_GET_VAL_con   = (byte)0x4B;


  /** ourself */
  protected static FT1_2Monitor instance=null;

  /** message buffer */
  byte msgbuf[] = new byte[50];
  int mpos = 0;
  boolean receiving = false;

  /**
   * Main method.
   */
  public static void main(String[] args) {
    try {
      new FT1_2Monitor();
    } catch (IOException e) {}
  }

  private static Buzzer buzzer=new Buzzer();

  /**
   * Constructor.
   * @see java.lang.Object#Object()
   */
  public FT1_2Monitor() throws IOException {
    super(BAUDRATE);               // always use default baudrate
    setParams(PARITY_EVEN);
    RS232Selector.selectPort(RS232Selector.INTERNAL);

    keys = new Keyboard();
    lcd = new Display();
    d = new DisplayConsole(lcd);

    // switch to powerline
//    Portpins.setMode(8, Portpins.OUT); // switch to low (following pullup!)
//    Portpins.set(8, false);

    // turn on backlight
    PWM.setFrequency((short)200);
    PWM.setDuty((byte)2, (short)0);

    buzzer.on((short)500,(short)100);
    buzzer.on((short)1000,(short)100);
    buzzer.on((short)2000,(short)100);

    new Thread(this).start();

    // wait for keyboard activity
    for(;;) {
      char c = keys.read();
      switch (c) {
        case 'L': byte b[] = getTestDgram(L_DATA_req, new byte[]{0x60,0x0}, 5);
                  sendFrame(b);
                  break;

        case 'R': b = getTestDgram(L_DATA_req, new byte[]{0x60,0x0}, 10);
                  sendFrame(b);
                  break;

        case 'U': b = getTestDgram(L_DATA_req, new byte[]{0x60,0x1}, 5);
                  sendFrame(b);
                  break;

        case 'D': b = getTestDgram(L_DATA_req, new byte[]{0x60,0x1}, 10);
                  sendFrame(b);
                  break;
        case 'S': sendPCDataReq();
                  break;
        default:
      }
    }
  }


  /**
   * FT1_2 event listener. This method is invoked everytime
   * a FT1_2 frame drops in.
   *
   * @see jcontrol.comm.FT1_2EventListener#onIndication(byte[], int)
   */
  public void run() {
    char c;

    d.println("FT1.2-Monitor ready");

    for(;;){
      try {
        char r = 0;
        c = super.read(); // wait for traffic

        if (c == VARIABLE_LENGTH_START) {
          msgbuf[mpos++] = (byte)c;
          do {
            r = super.read();
            msgbuf[mpos++] = (byte)r;
          } while (r != VARIABLE_LENGTH_END && mpos < 49);
        }

        else if (c == FIXED_LENGTH_START) {
          msgbuf[mpos++] = (byte)c;
          do {
            r = super.read();
            msgbuf[mpos++] = (byte)r;
          } while (r != FIXED_LENGTH_END && mpos < 49);
        }

        if (mpos >= 49) { // Wenn Müll kommt...
          mpos = 0;
          debugFrame(msgbuf, 49);
        }

        else if (r == FIXED_LENGTH_END || r == VARIABLE_LENGTH_END) {
          // send ACK
          sendACK();
        }

        // debug frame
        d.print("IN  ");
        if (c == SINGLE_CHARACTER_ACK)
          d.println("ACK");
        else if (mpos > 0)
          debugFrame(msgbuf, mpos);

        mpos = 0;
      } catch (IOException e) {
        buzzer.on((short)200, (short)50);
        errorCode(); // read out rs232 errorcode
      }
    }

  }


  /**
   * Generate simple test datagram.
   *
   * @return byte[] the test data
   */
  static byte[] getTestDgram(byte type, byte[] destAddr, int size) {
    byte frame[];
    frame = new byte[8+size];
    frame[0] = (byte)type;
    frame[1] = 0x0C; //Priority = 11b
    frame[2] = 0x00; //SRC high (data_req: 0x00)
    frame[3] = 0x00; //SRC low  (data_req: 0x00)
    frame[4] = (byte)destAddr[0];
    frame[5] = (byte)destAddr[1];
    frame[6] = (byte)(0x70 | size);
    frame[7] = 0x00; //TPDU-sequence Number-APCI
    for (byte i=0; i < size; i++) {
      frame[(8+i)] = i;
    }

    return frame;
  }


  /**
   * send single character ACK
   */
  void sendACK() {
    try {
      write(SINGLE_CHARACTER_ACK);
    } catch (IOException e) {}
  }



  /**
   * send FT1.2 message
   */
  public void sendFrame(byte[] bytes) {
    int i;
    fcb ^= CF_FCB;
    int csum = CF_SNDDIR+CF_PRM+fcb+CF_FCV+CF_PRM_SEND_UDAT;
    int control = csum;

    // calculate checksum
    for (i = 0; i < bytes.length; i++)
        csum += bytes[i];

    // constuct datagram
    byte sendBytes[] = new byte[50];
    sendBytes[0] = VARIABLE_LENGTH_START;
    sendBytes[1] = (byte)(bytes.length+1);
    sendBytes[2] = (byte)(bytes.length+1);
    sendBytes[3] = VARIABLE_LENGTH_START;
    sendBytes[4] = (byte)control;

    for (i = 0; i < bytes.length; i++)
      sendBytes[5+i] = (byte)bytes[i];

    i += 5;

    sendBytes[i++] = (byte)csum;
    sendBytes[i++] = VARIABLE_LENGTH_END;
    sendBytes[i+1] = 0;

    // now send datagram
    try {
      super.write(sendBytes, 0, i);
      d.print("OUT ");
      debugFrame(sendBytes, i);
    } catch (IOException e) {}
  }


  /**
   * sends a PC_DATA_REQ to read out bcu memory
   */
  public void sendPCDataReq() {
    byte n[] = new byte[4];
    n[0] = PC_GET_VAL_req;
    n[1] = 7;
    n[2] = 0;
    n[3] = 0;
    sendFrame(n); // send verification request
  }



  /**
   * Debug rs232 data to display.
   * @param udat
   */
  public void debugFrame(byte[] udat, int length) {
    boolean first = true;

    buzzer.on((short)1000,(short)50);

    for (int i = 0; i < length; i++) {
      if ((udat[i] & 0xFF) < 0x10)
        d.print("0");
        d.print(Integer.toHexString((int)udat[i] & 0xFF));
      d.print(" ");
      if (first && i>0 && (i%5 == 0)) {
        d.println();
        first = false;
      }
      else if (i>0 && (i+5)%10 == 0)
        d.println();
    }
    d.println();
  }
}
