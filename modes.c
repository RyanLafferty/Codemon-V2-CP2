/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

#include "codemon.h"

/*
Desc: Compiles codemon instructions into a binary blob passed to a file.
Args: A pointer to the file name to compile (char *) and a pointer to the output file name (char *).
Ret: Returns -1 on error and 1 on success (int).
If successful, a binary blob will be printed to the specified file.
*/
int compileToFile(char * file1, char * file2)
{
    instruction * set;
    instruction * nav;
    uint32 start;
    uint64 temp;
    int retCode;
    int errorCount;
    FILE * f2;

    set = NULL;
    nav = NULL;
    start = 0;
    retCode = 0;
    errorCount = 0;
    temp = 0;
    f2 = NULL;

    if(file1 == NULL)
    {
        fprintf(stderr, "Error: no file1 name given!\n");
        return -1;
    }
    if(file2 == NULL)
    {
        fprintf(stderr, "Error: no file2 name given!\n");
        return -1;
    }

    set = parseFile(file1, &start);
    if(set == NULL)
    {
        fprintf(stderr, "Error: error parsing file!\n");
        return -1;
    }
    retCode = updateLocations(set);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not update locations!\n");
        retCode = clean(&set);
        return -1;
    }
    retCode = updateLabels(set);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not update labels!\n");
        retCode = clean(&set);
        return -1;
    }
    retCode = errorCheck2(set);
    if(retCode < 0)
    {
        clean(&set);
        return retCode;
    }
    errorCount = retCode;

    retCode = generateBitStrings(set);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not generate bit strings!\n");
        retCode = clean(&set);
        return -1;
    }

    nav = set;

    f2 = fopen(file2, "wb");
    if(f2 == NULL)
    {
        retCode = clean(&set);
        fprintf(stderr, "Error: could not open file!\n");
        return -1;
    }

    fwrite(&start, sizeof(uint32), 1, f2);
    while(nav != NULL)
    {
        temp = nav->bitStr;
        fwrite(&temp, sizeof(uint64), 1, f2);
        nav = nav->next;
    }


    retCode = clean(&set);
    fclose(f2);

    return 0;
}

/*
Desc: Runs a solo self test on the server using a single codemon.
Args: A pointer to a binary file name containing the codemon (char *),
a pointer to the codemon name (char *),
and the limit of turns that the codemon should run for (int).
Ret: On failure -1 is returned, on success the report id is returned (int).
On success the codemon will be sent for a solo test to the server,
run for the given turn limit and the integer report id will be
returned.
*/
int testJni(char * fileName, char * name, int limit)
{
    int retCode;
    int i;
    int id;
    struct Codemon_pkg pkg;

    retCode = 0;
    i = 0;
    id = 0;

    pkg.lines = 0;
    pkg.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg.program[i] = 0;
    }

    i = 0;

    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no file name given!\n");
        return -1;
    }
    if(name == NULL)
    {
        fprintf(stderr, "Error: no name given!\n");
        return -1;
    }
    if(limit < 0 || limit > MAXTURNS)
    {
        fprintf(stderr, "Error: invalid turn limit!\n");
        return -1;
    }

    retCode = readFile(fileName, &(pkg.begin), &(pkg.lines), pkg.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file!\n");
        return -1;
    }

    strncpy(pkg.name, name, MAXNAME);

    //Debugging
    /*printf("begin = %d\n", pkg.begin);
    printf("lines = %d\n", pkg.lines);
    for(i = 0; i < MAXINST; i++)
    {
        printf("ins %d: %016llx\n", i, pkg.program[i]);
    }*/


    id = runTest(&pkg, NULL, limit);
    //printf("Report ID: %d\n", id);
    //used with make testAll -> comment the line above
    //printf("%d\n", id);

    return id;
}

