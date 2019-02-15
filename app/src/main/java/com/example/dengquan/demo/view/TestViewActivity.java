package com.example.dengquan.demo.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dengquan.demo.R;

/**
 * Created by dengquan on 2018/4/2.
 */

public class TestViewActivity extends Activity {
    private LinearLayout showview;
    private int x;
    private BottomReportCardView bottomReportCardView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        bottomReportCardView = new BottomReportCardView(this);
        showview = (LinearLayout) findViewById(R.id.showview);
        TextView textView = (TextView) findViewById(R.id.refresh);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    changeView();
            }
        });
    }

    private void changeView(){
        bottomReportCardView.setmThx("count ："+x);
        Log.e("view","test view before invalidate ");
        x++;
        Log.e("view","test view after invalidate");
        TextView textView = new TextView(this);
        textView.setText("count ："+x);
        showview.removeAllViews();
        showview.addView(textView);
    }
}
