
/**
 * Java file created by JControl/IDE
 * 
 * Created on 30.11.05 09:55
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class TestPage extends AbstractSystemSetupPage implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 5;
    
    private final String[] HEADLINE = {
            "Test",
            "Test",
    };
    private final String[] SYSTEM_TEST = {
            "System test",
            "System-Test",
    };
    private final String[] START = {
            "Start",
            "Start",
    };
    private final String[] DETAILS = {
            "Details",
            "Details",
    };
    private final String[] COMPONENT_TEST = {
            "Component test",
            "Komponenten-Test",
    };
    private final String[] DISPLAY = {
            "Display",
            "Display",
    };
    private final String[] SCREEN = {
            "Touch-Screen",
            "Touch-Screen",
    };
    private final String[] BUZZER = {
            "Buzzer",
            "Buzzer",
    };    
    private final String[] INTERFACES = {
            "Interfaces",
            "Schnittstellen",
    };
    private final String[] SHOW_LOG = {
            "Show log-file",
            "Log-Datei anzeigen",
    };
    
    
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Border m_componentsBorder;
	private Container m_components;
	private Button m_logButton;
	private Border m_systemTestBorder;
	private Container m_systemTestButtons;
	private Button m_interfacesButton;
	private Button m_buzzerButton;
	private Button m_displayButton;
	private Button m_touchScreenButton;
	private Button m_systemTestButton;
	private Button m_detailsButton;

	/**
	 * Constructor TestPage
	 * 
	 * @param parent
	 */
	public TestPage(SystemSetup parent) {
		super(MAX_CAPACITY);		
		m_parentApplication = parent;
        int language = m_parentApplication.getLanguage();
        m_parentApplication.setTitle(HEADLINE[language]);
		try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		m_componentsBorder = new Border(COMPONENT_TEST[language], 110, 100, 210, 93, Border.STYLE_SIMPLE_BORDER);
		this.add(m_componentsBorder);
		m_components = new Container(4);
		this.add(m_components);
		m_interfacesButton = new Button(INTERFACES[language], 219, 157, 91, 30);
		m_components.add(m_interfacesButton);
		m_interfacesButton.setActionListener(this);
		m_buzzerButton = new Button(BUZZER[language], 120, 157, 91, 30);
		m_components.add(m_buzzerButton);
		m_buzzerButton.setActionListener(this);
		m_displayButton = new Button(DISPLAY[language], 120, 119, 91, 30);
		m_components.add(m_displayButton);
		m_displayButton.setActionListener(this);
		m_touchScreenButton = new Button(SCREEN[language], 219, 119, 91, 30);
		m_components.add(m_touchScreenButton);
		m_touchScreenButton.setActionListener(this);
		m_logButton = new Button(SHOW_LOG[language], 200, 204, 110, 34);
		this.add(m_logButton);
		m_logButton.setActionListener(this);
		m_systemTestBorder = new Border(SYSTEM_TEST[language], 110, 40, 210, 58, Border.STYLE_SIMPLE_BORDER);
		this.add(m_systemTestBorder);
		m_systemTestButtons = new Container(2);
		this.add(m_systemTestButtons);
		m_systemTestButton = new Button(START[language], 120, 59, 91, 30);
		m_systemTestButtons.add(m_systemTestButton);
		m_systemTestButton.setActionListener(this);
		m_detailsButton = new Button(DETAILS[language], 219, 59, 91, 30);
		m_systemTestButtons.add(m_detailsButton);
		m_detailsButton.setActionListener(this);       
	}
    

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_buzzerButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.BUZZERTESTPAGE);
			}
		} else if (e.source == m_detailsButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				// add your code here
				e.source = null;
			}
		} else if (e.source == m_displayButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.DISPLAYTESTPAGE);
			}
		}else if (e.source == m_interfacesButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				// add your code here
				e.source = null;
			}
		}else if (e.source == m_logButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.LOGFILEPAGE);
			}
		}else if (e.source == m_systemTestButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				// add your code here
				e.source = null;
			}
		}else if (e.source == m_touchScreenButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.TOUCHTESTPAGE);
			}
		}
	}
}