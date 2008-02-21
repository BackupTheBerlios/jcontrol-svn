/**
 * Java file created by JControl/IDE
 * 
 * Created on 24.09.06 12:08
 * 
 * @author Marcus Timmermann
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.ui.wombat.Button;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Label;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.event.ActionEvent;

public class House extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 7;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Button m_lounge;
	private Button m_livingRoom;
	private Button m_kitchen;
	private Button m_bathroom;
	private Button m_bedroom;
	private Label m_houseLabel;
	private Label m_headline;

	/**
	 * Constructor House
	 * 
	 * @param parent
	 */
	public House(HouseDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_lounge = new Button(null, 85, 85, 70, 70);
		try {
			m_lounge.setImage(new Resource("lounge.jcif"));
		} catch(IOException ioe) {}
		this.add(m_lounge);
		m_lounge.setActionListener(this);
		m_livingRoom = new Button(null, 85, 165, 70, 70);
		try {
			m_livingRoom.setImage(new Resource("livingroom.jcif"));
		} catch(IOException ioe) {}
		this.add(m_livingRoom);
		m_livingRoom.setActionListener(this);
		m_kitchen = new Button(null, 245, 85, 70, 70);
		try {
			m_kitchen.setImage(new Resource("kitchen.jcif"));
		} catch(IOException ioe) {}
		this.add(m_kitchen);
		m_kitchen.setActionListener(this);
		m_bathroom = new Button(null, 165, 163, 70, 70);
		try {
			m_bathroom.setImage(new Resource("bathroom.jcif"));
		} catch(IOException ioe) {}
		this.add(m_bathroom);
		m_bathroom.setActionListener(this);
		m_bedroom = new Button(null, 165, 85, 70, 70);
		try {
			m_bedroom.setImage(new Resource("bedroom.jcif"));
		} catch(IOException ioe) {}
		this.add(m_bedroom);
		m_bedroom.setActionListener(this);
		m_houseLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_houseLabel.setImage(new Resource("house.jcif"));
		} catch(IOException ioe) {}
		m_houseLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_houseLabel);
		m_headline = new Label("MY HOME", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		try {
			m_headline.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_bathroom) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.BATH);
			}
		} else if (e.source == m_bedroom) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.BEDROOM);
			}
		} else if (e.source == m_kitchen) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.KITCHEN);
			}
		} else if (e.source == m_livingRoom) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.LIVING_ROOM);
			}
		} else if (e.source == m_lounge) {
			if (e.type == ActionEvent.BUTTON_RELEASED) {
				e.source = null;
				m_parentApplication.showPage(HouseDemo.LOUNGE);
			}
		}
	}
}