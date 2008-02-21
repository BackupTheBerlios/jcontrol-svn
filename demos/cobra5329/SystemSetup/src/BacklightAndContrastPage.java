import java.io.IOException;

import jcontrol.ui.wombat.Button;
import jcontrol.ui.wombat.Component;
import jcontrol.ui.wombat.Label;
import jcontrol.ui.wombat.Slider;
import jcontrol.ui.wombat.event.ActionEvent;
import jcontrol.ui.wombat.event.ActionListener;

import jcontrol.graphics.XDisplay;
import jcontrol.graphics.XGraphics;
import jcontrol.io.Resource;
import jcontrol.lang.Math;
import jcontrol.system.Management;

public class BacklightAndContrastPage extends AbstractSystemSetupPage implements ActionListener {
	
	// The number of top level components on this page.
	private static final int MAX_CAPACITY = 2;

	private SystemSetup m_parentApplication;
	Slider brightness, contrast;
	Label  brightLab,contrLab;
	Button backButton;
	
	XDisplay d = new XDisplay();
	
	//
    private final String[] BACKBUTTON = {
            "Back & Save",
            "Zurück & Speichern",
    };
	//
    private final String[] CONTRAST = {
            "Contrast: ",
            "Kontrast: ",
    };
	//
    private final String[] BRIGHTNESS = {
            "Brightness: ",
            "Helligkeit: ",
    };

	
	
	public BacklightAndContrastPage(SystemSetup parent) {
		super(MAX_CAPACITY);
		m_parentApplication = parent;
		m_parentApplication.getOutline().setVisible(false);

		try {
			this.setFont(new Resource("SansSerif_14px.jcfd"));
		} catch(IOException ioe) {}

        brightness = new Slider(0,20,25,220,0,XDisplay.MAX_BRIGHTNESS,Slider.ORIENTATION_VERTICAL);
        contrast = new Slider(300,20,25,220,0,XDisplay.MAX_CONTRAST,Slider.ORIENTATION_VERTICAL);

        backButton = new Button( BACKBUTTON[m_parentApplication.getLanguage()], 105, 5, 110, 20 );
        add( backButton );
        backButton.setActionListener( this );
        add( brightness );
        add( contrast );
        add( new ColoredBackground(22,30,260,200,25) );
		
        brightness.setActionListener(this);
        contrast.setActionListener(this);

        brightness.setValue( d.getBrightness() );
        contrast.setValue( d.getContrast() );
        
        brightLab = new Label(getTextBrightness(), 5, 0, 100, 15, Label.STYLE_ALIGN_LEFT);
        contrLab = new Label(getTextContrast(), 215, 0, 100, 15, Label.STYLE_ALIGN_RIGHT);

        add( brightLab );
        add( contrLab );
        
	}
	
	public String getTextBrightness() {
		return BRIGHTNESS[m_parentApplication.getLanguage()].concat( Integer.toHexString( brightness.getValue() ) );
	}

	public String getTextContrast() {
		return CONTRAST[m_parentApplication.getLanguage()].concat( Integer.toHexString(  contrast.getValue() ));
	}

	public void onActionEvent(ActionEvent e) {
		if ( e.source == brightness ) {
			d.setBrightness( brightness.getValue() );
	        brightLab.setText( getTextBrightness() );        
	        brightLab.setRedraw(true);
		} else if ( e.source == contrast ) {
			d.setContrast( contrast.getValue() );
	        contrLab.setText( getTextContrast() );
	        contrLab.setRedraw(true);
		} else if ( e.source == backButton ) {
			Management.setProperty("display.brightness", Integer.toString(brightness.getValue()) );
			Management.setProperty("display.contrast", Integer.toString(contrast.getValue()) );
			Management.saveProperties();
			m_parentApplication.getOutline().setVisible(true);
			m_parentApplication.showPage(SystemSetup.TOUCHPAGE);
	    }
	}

	class ColoredBackground extends Component {

		private int m_x, m_y, m_width, m_height, m_boxsize;
		
		public ColoredBackground(int x, int y, int width, int height, int boxsize) {
			m_x = x;
			m_y = y;
			m_width = width;
			m_height = height;
			m_boxsize = boxsize;
		}
		
		public void paint(XGraphics g) {
		    if ((state & STATE_DIRTY_MASK)==STATE_DIRTY_PAINTALL) {
		    	// do nothing
		    }

		    //g.setBrightness(brightness.getValue());
		    //g.setContrast(contrast.getValue());

		    for ( int x=m_x; x<m_x+m_width; x+=m_boxsize) {
				for ( int y=m_y; y<m_y+m_height; y+=m_boxsize) {
					int color = 0xff<<24 | Math.rnd(0xffffff);
					g.setColor(color);
					g.fillRect(x,y,m_boxsize,m_boxsize);	
				}
			}
	        state &=~ (STATE_DIRTY_MASK|STATE_REVALIDATE);	    
		}
		
	}
	
}

