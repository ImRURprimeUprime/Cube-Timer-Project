# Rubik's Cube Timer

## 

The project that I will be working on for the term
is a Rubik's cube timer.

The timer will contain the ***following features***:
- timing a solve
- generating scrambles for the cube
- keeping track of the previous solves
  - time, scramble, average
- being able to view and edit previous solves
  - deleting, adding on penalties
- folders to copy the solves with user's desired
qualities
  - solves under a certain amount of time, solves 
  with interesting scrambles

The potential users of this application are
individuals who would like to time their solves.
For instance, a speedcuber looking to attend
competitions could use the application to calculate
their average times to get an estimate for the times
they would get at the competition. A casual cuber
could also use it to keep track of their times and 
interesting scrambles they come across.

This project is of interest to me because I am also
a cuber, and I think a feature that nearly all the
Rubik's cube timers on the market currently lack is 
the feature to save/mark certain solves that I
would like to revisit in the future. Therefore, if
I am able to apply this feature to my application, 
it would be something that makes my application 
desirable to the cubing community.

## User Stories
- As a user, I would like my solves to be stored in 
a session
- As a user, I would like to save interesting solves
to a list or folder, so I can find them later easily
- As a user, I want to have all my average times 
calculated for me
- As a user, I want to be able to edit the times 
that have been recorded
- As a user, I want the application to generate 
a scramble for me and match the scramble with the 
time that I got
- As a user, I want to have the option to save my
progress in the current session
- As a user, I want to have the option to load up 
my session from where I left off at an earlier time

# Instructions for Grader

- You can generate the first required action related to adding Xs to a Y (Removing an X from Y) by first adding a few 
solves to the session. You can add a single solve by pressing space to start the timer, and press space again to stop 
the timer. After adding a few solves, click the "Delete solve..." button below the panel that displays all the solve
times. Enter the number of the solve you wish to delete, and it should now be removed from the session, and this change
is also visualized in the panel that contains the recorded times.
- You can generate the second required action related to adding Xs to a Y by first adding 2 solves to the session 
according to the instructions given in the previous step. Then click the "Bookmark solve..." button that is right below
the "Penalty..." button, and enter 1. The first solve should now be bookmarked; now click the "View bookmarks" button 
and a new window should pop up, and it should contain the time of solve number 1, and its corresponding scramble
  (showing a subset of Xs in Y).
- You can locate my visual component by adding a solve to the session according to the instructions given in previous 
steps, and then click the "Bookmark solve..." button on the left side panel and enter 1 and click ok. Then the visual
component should be shown. (This should already have been shown in previous steps)
- You can save the state of my application by clicking the "Save session" button at the bottom of the right side panel,
information about the saved session should then show up in the data/session.json
- You can reload the state of my application by clicking the "Load session" button at the bottom of the right side panel

# Phase 4: Task 2

Wed Aug 09 00:00:15 PDT 2023 <br />
Added a new solve to session. Time: 0.57 <br />
Wed Aug 09 00:00:17 PDT 2023 <br />
Added a new solve to session. Time: 0.34 <br />
Wed Aug 09 00:00:18 PDT 2023 <br />
Added a new solve to session. Time: 0.46 <br />
Wed Aug 09 00:00:19 PDT 2023 <br />
Added a new solve to session. Time: 0.24 <br />
Wed Aug 09 00:00:20 PDT 2023 <br />
Added a new solve to session. Time: 0.82 <br />
Wed Aug 09 00:00:27 PDT 2023 <br />
Added 2 seconds time penalty to solve 1, new time: 2.57 <br />
Wed Aug 09 00:00:39 PDT 2023 <br />
Removed 2 seconds time penalty from solve 1, new time: 0.57 <br />
Wed Aug 09 00:00:47 PDT 2023 <br />
Solve 2 has been bookmarked. <br />
Wed Aug 09 00:00:50 PDT 2023 <br />
Removed bookmark from solve 2. <br />
Wed Aug 09 00:01:00 PDT 2023 <br />
Name of the session was set to Allan's Session <br />
Wed Aug 09 00:01:10 PDT 2023 <br />
Removed solve 2 from session <br />
Wed Aug 09 00:01:17 PDT 2023 <br />
Added 2 seconds time penalty to solve 4, new time: 2.82 <br />

# Phase 4: Task 3

Reflecting on the UML class diagram, there are two associations that might be redundant, they are the associations from 
SessionPanel (SP) to Session, and from TimerPanel (TP) to Session. This is because SP and TP do not actually
need to have a field of type Session. One possible refactoring that could be applied is adding a parameter of type
Session for the methods that currently use the session field in SP and TP. This refactoring is valid; in the current
state of the project, TimerUI passes its Session field into the constructor of SP and TP, and SP and TP simply sets 
their Session field to the Session parameter that was passed in by TimerUI. This implies that the Session field in all 3
classes refer to the same object. It is possible to achieve the same functionalities if we just pass in the Session
field from TimerUI as a parameter into the methods of SP and TP. For instance, when adding a solve to Session in TP, it
is fine if we just call the addSolve method on the parameter of type Session, since the parameter points to the same 
object as the Session field in TimerUI anyway. Thus, this refactoring is advantageous, since it eliminates 2 redundant
associations, and it would definitely be implemented if there was more time to ensure that the project runs as usual.

Another change I could make is only keeping one association arrow from Session to Solve. There is a List<Solve> field 
that simply stores the solves that have been bookmarked, and a Solve field that refers to the fastest solve in the 
session. Neither of these have to be fields, since I could just make the calculate methods and make the calculate 
methods return these values after they have completed calculating (the calculate methods in the current state of the
project are void), instead of writing an additional getter method for these fields as well. 