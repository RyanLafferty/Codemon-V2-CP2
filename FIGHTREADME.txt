/****Operations****/

--Config--
Source Directory: Change Source Directory for codemon input files.
Codemon Directory: Change Output Directory for assembled Codemon.
Reports Directory: Directory for storage of reports.
Iteration Limit: Turn Limit for test and self games. Default is 0.
vs. 1: Selects PVP2 mode.
vs. 2: Selects PVP3 mode.
vs. 3: Selects PVP4 mode.

--Reports--
View: View the currently selected report.
Delete: Delete the currently selected report.
Fetch all: Attempts to fetch all pending reports from the server.
DNE: Means that the report does not exist either because it is not in the current
directory or because the report has yet to be retrieved.

--Help--
Displays this readme.

/****Buttons****/
In order from left to right:
run test: Runs a test game using Codemon 1 and iteration limit.
run self: runs a test game between Codemon 1 and Codemon 2 using
the given iteration limit.
run PVP: Submits a pvp request to the server using Codemon 1 and
the currently selected vs mode.
delete: Deletes the currently selected report.

/****Indicators****/
Status: the status of the program.
