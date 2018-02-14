#include "codemon.h"

/*
 * Class:     Codemon_Codemon
 * Method:    compile
 * Signature: (Ljava/lang/String;)I
 */
 JNIEXPORT jint JNICALL Java_jniTest_compile
   (JNIEnv * env, jobject obj, jstring f)
{
    const char * fileName = (*env)->GetStringUTFChars(env, f, NULL);

    if(fileName == NULL)
    {
        //report error
        return -1;
    }
    printf("FILE NAME = %s\n", fileName);
    compile((char *) fileName);

    (*env)->ReleaseStringUTFChars(env, f, fileName);
    return 1;
}

/*
 * Class:     Codemon_Codemon
 * Method:    test
 * Signature: (Ljava/lang/String;I)I
 */
 JNIEXPORT jint JNICALL Java_jniTest_test
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
 JNIEXPORT jint JNICALL Java_jniTest_selfTest
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
 JNIEXPORT jint JNICALL Java_jniTest_pvp
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
 JNIEXPORT jint JNICALL Java_jniTest_getReport
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
