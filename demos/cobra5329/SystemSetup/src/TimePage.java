/**
 * Java file created by JControl/IDE
 * 
 * Created on 29.11.05 13:20
 * 
 * @author timmer
 */
import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Button;
import jcontrol.graphics.Color;
import jcontrol.graphics.XGraphics;

import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.ActionEvent;

import jcontrol.system.Time;
import jcontrol.system.RTC;
import jcontrol.lang.*;

public class TimePage extends AbstractSystemSetupPage implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 2;
    
    private final String[] HEADLINE = {
            "Date/Time",
            "Datum/Uhrzeit",
    };
	
	private final String[][] MONTHS = {
       {
           "Jan",
    		"Feb",
    		"March",
    		"April",
    		"May",
    		"June",
    		"July",
    		"Aug",
    		"Sep",
    		"Oct",
    		"Nov",
    		"Dec"
        },
        {
            "Jan",
            "Feb",
            "März",
            "April",
            "Mai",
            "Juni",
            "Juli",
            "Aug",
            "Sep",
            "Okt",
            "Nov",
            "Dez"
        }
	};
	
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Container m_timeContainer;
	private Container m_dateContainer;
	private Container m_second;
	private Container m_minute;
	private Container m_hour;
	private Button m_secDownButton;
	private Label m_secLabel;
	private Button m_secUpButton;
	private Button m_minDownButton;
	private Label m_minLabel;
	private Button m_minUpButton;
	private Button m_hourDownButton;
	private Label m_hourLabel;
	private Button m_hourUpButton;
	private Container m_year;
	private Container m_month;
	private Container m_day;
	private Button m_yearDownButton;
	private Label m_yearLabel;
	private Button m_yearUpButton;
	private Button m_monthDownButton;
	private Label m_monthLabel;
	private Button m_monthUpButton;
	private Button m_dayDownButton;
	private Label m_dayLabel;
	private Button m_dayUpButton;    
	
	private Time m_time = new Time();
	
	private boolean m_modified = false;
    private TimeThread m_timeThread;

    private boolean m_init = false;
    
	/**
	 * Constructor TimePage
	 * 
	 * @param parent
	 */
	public TimePage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_parentApplication.showOkButton(true);
		m_parentApplication.showCancelButton(true);
        m_parentApplication.setTitle(HEADLINE[m_parentApplication.getLanguage()]);
		try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		m_timeContainer = new Container(3);
		this.add(m_timeContainer);
		m_second = new Container(3);
		m_timeContainer.add(m_second);
		m_secDownButton = new Button(null, 252, 102, 66, 25);
		try {
			m_secDownButton.setImage(new Resource("Down_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_second.add(m_secDownButton);
		m_secDownButton.setActionListener(this);
		m_secLabel = new Label((String)null, 252, 68, 66, 34, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		try {
			m_secLabel.setFont(new Resource("SansSerif_24px.jcfd"));
		} catch(IOException ioe) {}
		m_secLabel.setBackgroundColor(Color.WHITE);
		m_second.add(m_secLabel);
		m_secUpButton = new Button(null, 252, 43, 66, 25);
		try {
			m_secUpButton.setImage(new Resource("Up_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_second.add(m_secUpButton);
		m_secUpButton.setActionListener(this);
		m_minute = new Container(3);
		m_timeContainer.add(m_minute);
		m_minDownButton = new Button(null, 186, 102, 66, 25);
		try {
			m_minDownButton.setImage(new Resource("Down_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_minute.add(m_minDownButton);
		m_minDownButton.setActionListener(this);
		m_minLabel = new Label((String)null, 186, 68, 66, 34, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		try {
			m_minLabel.setFont(new Resource("SansSerif_24px.jcfd"));
		} catch(IOException ioe) {}
		m_minLabel.setBackgroundColor(Color.WHITE);
		m_minute.add(m_minLabel);
		m_minUpButton = new Button(null, 186, 43, 66, 25);
		try {
			m_minUpButton.setImage(new Resource("Up_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_minute.add(m_minUpButton);
		m_minUpButton.setActionListener(this);
		m_hour = new Container(3);
		m_timeContainer.add(m_hour);
		m_hourDownButton = new Button(null, 120, 102, 66, 25);
		try {
			m_hourDownButton.setImage(new Resource("Down_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_hour.add(m_hourDownButton);
		m_hourDownButton.setActionListener(this);
		m_hourLabel = new Label((String)null, 120, 68, 66, 34, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		try {
			m_hourLabel.setFont(new Resource("SansSerif_24px.jcfd"));
		} catch(IOException ioe) {}
		m_hourLabel.setBackgroundColor(Color.WHITE);
		m_hour.add(m_hourLabel);
		m_hourUpButton = new Button(null, 120, 43, 66, 25);
		try {
			m_hourUpButton.setImage(new Resource("Up_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_hour.add(m_hourUpButton);
		m_hourUpButton.setActionListener(this);
		m_dateContainer = new Container(3);
		this.add(m_dateContainer);
		m_year = new Container(3);
		m_dateContainer.add(m_year);
		m_yearDownButton = new Button(null, 252, 204, 66, 25);
		try {
			m_yearDownButton.setImage(new Resource("Down_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_year.add(m_yearDownButton);
		m_yearDownButton.setActionListener(this);
		m_yearLabel = new Label((String)null, 252, 170, 66, 34, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		try {
			m_yearLabel.setFont(new Resource("SansSerif_24px.jcfd"));
		} catch(IOException ioe) {}
		m_yearLabel.setBackgroundColor(Color.WHITE);
		m_year.add(m_yearLabel);
		m_yearUpButton = new Button(null, 252, 145, 66, 25);
		try {
			m_yearUpButton.setImage(new Resource("Up_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_year.add(m_yearUpButton);
		m_yearUpButton.setActionListener(this);
		m_month = new Container(3);
		m_dateContainer.add(m_month);
		m_monthDownButton = new Button(null, 186, 204, 66, 25);
		try {
			m_monthDownButton.setImage(new Resource("Down_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_month.add(m_monthDownButton);
		m_monthDownButton.setActionListener(this);
		m_monthLabel = new Label((String)null, 186, 170, 66, 34, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		try {
			m_monthLabel.setFont(new Resource("SansSerif_24px.jcfd"));
		} catch(IOException ioe) {}
		m_monthLabel.setBackgroundColor(Color.WHITE);
		m_month.add(m_monthLabel);
		m_monthUpButton = new Button(null, 186, 145, 66, 25);
		try {
			m_monthUpButton.setImage(new Resource("Up_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_month.add(m_monthUpButton);
		m_monthUpButton.setActionListener(this);
		m_day = new Container(3);
		m_dateContainer.add(m_day);
		m_dayDownButton = new Button(null, 120, 204, 66, 25);
		try {
			m_dayDownButton.setImage(new Resource("Down_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_day.add(m_dayDownButton);
		m_dayDownButton.setActionListener(this);
		m_dayLabel = new Label((String)null, 120, 170, 66, 34, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		try {
			m_dayLabel.setFont(new Resource("SansSerif_24px.jcfd"));
		} catch(IOException ioe) {}
		m_dayLabel.setBackgroundColor(Color.WHITE);
		m_day.add(m_dayLabel);
		m_dayUpButton = new Button(null, 120, 145, 66, 25);
		try {
			m_dayUpButton.setImage(new Resource("Up_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_day.add(m_dayUpButton);
		m_dayUpButton.setActionListener(this);
        updateLabels();
		m_timeThread = new TimeThread();
	}
	
	public void update(XGraphics g) {
		super.update(g);
		if ( !m_modified && !m_init ) {
			m_init=true;
			m_timeThread.start();
		}
	}
	
	public void ok() {
		if (!m_modified) return;
		synchronized (m_time) {
			RTC.setTime(m_time);
			m_modified = false;
			if (m_timeThread==null || !m_timeThread.isAlive()) {
				m_timeThread = new TimeThread();
				m_timeThread.start();
			}		
		}
	}
    
    public void cancel() {
		if (!m_modified) return;
	    synchronized (m_time) {
	    	RTC.getTime(m_time);
	    	updateLabels();
	    	m_modified = false;
			if (m_timeThread==null || !m_timeThread.isAlive()) {
				m_timeThread = new TimeThread();
				m_timeThread.start();
			}		
	    }
    }   
    
    private void updateLabels() {
    	m_hourLabel.setText(getString(m_time.hour));
		m_minLabel.setText(getString(m_time.minute));
		m_secLabel.setText(getString(m_time.second));
		m_dayLabel.setText(getString(m_time.day));
		if (m_time.month>0) m_monthLabel.setText(MONTHS[m_parentApplication.getLanguage()][m_time.month-1]);
		m_yearLabel.setText(String.valueOf(m_time.year));		
    }      
	
	private int getDays(int month, int year) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11: return 30;
            case 2: if (((year%4==0) && !(year%100==0)) || (year%400==0)) return 29;
                else return 28;
            default: return 31;
        }
    }
       
    private String getString(int value) {
    	if (value<10) {
    		return "0".concat(String.valueOf(value));
    	} else {
    		return String.valueOf(value);
    	}
    }

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		m_modified = true;
		if (e.source == m_dayDownButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {								
				int days = getDays(m_time.month, m_time.year);
				m_time.day--;
				if (m_time.day<1) {
					m_time.day = days;
				}
				m_dayLabel.setText(getString(m_time.day));
			}
		} else if (e.source == m_dayUpButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				int days = getDays(m_time.month, m_time.year);
				m_time.day++;                    		
        		if (m_time.day>days) {
        			m_time.day = 1;							
        		}
				m_dayLabel.setText(getString(m_time.day));						
			}
		} else if (e.source == m_monthDownButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.month--;
                if (m_time.month<1) m_time.month=12;
				m_monthLabel.setText(MONTHS[m_parentApplication.getLanguage()][m_time.month-1]);
				int days = getDays(m_time.month, m_time.year);
				if (m_time.day>days) {
					m_time.day = days;
					m_dayLabel.setText(getString(m_time.day));
				}						
			}
		} else if (e.source == m_monthUpButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.month++;
                if (m_time.month>12) m_time.month=1;
				m_monthLabel.setText(MONTHS[m_parentApplication.getLanguage()][m_time.month-1]);
				int days = getDays(m_time.month, m_time.year);
				if (m_time.day>days) {
					m_time.day = days;
					m_dayLabel.setText(getString(m_time.day));
				}
			}
		} else if (e.source == m_yearDownButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.year--;
				if (m_time.year<2000) m_time.year=2050;
				m_yearLabel.setText(String.valueOf(m_time.year));
				int days = getDays(m_time.month, m_time.year);
				if (m_time.day>days) {
					m_time.day = days;
					m_dayLabel.setText(getString(m_time.day));
				}
			}
		} else if (e.source == m_yearUpButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.year++;
				if (m_time.year>2050) m_time.year=2000;
				m_yearLabel.setText(String.valueOf(m_time.year));
				int days = getDays(m_time.month, m_time.year);
				if (m_time.day>days) {
					m_time.day = days;
					m_dayLabel.setText(getString(m_time.day));
				}
			}		
		} else if (e.source == m_hourDownButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.hour+=23;
				m_time.hour%=24;
				m_hourLabel.setText(getString(m_time.hour));						
			}
		} else if (e.source == m_hourUpButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.hour++;
				m_time.hour%=24;
				m_hourLabel.setText(getString(m_time.hour));						
			}
		} else if (e.source == m_minDownButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.minute+=59;
				m_time.minute%=60;
				m_minLabel.setText(getString(m_time.minute));						
			}
		} else if (e.source == m_minUpButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.minute++;
				m_time.minute%=60;
				m_minLabel.setText(getString(m_time.minute));						
			}
		} else if (e.source == m_secDownButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.second+=59;
				m_time.second%=60;
				m_secLabel.setText(getString(m_time.second));						
			}
		} else if (e.source == m_secUpButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_time.second++;
				m_time.second%=60;
				m_secLabel.setText(getString(m_time.second));						
			}
		}		
	}	
	
	class TimeThread extends Thread {
		public void run() {			
			Deadline dl = new Deadline(1000);
			synchronized (dl) {
				while (!m_modified && (state&STATE_DISPOSED)==0 && parent!=null) {
					synchronized (m_time) {
						RTC.getTime(m_time);
						updateLabels();
					}					
					try {
						dl.append(1000);
					} catch (DeadlineMissException dme) {						
					}
				}			
			}
			m_timeThread = null;
		}
	}
    
}