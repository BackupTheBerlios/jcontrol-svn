/**
 * Java file created by JControl/IDE
 * 
 * Created on 24.09.06 11:13
 * 
 * @author Marcus Timmermann
 */

import jcontrol.ui.wombat.Container;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Label;

public class Outline extends Container {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 1;
	/* 
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private Label m_logo;

	/**
	 * Constructor Outline
	 * 
	 * @param parent
	 */
	public Outline(HouseDemo parent) {
		super(MAX_CAPACITY);
		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		this.add(m_logo);
	}
}