
JControl/GameCollection - some simple and well-known games
=======================

The GameCollection demonstrates some graphical action and user interaction. It
does not attach importance to real time behaviour. First a simple graphical
menu appears to choose one of five games from (by sliding up/down and then
selecting).

Connect4
--------
A simple (vertical) board game. Put the chips alternating with JControl in the
slots to get first four in a row (horizontal, vertical or diagonal). Choose the
slot using left/right or up/down on the keyboard and then press select.
After success JControls game level is increased.

Minesweeper
-----------
Find all mines hidden in the playfield. Navigate using the four cursor
directions (on the JControl/Sticker navigation is a littly tricky because there
is only up/down, use up for vertical and down for horizontal movements). By
hitting select you can peek under a tile, if it's a mine you've lost. If not
the number shows how many mines are under the eight adjacent tiles.

Pong
----
Classical arcade game. Move your paddle up/down to hit the ball. Select starts
playing.

Breakout
--------
Classical arcade game. Move your paddle left/right, hit the ball and clear all
blocks. Select starts playing and throws-in a new ball.

Tetris
------
Nothing more to say but the keyboard controls: use up/down or left/right for
movement and select for rotation.

Requirements
------------
* JControl/Sticker, JControl/PLUI, JControl/PCwatch or any JControl device
  supporting a display and a keyboard.
* JControl/JCManger(Pro) for upload along with a device specific profile.

Files contained in the distribution
-----------------------------------
GameCollection/GameCollection_readme.txt this file
GameCollection/GameCollection.job       jobfile for use with JCManager(Pro)
GameCollection/src/jcontrol/demos/games/GameCollection.java
GameCollection/bin/jcontrol/demos/games/GameCollection.class
                                        source and binary of the application
GameCollection/src/jcontrol/demos/games/Breakout.java
GameCollection/src/jcontrol/demos/games/Connect4.java
GameCollection/src/jcontrol/demos/games/Minesweeper.java
GameCollection/src/jcontrol/demos/games/Pong.java
GameCollection/src/jcontrol/demos/games/Tetris.java
GameCollection/bin/jcontrol/demos/games/Breakout$Ball.class
GameCollection/bin/jcontrol/demos/games/Breakout.class
GameCollection/bin/jcontrol/demos/games/Connect4.class
GameCollection/bin/jcontrol/demos/games/Minesweeper.class
GameCollection/bin/jcontrol/demos/games/Pong$Ball.class
GameCollection/bin/jcontrol/demos/games/Pong$ComputerPlayer.class
GameCollection/bin/jcontrol/demos/games/Pong.class
GameCollection/bin/jcontrol/demos/games/Tetris$Keyrequest.class
GameCollection/bin/jcontrol/demos/games/Tetris.class
                                        other sources and binaries
GameCollection/rsc/Breakout.jcif
GameCollection/rsc/Connect4.jcif
GameCollection/rsc/GameCollection.jcif
GameCollection/rsc/Jcontrol_logo.jcif
GameCollection/rsc/Minesweeper.jcif
GameCollection/rsc/Pong.jcif
GameCollection/rsc/Tetris.jcif
GameCollection/rsc/Tetris_logo.jcif     image resources

API classes used (contained on the device)
----------------
java.lang.Object
java.lang.String
java.lang.Thread
java.lang.InterruptedException
java.lang.Integer
java.io.IOException
jcontrol.io.Display
jcontrol.io.Keyboard
jcontrol.io.Resource
jcontrol.lang.Math
jcontrol.lang.ThreadExt
jcontrol.system.Management
