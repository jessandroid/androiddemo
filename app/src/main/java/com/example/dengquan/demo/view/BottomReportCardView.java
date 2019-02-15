package com.example.dengquan.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dengquan.demo.R;

/**
 * 成绩单分享的页面，带有二维码的底部布局
 * Created by dengquan on 2018/4/2.
 */

public class BottomReportCardView extends LinearLayout {
    private Context mContext;
    private ImageView mQrImg;
    private TextView mThxTv;

    private Bitmap mQrBitmap;
    private String mThx;
    public BottomReportCardView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BottomReportCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public BottomReportCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        this.addView(LayoutInflater.from(mContext).inflate(R.layout.view_bottom_report_card,this,false));
        mQrImg = (ImageView) this.findViewById(R.id.img_qr_bitmap);
        mThxTv = (TextView) this.findViewById(R.id.change_view);
    }

    public Bitmap getmQrBitmap() {
        return mQrBitmap;
    }

    public void setmQrBitmap(Bitmap mQrBitmap) {
        this.mQrBitmap = mQrBitmap;
    }

    public String getmThx() {
        return mThx;
    }

    public void setmThx(String mThx) {
        this.mThx = mThx;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("view","test view on draw");
        super.onDraw(canvas);
        if(mQrBitmap != null){
            mQrImg.setImageBitmap(mQrBitmap);
        }
        if(!TextUtils.isEmpty(mThx)){
            mThxTv.setText(mThx);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
