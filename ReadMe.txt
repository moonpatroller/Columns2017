ReadMe.txt
Game play:
To play the game drops down a column of assorted colors down.
The goal of the game is to connect 3 or more of the same color together.
Controls:
	Space Bar: forces the column down to where it would normally stack in the column it is in at the time.
	Down Arrow: Forces the piece down 3 spaces
	Left Arrow: Shifts the column to the left by 1 space.
	Right Arrow: Shifts the column to the right by 1 space.
	Up Arrow: Shifts all of the columns down 1 except the bottom tile which goes to the top

The game is scored on how many in a row you get -2.
IE: If you get 3 in a row you get a score of 1, 4 in a row you get a score 2.

Note: Difficulty in this game changes the number of different types of colors up to 8 colors.

Program Description:

Piece.java:
Object that manages the pieces being dropped down into the game.
Contains useful tools such as chaning the order of the tiles, and
randomly generates tiles for the piece

BlockManger.java:
The game logic is stored inside BlockManager.java, with the game
being represented in a 2-d array of chars.
The game has a scanning feature in any direction that requires the core
coordinate and direction of the scan.
BlockManager.java also handles the gravity in the game with its methods:
oneDropPiece(...), hardDropPiece(...), clearInARow(...), readjustBoard(...).
oneDropPiece(...) and hardDropPiece(...) handle the dropping of the piece, while
clearInARow(...) and readjustBoard(...) gravity when pieces are taken away
for being more than 3 in a row.

ColumnsStartGUI.java:
The launch GUI for the Columns game.
It handles setting up the state of the columns board, with a user choice
of row number, column number, and the difficulty.
The default options can be accessed by pressing default settings,
or pressing "Start Game" with no variables placed in for that variable.

ColumnsGameGUI.java:
Handles the graphical represntation of the Columns backend gamestate
and handles mouse and key stroke inputs.

ColumnsEndGUI.java:
Pops up once the player lost the current game session.
Gives the options to either create a new board to play on, replay the same board and
settings, or stop playing the game(exiting out of the window).
