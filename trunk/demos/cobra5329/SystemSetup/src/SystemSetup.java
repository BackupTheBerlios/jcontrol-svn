/*
 * SystemSetup.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */
import jcontrol.ui.wombat.Frame;

/**
 * <p>Demonstrate the usage of the JControl/Wombat GUI and especially 
 * how to build an multi-language system setup menu, including system tests, 
 * touch calibrate, time settings and much more.</p>
 * 
 * Java file created by JControl/IDE
 * 
 * @author timmer
 * @created on 29.11.05 13:37
 */
public class SystemSetup extends Frame {       

	public static final int COMMONPAGE = 0;
	public static final int LANGUAGEPAGE = 1;
	public static final int TIMEPAGE = 2;
	public static final int TOUCHPAGE = 3;	
	public static final int TESTPAGE = 4;
	public static final int RESETPAGE = 5;
	public static final int TOUCHCALIBPAGE = 20;
	public static final int TOUCHTESTPAGE = 21;	
	public static final int DISPLAYTESTPAGE = 22;
	public static final int BUZZERTESTPAGE = 23;
	public static final int LOGFILEPAGE = 24;
	public static final int CONTRASTPAGE = 25;


	
	private int m_currPage = -1;
    private int m_language;

	// Constructor SystemSetup
	public SystemSetup() {
		setOutline(new Outline(this));
		//showPage(TOUCHCALIBPAGE);
		showPage(COMMONPAGE);
		setVisible(true);
	}

	/**
	 * This method is used to perform a page switch by replacing the conent
	 * of this frame.
	 * 
	 * @param page
	 *        The page to show, represented by a final field in this class.
	 */
	public void showPage(int page) {
		if (page==m_currPage) return;
	    ((Outline)getOutline()).setMode(page);
	    setOKButtonText(null);
	    setCancelButtonText(null);
	    showOkButton(false);
	    showCancelButton(false);
	    showPrevButton(false);
	    showNextButton(false);
		if (page == COMMONPAGE) {
			setContent(new CommonPage(this));
			m_currPage = page;
		} else if (page == TOUCHPAGE) {
			setContent(new TouchPage(this));
			m_currPage = page;
		} else if (page == TIMEPAGE) {
			setContent(new TimePage(this));
			m_currPage = page;
		} else if (page == LANGUAGEPAGE) {
			setContent(new LanguagePage(this));
			m_currPage = page;
		} else if (page == TOUCHCALIBPAGE) {
			setContent(new TouchCalibPage(this));
			m_currPage = page;
		} else if (page == CONTRASTPAGE) {
			setContent(new BacklightAndContrastPage(this));
			m_currPage = page;
		} else if (page == TOUCHTESTPAGE) {
			setContent(new TouchTestPage(this, m_currPage));
			m_currPage = page;
		} else if (page == TESTPAGE) {
			setContent(new TestPage(this));
			m_currPage = page;
		} else if (page == DISPLAYTESTPAGE) {
			setContent(new DisplayTestPage(this));
			m_currPage = page;
		} else if (page == BUZZERTESTPAGE) {
			setContent(new BuzzerTestPage(this));
			m_currPage = page;
		} else if (page == RESETPAGE) {
			setContent(new ResetPage(this, m_currPage));
			m_currPage = page;
		} else if (page == LOGFILEPAGE) {
			setContent(new LogFilePage(this, m_currPage));
			m_currPage = page;
		}
	}
    
    public void setLanguage(int language) {
        if (language==m_language) return;
        m_language = language;
        ((Outline)getOutline()).setLanguage(m_language);              
    }
    
    public int getLanguage() {
        return m_language;
    }
	
	public void setOKButtonText(String okText) {
		((Outline)getOutline()).setOKButtonText(okText);
	}
	
	public void setCancelButtonText(String cancelText) {
		((Outline)getOutline()).setCancelButtonText(cancelText);					
	}
	
	public void showOkButton(boolean onoff) {
		((Outline)getOutline()).showOkButton(onoff);
	}
	
	public void showCancelButton(boolean onoff) {
		((Outline)getOutline()).showCancelButton(onoff);
	}
	
	public void showPrevButton(boolean onoff) {
		((Outline)getOutline()).showPrevButton(onoff);		
	}
	
	public void showNextButton(boolean onoff) {		
		((Outline)getOutline()).showNextButton(onoff);		
	}
	
	public void showButtonBar(boolean onoff) {
		((Outline)getOutline()).showButtonBar(onoff);
	}
	
	public void ok() {
		if (getContent() instanceof AbstractSystemSetupPage) {
			((AbstractSystemSetupPage)getContent()).ok();
		}
	}
	
	public void cancel() {
		if (getContent() instanceof AbstractSystemSetupPage) {
			((AbstractSystemSetupPage)getContent()).cancel();
		}
	}
	
	public void prev() {
		if (getContent() instanceof AbstractSystemSetupPage) {
			((AbstractSystemSetupPage)getContent()).showPrev();
		}
	}
	
	public void next() {
		if (getContent() instanceof AbstractSystemSetupPage) {
			((AbstractSystemSetupPage)getContent()).showNext();
		}
	}
	
	public void setTitle(String title) {
		((Outline)getOutline()).setTitle(title);
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *        The main arguments
	 */
	public static void main(String[] args) {
		new SystemSetup();
	}
}