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

/**
 * <p>
 * This class <code>AnalogMeter.class</code> implements a graphical analog meter for JControl.
 * Several parameters of the analog meter such as opening angle and m_orientation are user
 * controllable.
 * </p>
 * 
 * @author Marcus Timmermann, Wolfgang Klingauf, Gerrit Telkamp
 * @since Viper 1.0
 * @version $Revision$
 */
public class AnalogMeter extends AbstractMeter {

    public static final int STYLE_SHOW_NUMERIC_VALUE = 4;

    /** Align the scale of the analog meter to the bottom-left corner of it's bounding box. */
    public final static int STYLE_ORIENTATION_LEFT = 1;

    /** Align the scale of the analog meter to the middle. */
    public final static int STYLE_ORIENTATION_CENTER = 3;

    /** Align the scale of the analog meter to the bottom-right corner of it's bounding box. */
    public final static int STYLE_ORIENTATION_RIGHT = 2;

    private final static int ORIENTATION_MASK = 3;

    /** The opening angle of the scale (0..180) */
    private int m_openAngle = 180;

    /** The number of dials on the scale */
    private int m_dials;

    /** Optional caption text */
    private String m_captionMin, m_captionMax;

    private int m_lastNumericWidth;

    /** last position of the needle */
    private int m_lastX = -1;

    private int m_lastY = -1;

    /** Internal coordinates and parameters */
    private int m_radius;

    private int m_anchorX;

    private int m_anchorY;

