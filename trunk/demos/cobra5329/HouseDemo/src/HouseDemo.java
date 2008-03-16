/*
 * HouseDemo.java
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
 * <p>Demonstrate the power of the JControl/Wombat GUI and especially 
 * how easy to programming a complex house control gui. Here you can 
 * see what lights are on, the temperature of rooms and many other options.</p>
 * 
 * @author Marcus Timmermann
 * @created on 24.09.06 11:48
 */
public class HouseDemo extends Frame {
	
    public static final int HOUSE = 0;
    public static final int LOUNGE = 1;
    public static final int LOCKING_PAGE = 2;
    public static final int LIVING_ROOM = 3;
    public static final int WINDOW_PAGE = 4;
    public static final int AC_PAGE = 5;
    public static final int KITCHEN = 6;
    public static final int OVEN_PAGE = 7;
    public static final int BATH = 8;
    public static final int WATER_PAGE = 9;
    public static final int BEDROOM = 10;
    public static final int LIGHT_PAGE = 11;
    public static final int BED_LIGHT = 12;
    
    int m_lastPage = -1;
    
//  public static HS485Protocol hs485Protocol;
//  public static HS485Device hs485device;
    
    public static LightPage lightPage = null;
    public static LockingPage lockingPage = null;

    /**
     * Constructor AskIt
     */
    public HouseDemo() {
        setOutline(new Outline(this));
        showPage(HOUSE);
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
        setContent(null);
        if (page == HOUSE) {
            setContent(new House(this));
        } else if (page == LOUNGE) {
            setContent(new Lounge(this));
        } else if (page == LOCKING_PAGE) {
            setContent(new LockingPage(this));
        } else if (page == LIVING_ROOM) {
            setContent(new LivingRoom(this));
        } else if (page == WINDOW_PAGE) {
            setContent(new WindowPage(this));
        } else if (page == AC_PAGE) {
            setContent(new ACPage(this));
        } else if (page == KITCHEN) {
            setContent(new Kitchen(this));
        } else if (page == OVEN_PAGE) {
            setContent(new OvenPage(this));
        } else if (page == BATH) {
            setContent(new Bath(this));
        } else if (page == WATER_PAGE) {
            setContent(new WaterPage(this));
        } else if (page == BEDROOM) {
            setContent(new Bedroom(this));
        } else if (page == LIGHT_PAGE) {
            setContent(new LightPage(this, m_lastPage));
        } else if (page == BED_LIGHT) {
            setContent(new BedLight(this));
        }
        m_lastPage = page;
    }

    /**
     * The main method.
     * 
     * @param args
     *        The main arguments
     */
    public static void main(String[] args) {
        new HouseDemo();
    }
}