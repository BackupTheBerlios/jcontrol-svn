/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 16:20
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.NumberChooser;
import jcontrol.ui.wombat.event.ActionEvent;

public class NumberChooserPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 4;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Label m_displayLabel;
	private Label m_actionTitle;
	private NumberChooser m_numberChooser;

	/**
	 * Constructor NumberChooserPage
	 * 
	 * @param parent
	 */
	public NumberChooserPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_Digits_32px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("NumberChooser", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_displayLabel = new Label("0", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Selected Number", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_actionTitle);
		m_numberChooser = new NumberChooser(195, 75, 0, 999);
		this.add(m_numberChooser);
		m_numberChooser.setActionListener(this);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_numberChooser) {
			if (e.type == ActionEvent.VALUE_CHANGED) {
				m_displayLabel.setText(String.valueOf(m_numberChooser.getValue()));				
			}
		}
	}
}