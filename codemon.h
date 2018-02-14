/*Name: Ryan Lafferty
ID: 0853370
Date: 9/18/2015
Assignment 1*/

#ifndef INCLUDED
#define INCLUDED

#define LABEL 0
#define OPCODE 1
#define AFIELD 2
#define BFIELD 3
#define COMMENT 4
#define START 5
#define ALPHA 6
#define ERROR -1
#define DONE -2

#include <jni.h>
#include "jniTest.h"
#include "Codemon_Codemon.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <unistd.h>
#include "arch.h"
#include "common.h"

typedef struct instruction
{
    int pos;
    char * label;
    char * op;
    char * aField;
    char * bField;

    char aPrefix;
    int aNum;
    char bPrefix;
    int bNum;

    uint64 bitStr;

    struct instruction * next;
} instruction;

/*****codemon.c*****/
/*****codemon.c*****/

/*****parse.c******/
instruction * parseFile(char * fileName, uint32 * start);
FILE * getStart(FILE * f, uint32 * start);
FILE * getComment(FILE * f);
FILE * getField(FILE * f, char ** field, char * ch);
int updateFields(instruction * set);
/*****parse.c*****/

/*****modes.c******/
int compileToFile(char * file1, char * file2);
int testJni(char * fileName, char * name, int limit);
int selfJni(char * file1, char * file2, char * name1, char * name2, int limit);
int pvpJni(int players, char * fileName, char * name);
int reportJni(char * reportID, char * fileName);

int compile(char * fileName);
int test(char * fileName, int limit);
int self(char * file1, char * file2, int limit);
int pvp(int players, char * fileName);
int report(char * reportID);

int readFile(char * fileName, uint32 * begin, uint32 * size, uint64 ins[MAXINST]);
int getCodemonName(char * fileName, char codemonName[MAXNAME+1]);
int errorCheck(instruction * set);
int errorCheck2(instruction * set);
/*****modes.c******/

/*****store.c*****/
instruction * storeInstruction(char * label, char * op, char * aField, char * bField, int pos);
/*****store.c*****/

/*****clean.c*****/
int clean(instruction ** set);
int destroyInstruction(instruction * i);
/*****clean.c*****/

/*****locationHandler.c*****/
int updateLocations(instruction * set);
char getPrefix(char * str);
int isNegative(char * str);
int getNumber(char * str);
int updateLabels(instruction * set);
/*****locationHandler.c*****/

/*****bitStrings.c*****/
int generateBitStrings(instruction * set);
uint64 generateOPBitMask(char * op);
uint64 generateModeMask(char mode);
uint64 generateNumMask(int num);
/*****bitStrings.c*****/

#endif
