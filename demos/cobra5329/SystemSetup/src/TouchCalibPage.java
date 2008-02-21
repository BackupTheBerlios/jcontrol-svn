
/**
 * Java file created by JControl/IDE
 * 
 * Created on 29.11.05 13:25
 * 
 * @author timmer
 */

import jcontrol.io.Resource;
import jcontrol.lang.Math;
import jcontrol.graphics.*;
import jcontrol.system.Management;
import jcontrol.lang.ThreadExt;

import java.io.IOException;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.TouchListener;
import jcontrol.ui.wombat.event.TouchEvent;

public class TouchCalibPage extends AbstractSystemSetupPage {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 6;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
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
	
	private int m_mode = TOP_LEFT;
	
	private int m_x0, m_y0,
				m_x1, m_y1,
				m_x2, m_y2,
				m_x3, m_y3,
				m_calibX0, m_calibY0,
				m_calibX1, m_calibY1;
    
    private final String[] INTRO1 = {
            "Please touch",
            "Bitte berühren Sie",
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
            "Kalibrierungstest - bitte berühren Sie",
    };
    private final String[] CALIBRATION = {
            "the arrow",
            "den Pfeil",
    };
    private final String[] CALIBRATION_FAILED = {
            "Calibration failed! Please touch",
            "Kalibrierung fehlgeschlagen! Bitte berühren Sie",
    };

	/**
	 * Constructor TouchCalibPage
	 * 
	 * @param parent
	 */
	public TouchCalibPage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_parentApplication.getOutline().setVisible(false);
        int language = m_parentApplication.getLanguage();
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
        int language = m_parentApplication.getLanguage();
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
					SystemSetup.pointingDevice.calibrate(m_x0, m_y0, m_x1, m_y1, m_x2, m_y2, m_x3, m_y3);
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
					if ( Math.abs(m_calibX0 - CALIB_POINT_0_X) > CALIB_TOLERANCE || 
						 Math.abs(m_calibY0 - CALIB_POINT_0_Y) > CALIB_TOLERANCE ||
						 Math.abs(m_calibX1 - CALIB_POINT_1_X) > CALIB_TOLERANCE ||
						 Math.abs(m_calibY1 - CALIB_POINT_1_Y) > CALIB_TOLERANCE
					) {
						m_calibCheckPoint1.setVisible(false);
						m_upperLeft.setVisible(true);
						m_label1.setText(CALIBRATION_FAILED[language]);
						m_Label2.setText(UPPER_LEFT[language]);
						m_mode = TOP_LEFT;
					} else {
						Management.saveProperties();
						m_parentApplication.getOutline().setVisible(true);
						m_parentApplication.showPage(SystemSetup.TOUCHPAGE);
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
								m_x0 = SystemSetup.pointingDevice.getRawX();
								m_y0 = SystemSetup.pointingDevice.getRawY();
								break;
						case BOTTOM_RIGHT:
								// save e.x, e.y as bottom right
								m_x2 = SystemSetup.pointingDevice.getRawX();
								m_y2 = SystemSetup.pointingDevice.getRawY();
								break;
						case TOP_RIGHT:
								// save e.x, e.y as top right						
								m_x1 = SystemSetup.pointingDevice.getRawX();
								m_y1 = SystemSetup.pointingDevice.getRawY();
								break;
						case BOTTOM_LEFT:
								// save e.x, e.y as bottom left
								m_x3 = SystemSetup.pointingDevice.getRawX();
								m_y3 = SystemSetup.pointingDevice.getRawY();
								break;
						case CALIB_POINT_0:
								// save e.x, e.y as calibration point 0
								m_calibX0 = SystemSetup.pointingDevice.getX();
								m_calibY0 = SystemSetup.pointingDevice.getY();
								break;
						case CALIB_POINT_1:
								// save e.x, e.y as calibration point 1
								m_calibX1 = SystemSetup.pointingDevice.getX();
								m_calibY1 = SystemSetup.pointingDevice.getY();
								break;
					}
					m_mode++;
					switchMode(m_mode);
					// for the user only
				    d.setColor(0xFFFF0000);
				    d.fillOval(SystemSetup.pointingDevice.getX()-5,SystemSetup.pointingDevice.getY()-5,10,10);
				    try { ThreadExt.sleep(500); } catch ( InterruptedException e) {}
			}
			return RESULT_ACCEPTED;
		}
	}
}