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

/**
 * TODO missing
 * 
 * @version $Revision$
 */

public class DAC6573 extends I2C {

    public DAC6573(int address) {
        super(address);
    }

    // output 1 - 4 !!!
    public void setOutput1024(int output, int value) throws IOException {
        byte[] data = new byte[2];
        byte[] cmd = new byte[1];
        byte[] channel = new byte[2];

        final int A3 = 0;
        final int A2 = 0;
        final int Load1 = 0;
        final int Load0 = 1;
        final int PD0 = 0;

        if (value > 1023) value = 1023;
        if (value < 0) value = 0;

        switch (output) {
            case 1:
                channel[0] = 0;
                channel[1] = 0;
                break;
            case 2:
                channel[0] = 1;
                channel[1] = 0;
                break;
            case 3:
                channel[0] = 0;
                channel[1] = 1;
                break;
            case 4:
                channel[0] = 1;
                channel[1] = 1;
                break;
            default:
                channel[0] = 0;
                channel[1] = 0;
                break;
        }

        cmd[0] = (byte) A3 << 1;
        cmd[0] = (byte) ((cmd[0] + A2) << 1);
        cmd[0] = (byte) ((cmd[0] + Load1) << 1);
        cmd[0] = (byte) ((cmd[0] + Load0) << 1);
        cmd[0] = (byte) ((cmd[0] + 0) << 1);
        cmd[0] = (byte) ((cmd[0] + channel[1]) << 1);
        cmd[0] = (byte) ((cmd[0] + channel[0]) << 1);
        cmd[0] = (byte) ((cmd[0] + PD0));

        // >> 1 : schiftet 1 nach rechts

        data[0] = (byte) (value / 4);
        data[1] = (byte) ((value % 4) << 6);

        write(cmd, data, 0, 2);
    }

    public void setOutput512(int channel, int value) throws IOException {
        setOutput1024(channel, value * 2);
    }

    public void setOutput256(int channel, int value) throws IOException {
        setOutput1024(channel, value * 4);
    }

    public void setOutputPercent(int channel, int percent) throws IOException {
        int output = (1024 * percent) / 100;
        setOutput1024(channel, output);
    }

    public void setOutputPromille(int channel, int promille) throws IOException {
        int output = (1024 * promille) / 1000;
        setOutput1024(channel, output);
    }
}
