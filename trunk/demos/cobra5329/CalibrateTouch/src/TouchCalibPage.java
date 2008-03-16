/*
 * TouchCalibPage.java
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
import jcontrol.lang.Math;
import jcontrol.graphics.*;
import jcontrol.system.Management;
import java.io.IOException;
import jcontrol.io.Resource;
import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.TouchListener;
import jcontrol.ui.wombat.event.TouchEvent;
import jcontrol.io.Console;

/**
 * <p>Calibrates your touch screen.</p>
 * 
 * @author roebbenack
 */
public class TouchCalibPage extends Container {
	
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 6;
	
	private static final int TOP_LEFT = 0;
	private static final int BOTTOM_RIGHT = 1;
    private static final int TOP_RIGHT = 2;
    private static final int BOTTOM_LEFT = 3;
    private static final int CALIB_POINT_0 = 4;
    private static final int CALIB_POINT_1 = 5;

    private static final int CALIB_POINT_0_X = 50;
    private static final int CALIB_POINT_0_Y = 50;
    private static final int CALIB_POINT_1_X = 220;
    private static final int CALIB_POINT_1_Y = 170;
    private static final int CALIB_TOLERANCE = 15; // pixels in each direction
	
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	//private CalibrateTouch m_parentApplication;
	
	private Label m_label1;
	private Label m_Label2;
	private Label m_upperLeft;
	private Label m_upperRight;
	private Label m_lowerLeft;
	private Label m_lowerRight;
	
	private XDisplay d = new XDisplay();

	// for checking calibration (before save properties)
	private Label m_calibCheckPoint0;	
	private Label m_calibCheckPoint1; 
	
	private int m_mode = TOP_LEFT;
	
	private int m_x0, m_y0,
				m_x1, m_y1,
				m_x2, m_y2,
				m_x3, m_y3,
				m_calibX0, m_calibY0,
				m_calibX1, m_calibY1;

    private int language = 0;    
    
    private final String[] INTRO1 = {
            "Please touch",
            "Bitte beruehren Sie",
    };
    private final String[] UPPER_LEFT = {
            "the upper left arrow",
            "den Pfeil oben links",
    };
    private final String[] UPPER_RIGHT = {
            "the upper right arrow",
            "den Pfeil oben rechts",
    };
    private final String[] LOWER_LEFT = {
            "the lower left arrow",
            "den Pfeil unten links",
    };
    private final String[] LOWER_RIGHT = {
            "the lower right arrow",
            "den Pfeil unten rechts",
    };

    private final String[] CALIBRATION_INTRO = {
            "Calibration test - please touch",
            "Kalibrierungstest - bitte beruehren Sie",
    };
    private final String[] CALIBRATION = {
            "the arrow",
            "den Pfeil",
    };
    private final String[] CALIBRATION_FAILED = {
            "Calibration failed! Please touch",
            "Kalibrierung fehlgeschlagen! Bitte beruehren Sie",
    };

    private final String[] CALIBRATION_SUCCESSFUL = {
            "Calibration successful...",
            "Kalibrierung erfolgreich...",
    };

    private final String[] CALIBRATION_SUCCESSFUL2 = {
            "flash your application.",
            "flashen Sie nun ihre Anwendung.",
    };

	/**
	 * Constructor TouchCalibPage
	 * 
	 * @param parent
	 */
	public TouchCalibPage(CalibrateTouch parent) {
		super(MAX_CAPACITY);
		//m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_label1 = new Label(INTRO1[language], 60, 85, 210, 34, Label.STYLE_ALIGN_CENTER);
		this.add(m_label1);
		m_Label2 = new Label(UPPER_LEFT[language], 90, 120, 150, 34, Label.STYLE_ALIGN_CENTER);
		this.add(m_Label2);
		m_upperLeft = new TouchLabel(0, 0);
		try {
			m_upperLeft.setImage(new Resource("upper_left.jcif"));
		} catch(IOException ioe) {}
		this.add(m_upperLeft);
		m_upperRight = new TouchLabel(286, 0);
		try {
			m_upperRight.setImage(new Resource("upper_right.jcif"));
		} catch(IOException ioe) {}
		this.add(m_upperRight);
		m_lowerLeft = new TouchLabel(0, 206);
		try {
			m_lowerLeft.setImage(new Resource("lower_left.jcif"));
		} catch(IOException ioe) {}
		this.add(m_lowerLeft);
		m_lowerRight = new TouchLabel(286, 206);
		try {
			m_lowerRight.setImage(new Resource("lower_right.jcif"));
		} catch(IOException ioe) {}
		this.add(m_lowerRight);		
		m_upperRight.setVisible(false);
		m_lowerRight.setVisible(false);
		m_lowerLeft.setVisible(false);
		
		// --- add calibration-points ---
		
		m_calibCheckPoint0 = new TouchLabel(CALIB_POINT_0_X, CALIB_POINT_0_Y);
		try { m_calibCheckPoint0.setImage(new Resource("upper_left.jcif")); } catch(IOException ioe) {}
		this.add(m_calibCheckPoint0);
		m_calibCheckPoint0.setVisible(false);

		m_calibCheckPoint1 = new TouchLabel(CALIB_POINT_1_X, CALIB_POINT_1_Y);
		try { m_calibCheckPoint1.setImage(new Resource("upper_left.jcif")); } catch(IOException ioe) {}
		this.add(m_calibCheckPoint1);
		m_calibCheckPoint1.setVisible(false);
		
	}
	
