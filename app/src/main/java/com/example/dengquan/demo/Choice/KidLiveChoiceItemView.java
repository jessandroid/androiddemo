package com.example.dengquan.demo.Choice;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dengquan.demo.R;

/**
 * 选择的单个ITEM
 * Created by dengquan on 2018/3/1.
 */

public class KidLiveChoiceItemView extends LinearLayout {
    private Context mContext;
    private ImageView selectArrow;
    private TextView choiceViewDescription;
    private AttributeSet mAttr;
    private boolean mIsSelected;
    public KidLiveChoiceItemView(Context context) {
        this(context,null);
    }

    public KidLiveChoiceItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public KidLiveChoiceItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttr = attrs;
        init();
    }

    private void init(){
        View homeView = LayoutInflater.from(mContext).inflate(R.layout.item_kid_live_choice_view,this,false);
        this.addView(homeView);
        selectArrow = (ImageView)this.findViewById(R.id.selectArrow);
        choiceViewDescription = (TextView)this.findViewById(R.id.choiceViewDescription);
        if(mAttr != null && mContext != null){
            TypedArray array = mContext.obtainStyledAttributes(mAttr,R.styleable.KidLiveChoiceItemViewAttr);
            if(array != null && array.getIndexCount() > 0){
                String description =
                        array.getString(R.styleable.KidLiveChoiceItemViewAttr_choice_item_view_text);
                setChoiceViewDescription(description);
                array.recycle();
            }
        }
    }

    private void setChoiceViewDescription(String description){
        if(!TextUtils.isEmpty(description)){
            choiceViewDescription.setText(description);
        }
    }

    /***
     * 是否被选择
     * @param isSelected
     */
    public void setChoiceViewSelected(boolean isSelected){
        mIsSelected = isSelected;
        if(selectArrow != null){
            selectArrow.setSelected(isSelected);
        }
        if(choiceViewDescription != null && mContext != null){
            if(isSelected){
                choiceViewDescription.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }else {
                choiceViewDescription.setTextColor(mContext.getResources().getColor(R.color.edittext_text_normal));
            }
        }
    }





    public boolean ismIsSelected() {
        return mIsSelected;
    }
}
