
/**
 * Java file created by JControl/IDE
 * 
 * Created on 29.11.05 13:07
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.graphics.Color;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class LanguagePage extends AbstractSystemSetupPage implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 1;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Container m_buttons;
	private Button m_germanButton;
	private Button m_englishButton;
	private Button m_chineseButton;
    private int m_language = -1;
    
    private final String[] STRING_LANGUAGE = {
            "Language",
            "Sprache",
    };

	/**
	 * Constructor LanguagePage
	 * 
	 * @param parent
	 */
	public LanguagePage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
        try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		m_buttons = new Container(3);
		this.add(m_buttons);
		m_englishButton = new Button("English ", 170, 68, 100, 34);
		try {
			m_englishButton.setImage(new Resource("english.jcif"));
		} catch(IOException ioe) {}
		m_buttons.add(m_englishButton);
		m_englishButton.setActionListener(this);
        m_germanButton = new Button("Deutsch", 170, 102, 100, 34);
        try {
            m_germanButton.setImage(new Resource("german.jcif"));
        } catch(IOException ioe) {}
        m_buttons.add(m_germanButton);
        m_germanButton.setActionListener(this);
        m_chineseButton = new Button("Chinese", 170, 136, 100, 34);
		try {
			m_chineseButton.setImage(new Resource("chinese.jcif"));
		} catch(IOException ioe) {}
		m_buttons.add(m_chineseButton);
		m_chineseButton.setActionListener(this);
        setLanguage(m_parentApplication.getLanguage());
	}       
    
    private void setLanguage(int language) {
        if (language==m_language) return;
        switch (m_language) {
            case 0:  
                m_englishButton.setBackgroundColor(null);
                break;
            case 1:
                m_germanButton.setBackgroundColor(null);
                break;
        }
        m_language = language;
        switch (m_language) {
            case 0:  
                m_englishButton.setBackgroundColor(Color.YELLOW);
                break;
            case 1:
                m_germanButton.setBackgroundColor(Color.YELLOW);
                break;
        }
        m_parentApplication.setTitle(STRING_LANGUAGE[m_language]);
    }
		
	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_chineseButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				// add your code here
				e.source = null;
			}
		} else if (e.source == m_englishButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
                setLanguage(0);
				m_parentApplication.setLanguage(0);
			}
		} else if (e.source == m_germanButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
                setLanguage(1);
                m_parentApplication.setLanguage(1);
			}
		}
	}
}