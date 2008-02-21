
/**
 * Java file created by JControl/IDE
 * 
 * Created on 29.11.05 13:00
 * 
 * @author timmer
 */
 
import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import jcontrol.graphics.Color;
import java.io.IOException;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class Outline extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 2;
    /**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Container m_menuBar;
	private Container m_buttonBar;
	private Label m_title;
	private Button m_prevButton;
	private Button m_nextButton;
	private Button m_okButton;
	private Button m_cancelButton;
	private Button m_timeButton;
	private Button m_languageButton;
	private Button m_screenButton;
	private Button m_resetButton;
	private Button m_testButton;
	private Button m_commonsButton;


	private int m_mode = -1;
	
	int m_language = -1;
    
    private final String[] STRING_OK = { 
            "OK",
            "OK"
    };
    private final String[] STRING_CANCEL = { 
            "Cancel",
            "Abbr." 
    };
    private final String[] STRING_TIME = {
            "Date/Time",
            "Datum/Uhrzeit",
    };
    private final String[] STRING_SCREEN  = {
            "Display",
            "Display",
    };
    private final String[] STRING_RESET = {
            "Reset",
            "Zurücksetzen",
    };
    private final String[] STRING_TEST = {
            "Test",
            "Test",
    };
    private final String[] STRING_COMMONS = {
            "Common",
            "Allgemeines",
    };
    private final String[] STRING_LANGUAGE = {
            "Language",
            "Sprache",
    };
       
	/**
	 * Constructor Outline
	 * 
	 * @param parent
	 */
	public Outline(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
        m_menuBar = new Container(5);
		try {
			m_menuBar.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		this.add(m_menuBar);
		m_title = new Label(((String)null), 200, 0, 120, 34, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		m_title.setBackgroundColor(Color.WHITE);
		m_menuBar.add(m_title);
		m_prevButton = new Button(null, 0, 0, 50, 34);
		try {
			m_prevButton.setImage(new Resource("Left_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_menuBar.add(m_prevButton);
		m_prevButton.setActionListener(this);
		m_nextButton = new Button(null, 50, 0, 50, 34);
		try {
			m_nextButton.setImage(new Resource("Right_Arrow.jcif"));
		} catch(IOException ioe) {}
		m_menuBar.add(m_nextButton);
		m_nextButton.setActionListener(this);
		m_okButton = new Button(null, 100, 0, 50, 34);
		m_menuBar.add(m_okButton);
		m_okButton.setActionListener(this);
		m_cancelButton = new Button(null, 150, 0, 50, 34);
		m_menuBar.add(m_cancelButton);
		m_cancelButton.setActionListener(this);
		m_buttonBar = new Container(6);
		try {
			m_buttonBar.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		this.add(m_buttonBar);
		m_timeButton = new Button(null, 0, 102, 100, 34);
		m_buttonBar.add(m_timeButton);
		m_timeButton.setActionListener(this);
		m_languageButton = new Button(null, 0, 68, 100, 34);
		m_buttonBar.add(m_languageButton);
		m_languageButton.setActionListener(this);
		m_screenButton = new Button(null, 0, 136, 100, 34);
		m_buttonBar.add(m_screenButton);
		m_screenButton.setActionListener(this);
		m_resetButton = new Button(null, 0, 204, 100, 34);
		m_buttonBar.add(m_resetButton);
		m_resetButton.setActionListener(this);
		m_testButton = new Button("Test", 0, 170, 100, 34);
		m_buttonBar.add(m_testButton);
		m_testButton.setActionListener(this);
		m_commonsButton = new Button(null, 0, 34, 100, 34);
		m_buttonBar.add(m_commonsButton);
		m_commonsButton.setActionListener(this);
        setLanguage(m_parentApplication.getLanguage());        
	}
       
    public void setLanguage(int language) {
        if (language==m_language) return;
        m_language = language;
        m_okButton.setText(STRING_OK[m_language]);
        m_cancelButton.setText(STRING_CANCEL[m_language]);
        m_commonsButton.setText(STRING_COMMONS[m_language]);
        m_languageButton.setText(STRING_LANGUAGE[m_language]);
        m_timeButton.setText(STRING_TIME[m_language]);        
        m_screenButton.setText(STRING_SCREEN[m_language]);
        m_testButton.setText(STRING_TEST[m_language]);
        m_resetButton.setText(STRING_RESET[m_language]);
    }
	
	public void setTitle(String title) {
		m_title.setText(title);
	}
	
	public void setOKButtonText(String okText) {
		if (okText==null) {
			m_okButton.setText(STRING_OK[m_language]);
		} else {
			m_okButton.setText(okText);
		}					
	}
	
	public void setCancelButtonText(String cancelText) {
		if (cancelText==null) {
			m_cancelButton.setText(STRING_CANCEL[m_language]);
		} else {
			m_cancelButton.setText(cancelText);
		}					
	}	
	
	public void showOkButton(boolean onoff) {
		m_okButton.setEnabled(onoff);		
	}
	
	public void showCancelButton(boolean onoff) {
		m_cancelButton.setEnabled(onoff);		
	}
	
	public void showPrevButton(boolean onoff) {
		m_prevButton.setEnabled(onoff);		
	}
	
	public void showNextButton(boolean onoff) {
		m_nextButton.setEnabled(onoff);		
	}
	
	public void showButtonBar(boolean onoff) {
		m_buttonBar.setVisible(onoff);
	}
	
	public void setMode(int mode) {
	    switch (m_mode) {
			case SystemSetup.COMMONPAGE:
				m_commonsButton.setBackgroundColor(null);
				break;
			case SystemSetup.TOUCHPAGE:
				m_screenButton.setBackgroundColor(null);
				break;
			case SystemSetup.TIMEPAGE:
			    m_timeButton.setBackgroundColor(null);
				break;			    
		    case SystemSetup.LANGUAGEPAGE:
			    m_languageButton.setBackgroundColor(null);
				break;
			case SystemSetup.TESTPAGE:
			    m_testButton.setBackgroundColor(null);
				break;
			case SystemSetup.RESETPAGE:
				m_resetButton.setBackgroundColor(null);
				break;
		}
	    m_mode = mode;
		switch (m_mode) {
			case SystemSetup.COMMONPAGE:
				m_commonsButton.setBackgroundColor(Color.YELLOW);
				break;
			case SystemSetup.TOUCHPAGE:
				m_screenButton.setBackgroundColor(Color.YELLOW);
				break;
			case SystemSetup.TIMEPAGE:
			    m_timeButton.setBackgroundColor(Color.YELLOW);
				break;			    
		    case SystemSetup.LANGUAGEPAGE:
			    m_languageButton.setBackgroundColor(Color.YELLOW);
				break;
			case SystemSetup.TESTPAGE:
			    m_testButton.setBackgroundColor(Color.YELLOW);
				break;
			case SystemSetup.RESETPAGE:
				m_resetButton.setBackgroundColor(Color.YELLOW);
				break;
			
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
		if (e.source == m_commonsButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {			
				m_parentApplication.showPage(SystemSetup.COMMONPAGE);
			}
		} else if (e.source == m_languageButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.LANGUAGEPAGE);
			}
		} else if (e.source == m_timeButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.TIMEPAGE);
			}
		} else if (e.source == m_screenButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.TOUCHPAGE);
			}
		} else if (e.source == m_testButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.TESTPAGE);
			}
		} else if (e.source == m_resetButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.RESETPAGE);
			}
		} else if (e.source == m_cancelButton) {
			m_parentApplication.cancel();			
		} else if (e.source == m_nextButton) {
			m_parentApplication.next();			
		} else if (e.source == m_okButton) {
			m_parentApplication.ok();			
		} else if (e.source == m_prevButton) {
			m_parentApplication.prev();			
		}
	}
}