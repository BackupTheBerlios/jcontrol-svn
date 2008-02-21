/**
 * Java file created by JControl/IDE
 * 
 * Created on 24.09.06 11:48
 * 
 * @author Marcus Timmermann
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Label;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class WindowPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 4;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Label m_windowLabel;
	private Label m_headline;
	private Button m_buttonBack;
	private Label m_statusLabel;

	/**
	 * Constructor WindowPage
	 * 
	 * @param parent
	 */
	public WindowPage(HouseDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_windowLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_windowLabel.setImage(new Resource("window.jcif"));
		} catch(IOException ioe) {}
		m_windowLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_windowLabel);
		m_headline = new Label("WINDOWS", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
		m_buttonBack = new Button(null, 5, 85, 70, 70);
		m_buttonBack.setBackgroundColor(Color.WHITE);
		try {
			m_buttonBack.setImage(new Resource("arrow-left.jcif"));
		} catch(IOException ioe) {}
		this.add(m_buttonBack);
		m_buttonBack.setActionListener(this);
		m_statusLabel = new Label("All windows closed!", 100, 110, 0, 0, 0);
		this.add(m_statusLabel);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_buttonBack) {
		    if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.LIVING_ROOM);
			}
		}
	}
}