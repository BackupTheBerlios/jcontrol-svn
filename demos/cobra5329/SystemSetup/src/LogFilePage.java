
/**
 * Java file created by JControl/IDE
 * 
 * Created on 09.12.05 15:43
 * 
 * @author timmer
 */

import java.io.IOException;
import jcontrol.io.Resource;
import jcontrol.ui.wombat.TextViewer;

public class LogFilePage extends AbstractSystemSetupPage {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 1;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private TextViewer m_textViewer_1;
	private int m_lastPage;
    
    private final String[] HEADLINE = {
         "Log-File",
         "Log-Datei",
    };
    
    private final String[] CLEAR = {
            "Clear",
            "Löschen"
    };

	/**
	 * Constructor LogFilePage
	 * 
	 * @param parent
	 */
	public LogFilePage(SystemSetup parent, int lastPage) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_lastPage = lastPage;
		m_parentApplication.showOkButton(true);
		m_parentApplication.showCancelButton(true);
        int language = m_parentApplication.getLanguage();
        m_parentApplication.setTitle(HEADLINE[language]);
        m_parentApplication.setCancelButtonText(CLEAR[language]);
		m_textViewer_1 = new TextViewer((String)null, 100, 34, 220, 202, TextViewer.STYLE_SHOW_SCROLLBAR);               
		try {
			m_textViewer_1.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		this.add(m_textViewer_1);
        loadLogFile();        
	}
	
	private void loadLogFile() {
		// TODO
		m_textViewer_1.setText("Hier könnte ein Log-File stehen.\nZeilenumbrüche werden hier automatisch durchgeführt.");
	}	
	
	public void ok() {
	    m_parentApplication.showPage(m_lastPage);
	}
    
    public void cancel() {
		m_textViewer_1.setText("");
		// TODO: delete log file
    }
    
}