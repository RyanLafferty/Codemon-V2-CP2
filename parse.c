/*Name: Ryan Lafferty
ID: 0853370
Date: 9/18/2015
Assignment 1*/

#include "codemon.h"

/*
Def: Parses a file into a data structure.
Args: A pointer to the file name to parse from (char *),
and a pointer to the 32 bit integer which will store
the start value (uint32 *).
Ret: On success, a pointer to the data structure containing
the parsed instructions (instruction *). On failure NULL is returned.
*/
instruction * parseFile(char * fileName, uint32 * start)
{
    int currentInstruction;
    int state;
    int i;
    int ii;
    int gotLabel;
    int gotOP;
    int gotAField;
    int gotBField;
    int previousState;
    int pos;
    int whiteS;
    char ch;
    char buffer[1000];
    char * aField;
    char * bField;
    char * label;
    char * op;
    instruction * set;
    instruction * head;
    FILE * f;
    uint32 tempStart;

    currentInstruction = 0;
    previousState = START;
    state = START;
    ch = 'a';
    i = 0;
    ii = 0;
    gotLabel = 0;
    gotOP = 0;
    gotAField = 0;
    gotBField = 0;
    aField = NULL;
    bField = NULL;
    label = NULL;
    op = NULL;
    set = NULL;
    head = NULL;
    f = NULL;
    tempStart = 0;
    whiteS = 0;

    f = fopen(fileName, "r");
    if(f == NULL)
    {
        fprintf(stderr, "Error: Could not open file!\n");
        return NULL;
    }

    for(i = 0; i < 1000; i++)
    {
        buffer[i] = 0;
    }

    if(f == NULL)
    {
        fprintf(stderr, "Error: no file given!\n");
        return NULL;
    }

    i = 0;
    pos = 0;

    f = getStart(f, &tempStart);
    *start = tempStart;

    while(ch != EOF)
    {
        ch = fgetc(f);
        whiteS--;
        if((ch == ' ' || ch == '\n' || ch == '\t' || ch == '!') && whiteS >= 0)
        {
            while(ch == ' ' || ch == '\n' || ch == '\t')
            {
                if(ch == EOF || isalpha(ch) || ch == '!')
                {
                    break;
                }
                ch = fgetc(f);
                if(ch == EOF || isalpha(ch) || ch == '!')
                {
                    break;
                }
            }
        }

        if(state == START && isalpha(ch))
        {
            state = ALPHA;
        }
        if(ch == EOF)
        {
            state = DONE;
        }
        if(ch == '!')
        {
            previousState = state;
            state = COMMENT;
        }
        if(ch == ':' && state == ALPHA)
        {
            state = LABEL;
            whiteS = 1;
        }
        if((ch == ' ' || ch == '\n' || ch == '\t') && state == ALPHA) //change #1 based on feedback
        {
            state = OPCODE;
        }

        switch(state)
        {
            case LABEL:
            {
                buffer[i] = '\0';

                label = malloc(sizeof(char)*(i+1));
                if(label == NULL)
                {
                    fprintf(stderr, "Error: out of memory!\n");
                    return head;
                }
                strncpy(label, buffer, (i+1));
                /*printf("Label = %s\n", label);*/

                state = ALPHA;
                i = 0;
                break;
            }
            case OPCODE:
            {
                buffer[i] = '\0';
                /*printf("Opcode = %s\n", buffer);*/

                op = malloc(sizeof(char)*(i+1));
                if(op == NULL)
                {
                    fprintf(stderr, "Error: out of memory!\n");
                    return head;
                }
                strncpy(op, buffer, (i+1));
                /*printf("op = %s\n", op);*/

                for(ii = 0; ii < strlen(op); ii++)
                {
                    if(isalpha(op[ii]))
                    {
                        op[ii] = toupper(op[ii]);
                    }
                }

                state = AFIELD;
                i = 0;
                break;
            }
            case AFIELD:
            {
                if(isalpha(ch) || isdigit(ch) || ch == '$' || ch == '#' || ch == '[' || ch == ']' || ch == '@'
                || ch == '*' || ch == '{' || ch == '}' || ch == '<' || ch == '>' || ch == '-')
                {
                    f = getField(f, &aField, &ch);
                }
                else if((ch == '!' || ch == ';') &&
                (strcmp(op, "SET") == 0 || strcmp(op, "CLR") == 0 ||strcmp(op, "NOP") == 0))
                {
                    ch = ';';
                    break;
                }

                if(aField != NULL)
                {
                    gotAField = 1;
                    state = 99;
                    /*printf("a = %s\n", aField);*/
                }
                break;
            }
            case BFIELD:
            {
                f = getField(f, &bField, &ch);
                if(bField != NULL)
                {
                    gotBField = 1;
                    state = 99;
                    /*printf("b = %s\n", bField);*/
                }
                break;
            }
            case COMMENT:
            {
                /*printf("got here C\n");*/
                f = getComment(f);
                if(f == NULL)
                {
                    return head;
                }
                state = previousState;
                break;
            }
            case ALPHA:
            {
                buffer[i] = ch;
                i++;
                break;
            }
            default:
            {
                /*printf("%c", ch);*/
                break;
            }
        }

        if(ch == ';')
        {
            state = START;
            if(head == NULL && op != NULL)
            {
                head = storeInstruction(label, op, aField, bField, pos);
                set = head;
            }
            else
            {
                set->next = storeInstruction(label, op, aField, bField, pos);
                set = set->next;
            }

            i = 0;
            pos++;
            gotLabel = 0;
            gotOP = 0;
            gotAField = 0;
            gotBField = 0;
            whiteS = 0;

            label = NULL;
            op = NULL;
            aField = NULL;
            bField = NULL;
        }
        if(ch == ',')
        {
            state = BFIELD;
        }
    }

    updateFields(head);
    fclose(f);

    set = NULL;
    return head;
}

