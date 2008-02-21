
JControl/TeaTimer - a simple application to demonstrate accurate timing
=================

This application shows how to implement a clock using deadlines. If you don't
have an RTC or similar time counting device it is nearly impossible to design
an application with accurate timing or equidistant time steps with a standard
Java environment. You can do a sleep() for a second and then draw the clock
again, then wait for another second, but while drawing there's time lost and
you can't know how much it is. Even more Java is multi-threaded, so you can't
know if another thread is stealing some more time of your seconds.

Using JControl deadlines you can specify that counting a second takes exacly
one second, it is up to the virtual machine to guarantee this timing.

Usage
-----
After startup you can set the brewing time using up/down (defaults to 3 mins),
after pressing select the clock starts ticking. If the selected time span
expires the buzzer beeps a couple of times, pressing a key aborts beeping and
the program starts again but with the duration selected before. It is possible
to break the count-down holding the "up" key.

Requirements
------------
* JControl/Sticker, JControl/PLUI, JControl/PCwatch or any JControl device
  supporting a Display and a Buzzer or similar SoundDevice.
* JControl/JCManger(Pro) for upload along with a device specific profile.

Files contained in the distribution
-----------------------------------
TeaTimer/TeaTimer_readme.txt            this file
TeaTimer/TeaTimer.job                   jobfile for use with JCManager(Pro)
TeaTimer/src/TeaTimer.java
TeaTimer/bin/TeaTimer.class             source and binary of the application
TeaTimer/rsc/Digits.jcif
TeaTimer/rsc/TeaTimer.jcif              image resources

API classes used (contained on the device)
----------------
java.lang.Object
java.lang.String
java.lang.Integer
java.lang.InterruptedException
java.io.IOException
jcontrol.io.Display
jcontrol.io.Keyboard
jcontrol.io.PWM
jcontrol.io.Resource
jcontrol.lang.Deadline
jcontrol.lang.DeadlineMissException
jcontrol.lang.ThreadExt
jcontrol.system.Management

Extended API classes used (contained in the profile)
-------------------------
jcontrol.io.SoundDevice
jcontrol.io.Buzzer
