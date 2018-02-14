/*Name: Ryan Lafferty
ID: 0853370
Date: 9/18/2015
Assignment 1*/

#include "codemon.h"

/*
Def: Generates the bit string instructions to be later executed by the server.
Args: A pointer to the instruction set (instruction *).
Ret: On success 1 and on failure -1 (int).
On success the bit strings will be stored in the instruction set.
*/
int generateBitStrings(instruction * set)
{
    uint64 opMask;
    uint64 aModeMask;
    uint64 bModeMask;
    uint64 aNumMask;
    uint64 bNumMask;
    uint64 bitStr;

    opMask = 0;
    aModeMask = 0;
    bModeMask = 0;
    aNumMask = 0;
    bNumMask = 0;
    bitStr = 0;

    if(set == NULL)
    {
        fprintf(stderr, "Error: no set given!\n");
        return -1;
    }

    while(set != NULL)
    {
        if(set != NULL)
        {
            opMask = generateOPBitMask(set->op);
            aModeMask = generateModeMask(set->aPrefix);
            bModeMask = generateModeMask(set->bPrefix);

            if(set->aNum < 0)
            {
                set->aNum = 0;
            }
            if(set->bNum < 0)
            {
                set->bNum = 0;
            }

            aNumMask =  (uint64) set->aNum;
            bNumMask =  (uint64) set->bNum;

            //Debugging
            /*printf("op = %s\n", set->op);
            printf("aNum = %d\n", set->aNum);
            printf("aNum mask = %llu\n", aNumMask);
            printf("bNum = %d\n", set->bNum);
            printf("bNum mask = %llu\n", bNumMask);
            */

            opMask = opMask << 58;
            aModeMask = aModeMask << 54;
            aNumMask = aNumMask << 29;
            bModeMask = bModeMask << 25;
            bNumMask = bNumMask << 0;

            bitStr = 0;

            /*manipulate bit string*/
            bitStr = bitStr | opMask;
            bitStr = bitStr | aModeMask;
            bitStr = bitStr | aNumMask;
            bitStr = bitStr | bModeMask;
            bitStr = bitStr | bNumMask;

            set->bitStr = bitStr;

            //Debugging
            /*printf("\n\n%016llx\n", opMask);
            printf("%016llx\n", aModeMask);
            printf("%016llx\n", aNumMask);
            printf("%016llx\n", bModeMask);
            printf("%016llx\n", bNumMask);
            printf("%016llx\n\n", bitStr);*/
        }
        set = set->next;
    }

    return 1;
}

/*
Def: Generates the op code bit mask.
Args: A pointer to the op code (char *).
Ret: An unsigned 64 bit integer mask to be used
to create the bit strings (uint64). Returns 0 on failure.
*/
uint64 generateOPBitMask(char * op)
{
    int i;
    uint64 opMask;

    i = 0;
    opMask = 0;

    if(op == NULL)
    {
        fprintf(stderr, "Error: no op code given!\n");
        return 0;
    }

    for(i = 0; i < strlen(op); i++)
    {
        if(isalpha(op[i]))
        {
            op[i] = toupper(op[i]);
        }
    }

    /*Opcode Lookup Table*/
    if(strcmp("DAT", op) == 0)
    {
        opMask = 0x00;
    }
    else if(strcmp("MOV", op) == 0)
    {
        opMask = 0x01;
    }
    else if(strcmp("ADD", op) == 0)
    {
        opMask = 0x02;
    }
    else if(strcmp("SUB", op) == 0)
    {
        opMask = 0x03;
    }
    else if(strcmp("MUL", op) == 0)
    {
        opMask = 0x04;
    }
    else if(strcmp("DIV", op) == 0)
    {
        opMask = 0x05;
    }
    else if(strcmp("MOD", op) == 0)
    {
        opMask = 0x06;
    }
    else if(strcmp("JMP", op) == 0)
    {
        opMask = 0x07;
    }
    else if(strcmp("JMZ", op) == 0)
    {
        opMask = 0x08;
    }
    else if(strcmp("JMN", op) == 0)
    {
        opMask = 0x09;
    }
    else if(strcmp("DJN", op) == 0)
    {
        opMask = 0x0A;
    }
    else if(strcmp("SEQ", op) == 0)
    {
        opMask = 0x0B;
    }
    else if(strcmp("SNE", op) == 0)
    {
        opMask = 0x0C;
    }
    else if(strcmp("SLT", op) == 0)
    {
        opMask = 0x0D;
    }
    else if(strcmp("SET", op) == 0)
    {
        opMask = 0x0E;
    }
    else if(strcmp("CLR", op) == 0)
    {
        opMask = 0x0F;
    }
    else if(strcmp("FRK", op) == 0)
    {
        opMask = 0x10;
    }
    else if(strcmp("NOP", op) == 0)
    {
        opMask = 0x11;
    }
    else if(strcmp("RND", op) == 0)
    {
        opMask = 0x12;
    }
    else
    {
        opMask = 0x00;
    }

    return opMask;
}

/*
Def: Generates the mode mask used to create bit strings.
Args: A character containing the field mode (char).
Ret: On success a 64 bit unsigned integer containing the mode mask (uint64).
On failure, 0 is returned.
*/
uint64 generateModeMask(char mode)
{
    uint64 modeMask;

    modeMask = 0x00;

    /*Addressing Mode Lookup Table*/
    if(mode == '$' || mode == 0)
    {
        modeMask = 0x00;
    }
    else if(mode == '#')
    {
        modeMask = 0x01;
    }
    else if(mode == '[')
    {
        modeMask = 0x02;
    }
    else if(mode == ']')
    {
        modeMask = 0x03;
    }
    else if(mode == '*')
    {
        modeMask = 0x04;
    }
    else if(mode == '@')
    {
        modeMask = 0x05;
    }
    else if(mode == '{')
    {
        modeMask = 0x06;
    }
    else if(mode == '}')
    {
        modeMask = 0x07;
    }
    else if(mode == '<')
    {
        modeMask = 0x08;
    }
    else if(mode == '>')
    {
        modeMask = 0x09;
    }
    else
    {
        modeMask = 0x00;
    }

    return modeMask;
}