    /**
     * Creates a new AnalogMeter.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width
     * @param height The height
     * @param openAngle An angle between 0 and 180 degrees. Default value is 180 degrees.
     * @param dials Number of dial lines
     * @param style Possible values are <code>STYLE_ORIENTATION_LEFT</code>,
     *        <code>STYLE_ORIENTATION_CENTER</code> and <code>STYLE_ORIENTATION_RIGHT</code> or
     *        <code>STYLE_SHOW_NUMERIC_VALUE</code>.
     */
    public AnalogMeter(int x, int y, int width, int height, int openAngle, int dials, int style) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // set the open angle, orientation and dials of the analog meter display
        m_openAngle = openAngle;
        m_style = style;
        m_dials = dials;
    }

    /**
     * Draws the AnalogMeter's pointer with the current value.
     */
    private void drawValue(Graphics g) {
        int needleX;
        int needleY;
        {
            int angle;
            switch (m_style & ORIENTATION_MASK) {
                case STYLE_ORIENTATION_LEFT:
                    if (m_min < m_max) {
                        angle = 90 + Math.scale(m_value - m_min, m_max - m_min, m_openAngle);
                    } else if (m_min > m_max) {
                        angle = 90 + Math.scale(m_min - m_value, m_min - m_max, m_openAngle);
                    } else
                        return;
                    break;
                case STYLE_ORIENTATION_RIGHT:
                    if (m_min < m_max) {
                        angle = 270 - m_openAngle
                                + Math.scale(m_value - m_min, m_max - m_min, m_openAngle);
                    } else if (m_min > m_max) {
                        angle = 270 - m_openAngle
                                + Math.scale(m_min - m_value, m_min - m_max, m_openAngle);
                    } else
                        return;
                    break;
                default:
                    if (m_min < m_max) {
                        angle = (180 - (m_openAngle >> 1))
                                + Math.scale(m_value - m_min, m_max - m_min, m_openAngle);
                    } else if (m_min > m_max) {
                        angle = (180 - (m_openAngle >> 1))
                                + Math.scale(m_min - m_value, m_min - m_max, m_openAngle);
                    } else
                        return;
                    break;
            }
            needleX = x + m_anchorX - Math.sin(angle) / 129 * 88 / 100 * m_radius / 254;
            needleY = y + m_anchorY + Math.cos(angle) / 129 * 88 / 100 * m_radius / 254;
        }
        if ((needleX != m_lastX) || (needleY != m_lastY)) {
            // draw new needle
            g.setDrawMode(Display.XOR);
            g.drawLine(x + m_anchorX, y + m_anchorY, needleX, needleY); // draw new needle
            // then clear old needle
            if ((m_lastX > 0) && (m_lastY > 0)) {
                g.drawLine(x + m_anchorX, y + m_anchorY, m_lastX, m_lastY);
            }
            g.setDrawMode(Display.NORMAL);
            m_lastX = needleX;
            m_lastY = needleY;
        }
        // draw value and unit of numeric display
        if ((m_style & STYLE_SHOW_NUMERIC_VALUE) != 0) {
            g.setFont(font);
            int decimals = m_dataProducer != null ? -m_dataProducer.getExponent() : 0;
            String v = String.valueOf(m_value >= 0 ? m_value : -m_value);
            if (v.length() > m_digits) v = v.substring(0, m_digits);
            if (decimals > 0) {
                // insert comma if m_decimals > 0
                int comma = v.length() - decimals;
                if (comma <= 0) {
                    v = "00000".concat(v).substring(comma + 4, v.length() + 5);
                    comma = 1;
                    if (v.length() > m_digits) v = v.substring(0, m_digits);
                }
                v = v.substring(0, comma).concat(".").concat(v.substring(comma, v.length()));
            }
            // set minus sign if negative
            if (m_value < 0) v = "-".concat(v);
            // append unit string if available
            if (m_dataProducer != null && m_dataProducer.getUnit() != null)
                v = v.concat(m_dataProducer.getUnit());
            int numericWidth = g.getTextWidth(v);
            if (numericWidth > width) {
                numericWidth = width;
            }
            // calculate position of the numeric display
            int xoff = m_anchorX - (numericWidth >> 1);
            if (xoff < 0)
                xoff = 0;
            else if (xoff + numericWidth > width) xoff = width - numericWidth;
            int yoff = m_anchorY + 2;
            // if new value string is smaller than last one, delete space before and behind it
            int padWidth = (m_lastNumericWidth - numericWidth + 3) >> 1;
            if (padWidth > 0) {
                if (xoff > padWidth)
                    g.clearRect(x + xoff - padWidth, y + yoff, padWidth, g.getFontHeight());
                g.clearRect(x + xoff + numericWidth, y + yoff, padWidth, g.getFontHeight());
            }
            g.drawString(v, x + xoff, y + yoff, numericWidth, g.getFontHeight(), 0, 0);
            m_lastNumericWidth = numericWidth;
            g.setFont(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see jcontrol.ui.viper.Component#paint(jcontrol.io.Graphics)
     */
    @Override
    public synchronized void paint(Graphics g) {
        switch ((state & STATE_DIRTY_MASK)) {
            case STATE_DIRTY_REPAINT:
            case STATE_DIRTY_PAINT_ALL:
                g.clearRect(x, y, width, height);
                m_lastX = -1;
                m_lastY = -1;

                g.setFont(font);
                // default last values
                m_lastNumericWidth = 0;
                // determine height of the caption
                int captionHeight = g.getFontHeight();
                // get analog-meter specific drawing parameters
                int angleOffset;
                switch (m_style & ORIENTATION_MASK) {
                    case STYLE_ORIENTATION_LEFT: {
                        // limit open angle to 90 deg.
                        if (m_openAngle > 90) m_openAngle = 90;
                        // get height of the scale
                        int scaleHeight = height - 1;
                        if ((m_captionMin != null) || (m_style & STYLE_SHOW_NUMERIC_VALUE) != 0)
                            scaleHeight -= captionHeight + 2;
                        if (m_captionMax != null) scaleHeight -= captionHeight;
                        if (scaleHeight < 0) scaleHeight = 0;
                        // get max radius for height
                        int maxRadius = scaleHeight * 254 / (Math.sin(m_openAngle) / 129);
                        m_radius = width < maxRadius ? width : maxRadius;
                        m_anchorX = width - 1;
                        m_anchorY = scaleHeight + ((m_captionMax == null) ? 0 : captionHeight);
                        angleOffset = 90;
                        // draw the caption
                        if (m_captionMin != null)
                            g.drawString(m_captionMin, x + m_anchorX - m_radius, y + height
                                    - captionHeight);
                        if (m_captionMax != null) {
                            int xoff = Math.cos(m_openAngle) / 129 * m_radius / 254;
                            if (xoff < g.getTextWidth(m_captionMax))
                                xoff = g.getTextWidth(m_captionMax);
                            int yoff = Math.sin(m_openAngle) / 129 * m_radius / 254 + captionHeight;
                            if (yoff + captionHeight > height) yoff = height - captionHeight;
                            g.drawString(m_captionMax, x + m_anchorX - xoff, y + m_anchorY - yoff);
                        }
                    }
                        break;
                    case STYLE_ORIENTATION_RIGHT: {
                        // limit open angle to 90 deg.
                        if (m_openAngle > 90) m_openAngle = 90;
                        // get height of the scale
                        int scaleHeight = height - 1;
                        if ((m_captionMin != null) || (m_style & STYLE_SHOW_NUMERIC_VALUE) != 0)
                            scaleHeight -= captionHeight + 2;
                        if (m_captionMax != null) scaleHeight -= captionHeight;
                        if (scaleHeight < 0) scaleHeight = 0;
                        // get max radius for height
                        int maxRadius = scaleHeight * 254 / (Math.sin(m_openAngle) / 129);
                        m_radius = width < maxRadius ? width : maxRadius;
                        m_anchorX = 0;
                        m_anchorY = scaleHeight + ((m_captionMax == null) ? 0 : captionHeight);
                        angleOffset = 270 - m_openAngle;
                        // draw the caption
                        if (m_captionMin != null) {
                            int xoff = Math.cos(m_openAngle) / 129 * m_radius / 254
                                    - g.getTextWidth(m_captionMin);
                            if (xoff < 0) xoff = 0;
                            int yoff = Math.sin(m_openAngle) / 129 * m_radius / 254 + captionHeight;
                            if (yoff + captionHeight > height) yoff = height - captionHeight;
                            g.drawString(m_captionMin, x + m_anchorX + xoff, y + m_anchorY - yoff);
                        }
                        if (m_captionMax != null)
                            g.drawString(m_captionMax, x + m_radius - g.getTextWidth(m_captionMax)
                                    + 2, y + height - captionHeight);
                    }
                        break;
                    default: { // STYLE_ORIENTATION_CENTER
                        // limit open angle to 180 deg.
                        if (m_openAngle > 180) m_openAngle = 180;
                        // get height of the scale
                        int scaleHeight = height - 1;
                        if ((m_captionMin != null) || (m_captionMax != null)
                                || (m_style & STYLE_SHOW_NUMERIC_VALUE) != 0) {
                            scaleHeight -= captionHeight + 2;
                            if (scaleHeight < 0) scaleHeight = 0;
                        }
                        // determine internal parameters
                        if ((width >> 1) < scaleHeight) {
                            m_radius = width >> 1;
                            m_anchorX = m_radius;
                            m_anchorY = scaleHeight;
                        } else {
                            m_radius = scaleHeight;
                            m_anchorX = width >> 1;
                            m_anchorY = m_radius;
                        }
                        angleOffset = (180 - (m_openAngle >> 1));
                        // draw the caption
                        // int xoff = (width >> 1) - (Math.cos((180 - m_openAngle) >> 1) / 129 * 88
                        // / 100 * m_radius /
                        // 254);
                        int yoff = height - Math.sin((180 - m_openAngle) >> 1) / 129 * 88 / 100
                                * m_radius / 254 - captionHeight;
                        if (m_captionMin != null) g.drawString(m_captionMin, x, y + yoff);
                        if (m_captionMax != null)
                            g.drawString(m_captionMax, x + width - g.getTextWidth(m_captionMax), y
                                    + yoff);
                    }
                        break;
                }
                // draw scale
                for (int d = 0; d < m_dials; d++) {
                    int dial = angleOffset
                            + (m_dials == 1 ? m_openAngle / 2 : (d * m_openAngle / (m_dials - 1)));
                    int sin = Math.sin(dial);
                    int cos = Math.cos(dial);
                    g.drawLine(x + m_anchorX - sin / 129 * 88 / 100 * m_radius / 254, y + m_anchorY
                            + cos / 129 * 88 / 100 * m_radius / 254, x + m_anchorX - sin / 129
                            * m_radius / 254, y + m_anchorY + cos / 129 * m_radius / 254);
                }
                g.setFont(null);
            default:
                // draw the current value
                drawValue(g);
        }
        state &= ~STATE_DIRTY_MASK;
    }

    /**
     * Sets a caption text which is displayed for the min and max values at the bottom of the analog
     * meter.
     * 
     * @param captionMin Text to display at the left bottom border
     * @param captionMax Text to display at the right bottom border
     */
    public void setCaption(String captionMin, String captionMax) {
        // copy the given caption strings to the local object
        if ((captionMin != null) && captionMin.equals(""))
            m_captionMin = null;
        else
            m_captionMin = captionMin;
        if ((captionMax != null) && captionMax.equals(""))
            m_captionMax = null;
        else
            m_captionMax = captionMax;
        redrawInternalAndParent();
    }
}