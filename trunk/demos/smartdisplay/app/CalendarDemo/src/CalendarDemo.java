/*
 * CalendarDemo.java
 * Copyright (C) 2000-2008 DOMOLOGIC Home Automation GmbH
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

import jcontrol.lang.ThreadExt;
import jcontrol.system.RTC;
import jcontrol.system.Time;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Button;
import jcontrol.ui.vole.Container;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.NumberChooser;
import jcontrol.ui.vole.event.ActionEvent;
import jcontrol.ui.vole.meter.AnalogClock;

/**
 * The CalendarDemo displays the current date and time. The user can adjust both
 * in an additional screen displayed on pressing a button.
 * 
 * @author Marcus Timmermann, mgn
 * @version $Revision$
 */
public class CalendarDemo extends Frame {

   /**
    * This Thread continously updates the analog clock and the date labels.
    */
   class TimeRequester extends Thread {

      public void run() {

         timeRequesterReady = false; // flag to indicate if the thread is
         // running
         for (; !modify;) {
            Time t = new Time(); // the current time

            // update labels if necessary
            if (years != t.year) {
               years = t.year;
               year.setLabel(String.valueOf(years), false);
            }
            if (months != t.month) {
               months = t.month;
               month.setLabel((months < 10 ? "0" : "").concat(
                  String.valueOf(months)).concat("."), false);
            }
            if (days != t.day) {
               days = t.day;
               day.setLabel((days < 10 ? "0" : "").concat(String.valueOf(days))
                  .concat("."), false);
            }
            // update clock
            clock.setValue(t.hour, t.minute, t.second);
            if (!modify) {
               try {
                  ThreadExt.sleep(200);
               } catch (InterruptedException e) {
               }
            }
         }
         timeRequesterReady = true; // thread runs out
      }
   }

   /**
    * The main method.
    * 
    * @param args leave this empty
    */
   public static void main(String[] args) {

      new CalendarDemo();
   }

   /**
    * press this button if you want to set the time
    */
   Button changeButton;

   /**
    * Th clock
    */
   AnalogClock clock;

   /**
    * Label for the day
    */
   Label day;

   /**
    * NumberChoosers to modify day
    */
   NumberChooser dayChange;

   /**
    * NumberChoosers to modify hour
    */
   NumberChooser hourChange;

   /**
    * the main container
    */
   Container mainContainer;

   /**
    * NumberChoosers to modify minute
    */
   NumberChooser minuteChange;

   /**
    * flag that indicates the current mode (time modify mode or normal mode)
    */
   boolean modify;

   /**
    * Label for the month
    */
   Label month;

   /**
    * NumberChoosers to modify month
    */
   NumberChooser monthChange;

   /**
    * keep old date values to verify if the labels
    */
   int months, years, days;

   /**
    * buttons to confirm or cancel the time
    */
   Button okButton, cancelButton;

   /**
    * NumberChoosers to modify second
    */
   NumberChooser secondChange;

   /**
    * time update thread
    */
   TimeRequester timeRequester;

   /**
    * running flag for the time requester
    */
   private boolean timeRequesterReady;

   /**
    * Label for the year
    */
   Label year;

   /**
    * NumberChoosers to modify year
    */
   NumberChooser yearChange;

   /**
    * Constructor for CalendarDemo.
    */
   public CalendarDemo() {

      // lights on!
      jcontrol.io.Backlight.setBrightness(jcontrol.io.Backlight.MAX_BRIGHTNESS);

      mainContainer = new Container();
      mainContainer.setBounds(0, 0, 128, 64);
      this.add(mainContainer);
      mainMenu();
      setVisible(true);
   }

