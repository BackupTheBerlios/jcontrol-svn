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

public class OvenPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 8;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Label m_ovenLabel;
	private Button m_buttonBack;
	private Label m_headline;
	private Label m_upperLeft;
	private Label m_upperRight;
	private Label m_lowerLeft;
	private Label m_lowerRight;
	private Label m_ovenState;

	/**
	 * Constructor OvenPage
	 * 
	 * @param parent
	 */
	public OvenPage(HouseDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_ovenLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_ovenLabel.setImage(new Resource("oven.jcif"));
		} catch(IOException ioe) {}
		m_ovenLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_ovenLabel);
		m_buttonBack = new Button(null, 5, 85, 70, 70);
		m_buttonBack.setBackgroundColor(Color.WHITE);
		try {
			m_buttonBack.setImage(new Resource("arrow-left.jcif"));
		} catch(IOException ioe) {}
		this.add(m_buttonBack);
		m_buttonBack.setActionListener(this);
		m_headline = new Label("OVEN", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
		m_upperLeft = new Label("OFF", 85, 85, 70, 70, Label.STYLE_ALIGN_CENTER);
		m_upperLeft.setForegroundColor(Color.WHITE);
		m_upperLeft.setBackgroundColor(Color.BLACK);
		this.add(m_upperLeft);
		m_upperRight = new Label("OFF", 165, 85, 70, 70, Label.STYLE_ALIGN_CENTER);
		m_upperRight.setForegroundColor(Color.WHITE);
		m_upperRight.setBackgroundColor(Color.BLACK);
		this.add(m_upperRight);
		m_lowerLeft = new Label("OFF", 85, 165, 70, 70, Label.STYLE_ALIGN_CENTER);
		m_lowerLeft.setForegroundColor(Color.WHITE);
		m_lowerLeft.setBackgroundColor(Color.BLACK);
		this.add(m_lowerLeft);
		m_lowerRight = new Label("OFF", 165, 165, 70, 70, Label.STYLE_ALIGN_CENTER);
		m_lowerRight.setForegroundColor(Color.WHITE);
		m_lowerRight.setBackgroundColor(Color.BLACK);
		this.add(m_lowerRight);
		m_ovenState = new Label("Oven\noff", 245, 85, 70, 70, Label.STYLE_ALIGN_CENTER);
		this.add(m_ovenState);
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
				m_parentApplication.showPage(HouseDemo.KITCHEN);
			}
		}
	}
}