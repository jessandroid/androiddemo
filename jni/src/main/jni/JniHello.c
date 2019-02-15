//
// Created by dengquan on 2019/2/15.
//
#include "com_chat_org_jni_JniLoader.h"
JNIEXPORT jstring JNICALL
Java_com_chat_org_jni_JniLoader_getHelloStr(JNIEnv *env, jobject instance)
  {
        return (*env)->NewStringUTF(env,"hello world!!!");
  }
