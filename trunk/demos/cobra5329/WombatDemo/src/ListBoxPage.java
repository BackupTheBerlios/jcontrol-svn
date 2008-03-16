/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 16:17
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.ListBox;
import jcontrol.ui.wombat.event.ActionEvent;

public class ListBoxPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 7;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Label m_displayLabel;
	private Label m_actionTitle;
	private ListBox m_listBox1;
	private Label m_title1;
	private ListBox m_listBox2;
	private Label m_title2;

	/**
	 * Constructor ListBoxPage
	 * 
	 * @param parent
	 */
	public ListBoxPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("ListBox", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_displayLabel = new Label("none", 160, 200, 150, 35, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		this.add(m_displayLabel);
		m_actionTitle = new Label("Last Action", 160, 185, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_actionTitle);
		m_listBox1 = new ListBox(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}, 160, 35, 145, 50, ListBox.STYLE_SHOW_BORDER);
		this.add(m_listBox1);
		m_listBox1.setActionListener(this);
		m_title1 = new Label("No scrollbar", 160, 20, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_title1);
		m_listBox2 = new ListBox(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}, 160, 115, 145, 50, ListBox.STYLE_SHOW_SCROLLBAR);
		this.add(m_listBox2);
		m_listBox2.setActionListener(this);
		m_title2 = new Label("With scrollbar", 160, 100, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_title2);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {		
		if (e.type == ActionEvent.ITEM_SELECTED) {
			m_displayLabel.setText("Selected index: ".concat(String.valueOf(((ListBox)e.source).getSelectedIndex())));				
		}		
	}
}