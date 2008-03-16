
JControl/MusicBox - a simple application to demonstrate iMelody playing
=================

The MusicBox demonstration requires beside the MusicBox classfile some
resources in iMelody format. These are loaded up to the JControl module along
with the application itself. Some are contained with this distribution.

The iMelody format is specified by the Infrared Date Assosiation (IrDA) and a
description can be found on the jcontrol.org website in the documents section.

The MusicBox application just uses the JControl extended API iMelody.class to
convert the text to tones so the application is only responsible for finding
all the iMelody resources and asking the user to play which song.

First the application reads all resources ending with ".imy" and stores the
names in a list. You can now choose an action using the keyboard. On the bottom
of the Display there are some action buttons diaplayed, sliding up and down the
switch on the sticker or left and right on the PLUI/PCwatch you can choose the
action, pressing the knob selects the chosen action:
[|>] plays the current selected song or restarts its play
[[]] stops the current playing song
[<<] selects the previous song in the list (and plays it)
[>>] selects the next song in the list (and plays it)

Requirements
------------
* JControl/Sticker, JControl/PLUI, JControl/PCwatch or any JControl device
  supporting a Display and a Buzzer or similar SoundDevice.
* JControl/JCManger(Pro) for upload along with a device specific profile with
  iMelody support.

Files contained in the distribution
-----------------------------------
MusicBox/MusicBox_readme.txt            this file
MusicBox/MusicBox.job                   jobfile for use with JCManager(Pro)
MusicBox/src/jcontrol/demos/music/MusicBox.java
MusicBox/bin/jcontrol/demos/music/MusicBox.class
                                        source and binary of the application
MusicBox/rsc/Badinerie.imy
MusicBox/rsc/Chiquitita.imy
MusicBox/rsc/DasBoot.imy
MusicBox/rsc/Star Trek.imy
MusicBox/rsc/StarWars.imy
MusicBox/rsc/The Entertainer.imy
MusicBox/rsc/Yesterday.imy              iMelody resources
MusicBox/rsc/MusicBox.jcif              background image resource
MusicBox/rsc/Times13.jcfd               font resource for displaying song names

API classes used (contained on the device)
----------------
java.lang.Object
java.lang.String
java.lang.Thread
java.lang.InterruptedException
java.lang.Runnable
java.lang.Integer
java.io.IOException
jcontrol.lang.ThreadExt
jcontrol.lang.Deadline
jcontrol.lang.DeadlineMissException
jcontrol.io.PWM
jcontrol.io.Display
jcontrol.io.Keyboard
jcontrol.io.Resource
jcontrol.system.Management

Extended API classes used (contained in the profile)
-------------------------
jcontrol.io.SoundDevice
jcontrol.io.Buzzer
jcontrol.ext.iMelody
