/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 16:10
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.ComboBox;
import jcontrol.ui.wombat.event.ActionEvent;

public class ComboBoxPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 5;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Label m_displayLabel;
	private Label m_actionTitle;
	private ComboBox m_comboBox1;
	private ComboBox m_comboBox2;

	/**
	 * Constructor ComboBoxPage
	 * 
	 * @param parent
	 */
	public ComboBoxPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("ComboBox", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_displayLabel = new Label("none", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Last Selected", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_actionTitle);
		m_comboBox1 = new ComboBox(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}, 170, 30, 110);
		this.add(m_comboBox1);
		m_comboBox1.setActionListener(this);
		m_comboBox2 = new ComboBox(new String[]{"First Entry", "Second Entry", "Third Entry", "Fourth Entry"}, 170, 80, 110);
		this.add(m_comboBox2);
		m_comboBox2.setActionListener(this);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_comboBox1) {
			if (e.type == ActionEvent.ITEM_SELECTED) {
				m_displayLabel.setText("ComboBox 1, ".concat(e.command));				
			}
		} else if (e.source == m_comboBox2) {
			if (e.type == ActionEvent.ITEM_SELECTED) {
				m_displayLabel.setText("ComboBox 2, index ".concat(String.valueOf(m_comboBox2.getSelectedIndex())));				
			}
		}
	}
}