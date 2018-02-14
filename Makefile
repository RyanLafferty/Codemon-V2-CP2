#Ryan Lafferty 0853370
CC = gcc
CFLAGS = -Wall
INCLUDES =
LIBS =
CFILES = modes.c client.c codemon.c parse.c store.c clean.c locationHandler.c bitStrings.c link.c
CFILES2 = *.c
CFILESA1 = modes.c client.c codemon.c parse.c store.c clean.c locati$
JFILES = Codemon/*.java
JHFILES = Codemon.Codemon
OFILES = modes.o client.o codemon.o parse.o store.o clean.o locationHandler.o bitStrings.o link.o
EXEC = codemon
TEST1 = compileState testGivenCodemon testTestFileCompile cleanReports
TEST2 = testLoadGivenCodemon testLoadGivenCodemon1000 testLoadTestFile
TEST3 = testSelfTest testPVP2 testPVP3 testPVP4 compileOPS compileETC loadOPSETC dumpReports errorTesting allDone
JNIHOME = -I/Library/Java/JavaVirtualMachines/jdk1.8.0_40.jdk/Contents/Home/include/
ENVHOME = -I/Library/Java/JavaVirtualMachines/jdk1.8.0_40.jdk/Contents/Home/include/darwin/
JNILABHOME = -I/Library/Java/JavaVirtualMachines/jdk1.8.0_60.jdk/Contents/Home/include/
ENVLABHOME = -I/Library/Java/JavaVirtualMachines/jdk1.8.0_60.jdk/Contents/Home/include/darwin
all: codemonCompile

codemonCompile:
	@echo Compiling
	javac $(JFILES)
	$(CC) -dynamiclib -o libcodemon.jnilib $(CFLAGS) $(JNILABHOME) $(ENVLABHOME) $(CFILES2) -framework JavaVM
	export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:.
	@echo Build Complete

generateHeader:
	javah $(JHFILES)

run: codemon
	java Codemon.Codemon

jniTest:
	javac jniTest.java
	java jniTest -c Source/kraken.cm

codemonA1: compileA1
	@echo Linking Object Files
	$(CC) $(CFLAGS) $(OFILES) -o $(EXEC)
	@echo Build Complete

compileA1: $(CFILESA1)
	@echo Compiling C Files
	$(CC) -c $(CFILESA1)

.PHONY: cleanA1
cleanA1:
	@echo Cleaning object files
	rm *.o $(EXEC)

.PHONY: clean
clean:
	@echo Cleaning object files
	rm Codemon/*.class
	rm libcodemon.jnilib
cleanjniTest:
	rm jniTest.class

#Testing Script Located Below.
#I didn't know anything about scripting before this assignment,
#So there might be so poor design decisions.

.PHONY: $(TEST1) $(TEST2) $(TEST3)

testAll: $(TEST1) $(TEST2) $(TEST3)

compileState:
	@echo Compiling program
	make
	@echo

testGivenCodemon:
	@echo Compiling codemon
	./codemon -c imp > imp.codemon
	./codemon -c dwarf > dwarf.codemon
	./codemon -c kraken > kraken.codemon
	@echo

testTestFileCompile:
	@echo Compiling test files
	./codemon -c testFile > testFile.codemon
	@echo

cleanReports:
	@echo cleaning reports
	rm reports
	rm reportDumps
	@echo create report files
	touch reports
	touch reportDumps
	@echo

testLoadGivenCodemon:
	@echo Test loading codemon for 0 turns
	./codemon -t imp.codemon 0 >> reports
	sleep 1
	./codemon -t dwarf.codemon 0 >> reports
	sleep 1
	./codemon -t kraken.codemon 0 >> reports
	sleep 1
	@echo

testLoadTestFile:
	@echo Loading test files
	./codemon -t testFile.codemon 0 >> reports
	sleep 5
	@echo

testLoadGivenCodemon1000:
	@echo Test loading codemon for 1000 turns
	./codemon -t imp.codemon 1000 >> reports
	sleep 1
	./codemon -t dwarf.codemon 1000 >> reports
	sleep 1
	./codemon -t kraken.codemon 1000 >> reports
	sleep 5
	@echo

testSelfTest:
	@echo Testing self test
	./codemon -s kraken.codemon kraken.codemon 0 >> reports
	sleep 1
	./codemon -s kraken.codemon kraken.codemon 1000 >> reports
	sleep 5
	@echo

testPVP2:
	@echo Testing pvp2
	./codemon -p 2 kraken.codemon >> reports
	./codemon -p 2 kraken.codemon
	sleep 5
	@echo

testPVP3:
	@echo Testing pvp3
	./codemon -p 3 imp.codemon >> reports
	./codemon -p 3 dwarf.codemon
	./codemon -p 3 kraken.codemon
	sleep 5
	@echo

testPVP4:
	@echo Testing pvp4
	./codemon -p 4 imp.codemon >> reports
	./codemon -p 4 imp.codemon
	./codemon -p 4 dwarf.codemon
	./codemon -p 4 kraken.codemon
	sleep 5
	@echo

compileOPS:
	@echo compiling OPS
	./codemon -c testOpCodes > testOpCodes.codemon
	./codemon -c testDat > testDat.codemon
	./codemon -c testMov > testMov.codemon
	./codemon -c testAdd > testAdd.codemon
	./codemon -c testSub > testSub.codemon
	./codemon -c testMul > testMul.codemon
	./codemon -c testDiv > testDiv.codemon
	./codemon -c testMod > testMod.codemon
	./codemon -c testJmp > testJmp.codemon
	./codemon -c testJmz > testJmz.codemon
	./codemon -c testJmn > testJmn.codemon
	./codemon -c testDjn > testDjn.codemon
	./codemon -c testSeq > testSeq.codemon
	./codemon -c testSne > testSne.codemon
	./codemon -c testSlt > testSlt.codemon
	./codemon -c testSet > testSet.codemon
	./codemon -c testClr > testClr.codemon
	./codemon -c testFrk > testFrk.codemon
	./codemon -c testNop > testNop.codemon
	sleep 5
	@echo

compileETC:
	@echo compiling ETC
	./codemon -c testComments > testComments.codemon
	./codemon -c testLabels > testLabels.codemon
	./codemon -c testSpacing > testSpacing.codemon
	./codemon -c testNegative > testNegative.codemon
	./codemon -c testEdges > testEdges.codemon
	./codemon -c testRollover > testRollover.codemon
	sleep 5
	@echo

loadOPSETC:
	@echo loading OPS and ETC files
	./codemon -t testOpCodes.codemon 0 >> reports
	sleep 1
	./codemon -t testDat.codemon 0 >> reports
	sleep 1
	./codemon -t testMov.codemon 0 >> reports
	sleep 1
	./codemon -t testAdd.codemon 0 >> reports
	sleep 1
	./codemon -t testSub.codemon 0 >> reports
	sleep 1
	./codemon -t testMul.codemon 0 >> reports
	sleep 1
	./codemon -t testDiv.codemon 0 >> reports
	sleep 1
	./codemon -t testMod.codemon 0 >> reports
	sleep 1
	./codemon -t testJmp.codemon 0 >> reports
	sleep 1
	./codemon -t testJmz.codemon 0 >> reports
	sleep 1
	./codemon -t testJmn.codemon 0 >> reports
	sleep 1
	./codemon -t testDjn.codemon 0 >> reports
	sleep 1
	./codemon -t testSeq.codemon 0 >> reports
	sleep 1
	./codemon -t testSne.codemon 0 >> reports
	sleep 1
	./codemon -t testSlt.codemon 0 >> reports
	sleep 1
	./codemon -t testSet.codemon 0 >> reports
	sleep 1
	./codemon -t testClr.codemon 0 >> reports
	sleep 1
	./codemon -t testFrk.codemon 0 >> reports
	sleep 1
	./codemon -t testNop.codemon 0 >> reports
	sleep 1
	./codemon -t testComments.codemon 0 >> reports
	sleep 1
	./codemon -t testLabels.codemon 0 >> reports
	sleep 1
	./codemon -t testSpacing.codemon 0 >> reports
	sleep 1
	./codemon -t testNegative.codemon 0 >> reports
	sleep 1
	./codemon -t testEdges.codemon 0 >> reports
	sleep 1
	./codemon -t testRollover.codemon 0 >> reports
	sleep 5
	@echo

dumpReports:
	@echo Dumping Reports
	./codemon -clean reports >> reportDumps
	sleep 1
	@echo

errorTesting:
	@echo Testing error handling
	./codemon -c errorFile
	@echo
	./codemon -c errorDat
	@echo
	./codemon -c errorMov
	@echo
	./codemon -c errorAdd
	@echo
	./codemon -c errorSub
	@echo
	./codemon -c errorMul
	@echo
	./codemon -c errorDiv
	@echo
	./codemon -c errorMod
	@echo
	./codemon -c errorJmp
	@echo
	./codemon -c errorJmz
	@echo
	./codemon -c errorJmn
	@echo
	./codemon -c errorDjn
	@echo
	./codemon -c errorSeq
	@echo
	./codemon -c errorSne
	@echo
	./codemon -c errorSlt
	@echo
	./codemon -c errorSet
	@echo
	./codemon -c errorClr
	@echo
	./codemon -c errorFrk
	@echo
	./codemon -c errorNop
	@echo

allDone:
	@echo All Done