/*
Def: Gets the start value from the file.
Args: A pointer to the file (FILE *) and a pointer to an integer to
store the start value in (int *).
Ret: Returns a pointer to the new file position on success and
returns NULL on failure (FILE *).
*/
FILE * getStart(FILE * f, uint32 * start)
{
    char ch;
    int i;
    int foundStart;
    int state;
    int negative;
    char buffer[1000];

    ch = 'a';
    i = 0;
    foundStart = 0;
    state = 0;
    negative = 0;

    for(i = 0; i < 1000; i++)
    {
        buffer[i] = 0;
    }

    i = 0;

    if(f == NULL)
    {
        fprintf(stderr, "Error: no file given!\n");
        return f;
    }

    while(ch != EOF && ch != ';')
    {
        ch = fgetc(f);
        if(ch == '@')
        {
            state = 1;
        }
        if(ch != '@' && state == 1)
        {
            if(ch == '-')
            {
                negative = 1;
            }
            if(isdigit(ch))
            {
                foundStart = 1;
                buffer[i] = ch;
                i++;
            }
        }
    }

    if(foundStart > 0)
    {
        buffer[i] = '\0';
        *start = atoi(buffer);
    }

    return f;
}

/*
Def: Destroy's a comment in the file.
Args: A pointer to the file (FILE *).
Ret: Returns a pointer to the file on success and returns
NULL on failure (FILE *).
*/
FILE * getComment(FILE * f)
{
    char ch;

    ch = 'a';

    if(f == NULL)
    {
        fprintf(stderr, "Error: no file given!\n");
        return f;
    }

    while(ch != '\n' && ch != EOF)
    {
        ch = fgetc(f);
    }

    if(ch == EOF)
    {
        fclose(f);
        return NULL;
    }

    return f;
}

