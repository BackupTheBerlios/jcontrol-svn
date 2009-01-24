/*
 * $Id$
 * 
 * Copyright (C) 2005-2009 The JControl Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package jcontrol.ui.viper.meter;

import jcontrol.io.Display;
import jcontrol.io.Graphics;
import jcontrol.lang.Math;
import jcontrol.system.Time;
import jcontrol.ui.viper.Component;

/**
 * <p>
 * This class provides an analog clock for JControl.
 * </p>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @since Viper 1.0
 * @version $Revision$
 */
public class AnalogClock extends Component {

    public static final int NONE = 0;

    public static final int STYLE_SHOW_SECONDS = RESERVED1;

    private static final int MINUTE_NEEDLE = 2;

    private static final int HOUR_NEEDLE = 1;

    private static final int SECOND_NEEDLE = 0;

    /** Current and last values */
    private int m_hour = 180;

    private int m_minute = 180;

    private int m_second = 180;

    private int m_lastNeedleHour = -1;

    private int m_lastNeedleMinute = -1;

    private int m_lastNeedleSec = -1;

    /**
     * Creates a clock.
     * 
     * @param x the upper left x coordinate
     * @param y the upper left y coordinate
     * @param width the clock width
     * @param height the clock height
     * @param style STYLE_SHOW_SECONDS or NONE
     */
    public AnalogClock(int x, int y, int width, int height, int style) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        state |= (style & STYLE_SHOW_SECONDS);
    }

    /**
     * Draws a needle of the analog clock
     * 
     * @param needle possible values are MINUTE_NEEDLE, HOUR_NEEDLE, SECOND_NEEDLE.
     * @param value the time to show (0-60 for minutes/SECOND_NEEDLE, 0-11 for hours)
     */
    private void drawNeedle(Graphics g, int needle, int value) {
        int size = width < height ? width - 1 >> 1 : height - 1 >> 1;
        int xoff = x + (width - 1 >> 1);
        int yoff = y + (height - 1 >> 1);
        int needleLength = 75;
        int lastValue;
        // save last value
        switch (needle) {
            case HOUR_NEEDLE:
                lastValue = m_lastNeedleHour;
                m_lastNeedleHour = value;
                needleLength = 56;
                break;
            case MINUTE_NEEDLE:
                lastValue = m_lastNeedleMinute;
                m_lastNeedleMinute = value;
                break;
            default:
                lastValue = m_lastNeedleSec;
                m_lastNeedleSec = value;
        }
        int[] xpoints = new int[4];
        int[] ypoints = new int[4];
        // delete old needle when available
        if ((value != lastValue) && (lastValue >= 0)) {
            g.setDrawMode(Display.INVERSE);
            xpoints[0] = xoff - Math.sin(lastValue) / 129 * needleLength / 100 * size / 254;
            ypoints[0] = yoff + Math.cos(lastValue) / 129 * needleLength / 100 * size / 254;
            if (needle == SECOND_NEEDLE) {
                g.drawLine(xoff, yoff, xpoints[0], ypoints[0]);
            } else {
                xpoints[1] = xoff - Math.sin((lastValue + 90) % 360) / 129 * 8 / 100 * size / 254;
                ypoints[1] = yoff + Math.cos((lastValue + 90) % 360) / 129 * 8 / 100 * size / 254;
                xpoints[2] = xoff - Math.sin((lastValue + 180) % 360) / 129 * 10 / 100 * size / 254;
                ypoints[2] = yoff + Math.cos((lastValue + 180) % 360) / 129 * 10 / 100 * size / 254;
                xpoints[3] = xoff - Math.sin((lastValue + 270) % 360) / 129 * 8 / 100 * size / 254;
                ypoints[3] = yoff + Math.cos((lastValue + 270) % 360) / 129 * 10 / 100 * size / 254;
                drawPolygon(g, xpoints, ypoints, 4);
            }
            g.setDrawMode(Display.NORMAL);
        }
        // draw the new needle
        xpoints[0] = xoff - Math.sin(value) / 129 * needleLength / 100 * size / 254;
        ypoints[0] = yoff + Math.cos(value) / 129 * needleLength / 100 * size / 254;
        if (needle == SECOND_NEEDLE) {
            g.drawLine(xoff, yoff, xpoints[0], ypoints[0]);
        } else {
            xpoints[1] = xoff - Math.sin((value + 90) % 360) / 129 * 8 / 100 * size / 254;
            ypoints[1] = yoff + Math.cos((value + 90) % 360) / 129 * 8 / 100 * size / 254;
            xpoints[2] = xoff - Math.sin((value + 180) % 360) / 129 * 10 / 100 * size / 254;
            ypoints[2] = yoff + Math.cos((value + 180) % 360) / 129 * 10 / 100 * size / 254;
            xpoints[3] = xoff - Math.sin((value + 270) % 360) / 129 * 8 / 100 * size / 254;
            ypoints[3] = yoff + Math.cos((value + 270) % 360) / 129 * 10 / 100 * size / 254;
            drawPolygon(g, xpoints, ypoints, 4);
        }
    }

    /**
     * Draws a polygon for the long and short needle of the clock.
     * 
     * @param xpoints the x coordinates
     * @param ypoints the y coordinates
     */
    private void drawPolygon(Graphics g, int[] xpoints, int[] ypoints, int npoints) {
        for (int i = 0; i < npoints - 1; i++) {
            g.drawLine(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1]);
        }
        g.drawLine(xpoints[0], ypoints[0], xpoints[npoints - 1], ypoints[npoints - 1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public synchronized void paint(Graphics g) {
        switch ((state & STATE_DIRTY_MASK)) {
            case STATE_DIRTY_PAINT_ALL:
                g.clearRect(x, y, width, height);
                m_lastNeedleSec = -1;
                m_lastNeedleMinute = -1;
                m_lastNeedleHour = -1;
            case STATE_DIRTY_REPAINT:
                // default last values
                // draw scale
            {
                int size = width < height ? width - 1 >> 1 : height - 1 >> 1;
                int xoff = x + (width - 1 >> 1);
                int yoff = y + (height - 1 >> 1);
                for (int dial = 0; dial < 360; dial += 30) { // Skala malen
                    int sin = Math.sin(dial);
                    int cos = Math.cos(dial);
                    g.drawLine(xoff - sin / 129 * 88 / 100 * size / 254, yoff + cos / 129 * 88
                            / 100 * size / 254, xoff - sin / 129 * size / 254, yoff + cos / 129
                            * size / 254);
                }
            }
            case STATE_DIRTY_UPDATE:

                if ((state & STYLE_SHOW_SECONDS) != 0) {
                    drawNeedle(g, SECOND_NEEDLE, m_second);
                }
                drawNeedle(g, HOUR_NEEDLE, m_hour);
                drawNeedle(g, MINUTE_NEEDLE, m_minute);
                state &= ~STATE_DIRTY_MASK;
        }
    }

    /**
     * Sets a the specified time to the clock.
     * 
     * @param time the time to set
     */
    public void setValue(Time time) {
        m_hour = ((time.hour + 6) % 12 * 30) + (time.minute >> 1);
        m_minute = (time.minute + 30) % 60 * 6;
        m_second = (time.second + 30) % 60 * 6;
        // check if time is already displayed
        if (m_second != m_lastNeedleSec || m_minute != m_lastNeedleMinute
                || m_hour != m_lastNeedleHour) {
            setDirty(STATE_DIRTY_UPDATE, true);
        }
    }
}