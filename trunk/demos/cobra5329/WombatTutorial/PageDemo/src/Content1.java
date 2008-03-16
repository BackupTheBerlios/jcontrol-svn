/**
 * Java file created by JControl/IDE
 * 
 * Created on 01.03.07 15:48
 * 
 */
import jcontrol.ui.wombat.Container;
import jcontrol.ui.wombat.Label;
import jcontrol.graphics.Color;

public class Content1 extends Container {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 1;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Label m_title;

	/**
	 * Constructor Content1
	 * 
	 * @param parent the main class
	 */
	public Content1(PageDemo parent) {
		super(MAX_CAPACITY);
		m_title = new Label("Page 1", 0, 0, 320, 20, Label.STYLE_ALIGN_LEFT|Label.STYLE_SHOW_BORDER);
		m_title.setForegroundColor(Color.WHITE);
		m_title.setBackgroundColor(new Color(0, 0, 128));
		this.add(m_title);
	}
}