/**
 * Java file created by JControl/IDE
 * 
 * Created on 28.02.07 16:33
 * 
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Border;

public class BorderPage extends Container {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 4;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Border m_mainBorder;
	private Border m_border1;
	private Border m_border2;
	private Border m_kopieVonBorder2;

	/**
	 * Constructor BorderPage
	 * 
	 * @param parent
	 */
	public BorderPage(WombatDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		m_mainBorder = new Border("Border", 150, 0, 170, 240, Border.STYLE_ETCHED_BORDER);
		this.add(m_mainBorder);
		m_border1 = new Border("Simple Border", 165, 25, 130, 65, Border.STYLE_SIMPLE_BORDER);
		this.add(m_border1);
		m_border2 = new Border("Round Border", 165, 95, 130, 65, Border.STYLE_ROUND_BORDER);
		this.add(m_border2);
		m_kopieVonBorder2 = new Border("Etched Border", 165, 165, 130, 65, Border.STYLE_ETCHED_BORDER);
		this.add(m_kopieVonBorder2);
	}
}