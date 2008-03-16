import jcontrol.io.GPIO;
import jcontrol.io.Display;

/**
 * This example shows how to read out and set GPIO states on JControl. In details, this 
 * example toggles GPIO 0 state and reads GPIO 1, 2 and 3 state each 2 sec and dump the 
 * values on the display. 
 *
 * @author roebbenack
 * @date 29.05.07 11:11
 *
 */
public class GPIOExample {
 
    public GPIOExample() {
        Display d = new Display();
        GPIO.setMode(0, GPIO.PUSHPULL); // outgoing
        //GPIO.setMode(0, GPIO.OPENDRAIN); // outgoing
        //GPIO.setMode(0, GPIO.FLOATING); // outgoing
        GPIO.setMode(1, GPIO.FLOATING); // incoming
        GPIO.setMode(2, GPIO.FLOATING); // incoming
        GPIO.setMode(3, GPIO.FLOATING); // incoming
		boolean state0 = GPIO.HIGH, state1, state2, state3;
        for (;;) {
        	
        	// toggle outgoing
        	d.drawString( "GPIO_0: ".concat(!state0?"true":"false"),0,0);
        	GPIO.setState(0, state0 = !state0); 

			// read incoming
        	state1 = GPIO.getState(1);
        	d.drawString( "GPIO_1: ".concat(state1?"true":"false"),0,10);

			// read incoming
        	state2 = GPIO.getState(2);
        	d.drawString( "GPIO_2: ".concat(state2?"true":"false"),0,20);

			// read incoming
        	state3 = GPIO.getState(3);
        	d.drawString( "GPIO_3: ".concat(state3?"true":"false"),0,30);
        
        	// wait a moment
        	try { jcontrol.lang.ThreadExt.sleep(2000); } catch ( InterruptedException e ) {}
        }
    }
    
    
    public static void main( String args[] ) {
    	new GPIOExample();
    }
    
}
