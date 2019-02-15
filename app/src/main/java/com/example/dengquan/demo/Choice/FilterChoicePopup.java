package com.example.dengquan.demo.Choice;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.dengquan.demo.MainActivity;
import com.example.dengquan.demo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 筛选的弹窗
 * Created by dengquan on 2018/3/1.
 */

public class FilterChoicePopup extends PopupWindow implements PopupWindow.OnDismissListener{
    private OnFilterChoiceListener onFilterChoiceListener;
    private Context mContext;
    private View rootView;
    private KidLiveChoiceItemView kidLiveChoiceItemView;

    private Timer timer;
    public FilterChoicePopup(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init(){
        timer = new Timer();

        rootView = LayoutInflater.from(mContext).inflate(R.layout.popup_filter_choice,null);
        this.setContentView(rootView);
        this.setFocusable(false);
        this.setOutsideTouchable(true);
        rootView.setFocusableInTouchMode(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(0));
        this.update();
        this.setOnDismissListener(this);
    }

    public void show(View view){
        if(view instanceof KidLiveChoiceItemView){
            kidLiveChoiceItemView = (KidLiveChoiceItemView) view;
        }
        this.showAsDropDown(view);
    }

    @Override
    public void onDismiss() {
        Log.e("popup","filter popup onDismiss start ");
        if(kidLiveChoiceItemView != null){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(mContext instanceof MainActivity){
                        ((MainActivity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("popup","filter popup onDismiss end ");
                                kidLiveChoiceItemView.setChoiceViewSelected(false);
                            }
                        });
                    }

                }
            },100);
        }
    }

    public void setOnFilterChoiceListener(OnFilterChoiceListener onFilterChoiceListener) {
        this.onFilterChoiceListener = onFilterChoiceListener;
    }

    /***
     *
     */
    public interface OnFilterChoiceListener{
        void onFilterChoiceResult();
    }
}