/*
Desc: Runs a pvp self test on the server using two codemon.
Args: A pointer to a binary file name containing the first codemon (char *),
a pointer to the name of the first codemon (char *), a pointer to the name
of the second codemon (char *).
A pointer to a binary file name containing the second codemon (char *),
and the limit of turns that the codemon should run for (int).
Ret: On failure -1 is returned, on success the report id is returned (int).
On success the codemon will be sent for a pvp test to the server,
run for the given turn limit and the integer report id will be returned.
*/
int selfJni(char * file1, char * file2, char * name1, char * name2, int limit)
{
    int retCode;
    int i;
    int id;
    struct Codemon_pkg pkg1;
    struct Codemon_pkg pkg2;

    retCode = 0;
    i = 0;
    id = 0;

    pkg1.lines = 0;
    pkg1.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg1.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg1.program[i] = 0;
    }

    pkg2.lines = 0;
    pkg2.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg2.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg2.program[i] = 0;
    }

    i = 0;

    if(file1 == NULL)
    {
        fprintf(stderr, "Error: no file1 name given!\n");
        return -1;
    }
    if(file2 == NULL)
    {
        fprintf(stderr, "Error: no file2 name given!\n");
        return -1;
    }
    if(name1 == NULL)
    {
        fprintf(stderr, "Error: no name1 name given!\n");
        return -1;
    }
    if(name2 == NULL)
    {
        fprintf(stderr, "Error: no name2 name given!\n");
        return -1;
    }
    if(limit < 0 || limit > MAXTURNS)
    {
        fprintf(stderr, "Error: invalid turn limit!\n");
        return -1;
    }

    retCode = readFile(file1, &(pkg1.begin), &(pkg1.lines), pkg1.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file 1!\n");
        return -1;
    }
    retCode = readFile(file2, &(pkg2.begin), &(pkg2.lines), pkg2.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file 2!\n");
        return -1;
    }

    strncpy(pkg1.name, name1, MAXNAME);
    strncpy(pkg2.name, name2, MAXNAME);

    id = runTest(&pkg1, &pkg2, limit);
    //printf("Report ID: %d\n", id);
    //used with make testAll -> comment the line above
    //printf("%d\n", id);

    return id;
}

/*
Desc: Runs a pvp request on the server with the supplied codemon.
Args: The number of players (int) and a pointer to the file name
containing the codemon (char *), a pointer to the codemon name (char *).
Ret: On failure -1 is returned, on success the report id is returned (int).
On success the codemon will be sent for a pvp request to the server,
run for the given number turns and the integer report id will be returned.
*/
int pvpJni(int players, char * fileName, char * name)
{
    int retCode;
    int i;
    int id;
    struct Codemon_pkg pkg;

    retCode = 0;
    i = 0;
    id = 0;

    pkg.lines = 0;
    pkg.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg.program[i] = 0;
    }

    i = 0;

    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no file name given!\n");
        return -1;
    }
    if(name == NULL)
    {
        fprintf(stderr, "Error: no name given!\n");
        return -1;
    }
    if(players < 2 || players > 4)
    {
        fprintf(stderr, "Error: invalid player count!\n");
        return -1;
    }

    retCode = readFile(fileName, &(pkg.begin), &(pkg.lines), pkg.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file!\n");
        return -1;
    }
    strncpy(pkg.name, name, MAXNAME);

    id = runPvP(&pkg, players);
    //printf("Report ID: %d\n", id);
    //used with make testAll -> comment the line above
    //printf("%d\n", id);

    return id;
}

/*
Desc: Gets a report from the server.
Args: A pointer to the report id (char *) and a pointer to the output file (char *).
Ret: -1 on failure and 1 on success (int).
On success, the report will be printed to file.
*/
int reportJni(char * reportID, char * fileName)
{
    FILE * file;
    int retVal;

    file = NULL;
    retVal = 0;

    if(reportID == NULL)
    {
        fprintf(stderr, "Error: no reportID given!\n");
        return -1;
    }
    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no reportID given!\n");
        return -1;
    }
    file = fopen(fileName, "w");
    if(file == NULL)
    {
        fprintf(stderr, "Error: could not open file!\n");
        return -1;
    }

    retVal = getReport(atoi(reportID), file);
    fclose(file);

    return retVal;
}


