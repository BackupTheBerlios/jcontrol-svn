/**
 * Java file created by JControl/IDE
 * 
 * Created on 24.09.06 11:04
 * 
 * @author Marcus Timmermann
 */

import java.io.IOException;

import jcontrol.graphics.Color;
import jcontrol.io.Resource;

import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.ActionEvent;
import jcontrol.ui.wombat.event.ActionListener;

public class LightPage extends Container implements ActionListener {
	
	private static final byte LAMP_OFF = 0;
	private static final byte LAMP_ON = 0x10;
	
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 5;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Label m_lightLabel;
	private Button m_onButton;
	private Button m_offButton;
	private Button m_buttonBack;
	private Label m_headline;
	int m_lastPage;

	/**
	 * Constructor LightPage
	 * 
	 * @param parent
	 */
	public LightPage(HouseDemo parent, int lastPage) {
		super(MAX_CAPACITY);
		
		// initialize side
		m_lastPage = lastPage;
		m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_lightLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_lightLabel.setImage(new Resource("ceilinglight.jcif"));
		} catch(IOException ioe) {}
		m_lightLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_lightLabel);
		m_onButton = new Button("ON", 85, 85, 70, 70);
		m_onButton.setBackgroundColor(new Color(0, 128, 0));
		m_onButton.setTransparentColor(Color.LIGHT_GRAY);
		m_onButton.setForegroundColor(Color.WHITE);
		this.add(m_onButton);
		m_onButton.setActionListener(this);
		m_offButton = new Button("OFF", 165, 85, 70, 70);
		m_offButton.setBackgroundColor(Color.RED);
		m_offButton.setTransparentColor(Color.LIGHT_GRAY);
		m_offButton.setForegroundColor(Color.WHITE);
		this.add(m_offButton);
		m_offButton.setActionListener(this);
		m_buttonBack = new Button(null, 5, 85, 70, 70);
		m_buttonBack.setBackgroundColor(Color.WHITE);
		try {
			m_buttonBack.setImage(new Resource("arrow-left.jcif"));
		} catch(IOException ioe) {}
		this.add(m_buttonBack);
		m_buttonBack.setActionListener(this);
		m_headline = new Label("CEILING LIGHT", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
		HouseDemo.lightPage = this;
		updateState();
	}
	
	int state  = LAMP_ON;
	
	// blocks until state updated!
	public void updateState() {
		for (int i=0; i<3; i++) {
			if ( state >= 0 ) {
				switch (state) {
					case LAMP_OFF: // off
						m_offButton.setEnabled(false);
						m_offButton.press(true);
						m_onButton.setEnabled(true);
						m_onButton.press(false);
						break;
					case LAMP_ON: // on
						m_onButton.setEnabled(false);
						m_onButton.press(true);
						m_offButton.setEnabled(true);
						m_offButton.press(false);
						break;
				}
				break;
			}
		}
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
				m_parentApplication.showPage(m_lastPage);
				HouseDemo.lightPage = null;
			}
		} else if (e.source == m_offButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				e.source = null;
				
						state  = LAMP_OFF;
						updateState();
			}
		} else if (e.source == m_onButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				e.source = null;
						state  = LAMP_ON;
						updateState();
			}
		}
	}
}