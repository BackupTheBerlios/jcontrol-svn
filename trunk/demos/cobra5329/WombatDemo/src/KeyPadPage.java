/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 15:52
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.KeyPad;
import jcontrol.ui.wombat.event.ActionEvent;

public class KeyPadPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 4;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Label m_displayLabel;
	private Label m_actionTitle;
	private KeyPad m_keyPad;

	/**
	 * Constructor KeyPadPage
	 * 
	 * @param parent
	 */
	public KeyPadPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("KeyPad", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_displayLabel = new Label("none", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Last Key", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_actionTitle);
		m_keyPad = new KeyPad(175, 20, 120, 160);
		try {
			m_keyPad.setFont(new Resource("SansSerif_22px.jcfd"));
		} catch(IOException ioe) {}
		this.add(m_keyPad);
		m_keyPad.setActionListener(this);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_keyPad) {
			if (e.type == ActionEvent.ITEM_SELECTED) {
				m_displayLabel.setText(e.command);				
			}
		}
	}
}