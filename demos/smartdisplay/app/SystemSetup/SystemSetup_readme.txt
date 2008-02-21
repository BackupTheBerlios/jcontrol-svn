
JControl/SystemSetup - JControl/Sticker default Application
====================

This demo is a special application, it's part of the JControl/Sticker distri-
bution. So if you have a Sticker you already have this application installed.
The SystemSetup is - like all applications - placed in flash memory so it's
updateable. Normally you won't need an update unless the flash bank 0 content
is lost or damaged by an software accident.

The SystemSetup is for basic setup, maintenance adjustment and software
updates. The SystemSetup starts from flash bank 0 and after displaying a
splash screen for one second the chosen user application is started from another
bank. To enter the setup menu hold up the slide switch while the splash screen
is displayed.

Usage
-----
Within the main menu you can choose a function by selecting an icon using up/
down and then pressing the switch:

* "Battery status" just displays a value indicating battery charge, you should
  change the batteries below 50%, use the switch to return to the main menu.
* "Time" is for setting the internal RTC (real time clock) in german date
  format (hh:mm:ss dd.mm.yyyy), one value is highlighted use up/down to change
  it, press the button to highlight the next value, after all values are
  set, the main menu appears again.
* "Display contrast" allows to change the contrast of the LCD, this depends on
  the viewing angle, the value is stored in the system property "display.contrast",
  change with up/down, press to return.
* "Sound" allows to change sound flags, you can change these system properties
  (from left to right): "buzzer" (used by the classes Buzzer and iMelody),
  "buzzer.keyboardbeep" (used by the class Keyboard) and "buzzer.systembeep"
  (used by jcontrol.system classes for signalling), use up/down to select an
  icon and press to toggle, if you select the rightmost icon, select returns
  to the main menu.
* "Boot-Bank" is for choosing one of the uploaded applications for execution,
  use up/down to switch the bank (currently there are up to 4 banks depending on
  your hardware, numbered from 0 to 3), the bank number is displayed and
  - if present - the name of the application which will start from this bank.
  Press the switch to start the chosen application (this will then also start
  after powering on or resetting the device, the value is stored in the system
  property "system.userbank"), if the SystemSetup itself was chosen (always
  bank 0) you'll just return to the main menu.
* "File upload" enables communication with a host PC running JCManager(Pro),
  once chosen only the host PC or a device reset are able to leave this mode,
  you can enter this download mode also if you hold down the slide switch while
  starting up (even if the SetupMenu is lost).
* "Credits" just scrolls some text, press the switch to leave.
* "Standby Wakeup" sets two RTC features, auto power off (standby after delay)
  and auto power on (alarm, at specified time the device will turn on and
  beeps), use up/down to select change a value (use 0 to turn auto standby off)
  and press to go to the next value (auto power on has to be enabled separately
  from alarm time setting), after all values are set you'll return to the main
  menu.
* "Demo" starts a application residing in a second archive in bank 0 (if
  available).
* "Off" manually turns the device off (the RTC still runs if the batteries are
  not removed).

Requirements
------------
* JControl/Sticker
* JControl/JCManger(Pro) for upload along with a device specific profile with
  iMelody support.

Files contained in the distribution
-----------------------------------
SystemSetup/SystemSetup_readme.txt      this file
SystemSetup/SystemSetup.job             jobfile for use with JCManager(Pro)
SystemSetup/src/jcontrol/system/setup/SystemSetup.java
SystemSetup/bin/jcontrol/system/setup/SystemSetup.class
                                        source and binary of the application
SystemSetup/src/jcontrol/demos/misc/VisitCard_JControl.java
SystemSetup/src/jcontrol/system/ErrorHandler.java
SystemSetup/src/jcontrol/system/setup/SetupBatt.java
SystemSetup/src/jcontrol/system/setup/SetupBootDevice.java
SystemSetup/src/jcontrol/system/setup/SetupContrast.java
SystemSetup/src/jcontrol/system/setup/SetupExplorer.java
SystemSetup/src/jcontrol/system/setup/SetupSound.java
SystemSetup/src/jcontrol/system/setup/SetupStandbyTime.java
SystemSetup/src/jcontrol/system/setup/SetupTime.java
SystemSetup/bin/jcontrol/demos/misc/VisitCard_JControl.class
SystemSetup/bin/jcontrol/system/ErrorHandler.class
SystemSetup/bin/jcontrol/system/setup/SetupBatt.class
SystemSetup/bin/jcontrol/system/setup/SetupBootDevice.class
SystemSetup/bin/jcontrol/system/setup/SetupContrast.class
SystemSetup/bin/jcontrol/system/setup/SetupExplorer.class
SystemSetup/bin/jcontrol/system/setup/SetupSound.class
SystemSetup/bin/jcontrol/system/setup/SetupStandbyTime.class
SystemSetup/bin/jcontrol/system/setup/SetupTime.class
SystemSetup/bin/jcontrol/system/setup/SystemSetup$Alarm.class
SystemSetup/bin/jcontrol/system/setup/SystemSetup$Menu.class
                                        other sources and binaries
SystemSetup/rsc/chooser.jcif
SystemSetup/rsc/jcontrol_s-w_klein.jcif
SystemSetup/rsc/jcontrol_splash.jcif
SystemSetup/rsc/small_logo.jcif
SystemSetup/rsc/systemsetup.jcif        image resources

API classes used (contained on the device)
----------------
java.lang.Object
java.lang.String
java.lang.Thread
java.lang.Runnable
java.lang.Integer
java.lang.InterruptedException
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
jcontrol.system.Download
jcontrol.system.Management
jcontrol.system.RTC
jcontrol.system.Time

Extended API classes used (contained in the profile)
-------------------------
jcontrol.io.Animateable
jcontrol.io.Buzzer
jcontrol.io.Drawable
jcontrol.io.SoundDevice
jcontrol.ui.AnalogClock
jcontrol.ui.TextScroller
