
/**
 * Java file created by JControl/IDE
 * 
 * Created on 30.11.05 11:52
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.Label;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.event.TouchListener;
import jcontrol.ui.wombat.event.TouchEvent;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class DisplayTestPage extends AbstractSystemSetupPage implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 2;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Container m_initialContainer;
	private Label m_label_1;
	private Label m_label_2;
	private Label m_label_3;
	private Button m_button_1;

	private int m_page = 0;
    
    private final String[] INTRO1 = {
            "After pressing \"OK\" eight testing patterns will appear that",
            "Nachfolgend erscheinen 8 Testmuster, die durch wiederholtes",
    };
    private final String[] INTRO2 = {
            "can be passed through by continuously touching the screen.",
            "Drücken des Touch-Screens durchgeschaltet werden können.",
    };
    private final String[] OK = {
            "OK",
            "OK",
    };

	/**
	 * Constructor DisplayTestPage
	 * 
	 * @param parent
	 */
	public DisplayTestPage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_parentApplication.getOutline().setVisible(false);
        int language = m_parentApplication.getLanguage();
		try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		m_initialContainer = new Container(3);
		this.add(m_initialContainer);
		m_label_1 = new TouchLabel(0, 0, 320, 240);
		m_label_2 = new Label(INTRO1[language], 0, 80, 320, 0, Label.STYLE_ALIGN_CENTER);
		m_initialContainer.add(m_label_2);
		m_label_3 = new Label(INTRO2[language], 0, 100, 320, 0, Label.STYLE_ALIGN_CENTER);
		m_initialContainer.add(m_label_3);
		m_button_1 = new Button(OK[language], 110, 136, 100, 30);
		m_initialContainer.add(m_button_1);
		m_button_1.setActionListener(this);
        
	}      
	
	public void showPrev() {
		showImage(m_page-1);
	}
    
    public void showNext() {
	    showImage(m_page+1);
    }
	
	public void ok() {
	    m_parentApplication.getOutline().setVisible(true);
		m_parentApplication.showPage(SystemSetup.TESTPAGE);
	}    
	
	private void showImage(int number) {
		if (number>=0 && number<9) {
			m_page = number;
			switch (m_page) {
				case 1:
					remove(m_initialContainer);
					add(m_label_1);
					m_label_1.repaint();
					try {
						m_label_1.setImage(new Resource("test0_320x240.jcif"));
					} catch (IOException e) {}										
					break;
				case 2:
					try {
						m_label_1.setImage(new Resource("test1_320x240.jcif"));
					} catch (IOException e) {}
					break;
				case 3:
					try {
						m_label_1.setImage(new Resource("test2_320x240.jcif"));
					} catch (IOException e) {}
					break;
				case 4:
					m_label_1.setImage(null);
					m_label_1.setBackgroundColor(Color.WHITE);
					break;
				case 5:
					m_label_1.setBackgroundColor(Color.BLACK);
					break;
				case 6:
					m_label_1.setBackgroundColor(Color.RED);
					break;
				case 7:
					m_label_1.setBackgroundColor(Color.GREEN);
					break;
			    case 8:
					m_label_1.setBackgroundColor(Color.BLUE);
					break;
			}
		} else {
			ok();
		}
		
	}
	
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_button_1) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				showNext();
			}
		}
	}
	
	class TouchLabel extends Label implements TouchListener {
	
		public TouchLabel(int x, int y, int w, int h) {
			super((String)null, x, y, w, h, Label.STYLE_ALIGN_CENTER);
		}
	
		public int onTouchEvent(TouchEvent event) {
			if (!isVisible()) return RESULT_NONE;
			switch (event.type) {
	            case TouchEvent.TYPE_TOUCH_PRESSED:
					showNext();					
			}
			return RESULT_ACCEPTED;
		}
	}    
}