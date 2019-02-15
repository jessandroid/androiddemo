package com.example.dengquan.demo.Choice;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.dengquan.demo.MainActivity;
import com.example.dengquan.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 排序的弹窗
 * Created by dengquan on 2018/3/1.
 */

public class SortChoicePopup extends PopupWindow implements PopupWindow.OnDismissListener{
    private Timer timer;
    private OnSortChoiceListener onSortChoiceListener;
    private Context mContext;
    private View rootView;
    private RecyclerView recyclerView;
    private SortChoiceAdapter sortChoiceAdapter;
    private KidLiveChoiceItemView kidLiveChoiceItemView;
    private  List<KidLiveSortBean> list = new ArrayList<>();
    private void initData() {
        list.add(new KidLiveSortBean("综合",0));
        list.add(new KidLiveSortBean("人气",1));
        list.add(new KidLiveSortBean("评分",2));
        list.add(new KidLiveSortBean("价格由低到高",3));
        list.add(new KidLiveSortBean("价格由高到低",4));
        list.add(new KidLiveSortBean("时间由长到短",5));
        list.add(new KidLiveSortBean("时间由短到长",6));
    }
    public SortChoicePopup(Context context) {
        super(context);
        mContext = context;
        init();
        initData();
    }

    private void init(){
        timer = new Timer();

        rootView = LayoutInflater.from(mContext).inflate(R.layout.popup_sort_choice,null);
        this.setContentView(rootView);
        this.setFocusable(false);
        this.setOutsideTouchable(true);
        rootView.setFocusableInTouchMode(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(0));
        this.update();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        sortChoiceAdapter = new SortChoiceAdapter(mContext);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(sortChoiceAdapter);
        sortChoiceAdapter.setDataChanged(list);
        this.setOnDismissListener(this);
    }

    public void show(View view){
        this.showAsDropDown(view);
        if(view instanceof KidLiveChoiceItemView){
            kidLiveChoiceItemView = (KidLiveChoiceItemView) view;
        }
    }

    @Override
    public void onDismiss() {
        Log.e("popup","sort popup onDismiss start ");
        if(kidLiveChoiceItemView != null){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(mContext instanceof MainActivity){
                        ((MainActivity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("popup","sort popup onDismiss end ");
                                kidLiveChoiceItemView.setChoiceViewSelected(false);
                            }
                        });
                    }

                }
            },100);

        }
    }


    public void setOnSortChoiceListener(OnSortChoiceListener onSortChoiceListener) {
        this.onSortChoiceListener = onSortChoiceListener;
    }

    public interface OnSortChoiceListener{
        void onSortChoiceResult();
    }
}
