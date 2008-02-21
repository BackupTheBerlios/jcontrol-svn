
/**
 * Java file created by JControl/IDE
 *
 * Created on 01.12.05 11:57
 *
 * @author timmer
 */

import jcontrol.ui.wombat.Container;
import jcontrol.io.Resource;
import java.io.IOException;
import jcontrol.ui.wombat.Label;

public class CommonPage extends AbstractSystemSetupPage {
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 3;
	/**
	 * A handle to the main application. This is needed to keep a backwards
	 * reference to perform page-switches etc.
	 */
	private SystemSetup m_parentApplication;
	private Container m_descriptions;
	private Container m_values;
	private Label m_logo;
	private Label m_product;
	private Label m_serial;
	private Label m_built;
	private Label m_manufacturer;
	private Label m_productValue;
	private Label m_serialValue;
	private Label m_builtValue;
	private Label m_manufacturerValue;    
    
    private final String[] HEADLINE = {
            "Common",
            "Allgemeines",
    };    
    private final String[] DEVICE = {
            "Device",
            "Gerät",
    };    
    private final String[] SERIAL = {
            "Serial number",
            "Seriennummer",
    };    
    private final String[] BUILD = {
            "Build date",
            "Datum",
    };
    private final String[] MANUFACTURER = {
            "Manufacturer",
            "Hersteller",
    };

	/**
	 * Constructor CommonPage
	 *
	 * @param parent
	 */
	public CommonPage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
        int language = m_parentApplication.getLanguage();
        m_parentApplication.setTitle(HEADLINE[language]);
		try {
			this.setFont(new Resource("SansSerif_14px_plain.jcfd"));
		} catch(IOException ioe) {}
		m_descriptions = new Container(4);
		try {
			m_descriptions.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}
		this.add(m_descriptions);
		m_product = new Label(DEVICE[language], 120, 50, 86, 14, Label.STYLE_ALIGN_LEFT);
		m_descriptions.add(m_product);
		m_serial = new Label(SERIAL[language], 120, 80, 86, 14, Label.STYLE_ALIGN_LEFT);
		m_descriptions.add(m_serial);
		m_built = new Label(BUILD[language], 120, 110, 86, 14, Label.STYLE_ALIGN_LEFT);
		m_descriptions.add(m_built);
		m_manufacturer = new Label(MANUFACTURER[language], 120, 140, 86, 14, Label.STYLE_ALIGN_LEFT);
		m_descriptions.add(m_manufacturer);
		m_values = new Container(4);
		this.add(m_values);
		m_productValue = new Label("Cobra5329", 210, 50, 100, 14, Label.STYLE_ALIGN_LEFT);
		m_values.add(m_productValue);
		m_serialValue = new Label("#373337-4432-2", 210, 80, 100, 14, Label.STYLE_ALIGN_LEFT);
		m_values.add(m_serialValue);
		m_builtValue = new Label("2007-Feb-08", 210, 110, 100, 14, Label.STYLE_ALIGN_LEFT);
		m_values.add(m_builtValue);
		m_manufacturerValue = new Label("JControl", 210, 140, 100, 14, Label.STYLE_ALIGN_LEFT);
		m_values.add(m_manufacturerValue);
		m_logo = new Label(((String)null), 170, 190, 0, 0, Label.STYLE_ALIGN_LEFT);
		try {
			m_logo.setImage(new Resource("jcontrol_logo.jcif"));
		} catch(IOException ioe) {}
		this.add(m_logo);      
	}   
    
}