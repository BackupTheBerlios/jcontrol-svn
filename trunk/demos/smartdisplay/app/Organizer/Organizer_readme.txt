
JControl/Organizer - a more elaborate application to demonstrate user
==================   interfaces and persistent storage access

The Organizer demonstration shows how simple user interfaces could be imple-
mented and the usage of persitent storage. It's a downscaled personal infor-
mation manager (PIM), you can just create some appointments composed of a date
and a message. The buzzer will beep if an date is reached.

The main application is designed for use with the JControl/Sticker, it should
also work on other display equipped JControl devices (PLUI, PCwatch). Most of
the user interaction takes place here. Also RTC accesses are managed here.

There are some additional classes that encapsulate some enhanced functionality:
* MenuSelector.class just displays a list if item the user should choose from
* Clock.class displays a analog clock running automatically and waiting for
  events,
* Date.class is data holder for appointments composed of a date and a message;
  appointments could be stored to and read from persistent storage,
* DateInput.class displays a user form for editing appointments,
* Typewriter.class displays a virtual QWERTZ keyboard with a cursor,
* Typewalker.class displays a larger virtual keyboard scrolling on the screen.
You can choose either of the virtual keyboards changing line 184 in
DateInput.java.

Usage
-----
After startup the analog clock is displayed (you can set the time using the
SetupMenu). If a date is arriving the appointment is displayed under a smaller
clock (this could also happen if the JControl/Sticker was turned off).
After pressing the slide switch a simple menu appeares you can choose using up/
down:
"Uhrzeit" display the clock again (this also happens after beeing 10 seconds
        idle),
"Neuer Termin" to create an appointment,
"Termine bearbeiten" to modify existing appointments,
"Ausschalten" to turn the JControl device off.

If you choose to modify appointments, a list is displayed.
Then you can choose a action by sliding the switch up/down and then select:
[/\] choose the previous appointment in the list,
[\/] choose the next appointment in the list,
"ändern" modify the currently chosen appointment,
"löschen" remove the currently chosen appointment from the list
"zurück" leave this mode without changes,

If you choose to create or to modify an appointment, you can set the date and
time by sliding up/down. Select is used to go to the next value. If you reached
the bottom line you can choose to modify the message by the virtual keyboard,
to store the appointment ("OK") or to discard the changes ("Abbr.").

Requirements
------------
* JControl/Sticker, JControl/PLUI, JControl/PCwatch or any JControl device
  supporting a Buzzer or similar SoundDevice.
* JControl/JCManger(Pro) for upload along with a device specific profile.

Files contained in the distribution
-----------------------------------
Organizer/Organizer_readme.txt        this file
Organizer/Organizer.job               jobfile for use with JCManager(Pro)
Organizer/src/jcontrol/demos/misc/Organizer.java
Organizer/bin/jcontrol/demos/misc/Organizer.class
                                      source and binary of the main application
Organizer/src/jcontrol/demos/misc/Date.java
Organizer/src/jcontrol/demos/misc/DateInput.java
Organizer/src/jcontrol/demos/uielements/Clock.java
Organizer/src/jcontrol/demos/uielements/MenuSelector.java
Organizer/src/jcontrol/demos/uielements/Typewalker.java
Organizer/src/jcontrol/demos/uielements/Typewriter.java
Organizer/bin/jcontrol/demos/misc/Date.class
Organizer/bin/jcontrol/demos/misc/DateInput.class
Organizer/bin/jcontrol/demos/uielements/Clock.class
Organizer/bin/jcontrol/demos/uielements/MenuSelector.class
Organizer/bin/jcontrol/demos/uielements/Typewalker.class
Organizer/bin/jcontrol/demos/uielements/Typewriter.class
                                      other sources and binaries
Organizer/rsc/organizer.jcif
Organizer/rsc/typewalker.jcif
Organizer/rsc/typewriter.jcif         image resources
Organizer/rsc/Times13.jcfd            font resource

API classes used (contained on the device)
----------------
java.lang.Object
java.lang.String
java.lang.Exception
java.lang.Thread
java.lang.InterruptedException
java.lang.Runnable
java.lang.Integer
java.io.IOException
jcontrol.io.Display
jcontrol.io.Flash
jcontrol.io.Keyboard
jcontrol.io.PWM
jcontrol.io.Resource
jcontrol.lang.Deadline
jcontrol.lang.DeadlineMissException
jcontrol.lang.Math
jcontrol.lang.ThreadExt
jcontrol.system.Management
jcontrol.system.RTC
jcontrol.system.Time

Extended API classes used (contained in the profile)
-------------------------
jcontrol.io.Animateable
jcontrol.io.Buzzer
jcontrol.io.Drawable
jcontrol.io.SoundDevice
jcontrol.storage.FlashTlv
jcontrol.storage.TlvFile
jcontrol.ui.AnalogClock