/*
Desc: Compiles codemon instructions into a binary blob passed to stdout.
Args: A pointer to the file name to compile (char *).
Ret: Returns -1 on error and 1 un success (int).
If successful, a binary blob will be printed to stdout.
*/
int compile(char * fileName)
{
    instruction * set;
    instruction * nav;
    uint32 start;
    uint64 temp;
    int retCode;

    set = NULL;
    nav = NULL;
    start = 0;
    retCode = 0;
    temp = 0;

    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no file name given!\n");
        return -1;
    }

    set = parseFile(fileName, &start);
    if(set == NULL)
    {
        fprintf(stderr, "Error: error parsing file!\n");
        return -1;
    }
    retCode = updateLocations(set);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not update locations!\n");
        retCode = clean(&set);
        return -1;
    }
    retCode = updateLabels(set);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not update labels!\n");
        retCode = clean(&set);
        return -1;
    }
    retCode = errorCheck(set);
    if(retCode < 0)
    {
        clean(&set);
        return retCode;
    }
    retCode = generateBitStrings(set);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not generate bit strings!\n");
        retCode = clean(&set);
        return -1;
    }

    nav = set;

    fwrite(&start, sizeof(uint32), 1, stdout);
    while(nav != NULL)
    {
        temp = nav->bitStr;
        fwrite(&temp, sizeof(uint64), 1, stdout);
        nav = nav->next;
    }


    retCode = clean(&set);

    return 1;
}

/*
Desc: Runs a solo self test on the server using a single codemon.
Args: A pointer to a binary file name containing the codemon (char *),
and the limit of turns that the codemon should run for (int).
Ret: On failure -1 is returned, on success 1 is returned (int).
On success the codemon will be sent for a solo test to the server,
run for the given turn limit and the integer report id will be
printed to stdout.
*/
int test(char * fileName, int limit)
{
    int retCode;
    int i;
    int id;
    struct Codemon_pkg pkg;

    retCode = 0;
    i = 0;
    id = 0;

    pkg.lines = 0;
    pkg.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg.program[i] = 0;
    }

    i = 0;

    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no file name given!\n");
        return -1;
    }
    if(limit < 0 || limit > MAXTURNS)
    {
        fprintf(stderr, "Error: invalid turn limit!\n");
        return -1;
    }

    retCode = readFile(fileName, &(pkg.begin), &(pkg.lines), pkg.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file!\n");
        return -1;
    }
    retCode = getCodemonName(fileName, (pkg.name));
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file name!\n");
        return -1;
    }

    //Debugging
    /*printf("begin = %d\n", pkg.begin);
    printf("lines = %d\n", pkg.lines);
    for(i = 0; i < MAXINST; i++)
    {
        printf("ins %d: %016llx\n", i, pkg.program[i]);
    }*/


    id = runTest(&pkg, NULL, limit);
    printf("Report ID: %d\n", id);
    //used with make testAll -> comment the line above
    //printf("%d\n", id);

    return 1;
}

/*
Desc: Runs a pvp self test on the server using two codemon.
Args: A pointer to a binary file name containing the first codemon (char *),
 A pointer to a binary file name containing the second codemon (char *),
and the limit of turns that the codemon should run for (int).
Ret: On failure -1 is returned, on success 1 is returned (int).
On success the codemon will be sent for a pvp test to the server,
run for the given turn limit and the integer report id will be
printed to stdout.
*/
int self(char * file1, char * file2, int limit)
{
    int retCode;
    int i;
    int id;
    struct Codemon_pkg pkg1;
    struct Codemon_pkg pkg2;

    retCode = 0;
    i = 0;
    id = 0;

    pkg1.lines = 0;
    pkg1.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg1.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg1.program[i] = 0;
    }

    pkg2.lines = 0;
    pkg2.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg2.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg2.program[i] = 0;
    }

    i = 0;

    if(file1 == NULL)
    {
        fprintf(stderr, "Error: no file1 name given!\n");
        return -1;
    }
    if(file2 == NULL)
    {
        fprintf(stderr, "Error: no file2 name given!\n");
        return -1;
    }
    if(limit < 0 || limit > MAXTURNS)
    {
        fprintf(stderr, "Error: invalid turn limit!\n");
        return -1;
    }

    retCode = readFile(file1, &(pkg1.begin), &(pkg1.lines), pkg1.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file 1!\n");
        return -1;
    }
    retCode = readFile(file2, &(pkg2.begin), &(pkg2.lines), pkg2.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file 2!\n");
        return -1;
    }
    retCode = getCodemonName(file1, (pkg1.name));
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file name 1!\n");
        return -1;
    }
    retCode = getCodemonName(file2, (pkg2.name));
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file name 2!\n");
        return -1;
    }

    id = runTest(&pkg1, &pkg2, limit);
    printf("Report ID: %d\n", id);
    //used with make testAll -> comment the line above
    //printf("%d\n", id);

    return 1;
}

