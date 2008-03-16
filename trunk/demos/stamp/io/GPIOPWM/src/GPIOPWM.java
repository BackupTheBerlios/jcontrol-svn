/*
 * GPIOPWM.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This	library	is free	software; you can redistribute it and/or
 * modify it under the terms of	the	GNU	Lesser General Public
 * License as published	by the Free	Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This	library	is distributed in the hope that	it will	be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A	PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received	a copy of the GNU Lesser General Public
 * License along with this library;	if not,	write to the Free Software
 * Foundation, Inc., 51	Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */
import jcontrol.io.GPIO;
import jcontrol.io.PWM;
import jcontrol.lang.ThreadExt;

/**
 * GPIO	and	PWM	demo. The GPIOs 4..11 can be used to set a binary value,
 * that will control the duty cycle of the PWM channel.
 */
public class GPIOPWM {

	/**	PWM	frequency */
	static final int PWM_FREQUENCY = 2000; // 2000Hz
	/**	PWM	channel	*/
	static final int PWM_CHANNEL = 1;
	/**	GPIO channels to be	used */
	static final int GPIO_START	= 4;
	static final int GPIO_STOP = 11;


	public static void main(String[] args) {

		// simple control variable
		int	pin;

		// set PWM frequency and start PWM generator
		PWM.setFrequency(PWM_FREQUENCY);
		PWM.setActive(PWM_CHANNEL, true);

		// set all GPIOs to	input stape	with pull up
		for	(pin = GPIO_START; pin <= GPIO_STOP; pin++)	{
			GPIO.setMode(pin, GPIO.PULLUP);
		}

		// application runs	in endless loop
		for	(;;) {
			// read	current	state of GPIO channels
			int	result = 0;
			for	(pin = GPIO_START; pin <= GPIO_STOP; pin++)	{
				result <<= 1;
				result |= GPIO.getState(pin)?1:0;
			}

			// set value as	duty cycle to PWM
			PWM.setDuty(PWM_CHANNEL, result);
			// wait for 200ms
			try	{
				ThreadExt.sleep(200);
			} catch	(Exception exc)	{
			}
		}
	}
}
