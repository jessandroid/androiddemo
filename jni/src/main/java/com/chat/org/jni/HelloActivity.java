package com.chat.org.jni;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * $DESC$
 *
 * @auth dengquan@360.cn
 * Create by dengquan on 2019/2/14
 */
public class HelloActivity extends Activity {
    private TextView mTx;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        JniLoader jniLoader = new JniLoader();
        mTx = (TextView) findViewById(R.id.txt);
        mTx.setText(jniLoader.getHelloStr());
    }
}
