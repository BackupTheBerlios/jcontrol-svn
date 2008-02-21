/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 11:47
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.CheckBox;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.ActionEvent;

public class CheckboxPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 6;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private CheckBox m_checkBox1;
	private CheckBox m_checkBox2;
	private CheckBox m_checkBox3;
	private Label m_displayLabel;
	private Label m_actionTitle;

	/**
	 * Constructor CheckboxPage
	 * 
	 * @param parent
	 */
	public CheckboxPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("CheckBox", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_checkBox1 = new CheckBox("CheckBox 1", 180, 55, 0, 0);
		this.add(m_checkBox1);
		m_checkBox1.setActionListener(this);
		m_checkBox2 = new CheckBox("CheckBox 2", 180, 90, 0, 0);
		this.add(m_checkBox2);
		m_checkBox2.setActionListener(this);
		m_checkBox3 = new CheckBox("CheckBox 3", 180, 125, 0, 0);
		this.add(m_checkBox3);
		m_checkBox3.setActionListener(this);
		m_displayLabel = new Label("none", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Last Action", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_actionTitle);
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
			m_displayLabel.setText(e.command.concat(((CheckBox)e.source).isSelected()?" On":" Off"));				
		}		
	}
}