/*
Desc: Runs a pvp request on the server with the supplied codemon.
Args: The number of players (int) and a pointer to the file name
containing the codemon (char *).
Ret: On failure -1 is returned, on success 1 is returned (int).
On success the codemon will be sent for a pvp request to the server,
run for the given number turns and the integer report id will be
printed to stdout.
*/
int pvp(int players, char * fileName)
{
    int retCode;
    int i;
    int id;
    struct Codemon_pkg pkg;

    retCode = 0;
    i = 0;
    id = 0;

    pkg.lines = 0;
    pkg.begin = 0;
    for(i = 0; i < (MAXNAME + 1); i++)
    {
        pkg.name[i] = 0;
    }
    for(i = 0; i < MAXINST; i++)
    {
        pkg.program[i] = 0;
    }

    i = 0;

    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no file name given!\n");
        return -1;
    }
    if(players < 2 || players > 4)
    {
        fprintf(stderr, "Error: invalid player count!\n");
        return -1;
    }

    retCode = readFile(fileName, &(pkg.begin), &(pkg.lines), pkg.program);
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file!\n");
        return -1;
    }
    retCode = getCodemonName(fileName, (pkg.name));
    if(retCode == -1)
    {
        fprintf(stderr, "Error: could not read file name!\n");
        return -1;
    }

    id = runPvP(&pkg, players);
    printf("Report ID: %d\n", id);
    //used with make testAll -> comment the line above
    //printf("%d\n", id);

    return 1;
}

/*
Desc: Gets a report from the server.
Args: A pointer to the report id (char *).
Ret: -1 on failure and 1 on success (int).
On success, the report will be printed to stdout.
*/
int report(char * reportID)
{
    if(reportID == NULL)
    {
        fprintf(stderr, "Error: no reportID given!\n");
        return -1;
    }

    getReport(atoi(reportID), stdout);

    return 1;
}

/*
Desc: Reads a binary file into the predefeined server data structure.
Args: A pointer to a binary file name to read in (char *), a pointer
to a 32 bit integer that will contain the begin point (uint32 *),
a pointer to a 32 bit integer that will contain the line count
of the program (uint32 *), a reference to the array which will store
the instruction read in from the file (uint64[MAXINST]).
Ret: -1 on failure and 1 on success (int).
On success the begin point will be stored, as well as the number of
lines contained in the codemon file, and the instructions will
be stored in an array to be passed to the server.
*/
int readFile(char * fileName, uint32 * begin, uint32 * size, uint64 ins[MAXINST])
{
    FILE * f;
    uint32 i;
    int maxSize;

    f = NULL;
    i = 0;
    maxSize = 0;

    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no file name given!\n");
        return -1;
    }

    f = fopen(fileName, "rb");
    if(f == NULL)
    {
        fprintf(stderr, "Error: could not open file!\n");
        return -1;
    }

    fread(begin, sizeof(uint32), 1, f);
    //printf("line %d = %016llx\n",  i, ins[i]);
    while(feof(f) == 0)
    {
        i++;
        fread(&(ins[i-1]), sizeof(uint64), 1, f);
        //printf("line %d = %016llx\n",  i, ins[i-1]);
        if(i == 50)
        {
            fprintf(stderr, "Error: max instruction count reached. Program will be truncated here.\n");
            maxSize = 1;
            break;
        }
    }
    if(maxSize == 0)
    {
        i--;
        ins[i] = 0;
    }
    *size = i;

    fclose(f);
    return 1;
}

