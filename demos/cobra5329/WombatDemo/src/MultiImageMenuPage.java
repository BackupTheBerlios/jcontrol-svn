/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 16:31
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.menu.MultiImageMenu;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.ActionEvent;
import jcontrol.graphics.Color;

public class MultiImageMenuPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 4;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private MultiImageMenu m_multiImageMenu;
	private Label m_displayLabel;
	private Label m_actionTitle;

	/**
	 * Constructor MultiImageMenuPage
	 * 
	 * @param parent
	 */
	public MultiImageMenuPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("MultiImageMenu", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_multiImageMenu = new MultiImageMenu(160, 25, 150, 150, 3, 3, MultiImageMenu.STYLE_SHOW_BORDER);
		m_multiImageMenu.addMenuItem("item1.jcif");
		m_multiImageMenu.addMenuItem("item2.jcif");
		m_multiImageMenu.addMenuItem("item3.jcif");
		m_multiImageMenu.addMenuItem("item4.jcif");
		m_multiImageMenu.addMenuItem("item5.jcif");
		m_multiImageMenu.addMenuItem("item6.jcif");
		m_multiImageMenu.addMenuItem("item7.jcif");
		m_multiImageMenu.addMenuItem("item8.jcif");
		m_multiImageMenu.setBackgroundColor(Color.WHITE);
		this.add(m_multiImageMenu);
		m_multiImageMenu.setActionListener(this);
		m_displayLabel = new Label("0", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Selected Item", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
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
		if (e.source == m_multiImageMenu) {
			if (e.type == ActionEvent.ITEM_SELECTED) {				
				m_displayLabel.setText(String.valueOf(m_multiImageMenu.getSelectedIndex()));				
			}
		}
	}
}