/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 16:39
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.ComboBox;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.ListBox;
import jcontrol.ui.wombat.event.ActionEvent;

public class MenuPage extends Container implements ActionListener {
	
	private static final int TYPE_BUTTONS = 0;
	private static final int TYPE_LABELS = 1;
	private static final int TYPE_MENUS = 2;

	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 7;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private WombatDemo m_parentApplication;
	private Border m_mainBorder;
	private ComboBox m_categoryChoice;
	private Label m_categoryTitle;
	private ListBox[] m_componentChoice = new ListBox[3];
	private Label m_componentChoiceTitle;
	
	private int m_category = TYPE_BUTTONS;

	/**
	 * Constructor MenuPage
	 * 
	 * @param parent
	 */
	public MenuPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("Components", 0, 0, 150, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_categoryChoice = new ComboBox(new String[]{"Buttons & Co.", "Labels & Text Fields", "Menus"}, 5, 45, 80);
		this.add(m_categoryChoice);
		m_categoryChoice.setActionListener(this);
		m_categoryTitle = new Label("Category", 5, 30, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_categoryTitle);
		m_componentChoice[TYPE_BUTTONS] = new ListBox(new String[]{"Button", "CheckBox", "RadioButton", "Slider", "ToggleSwitch", "KeyPad"}, 5, 95, 140, 140, ListBox.STYLE_SHOW_SCROLLBAR);
		this.add(m_componentChoice[TYPE_BUTTONS]);
		m_componentChoice[TYPE_BUTTONS].setActionListener(this);
		m_componentChoice[TYPE_BUTTONS].setVisible(true);
		m_componentChoice[TYPE_LABELS] = new ListBox(new String[]{"Label", "Border", "NumberChooser", "ListBox", "ComboBox"}, 5, 95, 140, 140, ListBox.STYLE_SHOW_SCROLLBAR);
		this.add(m_componentChoice[TYPE_LABELS]);
		m_componentChoice[TYPE_LABELS].setActionListener(this);
		m_componentChoice[TYPE_LABELS].setVisible(false);
		m_componentChoice[TYPE_MENUS] = new ListBox(new String[]{"MultiImageMenu"}, 5, 95, 140, 140, ListBox.STYLE_SHOW_SCROLLBAR);
		this.add(m_componentChoice[TYPE_MENUS]);
		m_componentChoice[TYPE_MENUS].setActionListener(this);
		m_componentChoice[TYPE_MENUS].setVisible(false);
		m_componentChoiceTitle = new Label("Select Component", 5, 80, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_componentChoiceTitle);
	}
	
	private void changeCategory(int type) {
	    if (type==m_category) return;
		m_componentChoice[m_category].setVisible(false);
		m_category = type;
		m_componentChoice[m_category].setVisible(true);
		int selectedIndex = m_componentChoice[m_category].getSelectedIndex();
		switch (m_category) {
			case TYPE_BUTTONS:
				m_parentApplication.showPage(selectedIndex);
				break;
			case TYPE_LABELS:
				m_parentApplication.showPage(selectedIndex+6);
				break;
			case TYPE_MENUS:
				m_parentApplication.showPage(selectedIndex+11);
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
		if (e.source == m_categoryChoice) {
			if (e.type == ActionEvent.ITEM_SELECTED) {
				changeCategory(m_categoryChoice.getSelectedIndex());				
			}
		} else if (e.source == m_componentChoice[TYPE_BUTTONS]) {
			if (e.type == ActionEvent.ITEM_SELECTED) {
				m_parentApplication.showPage(m_componentChoice[TYPE_BUTTONS].getSelectedIndex());
			}
		} else if (e.source == m_componentChoice[TYPE_LABELS]) {
			if (e.type == ActionEvent.ITEM_SELECTED) {
				m_parentApplication.showPage(m_componentChoice[TYPE_LABELS].getSelectedIndex()+6);
			}
		} else if (e.source == m_componentChoice[TYPE_MENUS]) {
			if (e.type == ActionEvent.ITEM_SELECTED) {
				m_parentApplication.showPage(m_componentChoice[TYPE_MENUS].getSelectedIndex()+11);
			}
		}
	}
}