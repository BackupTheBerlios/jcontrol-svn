/**
 * Java file created by JControl/IDE
 * 
 * Created on 24.09.06 12:08
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

public class LockingPage extends Container implements ActionListener {
	
	private static final byte LOCK_LOCK = 0;
	private static final byte LOCK_UNLOCK = 1<<0;
	
	private static final byte DOOR_OPEN = 1<<1;
	private static final byte DOOR_CLOSE = 0;
	
	
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 5;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private HouseDemo m_parentApplication;
	private Label m_keyLabel;
	private Button m_onButton;
	private Button m_offButton;
	private Button m_buttonBack;
	private Label m_headline;
	private Label m_doorLabel;

	/**
	 * Constructor LockingPage
	 * 
	 * @param parent
	 */
	public LockingPage(HouseDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		m_keyLabel = new Label(((String)null), 0, 0, 85, 70, Label.STYLE_ALIGN_CENTER);
		try {
			m_keyLabel.setImage(new Resource("key.jcif"));
		} catch(IOException ioe) {}
		m_keyLabel.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_keyLabel);
		m_onButton = new Button(null, 85, 85, 70, 70);
        try {
            m_onButton.setImage(new Resource("lock_open.jcif"));
        } catch (IOException e) {           
        }
		m_onButton.setBackgroundColor(new Color(0, 128, 0));
		m_onButton.setTransparentColor(Color.LIGHT_GRAY);
		m_onButton.setForegroundColor(Color.WHITE);
		this.add(m_onButton);
		m_onButton.setActionListener(this);
		m_offButton = new Button(null, 165, 85, 70, 70);
        try {
            m_offButton.setImage(new Resource("lock_close.jcif"));
        } catch (IOException e) {           
        }
		m_offButton.setBackgroundColor(Color.RED);
		m_offButton.setTransparentColor(Color.LIGHT_GRAY);
		m_offButton.setForegroundColor(Color.WHITE);
		this.add(m_offButton);
		m_doorLabel = new Label("", 245, 85, 70, 70, Label.STYLE_ALIGN_CENTER);
		m_doorLabel.setBackgroundColor(Color.WHITE);
		this.add(m_doorLabel);
		m_offButton.setActionListener(this);
		m_buttonBack = new Button(null, 5, 85, 70, 70);
		m_buttonBack.setBackgroundColor(Color.WHITE);
		try {
			m_buttonBack.setImage(new Resource("arrow-left.jcif"));
		} catch(IOException ioe) {}
		this.add(m_buttonBack);
		m_buttonBack.setActionListener(this);
		m_headline = new Label("DOOR LOCK", 85, 0, 235, 70, Label.STYLE_ALIGN_LEFT);
		m_headline.setForegroundColor(Color.WHITE);
		m_headline.setBackgroundColor(new Color(0, 130, 43));
		this.add(m_headline);
		HouseDemo.lockingPage = this;
		updateState();
	}
	int state = LOCK_UNLOCK;
	int door = DOOR_OPEN;
	// blocks until state updated!
	public void updateState() {	
		for (int i=0; i<3; i++) {
			if ( state >= 0 ) {
				switch (state) {
					case LOCK_LOCK: // lock
						m_offButton.setEnabled(false);
						m_offButton.press(true);
						m_onButton.setEnabled(true);
						m_onButton.press(false);
						break;
					case LOCK_UNLOCK: // unlock
						m_onButton.setEnabled(false);
						m_onButton.press(true);
						m_offButton.setEnabled(true);
						m_offButton.press(false);
						break;
				}				
				break;
			}
		}
		switch (door) {
			case DOOR_OPEN : // lock
				try {
					m_doorLabel.setImage(new Resource("lounge.jcif"));
				} catch(IOException ioe) {}									
				break;
			case DOOR_CLOSE: // unlock
				try {
					m_doorLabel.setImage(new Resource("door_closed.jcif"));
				} catch(IOException ioe) {}								
				break;
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
				m_parentApplication.showPage(HouseDemo.LOUNGE);
				HouseDemo.lockingPage = null;
			}
		} else if (e.source == m_offButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				// add your code here
				e.source = null;
				state = LOCK_LOCK;
						updateState();
			}
		} else if (e.source == m_onButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				// add your code here
				e.source = null;
				state = LOCK_UNLOCK;
						updateState();
			}
		}
	}
}