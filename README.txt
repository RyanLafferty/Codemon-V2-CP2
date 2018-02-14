/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

************
Compilation
************
The program was compiled using a Makefile in the a2 root directory.
The compilation can be called by typing make while in the a2
root directory.

Make sure that the java paths are set correctly in the make file, I have supplied two sets
of variables, one for my machine and one for the lab computers (the default one).

***********************
Running the program(s)
***********************
Either type "make run" to run the program or "java Codemon.Codemon"
to execute the program.

Keyboard acceleration
=====================
As noted in the spec, there should be keyboard accelerated actions in the train window.
The keys I have chosen are the F keys since they are not used while typing most the time.
If you mac is not setup such that the F-key is the priority rather than the function assigned
to the F-key, you will either have to enable the option to allow for F keys over function
keys or hold the "fn" key while pressing the specified F-key.

***********
Limitations
***********
Keyboard acceleration can only be accessed when the text area is focused since the KeyListener
is associated with the JFrame.

When opening a report it may take a couple seconds to load the report. Even reading
multiple lines at a time, the buffered reader incurs an overhead high enough, such that
longer files that contain thousands of lines will take a bit to load.
This limitation is on oracle's end as their buffered reader has a moderately
high overhead when dealing with large files.

Since all the method calls execute faster than the window can repaint, I allowed the
status field to display the last status code so that the user can see what happened.
If I cleared the status upon completion of the task you would almost never see the status
change.

This assignment was build as a package due to handling multiple java classes
so it must be invoked by typing "java codemon.codemon", alternatively you can
just type "make run".

The fetch all command will attempt to fetch all DNE reports, since we don't know
if the report has already been fetched by the user it will contact the server asking
for every report that doesn't exist. This in no way breaks the spec however if the
user is intentionally introducing errors then the server will get hit with bad requests.

Since swing is an event driven model global variables are visually updated on every action
event. This a limitation due to the event driven model of swing interfaces.

When selecting a file in the train window a JFileChooser will want your mouse focus,
so if you scroll through the list in the directory and try to select a file without
first focussing the window, the JFileChooser's scroll bar will jump back to the top.
This limitation is on oracle's end as you need to focus the JFileChooser before
selecting a file.

***********
Assumptions
***********
Labels are case sensitive, everything else is case insensitive. (Implementation Choice).

***********
Extra
***********
A2
==
There is a command line java program that I used for testing, it has no relevance to
the grading of the assignment but I left it in the repo to allow me to test my jni calls.
There are also several test commands in the make file, these can be ignored.

A1
==
There is a set of exhaustive tests in the testing directory.
The script can be executed by typing make testAll when with the
tests in the root directory. Before you run the script you must modify
some lines in mode.c. Comment out the getReport lines and uncomment the
line that prints the report id to the screen. You also have to uncomment
the block in main (codemon.c) that executes when the -clean flag is given.
