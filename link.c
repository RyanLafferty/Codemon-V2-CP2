#include "codemon.h"

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

/*
 * Class:     Codemon_Codemon
 * Method:    compileToFile
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
 /*
 Def: Compiles a codemon to a binary file.
 Args: The java environment object (JNIEnv *), the class object (jobject),
       a string of the code file path (jstring) and a string to the binary file path (jstring).
 Ret: On success 0 or a positive integer and on failure a negative integer (int).
 */
JNIEXPORT jint JNICALL Java_Codemon_Codemon_compileToFile
  (JNIEnv * env, jobject obj, jstring f1, jstring f2)
{
    if(f1 == NULL || f2 == NULL)
    {
        printf("Error: bad file(s) given\n");
        return -1;
    }

    const char * file1 = (*env)->GetStringUTFChars(env, f1, NULL);
    const char * file2 = (*env)->GetStringUTFChars(env, f2, NULL);
    int errorCount = 0;

    if(file1 == NULL || file2 == NULL)
    {
        printf("Error: bad file(s) given\n");
        if(file1 != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, f1, file1);
        }
        if(file2 != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, f2, file2);
        }
        return -1;
    }

    //printf("FILE NAME = %s\n", fileName);
    errorCount = compileToFile((char *) file1, (char *) file2);

    (*env)->ReleaseStringUTFChars(env, f1, file1);
    (*env)->ReleaseStringUTFChars(env, f2, file2);
    return errorCount;
}

/*
 * Class:     Codemon_Codemon
 * Method:    testJni
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)I
 */
 /*
 Def: Runs a self test request on the server.
 Args: The java environment object (JNIEnv *), the class object (jobject),
       a string to the file path (jstring), a string of the codemon name (jstring)
       and the turn limit (jint).
 Ret: Returns a negative value on failure and a positive value on success (int).
      Where the positive value is the reportID.
 */
JNIEXPORT jint JNICALL Java_Codemon_Codemon_testJni
  (JNIEnv * env, jobject obj, jstring f, jstring n, jint lim)
{
    if(f == NULL || n == NULL)
    {
        printf("Error: bad file(s) given\n");
        return -1;
    }

    const char * fileName = (*env)->GetStringUTFChars(env, f, NULL);
    const char * name = (*env)->GetStringUTFChars(env, n, NULL);
    int limit = (int) lim; //safe to typecast according to oracle
    int retVal = 0;

    if(fileName == NULL || name == NULL)
    {
        printf("Error: bad file(s) given\n");
        if(fileName != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, f, fileName);
        }
        if(name != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, n, name);
        }
        return -1;
    }
    retVal = testJni((char *) fileName, (char *) name, limit);

    (*env)->ReleaseStringUTFChars(env, f, fileName);
    (*env)->ReleaseStringUTFChars(env, n, name);
    return retVal;
}

/*
 * Class:     Codemon_Codemon
 * Method:    selfTestJni
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)I
 */
 /*
 Def: Runs a 2 player self test on the server.
 Args: The java environment object (JNIEnv *), the class object (jobject),
       a string to the first codemon file path (jstring), a string to the second codemon file path (jstring),
       a string of the first codemon name (jstring), a string of the second codemon name (jint)
       and the turn limit (jint).
 Ret: A negative value on failure and a positive value on success (int).
      Where the positive value is the reportID.
 */
JNIEXPORT jint JNICALL Java_Codemon_Codemon_selfTestJni
  (JNIEnv * env, jobject obj, jstring f1, jstring f2, jstring n1, jstring n2, jint lim)
{
    if(f1 == NULL || f2 == NULL || n1 == NULL || n2 == NULL)
    {
        printf("Error: bad file(s) given\n");
        return -1;
    }

    const char * file1 = (*env)->GetStringUTFChars(env, f1, NULL);
    const char * file2 = (*env)->GetStringUTFChars(env, f2, NULL);
    const char * name1 = (*env)->GetStringUTFChars(env, n1, NULL);
    const char * name2 = (*env)->GetStringUTFChars(env, n2, NULL);
    int limit = (int) lim; //safe to typecast according to oracle
    int retVal = 0;

    if(file1 == NULL || file2 == NULL || name1 == NULL || name2 == NULL)
    {
        printf("Error: bad file(s) given\n");
        if(file1 != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, f1, file1);
        }
        if(name1 != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, n1, name1);
        }
        if(file2 != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, f2, file2);
        }
        if(name2 != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, n2, name2);
        }
        return -1;
    }

    retVal = selfJni((char *) file1, (char *) file2, (char *) name1, (char *) name2, limit);

    (*env)->ReleaseStringUTFChars(env, f1, file1);
    (*env)->ReleaseStringUTFChars(env, f2, file2);
    (*env)->ReleaseStringUTFChars(env, n1, name1);
    (*env)->ReleaseStringUTFChars(env, n2, name2);

    return retVal;
}

/*
 * Class:     Codemon_Codemon
 * Method:    pvpJni
 * Signature: (ILjava/lang/String;Ljava/lang/String;)I
 */
 /*
 Def: Runs an N player pvp request on the server.
 Args: The java environment object (JNIEnv *), the class object (jobject), the player count (jint)
       a string to the file path (jstring) and a string of the codemon name (jstring).
 Ret: A positive value on success and a negative value on failure (int).
      Where the positive value is the reportID.
 */
