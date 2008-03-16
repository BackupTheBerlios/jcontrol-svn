/**
 * Java file created by JControl/IDE
 * 
 * Created on 25.04.07 16:02
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.event.ActionListener;
import jcontrol.graphics.Color;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.NumberChooser;
import jcontrol.ui.wombat.ComboBox;
import jcontrol.ui.wombat.ToggleSwitch;
import jcontrol.ui.wombat.Slider;
import jcontrol.ui.wombat.event.ActionEvent;

public class MainPage extends Container implements ActionListener {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 22;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Button m_button1;
	private Button m_button2;
	private Button m_button3;
	private Button m_button4;
	private Button m_button5;
	private Border m_border1;
	private Border m_border2;
	private Label m_label1;
	private Label m_label2;
	private Label m_label3;
	private Label m_label4;
	private Label m_label5;
	private NumberChooser m_numberChooser;
	private ComboBox m_comboBox1;
	private ComboBox m_comboBox2;
	private ComboBox m_comboBox3;
	private ToggleSwitch m_toggleSwitch1;
	private ToggleSwitch m_toggleSwitch2;
	private Slider m_slider1;
	private Slider m_slider2;
	private Label m_engine1;
	private Label m_engine2;

	/**
	 * Constructor MainPage
	 * 
	 * @param parent
	 */
	public MainPage(MachineGui parent) {
		super(MAX_CAPACITY);
		this.setBackgroundColor(Color.BLACK);
		try {
			this.setFont(new Resource("Arial8.jcfd"));
		} catch(IOException ioe) {}
		this.setForegroundColor(Color.WHITE);
		m_button1 = new Button("Status", 0, 0, 100, 40);
		m_button1.setTransparentColor(Color.BLACK);
		m_button1.setBackgroundColor(Color.LIGHT_GRAY);
		m_button1.setForegroundColor(Color.BLACK);
		this.add(m_button1);
		m_button1.setActionListener(this);
		m_button2 = new Button("Info", 0, 50, 100, 40);
		m_button2.setTransparentColor(Color.BLACK);
		m_button2.setBackgroundColor(Color.LIGHT_GRAY);
		m_button2.setForegroundColor(Color.BLACK);
		this.add(m_button2);
		m_button2.setActionListener(this);
		m_button3 = new Button("Main", 0, 100, 100, 40);
		m_button3.setTransparentColor(Color.BLACK);
		m_button3.setBackgroundColor(Color.LIGHT_GRAY);
		m_button3.setForegroundColor(Color.BLACK);
		this.add(m_button3);
		m_button3.setActionListener(this);
		m_button4 = new Button("Home", 0, 150, 100, 40);
		m_button4.setTransparentColor(Color.BLACK);
		m_button4.setBackgroundColor(Color.LIGHT_GRAY);
		m_button4.setForegroundColor(Color.BLACK);
		this.add(m_button4);
		m_button4.setActionListener(this);
		m_button5 = new Button("Close", 0, 200, 100, 40);
		m_button5.setTransparentColor(Color.BLACK);
		m_button5.setBackgroundColor(Color.LIGHT_GRAY);
		m_button5.setForegroundColor(Color.BLACK);
		this.add(m_button5);
		m_button5.setActionListener(this);
		m_border1 = new Border("Status", 110, 0, 210, 120, Border.STYLE_ETCHED_BORDER);
		this.add(m_border1);
		m_border2 = new Border("Control", 110, 120, 210, 120, Border.STYLE_ETCHED_BORDER);
		this.add(m_border2);
		m_label1 = new Label("Temperature", 130, 30, 0, 0, Label.STYLE_ALIGN_LEFT);
		m_label1.setForegroundColor(Color.RED);
		this.add(m_label1);
		m_label2 = new Label("Pressure 1", 130, 50, 0, 0, Label.STYLE_ALIGN_LEFT);
		m_label2.setForegroundColor(Color.GREEN);
		this.add(m_label2);
		m_label3 = new Label("Pressure 2", 130, 70, 0, 0, Label.STYLE_ALIGN_LEFT);
		m_label3.setForegroundColor(Color.GREEN);
		this.add(m_label3);
		m_label4 = new Label("Rotation Speed", 130, 90, 0, 0, Label.STYLE_ALIGN_LEFT);
		m_label4.setForegroundColor(Color.GREEN);
		this.add(m_label4);
		m_label5 = new Label("\u00b0C", 260, 30, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_label5);
		m_numberChooser = new NumberChooser(220, 30, 0, 999);
		m_numberChooser.setValue(82);
		m_numberChooser.setBackgroundColor(Color.LIGHT_GRAY);
		m_numberChooser.setTransparentColor(Color.BLACK);
		m_numberChooser.setForegroundColor(Color.BLACK);
		this.add(m_numberChooser);
		m_numberChooser.setActionListener(this);
		m_comboBox1 = new ComboBox(new String[]{"2000 bar"}, 220, 50, 80);
		m_comboBox1.setTransparentColor(Color.BLACK);
		m_comboBox1.setForegroundColor(Color.BLACK);
		m_comboBox1.setBackgroundColor(Color.LIGHT_GRAY);
		this.add(m_comboBox1);
		m_comboBox1.setActionListener(this);
		m_comboBox2 = new ComboBox(new String[]{"130 bar"}, 220, 70, 80);
		m_comboBox2.setTransparentColor(Color.BLACK);
		m_comboBox2.setForegroundColor(Color.BLACK);
		m_comboBox2.setBackgroundColor(Color.LIGHT_GRAY);
		this.add(m_comboBox2);
		m_comboBox2.setActionListener(this);
		m_comboBox3 = new ComboBox(new String[]{"100 rpm"}, 220, 90, 80);
		m_comboBox3.setTransparentColor(Color.BLACK);
		m_comboBox3.setForegroundColor(Color.BLACK);
		m_comboBox3.setBackgroundColor(Color.LIGHT_GRAY);
		this.add(m_comboBox3);
		m_comboBox3.setActionListener(this);
		m_toggleSwitch1 = new ToggleSwitch(126, 160);
		m_toggleSwitch1.setText("On", "Off");
		this.add(m_toggleSwitch1);
		m_toggleSwitch1.setActionListener(this);
		m_toggleSwitch2 = new ToggleSwitch(236, 160);
		m_toggleSwitch2.setSelected(true);
		m_toggleSwitch2.setText("On", "Off");
		this.add(m_toggleSwitch2);
		m_toggleSwitch2.setActionListener(this);
		m_slider1 = new Slider(180, 140, 18, 80, 0, 100, Slider.ORIENTATION_VERTICAL);
		m_slider1.setValue(100);
		m_slider1.setTransparentColor(Color.BLACK);
		m_slider1.setBackgroundColor(Color.GREEN);
		this.add(m_slider1);
		m_slider1.setActionListener(this);
		m_slider2 = new Slider(290, 140, 18, 80, 0, 100, Slider.ORIENTATION_VERTICAL);
		m_slider2.setValue(20);
		m_slider2.setTransparentColor(Color.BLACK);
		m_slider2.setBackgroundColor(Color.RED);
		this.add(m_slider2);
		m_slider2.setActionListener(this);
		m_engine1 = new Label("Engine 1", 120, 140, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_engine1);
		m_engine2 = new Label("Engine 2", 230, 140, 0, 0, Label.STYLE_ALIGN_LEFT);
		this.add(m_engine2);
	}

	/**
	 * This method is called every time any component on this page fires an
	 * action event.
	 * 
	 * @param e
	 */
	public void onActionEvent(ActionEvent e) {}
}