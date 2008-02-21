/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 15:49
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.ToggleSwitch;
import jcontrol.ui.wombat.event.ActionEvent;

public class ToggleSwitchPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 7;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Label m_displayLabel;
	private Label m_actionTitle;
	private ToggleSwitch m_toggleSwitch1;
	private ToggleSwitch m_toggleSwitch2;
	private ToggleSwitch m_toggleSwitch3;
	private ToggleSwitch m_toggleSwitch4;

	/**
	 * Constructor ToggleSwitchPage
	 * 
	 * @param parent
	 */
	public ToggleSwitchPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("ToggleSwitch", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_displayLabel = new Label("none", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Last Action", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_actionTitle);
		m_toggleSwitch1 = new ToggleSwitch(170, 45);
		m_toggleSwitch1.setText("On", "Off");
		this.add(m_toggleSwitch1);
		m_toggleSwitch1.setActionListener(this);
		m_toggleSwitch2 = new ToggleSwitch(205, 45);
		m_toggleSwitch2.setText("On", "Off");
		this.add(m_toggleSwitch2);
		m_toggleSwitch2.setActionListener(this);
		m_toggleSwitch3 = new ToggleSwitch(240, 45);
		m_toggleSwitch3.setText("On", "Off");
		this.add(m_toggleSwitch3);
		m_toggleSwitch3.setActionListener(this);
		m_toggleSwitch4 = new ToggleSwitch(275, 45);
		m_toggleSwitch4.setText("On", "Off");
		this.add(m_toggleSwitch4);
		m_toggleSwitch4.setActionListener(this);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_toggleSwitch1) {
			if (e.type == ActionEvent.STATE_CHANGED) {
				m_displayLabel.setText("ToggleSwitch 1".concat(((ToggleSwitch)e.source).isSelected()?" On":" Off"));				
			}
		} else if (e.source == m_toggleSwitch2) {
			if (e.type == ActionEvent.STATE_CHANGED) {
				m_displayLabel.setText("ToggleSwitch 2".concat(((ToggleSwitch)e.source).isSelected()?" On":" Off"));				
			}
		} else if (e.source == m_toggleSwitch3) {
			if (e.type == ActionEvent.STATE_CHANGED) {
				m_displayLabel.setText("ToggleSwitch 3".concat(((ToggleSwitch)e.source).isSelected()?" On":" Off"));				
			}
		} else if (e.source == m_toggleSwitch4) {
			if (e.type == ActionEvent.STATE_CHANGED) {
				m_displayLabel.setText("ToggleSwitch 4".concat(((ToggleSwitch)e.source).isSelected()?" On":" Off"));				
			}
		}
	}
}