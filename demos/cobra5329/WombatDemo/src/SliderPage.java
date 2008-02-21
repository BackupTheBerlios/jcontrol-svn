/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 12:12
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Slider;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.ActionEvent;

public class SliderPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 5;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Slider m_verticalSlider;
	private Label m_verticalLabel;
	private Slider m_horizontalSlider;
	private Label m_horizontalLabel;

	/**
	 * Constructor SliderPage
	 * 
	 * @param parent
	 */
	public SliderPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("Slider", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_verticalSlider = new Slider(230, 75, 18, 120, 0, 100, Slider.ORIENTATION_VERTICAL);
		this.add(m_verticalSlider);
		m_verticalSlider.setActionListener(this);
		m_verticalLabel = new Label("0", 222, 200, 35, 14, Label.STYLE_ALIGN_CENTER);
		this.add(m_verticalLabel);
		m_horizontalSlider = new Slider(160, 40, 115, 18, 0, 100, Slider.ORIENTATION_HORIZONTAL);
		this.add(m_horizontalSlider);
		m_horizontalSlider.setActionListener(this);
		m_horizontalLabel = new Label("0", 275, 40, 35, 18, Label.STYLE_ALIGN_RIGHT);
		this.add(m_horizontalLabel);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 *        The event that has been fired.
	 */
	public void onActionEvent(ActionEvent e) {
		if (e.source == m_horizontalSlider) {
			if (e.type == ActionEvent.VALUE_CHANGED) {
				m_horizontalLabel.setText(String.valueOf(m_horizontalSlider.getValue()));				
			}
		} else if (e.source == m_verticalSlider) {
			if (e.type == ActionEvent.VALUE_CHANGED) {
				m_verticalLabel.setText(String.valueOf(m_verticalSlider.getValue()));				
			}
		}
	}
}