/*
Def: Get's a field value from the file.
Args: A pointer to the file (FILE *), a double pointer to a string (char **)
and a pointer to a character (char *).
Ret: Returns a pointer to the file on success and NULL
on failure (FILE *).
On Success: Allocates and stores the field value using the field pointer,
stores the current character using the character pointer.
*/
FILE * getField(FILE * f, char ** field, char * ch)
{
    char buffer[1000];
    int i;
    int gotField;

    i = 0;
    gotField = 0;

    for(i = 0; i < 1000; i++)
    {
        buffer[i] = 0;
    }

    if(f == NULL)
    {
        fprintf(stderr, "Error: no file given!\n");
        return f;
    }

    i = 0;

    while(*ch != EOF && *ch != ',' && *ch != ';')
    {
        if(*ch == '!')
        {
            f = getComment(f);
        }
        else if(*ch != ' ' && *ch != '\t' && *ch != '\n')
        {
            gotField = 1;
            buffer[i] = *ch;
            i++;
        }
        *ch = fgetc(f);
    }

    if(gotField > 0)
    {
        buffer[i] = '\0';
        *field = malloc(sizeof(char)*(i+1));
        if(*field == NULL)
        {
            fprintf(stderr, "Error: out of memory!\n");
            fclose(f);
            return NULL;
        }
        strncpy(*field, buffer, (i+1));
    }

    return f;
}

/*
Def: Updates the a and b fields of an instruction if neccessary.
Args: A pointer to the instruction set (instruction *).
Ret: On success 1 and on failure -1 (int).
On success the a and b fileds will be updated in order
to align the fields according to the assignment spec.
*/
int updateFields(instruction * set)
{
    int swap;

    swap = 0;

    if(set == NULL)
    {
        fprintf(stderr, "Error: no instruction set given!\n");
        return -1;
    }

    while(set != NULL)
    {
        swap = 0;
        if(set->op != NULL && strcmp(set->op, "DAT") == 0
        && set->bField == NULL && set->aField != NULL)
        {
            swap = 1;
        }
        if(set->op != NULL && strcmp(set->op, "SET") == 0
        && set->bField == NULL && set->aField != NULL)
        {
            swap = 1;
        }
        if(set->op != NULL && strcmp(set->op, "CLR") == 0
        && set->bField == NULL && set->aField != NULL)
        {
            swap = 1;
        }
        if(set->op != NULL && strcmp(set->op, "NOP") == 0
        && set->bField == NULL && set->aField != NULL)
        {
            swap = 1;
        }

        if(set->op != NULL && strcmp(set->op, "SET") == 0 && set->aField == NULL && set->bField == NULL)
        {
            set->aNum = 0;
            set->bNum = 0;
            set->aPrefix = '#';
            set->bPrefix = '#';
        }
        if(set->op != NULL && strcmp(set->op, "CLR") == 0 && set->aField == NULL && set->bField == NULL)
        {
            set->aNum = 0;
            set->bNum = 0;
            set->aPrefix = '#';
            set->bPrefix = '#';
        }
        if(set->op != NULL && strcmp(set->op, "NOP") == 0 && set->aField == NULL && set->bField == NULL)
        {
            set->aNum = 0;
            set->bNum = 0;
            set->aPrefix = '#';
            set->bPrefix = '#';
        }
        if(set->op != NULL && strcmp(set->op, "JMP") == 0 && set->bField == NULL)
        {
            set->bNum = 0;
            set->bPrefix = '#';
        }
        if(set->op != NULL && strcmp(set->op, "FRK") == 0 && set->bField == NULL)
        {
            set->bNum = 0;
            set->bPrefix = '#';
        }

        if(swap == 1)
        {
            set->bField = set->aField;
            set->bPrefix = set->aPrefix;
            set->bNum = set->aNum;

            set->aField = NULL;
            set->aNum = 0;
            set->aPrefix = '#';
        }

        set = set->next;
    }

    return 1;
}
