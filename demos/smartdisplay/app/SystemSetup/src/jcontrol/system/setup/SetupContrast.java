/*
 * SetupContrast.java
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

package jcontrol.system.setup;

import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.system.Management;
import jcontrol.ui.vole.AnimationContainer;
import jcontrol.ui.vole.TextScroller;

/**
 * This class is used to set the display contrast. As different displays require
 * different contrast settings, a test card and a scrolling text are shown to optimize visibility.
 *
 * @author Marcus Timmermann
 * @version 1.0
 * @see jcontrol.system.setup.SystemSetup
 */
public class SetupContrast {

    /**
     * Program entry point
     * @param args (not used)
     */
    public static void main(String[] args) {
        go(new Display(),new Keyboard());
    }

    /**
     * This method is run from the main menu.
     *
     * @param gfx    the global Display object.
     * @param keys   the global Keyboard object.
     */
    public static void go(Display gfx, Keyboard keys) {
        String[] text={
            "This is",
            "a test to",
            "optimize",
            "visibility",
            "during",
            "scrolling...",
            ""};
        gfx.setFont(Display.SYSTEMFONT);
        gfx.clearRect(1,22,126,40);
        gfx.drawString("Contrast adjust",5,21);
        gfx.setDrawMode(Display.XOR);
        gfx.fillRect(1, 20, 126, 9);
        gfx.setDrawMode(Display.NORMAL);
        int value=Integer.parseInt(Management.getProperty("display.contrast")); // get the current contrast value
        int pos1=gfx.drawString("Contrast: ",69,30);
        // the test card image
        gfx.drawImage(new String[] {
                          "\uFF00\uFFFF\u00FF\uFFFF\u00FF\uFFFF\uFF00\u0055\u00B6\u0077\u00EF\u00DF\u00F7\u007F\u0000\u5727\u5700\uE5EA\uE5EA\u00BF\uBFBF\u0055\uAA55\uAA55\u00AA\u55AA\u5500\u55AA\u5500\uAA55\u0055",
                          "\uFF00\uFFFF\u00FF\uFFFF\u00FF\uFFFF\uFF00\u0055\u006D\u0077\u00BD\u00F7\u00FB\u007F\u0000\u5727\u5700\u95A9\u95A9\u00DF\uDFDF\u0055\uAA55\uAA55\u00AA\u55AA\u5500\u55AA\u5500\uAA55\u0055",
                          "\uFF00\uFFFF\u00FF\uFFFF\u00FF\uFFFF\uFF00\u0055\u00DB\u0077\u00F7\u007D\u00FD\u007F\u0000\u5727\u5700\u57A7\u57A7\u00EF\uEFEF\u0055\uAA55\uAA55\u00AA\u55AA\u5500\u55AA\u5500\uAA55\u0055",
                          "\u3F00\u3F3F\u003F\u3F3F\u003F\u3F3F\u3F00\u0015\u0036\u0037\u0036\u003F\u003D\u003F\u0000\u1727\u1700\u1E1E\u1E1E\u0037\u3737\u0015\u2A15\u2A15\u002A\u152A\u1500\u152A\u1500\u2A15\u0015"},
                      4,31,62,30,0,0);
        TextScroller ts = new TextScroller(text,66,38,61,25,TextScroller.ALIGN_CENTER);
        AnimationContainer ac = new AnimationContainer();
        ac.setGraphics(gfx);
        ac.add(ts);
        ac.setVisible(true);
        int oldvalue = -1;
        loop:
        for(;;){
            if (value<0) value=0;
			if (value>255) value=255;
            if (value!=oldvalue) {
                gfx.drawString(String.valueOf(value).concat("  "),pos1+70,30);
                Management.setProperty("display.contrast",String.valueOf(value));
                oldvalue = value;
            }
            switch (keys.read()) {
                case 'u':
                case 'R':
                    value++; break;
                case 'U':
                    value+=5-(value%5); break;
                case 'd':
                case 'L':
                    value--; break;
                case 'D':
                    value-=((value%5==0)?5:0)+(value%5); break;
                case 'M':
                case 'S': break loop;
            }
        }
        ac.removeAll();
        Management.saveProperties();
    }

}
