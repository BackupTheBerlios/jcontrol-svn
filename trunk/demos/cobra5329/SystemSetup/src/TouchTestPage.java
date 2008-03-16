
/**
 * Java file created by JControl/IDE
 * 
 * Created on 29.11.05 14:05
 * 
 * @author timmer
 */
import jcontrol.ui.wombat.Label;
import jcontrol.graphics.XGraphics;
import jcontrol.ui.wombat.event.TouchListener;
import jcontrol.ui.wombat.event.TouchEvent;
import jcontrol.graphics.Color;

public class TouchTestPage extends AbstractSystemSetupPage {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 1;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Label m_label_1;
	
	private int m_lastX = -1;
	private int m_lastY = -1;
    private int m_currX = -1;
	private int m_currY = -1;
	
	private int m_lastPage;
    
    private final String[] EXIT = {
            "Exit",
            "Verlassen",
    };
    private final String[] CLEAR = {
            "Clear",
            "Löschen",
    };


	/**
	 * Constructor TouchTestPage
	 * 
	 * @param parent
	 */
	public TouchTestPage(SystemSetup parent, int lastPage) {
		super(MAX_CAPACITY);
		m_lastPage = lastPage;
		m_parentApplication = parent;
		m_parentApplication.getOutline().setVisible(false);
		m_label_1 = new TouchLabel(0, 0, 320, 240);
		this.add(m_label_1);
	}
	
	public void showPrev() {
	}
    
    public void showNext() {
    }
	
	public void ok() {
		m_parentApplication.getOutline().setVisible(true);
		m_parentApplication.showPage(m_lastPage);	            		
	}
    
    public void cancel() {
    	m_parentApplication.getOutline().setVisible(true);
		m_parentApplication.showPage(m_lastPage);
    }
	
	class TouchLabel extends Label implements TouchListener {
	
		public TouchLabel(int x, int y, int w, int h) {
			super((String)null, x, y, w, h, Label.STYLE_ALIGN_LEFT);
		}
		
		public synchronized void paint(XGraphics g) {
			Color transparentColor = this.transparentColor==null?backgroundColor:this.transparentColor;
        
	        g.setBackground(transparentColor.getRGB());
	        if ((state&STATE_DIRTY_MASK)==STATE_DIRTY_PAINTALL) g.clearRect(x, y, width, height);
	        g.setBackground(Color.WHITE);
            int language = m_parentApplication.getLanguage();
	        g.drawString(EXIT[language], 0,0);
	        g.drawString(CLEAR[language], 295,0);
	        g.setBackground(backgroundColor.getRGB());	          
	        if (m_currX>=0 && m_currY>=0) {
	        	if (m_lastX>=0 && m_lastY>=0) {
	        		g.drawLine(m_lastX, m_lastY, m_currX, m_currY);
	        	} else {
					g.setPixel(m_currX, m_currY);
				}
			}	        
	        state&=~STATE_DIRTY_MASK;			
		}
	
		public int onTouchEvent(TouchEvent event) {
			switch (event.type) {
	            case TouchEvent.TYPE_TOUCH_PRESSED:
	            	if (event.x<=30 && event.y<=30) {
	            		ok();
	            		return RESULT_EXECUTED;
	            	} else if (event.x>=290 && event.y<=30) {
	            		setDirty(STATE_DIRTY_PAINTALL, true);
	            	}
	            case TouchEvent.TYPE_TOUCH_DRAGGED:
	            	m_lastX = m_currX;
	            	m_lastY = m_currY;
	            	m_currX = event.x;
	            	m_currY = event.y;
	            	repaint();
	            	break;
	            case TouchEvent.TYPE_TOUCH_RELEASED:
	            	m_lastX = -1;
	            	m_lastY = -1;
	            	m_currX = -1;
	            	m_currY = -1;	            	
	            	break;
					
			}
			return RESULT_NONE;
		}
	}
}