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

public class ACPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 6;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Label m_ACLabel;
	private Button m_warmerButton;
	private Button m_colderButton;
	private Button m_buttonBack;
	private Label m_headline;
	private Label m_temperature;
	
	static int m_temp = 22;

	/**
	 * Constructor ACPage
	 * 
	 * @param parent
	 */
	public ACPage(HouseDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_ACLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_ACLabel.setImage(new Resource("ac.jcif"));
		} catch(IOException ioe) {}
		m_ACLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_ACLabel);
		m_warmerButton = new Button("WARM", 85, 85, 70, 70);
		m_warmerButton.setBackgroundColor(Color.RED);
		m_warmerButton.setTransparentColor(Color.LIGHT_GRAY);
		m_warmerButton.setForegroundColor(Color.WHITE);
		this.add(m_warmerButton);
		m_warmerButton.setActionListener(this);
		m_colderButton = new Button("COOL", 245, 85, 70, 70);
		m_colderButton.setBackgroundColor(Color.BLUE);
		m_colderButton.setTransparentColor(Color.LIGHT_GRAY);
		m_colderButton.setForegroundColor(Color.WHITE);
		this.add(m_colderButton);
		m_colderButton.setActionListener(this);
		m_buttonBack = new Button(null, 5, 85, 70, 70);
		m_buttonBack.setBackgroundColor(Color.WHITE);
		try {
			m_buttonBack.setImage(new Resource("arrow-left.jcif"));
		} catch(IOException ioe) {}
		this.add(m_buttonBack);
		m_buttonBack.setActionListener(this);
		m_headline = new Label("AIR CONDITION", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
		m_temperature = new Label(String.valueOf(m_temp).concat("\u00b0C"), 165, 85, 70, 70, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		m_temperature.setBackgroundColor(Color.WHITE);
		this.add(m_temperature);
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
		} else if (e.source == m_colderButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				if (m_temp>10) {
					m_temp--;
					m_temperature.setText(String.valueOf(m_temp).concat("\u00b0C"));
				}
			}
		} else if (e.source == m_warmerButton) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				if (m_temp<35) {
					m_temp++;
					m_temperature.setText(String.valueOf(m_temp).concat("\u00b0C"));
				}
			}
		}
	}
}