package com.example.dengquan.demo.Choice;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.dengquan.demo.MainActivity;
import com.example.dengquan.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 分类的弹窗
 * Created by dengquan on 2018/3/1.
 */

public class ClassificationChoicePopup extends PopupWindow implements PopupWindow.OnDismissListener{
    private OnClassificationChoiceListener onClassificationChoiceListener;
    private Context mContext;
    private KidLiveChoiceItemView kidLiveChoiceItemView;
    private ClassificationChoiceAdapter mAdapter;
    private List<KidLiveClassificationBean> list = new ArrayList<>();
    private View rootView;
    private RecyclerView classificationRecycleView;
    private Button selectBtn;
    private Button confirmBtn;
    private Button cancelButton;

    private Timer timer;

    public ClassificationChoicePopup(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init(){
        initData();
        initView();
    }

    private void initView(){
        timer = new Timer();

        rootView = LayoutInflater.from(mContext).inflate(R.layout.popup_classification_choice,null);
        this.setContentView(rootView);
        this.update();
        this.setFocusable(false);
        this.setOutsideTouchable(true);
        rootView.setFocusableInTouchMode(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.edittext_bg_normal)));
        this.setOnDismissListener(this);
        selectBtn = (Button)rootView.findViewById(R.id.selectBtn);

        classificationRecycleView = (RecyclerView)rootView.findViewById(R.id.classificationRecycleView);
        mAdapter = new ClassificationChoiceAdapter(mContext);
        MultiSpanLayoutManager manager = new MultiSpanLayoutManager();
//        classificationRecycleView.setHasFixedSize(true);
        classificationRecycleView.setLayoutManager(manager);
        classificationRecycleView.setAdapter(mAdapter);
        mAdapter.setDataChanged(list);
    }

    private void initData(){
        list.add(new KidLiveClassificationBean("传统文化",false));
        list.add(new KidLiveClassificationBean("语文",false));
        list.add(new KidLiveClassificationBean("基础礼仪",false));
        list.add(new KidLiveClassificationBean("地理",false));
        list.add(new KidLiveClassificationBean("英语口语",false));
        list.add(new KidLiveClassificationBean("名人故事",false));
        list.add(new KidLiveClassificationBean("古典音乐",false));
    }

    public void show(View view){
        if(view instanceof KidLiveChoiceItemView){
            kidLiveChoiceItemView = (KidLiveChoiceItemView) view;
        }
        this.showAsDropDown(view,0,0);
    }

    @Override
    public void onDismiss() {
        Log.e("popup","classification popup onDismiss start ");
        if(kidLiveChoiceItemView != null){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(mContext instanceof MainActivity){
                        ((MainActivity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("popup","classification popup onDismiss end ");
                                kidLiveChoiceItemView.setChoiceViewSelected(false);
                            }
                        });
                    }

                }
            },100);
        }
    }

    public void setOnClassificationChoiceListener(OnClassificationChoiceListener onClassificationChoiceListener) {
        this.onClassificationChoiceListener = onClassificationChoiceListener;
    }

    public interface OnClassificationChoiceListener {
        void onClassificationChoiceResult();
    }
}
