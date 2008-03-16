/*
 * SetupExplorer.java
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

package jcontrol.system.setup;

import java.io.IOException;

import jcontrol.io.Display;
import jcontrol.io.Keyboard;
import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;
import jcontrol.system.Management;

/**
 * Application chooser.
 * 
 * @version 1.0
 * @author boehme
 */
public class SetupExplorer {

    /**
     * Program entry point.
     * @param args (not used)
     */
    public static void main(String[] args) {
        String file = SetupExplorer.go(new Display(), new Keyboard());
        if(file!=null) {
            Management.start(file,null,true);
        }
    }

    /**
     * This method is run from the main menu.
     * It lists all controllet names from the package jcontrol.demos.games or the name
     * of the main-application specified in the manifest.mf of the second archive.
     * 
     * @param d      the global Display object.
     * @param k      the global Keyboard object.
     * @return The name of the selected demo.
     */
    public static String go(Display d, Keyboard k) {
        d.clearRect(1, 20, 126, 43); // clear background
        
        String[] games = new String[10];
        String[] files = new String[10];
        int maxnumber=0;
        try {
            Resource in = new Resource(Resource.FLASHACCESS);
            Resource old=null;
            maxnumber = 0;
            int archivenum=1;
            while (in!=null) {
                String file = in.getName();
                if((archivenum>1)&&(file.equals("META-INF/MANIFEST.MF"))) {
                    String name=scanManifest(in);
                    if(name!=null) {
                        return name;
                    }
                }
                if (getPackage(file,2).equals("games")) {   // find games with package name "games"
                    String name = getPackage(file,3);
                    if (name.indexOf("$",0)==-1) {
                        if (maxnumber<5) d.drawString(name, 9, 21+8*maxnumber);
                        if (maxnumber+1>games.length) {
                            String[] newfiles = new String[games.length+5];
                            for (int c=0; c<maxnumber; c++) {
                                newfiles[c] = games[c];
                            }
                            games = newfiles;
                        }
                        games[maxnumber] = name;
                        files[maxnumber] = file;
                        maxnumber++;   
                    }
                }
                old=in;
                in=in.next();
                if(in==null) {
                    in=old.nextArchive();
                    archivenum++;
                }
            }
        } catch (IOException e) {}
        if (maxnumber<1) {
            d.drawString("Sorry, no Demo found",26,38);
            try {
                for(int i=0;i<50;i++){
                    ThreadExt.sleep(50);
                    if(k.getKey()!=0) return null;
                }
            } catch (InterruptedException e) {}
            return null;
        }
        d.setDrawMode(Display.XOR);            
        d.fillRect(8,21, 118, 8);
        games[maxnumber] = "Exit";
        files[maxnumber] = null;
        if (maxnumber<=4) {
            d.drawString("Exit", 9, 21+8*maxnumber);
        }
        if (maxnumber>4) {
            d.drawImage(new String[] {"\u040E\u1F0E\u0400"}, 2, 57, 5, 3, 0, 2);
        }
        maxnumber++;
        char key = 0;
        int oldshift = -1;
        int oldselect = 0;
        int shift=0;
        int select=0;
        do {
            if (shift!=oldshift) {
                if(oldshift==-1) oldshift=0;
                for (int c=oldshift; c<(oldshift+5<maxnumber?oldshift+5:maxnumber); c++) {     // draw old game name list
                    d.drawString(games[c], 9, 21+8*(c-oldshift));
                }
                oldselect = select;
                oldshift = shift;               
                for (int c=shift; c<(shift+5<maxnumber?shift+5:maxnumber); c++) {     // draw new game name list
                    d.drawString(games[c], 9, 21+8*(c-shift));
                } 

            }
            if (select!=oldselect) {
                d.fillRect(8,21+8*(oldselect-shift), 118, 8);     // old select bar
                d.fillRect(8,21+8*(select-shift), 118, 8);        // new select bar
                oldselect = select;
            }
            key = k.read();
            switch (key) {
                case 'U':
                case 'u':
                    if (select>0) select--;
                    break;
                case 'D':
                case 'd':
                    if (select<maxnumber-1) select++;
                    break;
            }
            if (select<shift) {
                shift=select;
            } else if (select>shift+4) {
                shift=select-4;
            }
        } while (key!='S');
        d.setDrawMode(Display.NORMAL);
        return files[select];
    }

    /**
     * Returns a substring between slashes number and number+1.
     */
    private static String getPackage(String string, int number) {
        byte[] bytes = string.getBytes();
        int start = 0, count = 0;
        for (int c=0; c<bytes.length; c++) {
            if ((char)bytes[c]=='/') {
                if (count==number) {
                    return new String(bytes,start,c-start);
                } else {
                    start = c+1;
                }
                count++;
            }
        }
        return new String(bytes,start,bytes.length-start);
    }

    /**
     * Searches a given manifest.mf resource for a main class specification.
     * @param manifest resource to scan
     * @return String with name the the main class or null if not found
     */
    private static String scanManifest(Resource manifest){
        try {
            for(;;){
                String line=manifest.readLine();
                if(line.length()==0) return null;
                int i=line.indexOf("Main-Class: ",0);
                if(i==0) return line.substring(12,line.length()).trim();
            }
        } catch (IOException e) {}
        return null;
    }
}