	private void switchMode(int mode) {
		switch (mode) {
			case BOTTOM_RIGHT:
					m_upperLeft.setVisible(false);
					m_lowerRight.setVisible(true);
					m_Label2.setText(LOWER_RIGHT[language]);
					break;
			case TOP_RIGHT:
					m_lowerRight.setVisible(false);
					m_upperRight.setVisible(true);
					m_Label2.setText(UPPER_RIGHT[language]);
					break;
			case BOTTOM_LEFT:
					m_upperRight.setVisible(false);
					m_lowerLeft.setVisible(true);
					m_Label2.setText(LOWER_LEFT[language]);
					break;
			case CALIB_POINT_0:
					// calibrate the display with previous got raw-coordinates
					CalibrateTouch.pointingDevice.calibrate(m_x0, m_y0, m_x1, m_y1, m_x2, m_y2, m_x3, m_y3);
					Console.out.println(Integer.toString(m_x0).concat("x").concat(Integer.toString(m_y0)));
					Console.out.println(Integer.toString(m_x1).concat("x").concat(Integer.toString(m_y1)));
					Console.out.println(Integer.toString(m_x2).concat("x").concat(Integer.toString(m_y2)));
					Console.out.println(Integer.toString(m_x3).concat("x").concat(Integer.toString(m_y3)));
					// now: test the calibration
					m_lowerLeft.setVisible(false);
					m_calibCheckPoint0.setVisible(true);
					m_label1.setText(CALIBRATION_INTRO[language]);
					m_Label2.setText(CALIBRATION[language]);
					break;
			case CALIB_POINT_1:
					m_calibCheckPoint0.setVisible(false);
					m_calibCheckPoint1.setVisible(true);
					m_Label2.setText(CALIBRATION[language]);
					break;
			default:
					m_calibCheckPoint1.setVisible(false);
					if ( Math.abs(m_calibX0 - CALIB_POINT_0_X) > CALIB_TOLERANCE || 
						 Math.abs(m_calibY0 - CALIB_POINT_0_Y) > CALIB_TOLERANCE ||
						 Math.abs(m_calibX1 - CALIB_POINT_1_X) > CALIB_TOLERANCE ||
						 Math.abs(m_calibY1 - CALIB_POINT_1_Y) > CALIB_TOLERANCE
					) {
						m_upperLeft.setVisible(true);
						m_label1.setText(CALIBRATION_FAILED[language]);
						m_Label2.setText(UPPER_LEFT[language]);
						m_mode = TOP_LEFT;
					} else {
						m_label1.setText(CALIBRATION_SUCCESSFUL[language]);
						m_Label2.setText(CALIBRATION_SUCCESSFUL2[language]);
						Management.saveProperties();
					}
		}
	}		
	
	class TouchLabel extends Label implements TouchListener {
	
		public TouchLabel(int x, int y) {
			super((String)null, x, y, 0, 0, Label.STYLE_ALIGN_LEFT);
		}
	
		public int onTouchEvent(TouchEvent event) {
			if (!isVisible()) return RESULT_NONE;
			switch (event.type) {
	            case TouchEvent.TYPE_TOUCH_PRESSED:
					switch (m_mode) {
						case TOP_LEFT:
								// save e.x, e.y as top left
								m_x0 = CalibrateTouch.pointingDevice.getRawX();
								m_y0 = CalibrateTouch.pointingDevice.getRawY();
								break;
						case BOTTOM_RIGHT:
								// save e.x, e.y as bottom right
								m_x2 = CalibrateTouch.pointingDevice.getRawX();
								m_y2 = CalibrateTouch.pointingDevice.getRawY();
								break;
						case TOP_RIGHT:
								// save e.x, e.y as top right						
								m_x1 = CalibrateTouch.pointingDevice.getRawX();
								m_y1 = CalibrateTouch.pointingDevice.getRawY();
								break;
						case BOTTOM_LEFT:
								// save e.x, e.y as bottom left
								m_x3 = CalibrateTouch.pointingDevice.getRawX();
								m_y3 = CalibrateTouch.pointingDevice.getRawY();
								break;
						case CALIB_POINT_0:
								// save e.x, e.y as calibration point 0
								m_calibX0 = CalibrateTouch.pointingDevice.getX();
								m_calibY0 = CalibrateTouch.pointingDevice.getY();
								break;
						case CALIB_POINT_1:
								// save e.x, e.y as calibration point 1
								m_calibX1 = CalibrateTouch.pointingDevice.getX();
								m_calibY1 = CalibrateTouch.pointingDevice.getY();
								break;
					}
					m_mode++;
					switchMode(m_mode);
					// show the user the touch origin
				    d.setColor(0xFFFF0000);
				    d.fillOval(CalibrateTouch.pointingDevice.getX()-3,CalibrateTouch.pointingDevice.getY()-3,6,6);
			}
			return RESULT_ACCEPTED;
		}
	}
}