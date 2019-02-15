package com.example.dengquan.demo.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dengquan.demo.R;

/**
 * 评分
 * Created by dengquan on 2018/3/5.
 */

public class KidLiveScoreActivity extends AppCompatActivity{
    private TextView scoreTxt;
    private KidLiveRatingBar ratingBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_live_score);
        scoreTxt = (TextView)this.findViewById(R.id.scoreTxt);
        ratingBar = (KidLiveRatingBar) this.findViewById(R.id.ratingBar);
        ratingBar.setOnStarChangeListener(new KidLiveRatingBar.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                scoreTxt.setText("用户评分  ："+mark);
            }
        });
    }
}
