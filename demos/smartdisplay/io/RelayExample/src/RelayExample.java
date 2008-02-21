import jcontrol.io.GPIO;
import jcontrol.lang.ThreadExt;

/**
 * RelayExample shows how to control a hardware relay
 * connected to a GPIO channel.
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2000-2007</p>
 */
public class RelayExample {


    /** GPIO channel the relay is connected to */
    static final int GPIO_RELAY = 2;


    /** init relay control */
    public RelayExample() {
        GPIO.setMode(GPIO_RELAY, GPIO.PUSHPULL);
        release();
    }


    /**
     * Switch relay to released state.
     */
    void release() {
        GPIO.setState(GPIO_RELAY, GPIO.HIGH);
    }


    /**
     * Switch relay to attracted state.
     */
    void attract() {
        GPIO.setState(GPIO_RELAY, GPIO.LOW);
    }


    /**
     * Main method. Init relay and attract it for one second.
     * @param args
     */
    public static void main(String args[]) {

        RelayExample r = new RelayExample();

        // endless loop
        for (;;) {

            // attract relay
            r.attract();

            // sleep one second
            try {
                ThreadExt.sleep(1000);
            } catch (InterruptedException e) {}

            // release relay
            r.release();

            // sleep one second
            try {
                ThreadExt.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
