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
import jcontrol.ui.wombat.Button;
import jcontrol.graphics.Color;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.event.ActionEvent;

public class ButtonPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 7;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Button m_fixedButton;
	private Button m_autoButton;
	private Button m_redButton;
	private Button m_imageButton;
	private Label m_displayLabel;
	private Label m_actionTitle;

	/**
	 * Constructor ButtonPage
	 * 
	 * @param parent
	 */
	public ButtonPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("Button", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_fixedButton = new Button("Button Fixed Size", 175, 30, 125, 35);
		this.add(m_fixedButton);
		m_fixedButton.setActionListener(this);
		m_autoButton = new Button("Button Auto Size", 180, 75, 0, 0);
		this.add(m_autoButton);
		m_autoButton.setActionListener(this);
		m_redButton = new Button("Red Button", 185, 105, 100, 30);
		m_redButton.setTransparentColor(Color.LIGHT_GRAY);
		m_redButton.setBackgroundColor(Color.RED);
		m_redButton.setForegroundColor(Color.WHITE);
		this.add(m_redButton);
		m_redButton.setActionListener(this);
		m_imageButton = new Button("Button with Image", 165, 145, 0, 0);
		try {
			m_imageButton.setImage(new Resource("smile.jcif"));
		} catch(IOException ioe) {}
		this.add(m_imageButton);
		m_imageButton.setActionListener(this);
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
		if (e.source == m_autoButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				m_displayLabel.setText(e.command.concat("\npressed"));				
			} else if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_displayLabel.setText(e.command.concat("\nreleased"));				
			}
		} else if (e.source == m_fixedButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				m_displayLabel.setText(e.command.concat("\npressed"));				
			} else if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_displayLabel.setText(e.command.concat("\nreleased"));				
			}
		} else if (e.source == m_imageButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				m_displayLabel.setText(e.command.concat("\npressed"));				
			} else if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_displayLabel.setText(e.command.concat("\nreleased"));				
			}
		} else if (e.source == m_redButton) {
			if (e.type == ActionEvent.BUTTON_PRESSED) {
				m_displayLabel.setText(e.command.concat("\npressed"));				
			} else if (e.type == ActionEvent.BUTTON_RELEASED) {
				m_displayLabel.setText(e.command.concat("\nreleased"));				
			}
		}
	}
}