JNIEXPORT jint JNICALL Java_Codemon_Codemon_pvpJni
  (JNIEnv * env, jobject obj, jint p, jstring f, jstring n)
{
    if(f == NULL || n == NULL)
    {
        printf("Error: bad file(s) given\n");
        return -1;
    }

    const char * fileName = (*env)->GetStringUTFChars(env, f, NULL);
    const char * name = (*env)->GetStringUTFChars(env, n, NULL);
    int retVal = 0;

    int players = (int) p; //safe to typecast according to oracle

    if(fileName == NULL || name == NULL)
    {
        printf("Error: bad file(s) given\n");
        if(fileName != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, f, fileName);
        }
        if(name != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, n, name);
        }
        return -1;
    }
    retVal = pvpJni(players, (char *) fileName, (char *) name);

    (*env)->ReleaseStringUTFChars(env, f, fileName);
    (*env)->ReleaseStringUTFChars(env, n, name);
    return retVal;
}

/*
 * Class:     Codemon_Codemon
 * Method:    reportJni
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
 /*
 Def:  Requests a report from the server.
 Args: The java environment object (JNIEnv *), the class object (jobject),
       a string containing the reportid (jstring) and a string
       containing the file path to the output file (jstring).
 Ret:  Negative value on failure and a positive value on success (int).
 */
JNIEXPORT jint JNICALL Java_Codemon_Codemon_reportJni
  (JNIEnv * env, jobject obj, jstring r, jstring f)
{
    if(f == NULL || r == NULL)
    {
        printf("Error: bad file(s) given\n");
        return -1;
    }
    const char * reportID = (*env)->GetStringUTFChars(env, r, NULL);
    const char * file = (*env)->GetStringUTFChars(env, f, NULL);
    int retVal = 0;

    if(reportID == NULL || file == NULL)
    {
        printf("Error: bad file(s) given\n");
        if(file != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, f, file);
        }
        if(reportID != NULL)
        {
            (*env)->ReleaseStringUTFChars(env, r, reportID);
        }
        return -1;
    }
    retVal = reportJni((char *) reportID, (char *) file);

    (*env)->ReleaseStringUTFChars(env, r, reportID);
    (*env)->ReleaseStringUTFChars(env, f, file);
    return retVal;
}

  /******LEGACY CMD LINE METHODS: USED FOR DEBUGGING*******/
  /******Behaves similarly to the code above**************/

/*
 * Class:     Codemon_Codemon
 * Method:    compile
 * Signature: (Ljava/lang/String;)I
 */
 JNIEXPORT jint JNICALL Java_Codemon_Codemon_compile
   (JNIEnv * env, jobject obj, jstring f)
{
    const char * fileName = (*env)->GetStringUTFChars(env, f, NULL);

    if(fileName == NULL)
    {
        //report error
        return -1;
    }
    //printf("FILE NAME = %s\n", fileName);
    compile((char *) fileName);

    (*env)->ReleaseStringUTFChars(env, f, fileName);
    return 1;
}

/*
 * Class:     Codemon_Codemon
 * Method:    test
 * Signature: (Ljava/lang/String;I)I
 */
 JNIEXPORT jint JNICALL Java_Codemon_Codemon_test
   (JNIEnv * env, jobject obj, jstring f, jint lim)
{
    const char * fileName = (*env)->GetStringUTFChars(env, f, NULL);
    int limit = (int) lim; //safe to typecast according to oracle

    if(fileName == NULL)
    {
        //report error
        return -1;
    }
    test((char *) fileName, limit);

    (*env)->ReleaseStringUTFChars(env, f, fileName);
    return 1;
}

/*
 * Class:     Codemon_Codemon
 * Method:    selfTest
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)I
 */
 JNIEXPORT jint JNICALL Java_Codemon_Codemon_selfTest
   (JNIEnv * env, jobject obj, jstring f1, jstring f2, jint lim)
{
    const char * file1 = (*env)->GetStringUTFChars(env, f1, NULL);
    const char * file2 = (*env)->GetStringUTFChars(env, f2, NULL);
    int limit = (int) lim; //safe to typecast according to oracle

    if(file1 == NULL || file2 == NULL)
    {
        //report error
        return -1;
    }

    self((char *) file1, (char *) file2, limit);

    (*env)->ReleaseStringUTFChars(env, f1, file1);
    (*env)->ReleaseStringUTFChars(env, f2, file2);

    return 1;
}

/*
 * Class:     Codemon_Codemon
 * Method:    pvp
 * Signature: (ILjava/lang/String;)I
 */
 JNIEXPORT jint JNICALL Java_Codemon_Codemon_pvp
   (JNIEnv * env, jobject obj, jint p, jstring f)
{
    const char * fileName = (*env)->GetStringUTFChars(env, f, NULL);
    int players = (int) p; //safe to typecast according to oracle

    if(fileName == NULL)
    {
        //report error
        return -1;
    }
    pvp(players, (char *) fileName);

    (*env)->ReleaseStringUTFChars(env, f, fileName);
    return 1;
}

/*
 * Class:     Codemon_Codemon
 * Method:    getReport
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_Codemon_Codemon_getReport
(JNIEnv * env, jobject obj, jstring r)
{
    const char * reportID = (*env)->GetStringUTFChars(env, r, NULL);

    if(reportID == NULL)
    {
        //report error
        return -1;
    }
    report((char *) reportID);

    (*env)->ReleaseStringUTFChars(env, r, reportID);
    return 1;
}
