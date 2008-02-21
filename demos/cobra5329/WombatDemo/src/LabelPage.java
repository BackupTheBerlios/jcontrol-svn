/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 16:30
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;
import jcontrol.ui.wombat.Label;
import jcontrol.graphics.Color;

public class LabelPage extends Container {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 9;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Label m_label1;
	private Label m_label2;
	private Label m_label3;
	private Label m_label4;
	private Label m_label5;
	private Label m_label6;
	private Label m_label7;
	private Label m_label8;

	/**
	 * Constructor LabelPage
	 * 
	 * @param parent
	 */
	public LabelPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("Label", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_label1 = new Label("Left aligned", 160, 25, 150, 16, Label.STYLE_ALIGN_LEFT);
		m_label1.setBackgroundColor(Color.GREEN);
		this.add(m_label1);
		m_label2 = new Label("Centered", 160, 50, 150, 16, Label.STYLE_ALIGN_CENTER);
		m_label2.setForegroundColor(Color.WHITE);
		m_label2.setBackgroundColor(Color.RED);
		this.add(m_label2);
		m_label3 = new Label("Right aligned", 160, 75, 150, 16, Label.STYLE_ALIGN_RIGHT);
		m_label3.setForegroundColor(Color.WHITE);
		m_label3.setBackgroundColor(new Color(0, 128, 192));
		this.add(m_label3);
		m_label4 = new Label("Left aligned, border", 160, 100, 150, 16, Label.STYLE_ALIGN_LEFT|Label.STYLE_SHOW_BORDER);
		m_label4.setForegroundColor(Color.WHITE);
		m_label4.setBackgroundColor(new Color(0, 128, 0));
		this.add(m_label4);
		m_label5 = new Label("Centered, border", 160, 125, 150, 16, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		m_label5.setForegroundColor(Color.WHITE);
		m_label5.setBackgroundColor(new Color(128, 0, 0));
		this.add(m_label5);
		m_label6 = new Label("Right aligned, border", 160, 150, 150, 16, Label.STYLE_ALIGN_RIGHT|Label.STYLE_SHOW_BORDER);
		m_label6.setForegroundColor(Color.WHITE);
		m_label6.setBackgroundColor(new Color(0, 0, 160));
		this.add(m_label6);
		m_label7 = new Label(((String)null), 160, 180, 45, 40, Label.STYLE_ALIGN_CENTER|Label.STYLE_SHOW_BORDER);
		m_label7.setBackgroundColor(Color.WHITE);
		try {
			m_label7.setImage(new Resource("smile.jcif"));
		} catch(IOException ioe) {}
		this.add(m_label7);
		m_label8 = new Label("Images are\nalso possible", 210, 185, 0, 0, 0);
		this.add(m_label8);
	}
}