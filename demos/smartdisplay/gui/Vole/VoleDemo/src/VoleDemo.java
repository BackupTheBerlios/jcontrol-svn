/*
 * VoleDemo.java
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

import jcontrol.io.Resource;
import jcontrol.lang.ThreadExt;
import jcontrol.ui.vole.AnimationContainer;
import jcontrol.ui.vole.Border;
import jcontrol.ui.vole.Button;
import jcontrol.ui.vole.CheckBox;
import jcontrol.ui.vole.ComboBox;
import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.Label;
import jcontrol.ui.vole.List;
import jcontrol.ui.vole.RadioButton;
import jcontrol.ui.vole.Slider;
import jcontrol.ui.vole.TextArea;
import jcontrol.ui.vole.event.ActionEvent;
import jcontrol.ui.vole.event.ActionListener;
import jcontrol.ui.vole.graph.Diagram;
import jcontrol.ui.vole.graph.Histogram;
import jcontrol.ui.vole.meter.AnalogMeter;
import jcontrol.ui.vole.meter.BarMeter;
import jcontrol.ui.vole.meter.DigitalMeter;
import jcontrol.ui.vole.meter.FanMeter;
import jcontrol.ui.vole.meter.Thermometer;


/**
 * <p>This <code>VoleDemo</code> demonstrates all Vole GUI components.
 * Vole is a small but powerful user interface for JControl
 * (<a href="http://www.jcontrol.org">www.jcontrol.org</a>) which
 * targets on 8 and 32 bit embedded systems with very little resources.</p>
 *
 * @author Wolfgang Klingauf, Marcus Timmermann
 */
public class VoleDemo extends Frame implements ActionListener {

  AnalogMeter am;

  // LCDMeter lcdmeter;
  Diagram diagram;
  Histogram histogram;
  Thermometer thermometer;
  List list;
  Label slidelabel;

  // Fan fan1;
  BarMeter barmeter, barmeter2;
  DigitalMeter digitalmeter;

  AnimationContainer downbox;

  ActionListener listener;

  /**
   * Introducing: the constructor.
   */
  public VoleDemo() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    listener = this;

    new VoleDemo$MoreComponents().logo();
    new VoleDemo$Components().combos(this);

