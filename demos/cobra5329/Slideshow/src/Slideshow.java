/*
 * Slideshow.java
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
import java.io.IOException;

import jcontrol.graphics.XDisplay;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;
import jcontrol.io.Console;

/**
 * <p>Simple shows each 5 seconds another picture. In especially it 
 * demonstrates the usage of image-resources and the display.</p>
 *
 * @author roebbenack
 */
public class Slideshow {

	final public static String PICTURES[] = {
		"dragonfly1.jcif",
		"dragonlady.jcif",
		"spider.jcif",
		"mantis.jcif",
	};
	
	final public static int DELAY = 1000*5; // 5 secs
	
	public static void main(String[] args) {
		
		XDisplay d = new XDisplay();
		
		int index = 0;
		
		do {
			
			try {
				Resource img = new Resource( PICTURES[index] );
				d.drawImage(img, 0, 0);
				//d.setColor(0xffffffff);
				//d.drawString(PICTURES[index], 0, 0);
				try { ThreadExt.sleep(DELAY); } catch (InterruptedException e) {}
			} catch (IOException e) {
				Console.out.println("IOEXception");
			}
			
			index = (index + 1) % PICTURES.length;
			
		} while (true);
		
	}

}
