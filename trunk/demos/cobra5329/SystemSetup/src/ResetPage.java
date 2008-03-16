/**
 * Java file created by JControl/IDE
 *
 * Created on 09.12.05 16:49
 *
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.TextViewer;
import jcontrol.ui.wombat.Label;

public class ResetPage extends AbstractSystemSetupPage {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 1;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Container m_warningContainer;
	private TextViewer m_textField_1;
	private Label m_warningIcon;
	private Label m_warningText;
    int m_lastPage;
    
    private final String[] HEADLINE = {
        "Reset",
        "Zurücksetzen",
    };
    private final String[] WARNING = {
            "WARNING!",
            "WARNUNG!",
    };
    private final String[] TEXT = {
            "Confirming with \"OK\" will reset the device to manufacturer defaults!\nAll user settings will be discarded.",
            "Durch Bestätigen mit \"OK\" wird das GerÃ¤t in den Auslieferungszustand zurück gesetzt!\nAlle vorgenommenen Einstellungen gehen damit verloren."
    };

	/**
	 * Constructor ResetPage
	 *
	 * @param parent
	 */
	public ResetPage(SystemSetup parent, int lastPage) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_lastPage = lastPage;		
		m_parentApplication.showOkButton(true);
		m_parentApplication.showCancelButton(true);
        int language = m_parentApplication.getLanguage();
        m_parentApplication.setTitle(HEADLINE[language]);
		try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		m_warningContainer = new Container(3);
		m_warningContainer.setBackgroundColor(Color.WHITE);
		this.add(m_warningContainer);
		m_textField_1 = new TextViewer(TEXT[language], 120, 97, 190, 80, TextViewer.STYLE_NONE);
		m_warningContainer.add(m_textField_1);
		m_warningIcon = new Label(((String)null), 120, 68, 0, 0, Label.STYLE_ALIGN_LEFT);
		try {
			m_warningIcon.setImage(new Resource("warning.jcif"));
		} catch(IOException ioe) {}
		m_warningContainer.add(m_warningIcon);
		m_warningText = new Label(WARNING[language], 151, 68, 159, 29, Label.STYLE_ALIGN_CENTER);
		try {
			m_warningText.setFont(new Resource("SansSerif_24px.jcfd"));
		} catch(IOException ioe) {}
		m_warningText.setForegroundColor(Color.RED);
		m_warningContainer.add(m_warningText);
	}
	
	public void ok() {
	    // reset
	}

    public void cancel() {
		m_parentApplication.showPage(m_lastPage);
    }
    
}