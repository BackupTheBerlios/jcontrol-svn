/**
 * A simple rs232-write demo. Press a button on the evalualtion board to
 * generate an output to the RS232-port.
 *
 * @author Remi Seiler
 * @date 19.02.08 15:09
 *
 */

import java.io.IOException;
import jcontrol.comm.RS232;
import jcontrol.io.Display;
import jcontrol.io.Buzzer;
import jcontrol.io.Backlight;
import jcontrol.io.Keyboard;

public class RS232Write {

	private static final int BAUDRATE = 19200;

    public static void main(String[] args) {
        Display lcd = new Display();
        Backlight.setBrightness(Backlight.MAX_BRIGHTNESS);
        Buzzer buzzer = new Buzzer();
        
        // some nice little output on the display
        lcd.drawString("JControl RS232 Write Example", 0,0);                
        
        RS232 rs232;
        try {
            rs232 = new RS232(BAUDRATE); // init RS232 access
        } catch (IOException e) {
            lcd.drawString("RS3232 ERROR!", 0, 12);
            return; // nothing to do because RS232 initialization failed
        }
        
        lcd.drawString("RS232 ready", 0,12);
        // print the current baudrate on the display
        lcd.drawString("Baudrate at ".concat(String.valueOf(BAUDRATE)), 0,20);
        
        lcd.drawString("Press a button...", 0,34);
        
        for(;;) {
        	Keyboard k = new Keyboard();
        	int key = k.getKey();
        	if (key == 'U') {
        		rs232.print("up");
             	buzzer.on(400, 50);
             	buzzer.off();
        	} else if (key == 'D') {
        		rs232.print("down");
             	buzzer.on(600, 50);
             	buzzer.off();
        	} else if (key == 'L') {
        		rs232.print("left");
             	buzzer.on(800, 50);
             	buzzer.off();
        	} else if (key == 'R') {
        		rs232.print("right");
             	buzzer.on(1000, 50);
             	buzzer.off();
        	} else if (key == 'S') {
        		rs232.print("select");
             	buzzer.on(1200, 50);
             	buzzer.off();
        	}
        }
    }
}
