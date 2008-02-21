/**
 * Java file created by JControl/IDE
 * 
 * Created on 01.03.07 15:47
 *  
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class Outline extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 2;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private PageDemo m_parentApplication;
	private Button m_page1Button;
	private Button m_page2Button;

	/**
	 * Constructor Outline
	 * 
	 * @param parent the main class 
	 */
	public Outline(PageDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_page1Button = new Button("Show Page 1", 0, 210, 60, 30);
		this.add(m_page1Button);
		m_page1Button.setActionListener(this);
		m_page2Button = new Button("Show Page 2", 60, 210, 60, 30);
		this.add(m_page2Button);
		m_page2Button.setActionListener(this);
	}

	/**
	 * This method is called every time one of the buttons specified in this
	 * class is pressed or released. The action is used to perform a page switch.
	 * 
	 * @param e The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_page1Button) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(PageDemo.CONTENT_1);				
			}
		} else if (e.source == m_page2Button) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_parentApplication.showPage(PageDemo.CONTENT_2);				
			}
		}
	}
}