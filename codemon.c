/*Name: Ryan Lafferty
ID: 0853370
Date: 9/18/2015
Assignment 1*/

#include "codemon.h"

int main(int argc, char * argv[])
{
    FILE * f;
    instruction * set;
    instruction * head;
    int c;
    uint32 start;

    f = NULL;
    start = 0;
    set = NULL;
    head = NULL;
    c = 0;
    start = 0;

    if(argc == 3 && strcmp(argv[1], "-c") == 0)
    {
        /*printf("Compile!\n");*/
        c = compile(argv[2]);
        return 0;
    }
    else if(argc == 4 && strcmp(argv[1], "-t") == 0)
    {
        /*printf("Test!\n");*/
        c = test(argv[2], atoi(argv[3]));
        return 0;
    }
    else if(argc == 5 && strcmp(argv[1], "-s") == 0)
    {
        /*printf("Self Test!\n");*/
        c = self(argv[2], argv[3], atoi(argv[4]));
        return 0;
    }
    else if(argc == 4 && strcmp(argv[1], "-p") == 0)
    {
        /*printf("PVP!\n");*/
        c = pvp(atoi(argv[2]), argv[3]);
        return 0;
    }
    else if(argc == 3 && strcmp(argv[1], "-r") == 0)
    {
        /*printf("Report!\n");*/
        c = report(argv[2]);
        return 0;
    }

    /*//Debugging Code
    else if(argc == 2 && strcmp(argv[1], "-m") == 0)
    {
        struct Codemon_pkg pkg;

        readFile("kraken.codemon", &(pkg.begin), &(pkg.lines), pkg.program);
        printf("%016llx\n", pkg.program[0]);

        return 0;
    }*/

    // make testAll code, uncomment when running script
    /*else if(argc == 3 && strcmp(argv[1], "-clean") == 0)
    {
        FILE * fp = fopen(argv[2], "r");
        int idd;
        char buffer[100];
        while((fgets(buffer, 100, fp)) != 0)
        {
            //printf("%d\n", (atoi(buffer)));
            sleep(1);
            idd = atoi(buffer);
            if(idd != 0)
            {
            getReport(idd,stdout);
            }
        }
        fclose(fp);
        return 0;
    }*/
    else
    {
        fprintf(stderr, "Error: invalid command line arguments!\n");
        return 0;
    }


    /*Debugging Code Below*/
    set = parseFile("errorFile", &start);
    if(set == NULL)
    {
        fprintf(stderr, "Error: no instruction set created!\n");
        return 1;
    }

    printf("Start = %d\n", start);

    c = updateLocations(set);

    /*Test A Field Values*/
    uint64 aNumMask = set->aNum;
    aNumMask = aNumMask << 41;
    printf("%016llx\n", aNumMask);

    updateLabels(set);

    c = errorCheck(set);
    if(c == -1)
    {
        clean(&set);
        return 0;
    }
    generateBitStrings(set);

    head = set;
    while(set != NULL && set->next != NULL)
    {
        printf("\n\npos = %d\n", set->pos);
        if(set->label != NULL)
        {
            printf("label = %s\n", set->label);
        }
        printf("op = %s\n", set->op);
        printf("bit String = %016llx\n", set->bitStr);
        if(set->aField != NULL)
        {
            printf("aField = %s\n", set->aField);
            printf("aPre = %c\n", set->aPrefix);
            printf("aNum = %d\n", set->aNum);
        }
        if(set->bField != NULL)
        {
            printf("bField = %s\n", set->bField);
            printf("bPre = %c\n", set->bPrefix);
            printf("bNum = %d\n", set->bNum);
        }
        set = set->next;
    }
    printf("\n\npos = %d\n", set->pos);
    printf("op = %s\n", set->op);
    if(set->label != NULL)
    {
        printf("label = %s\n", set->label);
    }
    printf("bit String = %016llx\n", set->bitStr);
    if(set->aField != NULL)
    {
        printf("aField = %s\n", set->aField);
        printf("aPre = %c\n", set->aPrefix);
        printf("aNum = %d\n", set->aNum);
    }
    if(set->bField != NULL)
    {
        printf("bField = %s\n", set->bField);
        printf("bPre = %c\n", set->bPrefix);
        printf("bNum = %d\n", set->bNum);
    }
    set = head;

    c = clean(&set);
    if(c <= 0)
    {
        fprintf(stderr, "Error: possible leaking memory.\n");
        return 1;
    }
    set = NULL;

    return 0;
}
