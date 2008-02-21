
/**
 * Java file created by JControl/IDE
 * 
 * Created on 01.12.05 08:32
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.Label;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.graphics.XGraphics;
import jcontrol.lang.ThreadExt; 
import jcontrol.io.Buzzer;

public class BuzzerTestPage extends Container {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 2;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Label m_label_1;
	private Label m_label_2;
	
	private int m_mode = 0;
	private Buzzer m_buzzer = new Buzzer();
    
    private final String[] STRING_FREQ = {
            "Frequency",
            "Frequenz",
    };

	/**
	 * Constructor BuzzerTestPage
	 * 
	 * @param parent
	 */
	public BuzzerTestPage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
        m_label_2 = new Label(STRING_FREQ[m_parentApplication.getLanguage()], 180, 102, 0, 0, Label.STYLE_ALIGN_LEFT);
        this.add(m_label_2);
        m_label_1 = new BlockingLabel(100, 116, 220, 20, Label.STYLE_ALIGN_CENTER);
		this.add(m_label_1);
		m_label_1.setText("500 Hz ...");        
	}
	
	class BlockingLabel extends Label {
	
		BlockingLabel(int x, int y, int w, int h, int style) {
			super((String)null, x, y, w, h, style);
		}
		
		public void paint(XGraphics g) {
			super.paint(g);
			switch (m_mode) {
				case 0:
					m_buzzer.on(500);
			    	try {
				    	ThreadExt.sleep(2000);
			    	} catch (Exception e) {}
			    	m_mode++;
			    	setText("1000 Hz ...");			    	
			    	break;
				case 1:					
					m_buzzer.on(1000);
			    	try {
				    	ThreadExt.sleep(2000);
			    	} catch (Exception e) {}
			    	m_mode++;
			    	setText("2000 Hz ...");
					break;
				case 2:
					m_buzzer.on(2000);
			    	try {
				    	ThreadExt.sleep(2000);
			    	} catch (Exception e) {}
			    	m_buzzer.off();			    	
			    	m_mode++;
					m_parentApplication.showPage(SystemSetup.TESTPAGE);
					break;
			}
		}
	}

}