   /**
    * The main menu with date labels and analog clock.
    */
   void mainMenu() {

      modify = false;
      mainContainer.removeAll(); // remove all components
      okButton = null; // delete all components for the garbage collector
      cancelButton = null;
      dayChange = null;
      monthChange = null;
      yearChange = null;
      hourChange = null;
      minuteChange = null;
      secondChange = null;
      if (graphics != null)
         graphics.clearRect(0, 0, 128, 64); // clear screen
      Border b1 = new Border("Datum", 0, 0, 53, 34); // create borders
      mainContainer.add(b1);
      Border b2 = new Border("Uhrzeit", 54, 0, 74, 64);
      mainContainer.add(b2);

      changeButton = new Button("stellen", 2, 51, 40, 11); // the set time
      // button
      mainContainer.add(changeButton);
      changeButton.setActionListener(this);

      clock = new AnalogClock(63, 5, 28, true); // analog clock
      mainContainer.add(clock);

      { // initialize time values
         Time t = new Time();
         years = t.year;
         months = t.month;
         days = t.day;
         clock.setValue(t.hour, t.minute, t.second); // initially draw the
         // clock
      }

      // create date labels
      day =
            new Label((days < 10 ? "0" : "").concat(String.valueOf(days))
               .concat("."), 2, 15);
      month =
            new Label((months < 10 ? "0" : "").concat(String.valueOf(months))
               .concat("."), 15, 15);
      year = new Label(String.valueOf(years), 28, 15);
      // add date labels
      mainContainer.add(day);
      mainContainer.add(month);
      mainContainer.add(year);

      changeButton.requestFocus();

      // start time requester thread
      timeRequester = new TimeRequester();
      timeRequester.start();

   }

   /**
    * The time modify page.
    */
   void modifyMenu() {

      modify = true; // stop the time request thread
      if (timeRequester != null) {
         while (!timeRequesterReady) {
            try {
               ThreadExt.sleep(100);
            } catch (InterruptedException ex) {
            }
         }
         timeRequester = null;
      }
      mainContainer.removeAll(); // remove all components
      graphics.clearRect(0, 0, 128, 64); // clear screen
      day = null; // delete all components for the garbage collector
      month = null;
      year = null;
      clock = null;
      changeButton = null;
      mainContainer.add(new Border("Datum/Uhrzeit stellen", 0, 0, 128, 64)); // border
      okButton = new Button("Ok", 2, 51, 40, 11); // ok button
      okButton.setActionListener(this);
      mainContainer.add(okButton);

      cancelButton = new Button("Abbrechen", 44, 51, 40, 11); // cancel button
      cancelButton.setActionListener(this);
      mainContainer.add(cancelButton);

      // initialize all number choosers with the current time
      Time t = new Time();
      dayChange = new NumberChooser(10, 10, 1, 31); // day
      dayChange.setValue(t.day);
      mainContainer.add(dayChange);
      dayChange.setActionListener(this);

      monthChange = new NumberChooser(40, 10, 1, 12); // month
      monthChange.setValue(t.month);
      mainContainer.add(monthChange);
      monthChange.setActionListener(this);

      yearChange = new NumberChooser(70, 10, 2003, 2099); // year
      yearChange.setValue(t.year);
      mainContainer.add(yearChange);
      yearChange.setActionListener(this);

      hourChange = new NumberChooser(10, 25, 0, 23); // hours
      hourChange.setValue(t.hour);
      mainContainer.add(hourChange);
      hourChange.setActionListener(this);

      minuteChange = new NumberChooser(40, 25, 0, 59); // minutes
      minuteChange.setValue(t.minute);
      mainContainer.add(minuteChange);
      minuteChange.setActionListener(this);

      secondChange = new NumberChooser(70, 25, 0, 59); // seconds
      secondChange.setValue(t.second);
      mainContainer.add(secondChange);
      secondChange.setActionListener(this);

      okButton.requestFocus();
   }

   /**
    * Overwritten ActionEvent event handler.
    * 
    * @param e the ActionEvent
    * 
    * @see jcontrol.ui.vole.Component.onActionEvent(ActionEvent e)
    */
   public void onActionEvent(ActionEvent e) {

      if (e.getType() == ActionEvent.BUTTON_PRESSED) {
         // check which button was pressed
         if (e.getSource() == changeButton) {
            modifyMenu(); // open the time modify page
         } else if (e.getSource() == okButton) { // confirm time settings
            // changes
            RTC.setTime(new Time(yearChange.getValue(), monthChange.getValue(),
               dayChange.getValue(), 0, hourChange.getValue(), minuteChange
                  .getValue(), secondChange.getValue()));
            mainMenu(); // go back to main menu
         } else if (e.getSource() == cancelButton) {
            mainMenu(); // go back to main menu without changes
         }
      }
   }
}