    // do some meter value nonsense
    int val = 0, valadd = 1;
    while (true) {
      val += valadd;
      if (val == 20) valadd = -1;
      else if (val == 0) valadd = 1;
      if (am != null)
        am.setValue(val);
      else if (digitalmeter != null)
        digitalmeter.setValue(val);
      else if (diagram!=null)
        diagram.setValue(val);
      else if (histogram!=null)
        histogram.setValue(val);
      else if (thermometer!=null)
        thermometer.setValue(val);
      else {
        if (barmeter!=null)
          barmeter.setValue(val);
        if (barmeter2!=null)
          barmeter2.setValue(20-val);
      }
      try {
        ThreadExt.sleep(100);
      } catch (InterruptedException e) {}
    }
  }



  /**
   * Remove and clear all components from downbox.
   */
  private void clearDownbox() {
    downbox.removeAll();
    am = null;
    diagram = null;
    histogram = null;
    list = null;
    slidelabel = null;
    //fan1= null;
    barmeter = null;
    barmeter2 = null;
    thermometer = null;
    digitalmeter = null;
  }


  /**
   * Event handler
   */
  public void onActionEvent(ActionEvent e) {

    if (e.getType() == ActionEvent.ITEM_SELECTED) {
      if (e.getActionCommand() != null) {

        // analog meter
        if (e.getActionCommand().equals("AnalogMeter")) {
          clearDownbox();
          new VoleDemo$Components().analogmeter();
        }

        // text area
        else if (e.getActionCommand().equals("TextArea")) {
          clearDownbox();
          new VoleDemo$Components().textarea();
        }

        else if (e.getActionCommand().equals("Slider")) {
          clearDownbox();
          new VoleDemo$Components().slider();
        }

        else if (e.getActionCommand().equals("Diagram")) {
          clearDownbox();
          new VoleDemo$Components().diagram();
        }

        else if (e.getActionCommand().equals("Histogram")) {
          clearDownbox();
          new VoleDemo$Components().histogram();
        }

        else if (e.getActionCommand().equals("ComboBox")) {
          clearDownbox();
          new VoleDemo$Components().combobox();
        }

        else if (e.getActionCommand().equals("List")) {
          clearDownbox();
          new VoleDemo$MoreComponents().list();
        }

        else if (e.getActionCommand().equals("Buttons")) {
          clearDownbox();
          new VoleDemo$Components().buttons();
        }

        else if (e.getActionCommand().equals("CheckBox")) {
          clearDownbox();
          new VoleDemo$MoreComponents().checkbox();
        }

        else if (e.getActionCommand().equals("RadioButton")) {
          clearDownbox();
          new VoleDemo$MoreComponents().radiobutton();
        }

        else if (e.getActionCommand().equals("Thermometer")) {
          clearDownbox();
          new VoleDemo$MoreComponents().thermometer();
        }

        else if (e.getActionCommand().equals("FanMeter")) {
          clearDownbox();
          new VoleDemo$MoreComponents().fan();
        }

        else if (e.getActionCommand().equals("BarMeter")) {
          clearDownbox();
          new VoleDemo$MoreComponents().barmeter();
        }

        else if (e.getActionCommand().equals("DigitalMeter")) {
          clearDownbox();
          new VoleDemo$MoreComponents().digitalmeter();
        }

        else if (e.getSource() == list) {
          downbox.removeAll();
          downbox.add(new Label("Good choice! We are now cooking", 0,35,128,10,Label.ALIGN_CENTER));
          downbox.add(new Label(e.getActionCommand(),0,45,128,10,Label.ALIGN_CENTER));
          downbox.add(new Label("for you.",0,55,128,8,Label.ALIGN_CENTER));
          downbox.add(new Border(0,43,128,10));
        }
      }
    }
    // Slider
  }


  /**
   * The main method.
   */
  public static void main(String args[]) {
    new VoleDemo();
  }


  /////////////////////////////////////////////////////////////////////////////

  /**
   * Inner class with methods to instantiate several Vole GUI components.
   */
  class VoleDemo$Components {

    /**
     * Create the main combo boxes.
     */
    void combos(Frame parent) {
      ComboBox combo1 = new ComboBox();
      combo1.setBounds(0,0,60,10);
      combo1.add("Buttons");
      combo1.add("Slider");
      combo1.add("CheckBox");
      combo1.add("RadioButton");
      combo1.setActionListener(listener);

      ComboBox combo2 = new ComboBox();
      combo2.setBounds(68,0,60,10);
      combo2.add("TextArea");
      combo2.add("List");
      combo2.add("ComboBox");
      combo2.setActionListener(listener);

      ComboBox combo3 = new ComboBox();
      combo3.setBounds(0,13,60,10);
      combo3.add("AnalogMeter");
      combo3.add("DigitalMeter");
      combo3.add("BarMeter");
      combo3.add("Thermometer");
      combo3.add("FanMeter");
      combo3.setActionListener(listener);

      ComboBox combo4 = new ComboBox();
      combo4.setBounds(68,13,60,10);
      combo4.add("Diagram");
      combo4.add("Histogram");
      combo4.setActionListener(listener);

      parent.add(combo1);
      parent.add(combo2);
      parent.add(combo3);
      parent.add(combo4);

      downbox = new AnimationContainer();
      downbox.setBounds(0, 32, 128, 32);

      downbox.add(new Label("Welcome to the Vole Demo!",0,40,128,10,Label.ALIGN_CENTER));
      downbox.add(new Label("Make your choice.",0,50,128,10,Label.ALIGN_CENTER));

      parent.add(downbox);

      // transfer input focus to combo1
      combo1.requestFocus();
    }


    /**
     * Make some buttons.
     */
    void buttons() {
      Button b1 = new Button("This", 3,32,30,12);
      Button b2 = new Button("is", 35,32,20,12);
      Button b3 = new Button("the", 57,32,20,12);
      Button b4 = new Button("Button", 79,32,40,12);
      Button b5 = new Button("parade!", 43,47,40,12);

      downbox.add(b1);
      downbox.add(b2);
      downbox.add(b3);
      downbox.add(b4);
      downbox.add(b5);
    }


    /**
     * Add an AnalogMeter to the frame
     */
    void analogmeter() {
      am = new AnalogMeter(14,32,100,30,0,20,180,AnalogMeter.ORIENTATION_CENTER,10);
      am.setCaption("0","20");
      downbox.add(am);
    }

    /**
     * Test the TextArea and ScrollBar
     */
    void textarea() {
      TextArea ta = new TextArea(0,32,128,30,true);
      ta.add("Vole GUI Demonstration");
      ta.add("Based on JControl embedded");
      ta.add("Java technology. Use");
      ta.add("the up- und down-keys");
      ta.add("to scroll the text.");
      ta.setScrollPos(0);
      downbox.add(ta);
    }


    /**
     * Draw a user controllable slider
     */
    void slider() {
      Slider sl = new Slider(0,40,62,0,10,1);
      downbox.add(sl);
      sl.setActionListener(listener);
      sl.requestFocus();

      Label l = new Label("<- Slide me!", 64, 41, 63, 8, Label.ALIGN_CENTER);
      downbox.add(l);

      slidelabel = new Label("",0,53,128,10, Label.ALIGN_CENTER);
      downbox.add(slidelabel);
    }

    /**
     * Draw a diagram component
     */
    void diagram() {
      diagram = new Diagram(0,32,126,30, 0, 20, 30);
      diagram.setCaption("0", "20", Diagram.ALIGN_RIGHT);
      downbox.add(diagram);
    }

    /**
     * Draw a histogram.
     */
    void histogram() {
      histogram = new Histogram(0,32,126,30, 0, 20, 30);
      histogram.setCaption("0", "20", Histogram.ALIGN_LEFT);
      downbox.add(histogram);
    }


    /**
     * Create a combo box with multiple choices.
     */
    void combobox() {
      ComboBox cb = new ComboBox(0,31);
      cb.add("Red");
      cb.add("Green");
      cb.add("Blue");

      ComboBox cb2 = new ComboBox(37,31);
      cb2.add("Vole");
      cb2.add("Tapir");
      cb2.add("Wombat");

      ComboBox cb3 = new ComboBox(82,31);
      cb3.add("Lights on");
      cb3.add("Lights off");
      cb3.setActionListener(listener);

      downbox.add(cb);
      downbox.add(cb2);
      downbox.add(cb3);
    }


  }

  /**
   * F*ck constant pool entries.
   */
  class VoleDemo$MoreComponents {

    /**
     * Do the Vole logo
     */
    void logo() {
      Label l, l2;

      try {
        l = new Label(new Resource("vole.jcif"),0,0);
        add(l);
        show();
        ThreadExt.sleep(150);
        l2 = new Label(new Resource("vole2.jcif"),104,37);
        add(l2);
        ThreadExt.sleep(150);
        l2.setLabel(new Resource("vole3.jcif"), false);
        ThreadExt.sleep(150);
        l2.setLabel(new Resource("vole2.jcif"), false);
        ThreadExt.sleep(150);
        l.setLabel(new Resource("vole.jcif"), false);
        ThreadExt.sleep(150);
      } catch (Exception e) {}

      removeAll();
    }


    /**
     * Create a list with multiple entries.
     */
    void list() {
      String items[] = new String[] {
        "Pizza el diavolo",
        "Brokkolir�stling",
        "Dumpfbacken an Rotweinso�e",
        "Schnupfnudeln a la nas ivin",
        "Lasagne vuoto",
        "Rhabarberkompott mit Oliven�l",
        "F�nf chicken nuggets mit Dip"
      };

      list = new List(items, 0,32,128,32,true);
      list.setActionListener(listener);
      downbox.add(list);
      list.requestFocus();
    }

    /**
     * Hey, it's a check box!
     */
    void checkbox() {
      CheckBox cb = new CheckBox("Check me!", 10,32);
      CheckBox cb2 = new CheckBox("No, check me!", 10,42);
      CheckBox cb3 = new CheckBox("I don't need no check. Yeah.", 10, 52);
      downbox.add(cb);
      downbox.add(cb2);
      downbox.add(cb3);
    }

    /**
     * Radio buttons.
     */
    void radiobutton() {
      RadioButton rb = new RadioButton("FFN - Easy & boring", 15, 32);
      RadioButton rb2 = new RadioButton("NDR2 - For lowbrows", 15, 42);
      RadioButton rb3 = new RadioButton("NJOY - Do you bear it?", 15, 52);
      downbox.add(rb);
      downbox.add(rb2);
      downbox.add(rb3);
    }

    /**
     * Very hot thermometer.
     */
    void thermometer() {
        thermometer = new Thermometer(20, 32, 40, 32, 0, 20);
//        thermometer.setBorder(true);
        thermometer.setCaption("0", "+20");
        thermometer.setNumericDisplay(2, 0, "�C");
        downbox.add(thermometer);
    }

    /**
     * Fan is an Animateable component.
     */
    void fan() {
      FanMeter fan1 = new FanMeter(0, 32);
      fan1.setCaption("Fan1");
      fan1.setNumericDisplay(4, 0, "RPM");
      fan1.setValue(1000);
      downbox.add(fan1);
      FanMeter fan2 = new FanMeter(64,32);
      fan2.setCaption("Fan2");
      fan2.setNumericDisplay(4, 0, "RPM");
      fan2.setValue(100);
      downbox.add(fan2);
    }


    /**
     * Two bar meters with different drawing styles.
     */
    void barmeter() {
      barmeter = new BarMeter(20,25,20,30,0,20,BarMeter.ORIENTATION_VERTICAL,BarMeter.FILL_SOLID);
      barmeter2 = new BarMeter(68,35,60,15,0,20,BarMeter.ORIENTATION_HORIZONTAL,BarMeter.FILL_LINE);
      downbox.add(barmeter);
      downbox.add(new Label("Vertical, solid", 0,56,60,8,Label.ALIGN_CENTER));
      downbox.add(barmeter2);
      downbox.add(new Label("Horizontal, lined",68,56,60,8,Label.ALIGN_CENTER));
    }


    /**
     * Draw a digital meter.
     */
    void digitalmeter() {
      digitalmeter = new DigitalMeter(0, 30, 3, 1);
      try {
        digitalmeter.setFont(new Resource("arial24.jcfd"));
      } catch (IOException e) {}
      downbox.add(digitalmeter);
    }
  }
}
