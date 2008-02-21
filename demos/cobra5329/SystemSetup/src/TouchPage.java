
/**
 * Java file created by JControl/IDE
 * 
 * Created on 29.11.05 13:07
 * 
 * @author timmer
 */
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class TouchPage extends AbstractSystemSetupPage implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 2;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Button m_calibrateButton;
	private Button m_testButton;
	private Button m_contrastButton;
    
    private final String[] HEADLINE  = {
            "Display-Configuration",
            "Display-Einstellungen",
    };
    private final String[] CALIBRATE = {
            "Calibrate Touch",
            "Touch kalibrieren",
    };
    private final String[] CONTRAST = {
            "Contrast & Brightness",
            "Konstrast & Helligkeit",
    };
    private final String[] TEST = {
            "Test Touch",
            "Test Touch",
    };

	/**
	 * Constructor TouchPage
	 * 
	 * @param parent
	 */
	public TouchPage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
        int language = m_parentApplication.getLanguage();
		m_parentApplication.setTitle(HEADLINE[language]);
		try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		m_calibrateButton = new Button(CALIBRATE[language], 145, 82, 150, 34);
		this.add(m_calibrateButton);
		m_calibrateButton.setActionListener(this);
		m_testButton = new Button(TEST[language], 145, 116, 150, 34);
		this.add(m_testButton);
		m_testButton.setActionListener(this);
		m_contrastButton = new Button(CONTRAST[language], 145, 150, 150, 34);
		this.add(m_contrastButton);
		m_contrastButton.setActionListener(this);
		
	}
		

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_calibrateButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.TOUCHCALIBPAGE);
			}
		} else if (e.source == m_testButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.TOUCHTESTPAGE);
			}
		} else if (e.source == m_contrastButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(SystemSetup.CONTRASTPAGE);
			}
		}
		
	}
}