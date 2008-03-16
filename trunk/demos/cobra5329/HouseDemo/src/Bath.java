/**
 * Java file created by JControl/IDE
 * 
 * Created on 24.09.06 11:37
 * 
 * @author Marcus Timmermann
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.ui.wombat.Label;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.event.ActionEvent;

public class Bath extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 5;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Label m_bathLabel;
	private Button m_ceilingLeight;
	private Button m_buttonHome;
	private Label m_headline;
	private Button m_waterTemp;

	/**
	 * Constructor Bath
	 * 
	 * @param parent
	 */
	public Bath(HouseDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_bathLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_bathLabel.setImage(new Resource("bathroom.jcif"));
		} catch(IOException ioe) {}
		m_bathLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_bathLabel);
		m_ceilingLeight = new Button(null, 85, 85, 70, 70);
		try {
			m_ceilingLeight.setImage(new Resource("ceilinglight.jcif"));
		} catch(IOException ioe) {}
		this.add(m_ceilingLeight);
		m_ceilingLeight.setActionListener(this);
		m_buttonHome = new Button(null, 5, 85, 70, 70);
		m_buttonHome.setBackgroundColor(Color.WHITE);
		try {
			m_buttonHome.setImage(new Resource("house.jcif"));
		} catch(IOException ioe) {}
		this.add(m_buttonHome);
		m_buttonHome.setActionListener(this);
		m_headline = new Label("BATH ROOM", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		try {
			m_headline.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
		m_waterTemp = new Button(null, 165, 85, 70, 70);
		try {
			m_waterTemp.setImage(new Resource("water-temp.jcif"));
		} catch(IOException ioe) {}
		this.add(m_waterTemp);
		m_waterTemp.setActionListener(this);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_buttonHome) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.HOUSE);
			}
		} else if (e.source == m_ceilingLeight) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.LIGHT_PAGE);
			}
		} else if (e.source == m_waterTemp) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.WATER_PAGE);
			}
		}
	}
}