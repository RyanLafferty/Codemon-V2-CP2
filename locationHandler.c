/*Name: Ryan Lafferty
ID: 0853370
Date: 9/18/2015
Assignment 1*/

#include "codemon.h"

/*
Def: Updates locations associated with the aField and bField of an instruction.
Args: A pointer to an instruction (instruction *).
Ret: 1 on success and -1 on failure (int).
*/
int updateLocations(instruction * set)
{
    char buffer[1000];
    int i;
    int neg;

    i = 0;
    neg = 0;

    for(i = 0; i < 1000; i++)
    {
        buffer[i] = 0;
    }

    i = 0;

    if(set == NULL)
    {
        fprintf(stderr, "Error: no instruction set given!\n");
        return -1;
    }

    while(set != NULL)
    {
        if(set->aField != NULL)
        {
            set->aPrefix = getPrefix(set->aField);
            neg = isNegative(set->aField);
            if(neg == 1)
            {
                set->aNum = getNumber(set->aField);
                set->aNum = (set->aNum * -1);
                while(set->aNum < 0)
                {
                    set->aNum = set->aNum + 8192;
                }
                /*printf("%s is negative\n", set->aField);
                printf("%d\n", set->aNum);*/
            }
            else if(set->aNum >= 0)
            {
                set->aNum = getNumber(set->aField);
            }

            if(set->aNum > 8191)
            {
                set->aNum = set->aNum % 8192;
            }
        }
        if(set->bField != NULL)
        {
            set->bPrefix = getPrefix(set->bField);
            neg = isNegative(set->bField);
            if(neg == 1)
            {
                set->bNum = getNumber(set->bField);
                set->bNum = (set->bNum * -1);
                while(set->bNum < 0)
                {
                    set->bNum = set->bNum + 8192;
                }
                /*printf("%s is negative\n", set->bField);
                printf("%d\n", set->bNum);*/
            }
            else if(set->bNum >= 0)
            {
                set->bNum = getNumber(set->bField);
            }

            if(set->bNum > 8191)
            {
                set->bNum = set->bNum % 8192;
            }
        }
        set = set->next;
    }

    return 1;
}

/*
Def: Get's the prefix of a given field value.
Args: A pointer to the field (char *).
Ret: The prefix character on success and 0 on failure (char).
*/
char getPrefix(char * str)
{
    int i;

    i = 0;

    if(str == NULL)
    {
        return 0;
    }

    for(i = 0; i < strlen(str); i++)
    {
        if(isdigit(str[i]))
        {
            return 0;
        }
        else if(str[i] == '-')
        {
            return 0;
        }
        else if(isalpha(str[i]))
        {
            return 0;
        }
        else
        {
            return str[i];
        }
    }

    return 0;
}

/*
Def: Checks if the field is negative.
Args: A pointer to the field (char *).
Ret: 0 if positive, 1 if negative and -1 on failure (int).
*/
int isNegative(char * str)
{
    int i;

    i = 0;

    if(str == NULL)
    {
        fprintf(stderr, "Error: no string given!\n");
        return -1;
    }

    for(i = 0; i < strlen(str); i++)
    {
        if(isalpha(str[i]))
        {
            return 0;
        }
        if(str[i] == '-')
        {
            return 1;
        }
    }

    return 0;
}

/*
Def: Get's a number from a supplied field.
Args: A pointer to the field (char *).
Ret: On success, returns the 32 bit integer from the field (int).
On failure returns -1 (int).
*/
int getNumber(char * str)
{
    int i;
    int j;
    int gotNum;
    char buffer[1000];

    i = 0;
    j = 0;
    gotNum = 0;
    for(i = 0; i < 1000; i++)
    {
        buffer[i] = 0;
    }

    if(str == NULL)
    {
        fprintf(stderr, "Error: no string given!\n");
        return -1;
    }

    for(i = 0; i < strlen(str); i++)
    {
        if(isalpha(str[i]))
        {
            return -1;
        }
        if(isdigit(str[i]))
        {
            gotNum = 1;
            buffer[j] = str[i];
            j++;
        }
    }

    if(gotNum > 0)
    {
        buffer[j] = '\0';
        return atoi(buffer);
    }

    return -1;
}

/*
Def: Updates the label references for each instruction.
Args: A pointer to the instruction set (instruction *).
Ret: -1 on failure and 1 on success (int).
*/
int updateLabels(instruction * set)
{
    instruction * head;
    instruction * nav;
    int stop1;
    int stop2;
    int location;
    int i;
    int j;
    int match;

    head = set;
    nav = NULL;
    stop1 = 0;
    stop2 = 0;
    location = 0;
    i = 0;
    j = 0;
    match = 1;

    if(set == NULL)
    {
        fprintf(stderr, "Error: no instruction set given!\n");
        return -1;
    }

    while(set != NULL && stop1 == 0)
    {
        if(set->label != NULL)
        {
            nav = head;
            stop2 = 0;
            while(nav != NULL && stop2 == 0)
            {
                //modify this section to allow for prefixes -> modified, labels can no longer start with a numeric value
                //possibly change detection method to detect if there is a prefix
                if(nav->aField != NULL)
                {
                    match = 1;
                    if(!isdigit(nav->aField[0]) && strcmp(set->label, nav->aField) != 0)
                    {
                        for(i = 0; i < strlen(set->label); i++)
                        {
                            if((i+1) < strlen(nav->aField) && set->label[i] == nav->aField[i+1])
                            {

                            }
                            else
                            {
                                //printf("%s: %c != %c\n", nav->aField, set->label[i], nav->aField[i+1]);
                                match = 0;
                                break;
                            }
                        }
                    }
                    if(match == 1)
                    {
                        location = set->pos - nav->pos;
                        if(location < 0)
                        {
                            location = location + 8192;
                        }
                        nav->aNum = location;
                    }
                }
                if(nav->bField != NULL)
                {
                    match = 1;
                    if(!isdigit(nav->bField[0]) && strcmp(set->label, nav->bField) != 0)
                    {
                        for(i = 0; i < strlen(set->label); i++)
                        {
                            if((i+1) < strlen(nav->bField) && set->label[i] == nav->bField[i+1])
                            {

                            }
                            else
                            {
                                //printf("%s: %c != %c\n", nav->bField, set->label[i], nav->bField[i+1]);
                                match = 0;
                                break;
                            }
                        }
                    }
                    if(match == 1)
                    {
                        //printf("%s matched\n", nav->bField);
                        location = set->pos - nav->pos;
                        if(location < 0)
                        {
                            location = location + 8192;
                        }
                        nav->bNum = location;
                    }
                }
                if(nav->next != NULL)
                {
                    nav = nav->next;
                }
                else
                {
                    stop2 = 1;
                }
            }
        }
        if(set->next != NULL)
        {
            set = set->next;
        }
        else
        {
            stop1 = 1;
        }
    }

    return 1;
}
