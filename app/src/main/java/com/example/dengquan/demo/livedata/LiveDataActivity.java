package com.example.dengquan.demo.livedata;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.dengquan.demo.R;

import java.util.Observable;

/****
 * $DESC$
 * @auth dengquan@360.cn
 * Create by dengquan on 2018/11/27
 *
 *****/
public class LiveDataActivity extends FragmentActivity {
    private TextView mFlagTxt;
    private NameViewModel nameViewModel;
    private Observer<String> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data);
        nameViewModel = ViewModelProviders.of(this).get(NameViewModel.class);
        mFlagTxt = (TextView) this.findViewById(R.id.txt_flag);
        observer = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mFlagTxt.setText(s);
            }
        };
        nameViewModel.getCurrentName().observe(this, observer);
        nameViewModel.getCurrentName().setValue("dengquan");
    }
}
