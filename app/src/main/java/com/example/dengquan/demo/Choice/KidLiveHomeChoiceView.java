package com.example.dengquan.demo.Choice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 首页的选择器
 * Created by dengquan on 2018/3/1.
 */

public class KidLiveHomeChoiceView extends ViewGroup {
    public KidLiveHomeChoiceView(Context context) {
        this(context,null);
    }

    public KidLiveHomeChoiceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KidLiveHomeChoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
