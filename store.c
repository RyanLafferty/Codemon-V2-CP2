/*Name: Ryan Lafferty
ID: 0853370
Date: 9/18/2015
Assignment 1*/

#include "codemon.h"

/*
Def: Stores an instruction into memory.
Args: A pointer to a label (char *) if there is one,
a pointer to an opcode (char *), a pointer to the afield (char *)
if there is one, a pointer to the bfield (char *) if there is one,
an integer (int) that specifies the current instruction position.
Ret: Returns a pointer to the instruction on success
and returns NULL on failure (instruction *).
*/
instruction * storeInstruction(char * label, char * op, char * aField, char * bField, int pos)
{
    instruction * inst;

    inst = NULL;

    if(op == NULL)
    {
        return NULL;
    }

    inst = malloc(sizeof(instruction)*1);
    if(inst == NULL)
    {
        fprintf(stderr, "Error: out of memory\n");
        return NULL;
    }

    inst->pos = pos;
    inst->label = NULL;
    inst->op = NULL;
    inst->aField = NULL;
    inst->bField = NULL;

    inst->aPrefix = 0;
    inst->aNum = 0;
    inst->bPrefix = 0;
    inst->bNum = 0;

    inst->bitStr = 0;

    inst->next = NULL;

    if(label != NULL)
    {
        inst->label = label;
    }
    if(op != NULL)
    {
        inst->op = op;
    }
    if(aField != NULL)
    {
        inst->aField = aField;
    }
    if(bField != NULL)
    {
        inst->bField = bField;
    }

    return inst;
}