/*
Desc: Gets the name of the codemon from the file.
Args: A pointer to the file name (char *) and a reference
to the character array containing the codemon name (char [MAXNAME+1]).
Ret: -1 on failure and 1 on success.
On success the name of the codemon is stored into the provided array,
names exceeding the character limit will be truncated.
*/
int getCodemonName(char * fileName, char codemonName[MAXNAME+1])
{
    int i;

    i = 0;

    if(fileName == NULL)
    {
        fprintf(stderr, "Error: no file name given!\n");
        return -1;
    }
    if(codemonName == NULL)
    {
        fprintf(stderr, "Error: no codemon name given!\n");
        return -1;
    }

    while(i < strlen(fileName) && fileName[i] != '.' && i < MAXNAME)
    {
        codemonName[i] = fileName[i];
        i++;
    }
    if(i < strlen(fileName) && fileName[i] != '.' && i == MAXNAME)
    {
        fprintf(stderr, "Error: codemon name exceeds length limit, name has been truncated.\n");
    }
    codemonName[i] = '\0';


    return 1;
}

/*
Desc: Checks for errrors during the compilation process.
Args: A pointer to the instruction set (instruction *).
Ret: -1 on failure and 1 on success.
On failure the user will be notified via stdout of
the lines containing invalid instructions and
-1 will be returned.
*/
int errorCheck(instruction * set)
{
    int errors;
    int i;
    int found;
    instruction * head;
    instruction * nav;

    errors = 0;
    i = 0;
    found = 0;
    head = set;
    nav = head;

    if(set == NULL)
    {
        fprintf(stderr, "Error: no set given!\n");
        return -1;
    }

    while(set != NULL)
    {
        if(set->op != NULL)
        {
            if(strcmp("DAT", set->op) != 0 && strcmp("MOV", set->op) != 0 &&
               strcmp("ADD", set->op) != 0 && strcmp("SUB", set->op) != 0 &&
               strcmp("MUL", set->op) != 0 && strcmp("DIV", set->op) != 0 &&
               strcmp("MOD", set->op) != 0 && strcmp("JMP", set->op) != 0 &&
               strcmp("JMZ", set->op) != 0 && strcmp("JMN", set->op) != 0 &&
               strcmp("DJN", set->op) != 0 && strcmp("SEQ", set->op) != 0 &&
               strcmp("SNE", set->op) != 0 && strcmp("SLT", set->op) != 0 &&
               strcmp("SET", set->op) != 0 && strcmp("CLR", set->op) != 0 &&
               strcmp("FRK", set->op) != 0 && strcmp("NOP", set->op) != 0 &&
               strcmp("RND", set->op) != 0)
            {
                fprintf(stderr, "Error: invalid instruction at line %d.\n", set->pos);
                errors++;
            }
            if(strcmp("DAT", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix != '#') || (set->bField != NULL && set->bPrefix != '#'))
                {
                    fprintf(stderr, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
                if(set->bField == NULL)
                {
                    fprintf(stderr, "Error: missing b field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("MOV", set->op) == 0 || strcmp("ADD", set->op) == 0
               || strcmp("SUB", set->op) == 0 || strcmp("MUL", set->op) == 0
               || strcmp("DIV", set->op) == 0 || strcmp("MOD", set->op) == 0
               || strcmp("JMZ", set->op) == 0 || strcmp("JMN", set->op) == 0
               || strcmp("SEQ", set->op) == 0 || strcmp ("SNE", set->op) == 0
               || strcmp("SLT", set->op) == 0 || strcmp("DJN", set->op) == 0
               || strcmp("RND", set->op) == 0)
            {
                if(set->aField == NULL)
                {
                    fprintf(stderr, "Error: missing a field at line %d\n", set->pos);
                    errors++;
                }
                if(set->bField == NULL)
                {
                    fprintf(stderr, "Error: missing b field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("JMP", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(stderr, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
                if(set->aField == NULL)
                {
                    fprintf(stderr, "Error: missing a field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("JMZ", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(stderr, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("JMN", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(stderr, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("DJN", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(stderr, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("FRK", set->op) == 0)
            {
                if(set->aField == NULL)
                {
                    fprintf(stderr, "Error: missing a field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(set->aField != NULL)
            {
                if(set->aNum < 0)
                {
                    fprintf(stderr, "Error: invalid a field value at line %d\n", set->pos);
                    errors++;
                }
                /*else
                {
                    found = 1;
                    for(i = 0; i < strlen(set->aField); i++)
                    {
                        if(isalpha(set->aField[i]))
                        {
                            found = 0;
                            nav = head;
                            while(nav != NULL)
                            {
                                if(nav->label != NULL &&
                                   strcmp(nav->label, set->aField) == 0)
                                {
                                    found = 1;
                                }
                                nav = nav->next;
                            }
                            break;
                        }
                    }
                    if(found == 0)
                    {
                        fprintf(stderr, "Error: invalid a field value at line %d\n", set->pos);
                        errors++;
                    }
                }*/
            }
            if(set->bField != NULL)
            {
                if(set->bNum < 0)
                {
                    fprintf(stderr, "Error: invalid b field value at line %d\n", set->pos);
                    errors++;
                }
                /*else
                {
                    found = 1;
                    for(i = 0; i < strlen(set->bField); i++)
                    {
                        if(isalpha(set->bField[i]))
                        {
                            found = 0;
                            nav = head;
                            while(nav != NULL)
                            {
                                if(nav->label != NULL &&
                                   strcmp(nav->label, set->bField) == 0)
                                {
                                    found = 1;
                                }
                                nav = nav->next;
                            }
                            break;
                        }
                    }
                    if(found == 0)
                    {
                        fprintf(stderr, "Error: invalid b field value at line %d\n", set->pos);
                        errors++;
                    }
                }*/
            }
        }
        set = set->next;
    }

    if(errors == 1)
    {
        fprintf(stderr, "There is %d error present in the file.\n", errors);
        return (errors * -1);
    }
    if(errors > 1)
    {
        fprintf(stderr, "There are %d errors present in the file.\n", errors);
        return (errors * -1);
    }

    return 1;
}

/*
Desc: Checks for errrors during the compilation process.
Args: A pointer to the instruction set (instruction *).
Ret: -1 on failure and 1 on success.
On failure the user will be notified via stdout of
the lines containing invalid instructions and
-1 will be returned. Errors are reported to the error file
*/
int errorCheck2(instruction * set)
{
    int errors;
    int i;
    int found;
    instruction * head;
    instruction * nav;
    FILE * errorFile;

    errors = 0;
    i = 0;
    found = 0;
    head = set;
    nav = head;
    errorFile = NULL;

    if(set == NULL)
    {
        fprintf(stderr, "Error: no set given!\n");
        return -1;
    }

    errorFile = fopen("ERRORFILE.txt", "w");
    if(errorFile == NULL)
    {
        fprintf(stderr, "Error: could not open error file!");
        return -1;
    }

    while(set != NULL)
    {
        if(set->op != NULL)
        {
            if(strcmp("DAT", set->op) != 0 && strcmp("MOV", set->op) != 0 &&
               strcmp("ADD", set->op) != 0 && strcmp("SUB", set->op) != 0 &&
               strcmp("MUL", set->op) != 0 && strcmp("DIV", set->op) != 0 &&
               strcmp("MOD", set->op) != 0 && strcmp("JMP", set->op) != 0 &&
               strcmp("JMZ", set->op) != 0 && strcmp("JMN", set->op) != 0 &&
               strcmp("DJN", set->op) != 0 && strcmp("SEQ", set->op) != 0 &&
               strcmp("SNE", set->op) != 0 && strcmp("SLT", set->op) != 0 &&
               strcmp("SET", set->op) != 0 && strcmp("CLR", set->op) != 0 &&
               strcmp("FRK", set->op) != 0 && strcmp("NOP", set->op) != 0 &&
               strcmp("RND", set->op) != 0)
            {
                fprintf(errorFile, "Error: invalid instruction at line %d.\n", set->pos);
                errors++;
            }
            if(strcmp("DAT", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix != '#') || (set->bField != NULL && set->bPrefix != '#'))
                {
                    fprintf(errorFile, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
                if(set->bField == NULL)
                {
                    fprintf(errorFile, "Error: missing b field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("MOV", set->op) == 0 || strcmp("ADD", set->op) == 0
               || strcmp("SUB", set->op) == 0 || strcmp("MUL", set->op) == 0
               || strcmp("DIV", set->op) == 0 || strcmp("MOD", set->op) == 0
               || strcmp("JMZ", set->op) == 0 || strcmp("JMN", set->op) == 0
               || strcmp("SEQ", set->op) == 0 || strcmp ("SNE", set->op) == 0
               || strcmp("SLT", set->op) == 0 || strcmp("DJN", set->op) == 0
               || strcmp("RND", set->op) == 0)
            {
                if(set->aField == NULL)
                {
                    fprintf(errorFile, "Error: missing a field at line %d\n", set->pos);
                    errors++;
                }
                if(set->bField == NULL)
                {
                    fprintf(errorFile, "Error: missing b field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("JMP", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(errorFile, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
                if(set->aField == NULL)
                {
                    fprintf(errorFile, "Error: missing a field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("JMZ", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(errorFile, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("JMN", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(errorFile, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("DJN", set->op) == 0)
            {
                if((set->aField != NULL && set->aPrefix == '#'))
                {
                    fprintf(errorFile, "Error: invalid instruction at line %d.\n", set->pos);
                    errors++;
                }
            }
            if(strcmp("FRK", set->op) == 0)
            {
                if(set->aField == NULL)
                {
                    fprintf(errorFile, "Error: missing a field at line %d\n", set->pos);
                    errors++;
                }
            }
            if(set->aField != NULL)
            {
                if(set->aNum < 0)
                {
                    fprintf(errorFile, "Error: invalid a field value at line %d\n", set->pos);
                    errors++;
                }
                /*else
                {
                    found = 1;
                    for(i = 0; i < strlen(set->aField); i++)
                    {
                        if(isalpha(set->aField[i]))
                        {
                            found = 0;
                            nav = head;
                            while(nav != NULL)
                            {
                                if(nav->label != NULL &&
                                   strcmp(nav->label, set->aField) == 0)
                                {
                                    found = 1;
                                }
                                nav = nav->next;
                            }
                            break;
                        }
                    }
                    if(found == 0)
                    {
                        fprintf(stderr, "Error: invalid a field value at line %d\n", set->pos);
                        errors++;
                    }
                }*/
            }
            if(set->bField != NULL)
            {
                if(set->bNum < 0)
                {
                    fprintf(errorFile, "Error: invalid b field value at line %d\n", set->pos);
                    errors++;
                }
                /*else
                {
                    found = 1;
                    for(i = 0; i < strlen(set->bField); i++)
                    {
                        if(isalpha(set->bField[i]))
                        {
                            found = 0;
                            nav = head;
                            while(nav != NULL)
                            {
                                if(nav->label != NULL &&
                                   strcmp(nav->label, set->bField) == 0)
                                {
                                    found = 1;
                                }
                                nav = nav->next;
                            }
                            break;
                        }
                    }
                    if(found == 0)
                    {
                        fprintf(stderr, "Error: invalid b field value at line %d\n", set->pos);
                        errors++;
                    }
                }*/
            }
        }
        set = set->next;
    }

    if(errors == 1)
    {
        fprintf(errorFile, "There is %d error present in the file.\n", errors);
        fclose(errorFile);
        return (errors * -1);
    }
    if(errors > 1)
    {
        fprintf(errorFile, "There are %d errors present in the file.\n", errors);
        fclose(errorFile);
        return (errors * -1);
    }

    fclose(errorFile);

    return 1;
}
