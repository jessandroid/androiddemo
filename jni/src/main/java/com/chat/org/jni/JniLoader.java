package com.chat.org.jni;

/**
 * Load Jni
 *
 * @auth dengquan@360.cn
 * Create by dengquan on 2019/2/14
 */
public class JniLoader {
    static {
        System.loadLibrary("HelloJni");
    }

    public native String getHelloStr();
}
