/*
 * Copyright (C) 2004-2008 DOMOLOGIC Home Automation GmbH This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with this library; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
import java.io.IOException;
import jcontrol.comm.I2C;
import jcontrol.lang.ThreadExt;

/**
 * TODO missing
 * 
 * @version $Revision$
 */
public class CRX14 extends I2C {

    public CRX14(int address) {
        super(address);
    }

    public int setParameter(byte[] data) throws IOException {
        byte cmd[] = {(byte) 0x00};
        return write(cmd, data, 0, 1);
    }

    public void powerOn() throws IOException {
        write(new byte[]{(byte) 0x00, (byte) 0x10}, 0, 2);
    }

    public void powerOff() throws IOException {
        write(new byte[]{(byte) 0x00, (byte) 0x00}, 0, 2);
    }

    public int getParameter() throws IOException {
        byte cmd[] = {(byte) 0x00};
        byte data[] = {-1};
        read(cmd, data, 0, 1);
        return (int) data[0];
    }

    public int initiate() throws IOException {
        byte cmd[] = {(byte) 0x01, (byte) 0x02, (byte) 0x06, (byte) 0x00};
        byte data[] = {-1, -1};
        write(cmd, data, 0, 0);
        read(new byte[]{(byte) 0x01}, data, 0, 2);
        if (data[0] == 0) return -1;
        return (data[1] & 0xff) % 16;
    }

    public int select(int Chip_ID) throws IOException {
        byte cmd[] = {(byte) 0x01, (byte) 0x02, (byte) 0x0e, (byte) Chip_ID};
        byte data[] = {-1, -1};
        write(cmd, data, 0, 0);
        read(new byte[]{(byte) 0x01}, data, 0, 2);
        if (data[0] == 0) return -1;
        return (data[1] & 0xff) % 16;
    }

    public int[] readBlock(int Adress) throws IOException {
        byte cmd[] = {(byte) 0x01, (byte) 0x02, (byte) 0x08, (byte) Adress};
        byte data[] = {-1, -1, -1};
        int idata[] = {-1, -1};
        write(cmd, data, 0, 0);
        read(new byte[]{(byte) 0x01}, data, 0, 3);
        if (data[0] == 0) {
            idata[0] = -1;
            idata[1] = -1;
            return idata;
        }
        try {
            ThreadExt.sleep(37); // unter 37 keine Funktion
        } catch (InterruptedException e) {
        }
        idata[0] = data[1] & 0xff;
        idata[1] = data[2] & 0xff;
        return idata;
    }

    public int writeBlock(int Adress, byte[] data) throws IOException {
        byte cmd[] = {(byte) 0x01, (byte) 0x04, (byte) 0x09, (byte) Adress, data[0], data[1]};
        write(cmd, data, 0, 0);
        return 0;
    }

    public int completion() throws IOException {
        byte cmd[] = {(byte) 0x01, (byte) 0x01, (byte) 0x0f};
        byte data[] = {0};
        return write(cmd, data, 0, 0);
    }

    public int reset_to_inv() throws IOException {
        byte cmd[] = {(byte) 0x01, (byte) 0x01, (byte) 0x0c};
        byte data[] = {0};
        return write(cmd, data, 0, 0);
    }
}
