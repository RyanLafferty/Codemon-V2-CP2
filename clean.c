/*Name: Ryan Lafferty
ID: 0853370
Date: 9/18/2015
Assignment 1*/

#include "codemon.h"

/*
Def: Cleans all memory associated with an instruction set.
Args: A double pointer to the instruction set (instruction **).
Ret: 1 on success and -1 on failure (int).
*/
int clean(instruction ** set)
{
    instruction * temp;

    temp = NULL;

    if(*set == NULL)
    {
        fprintf(stderr, "Error: cannot clean an empty list!\n");
        return -1;
    }

    while((*set) != NULL && (*set)->next != NULL)
    {
        if((*set) != NULL)
        {
            /*printf("Destroying %s\n", (*set)->op);*/
            temp = (*set);
            (*set) = (*set)->next;
            destroyInstruction(temp);
            temp = NULL;
        }
    }

    if((*set) != NULL)
    {
        /*printf("Destroying %s\n", (*set)->op);*/
        temp = (*set);
        destroyInstruction(temp);
        temp = NULL;
        *(set) = NULL;
    }

    return 1;
}

/*
Def: Cleans memory associated with aninstruction.
Args: A pointer to the instruction (instruction *).
Ret: 1 on success and -1 on failure (int).
*/
int destroyInstruction(instruction * i)
{
    if(i == NULL)
    {
        fprintf(stderr, "Error: cannot destroy an empty instruction!\n");
        return -1;
    }
    if(i->label != NULL)
    {
        /*printf("freed %s\n", i->label);*/
        free(i->label);
    }
    if(i->op != NULL)
    {
        /*printf("freed %s\n", i->op);*/
        free(i->op);
    }
    if(i->aField != NULL)
    {
        /*printf("freed %s\n", i->aField);*/
        free(i->aField);
    }
    if(i->bField != NULL)
    {
        /*printf("freed %s\n", i->bField);*/
        free(i->bField);
    }

    free(i);
    return 1;
}
