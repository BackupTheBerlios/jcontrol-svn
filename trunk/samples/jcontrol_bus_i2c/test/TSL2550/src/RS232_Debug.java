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
import jcontrol.comm.RS232;

/**
 * Java file created by JControl/IDE
 * 
 * @author Stk
 * @version $Revision$
 */
public class RS232_Debug {

    public static void println(String out) {
        try {
            RS232 rs232 = new RS232();
            rs232.println(out);
            rs232 = null;
        } catch (IOException e) {
        }
    }

    public static void print(String out) {
        try {
            RS232 rs232 = new RS232();
            rs232.print(out);
            rs232 = null;
        } catch (IOException e) {
        }
    }

    public static void println(int out) {
        println(String.valueOf(out));
    }

    public static void print(int out) {
        print(String.valueOf(out));
    }

    public static void println(boolean out) {
        println(out ? "true" : "false");
    }

    public static void print(boolean out) {
        print(out ? "true" : "false");
    }
}
