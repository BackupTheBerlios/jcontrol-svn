/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 11:49
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.RadioButton;
import jcontrol.ui.wombat.event.ActionEvent;

public class RadioButtonPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 6;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Label m_displayLabel;
	private Label m_actionTitle;
	private RadioButton m_radioButton1;
	private RadioButton m_radioButton2;
	private RadioButton m_radioButton3;

	/**
	 * Constructor RadioButtonPage
	 * 
	 * @param parent
	 */
	public RadioButtonPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("RadioButton", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_displayLabel = new Label("none", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Last Action", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_actionTitle);
		m_radioButton1 = new RadioButton("RadioButton 1", 185, 45, 0, 0);
		this.add(m_radioButton1);
		m_radioButton1.setActionListener(this);
		m_radioButton2 = new RadioButton("RadioButton 2", 185, 75, 0, 0);
		this.add(m_radioButton2);
		m_radioButton2.setActionListener(this);
		m_radioButton3 = new RadioButton("RadioButton 3", 185, 105, 0, 0);
		this.add(m_radioButton3);
		m_radioButton3.setActionListener(this);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.type == ActionEvent.STATE_CHANGED) {
			m_displayLabel.setText(e.command.concat(" selected"));				
		}		
	}
}