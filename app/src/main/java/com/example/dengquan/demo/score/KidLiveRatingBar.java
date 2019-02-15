package com.example.dengquan.demo.score;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.dengquan.demo.R;

/**
 * 自定义的RatingBar
 * Created by dengquan on 2018/3/5.
 */

public class KidLiveRatingBar extends View {
    private Context mContext;
    private AttributeSet mAttr;

    private int starDistance = 0; //星星间距
    private int starCount = 5; //星星个数
    private int starSize;  //星星高度大小
    private float starMark = 0.0F; //评分星星
    private Bitmap starFullBitmap;   //被选择的部分
    private Drawable starEmptyDrawble; //未被选择的星星

    private OnStarChangeListener onStarChangeListener;
    private Paint paint;//绘制星星画笔
    private boolean integerMark = false;

    public KidLiveRatingBar(Context context) {
        this(context, null);
    }

    public KidLiveRatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KidLiveRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttr = attrs;
        init();
    }

    private void init() {
        setClickable(true);
        TypedArray array = mContext.obtainStyledAttributes(mAttr, R.styleable.RatingBar);
        if (array != null && array.getIndexCount() > 0) {
            starDistance = (int) array.getDimension(R.styleable.RatingBar_starDistance, 0);
            starSize = (int) array.getDimension(R.styleable.RatingBar_starSize, 20);
            starCount = array.getInteger(R.styleable.RatingBar_starCount, 5);
            starEmptyDrawble = array.getDrawable(R.styleable.RatingBar_starEmpty);
            starFullBitmap = drawableToBitmap(array.getDrawable(R.styleable.RatingBar_starFull));
        }
        if (array != null) {
            array.recycle();
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(starFullBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    }


    /****
     * 设置是否需要整数评分
     * @param integerMark
     */
    public void setIntegerMark(boolean integerMark) {
        this.integerMark = integerMark;
    }


    /***
     * 设置显示的星星的分数，分数间隔固定在0.5分
     */
    public void setStarMark(float mark) {
        Log.e("rating", "markx :" + mark);
        if (integerMark) {
            starMark = (int) Math.ceil(mark);
        } else {
            starMark = Math.round(mark * 10) * 1.0f / 10;
        }
        Log.e("rating", "cal mark :" + starMark);
        int temp = Math.round(starMark);
        if (starMark - temp > 0) {
            starMark = Math.round(temp) + 0.5F;
        } else if (starMark - temp <= 0) {
            starMark = Math.round(temp);
        }
        Log.e("rating", "cal mark 111111:" + starMark);
        if (onStarChangeListener != null) {
            onStarChangeListener.onStarChange(starMark);
        }
        invalidate();
    }

    /******
     * 获取当前评分
     * @return
     */
    public float getStarMark() {
        return starMark;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starSize * starCount + starDistance * (starCount - 1), starSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("rating", "on draw start");
        if (starFullBitmap == null || starEmptyDrawble == null) {
            return;
        }
        /****
         * 绘制出底部未被选中的星星
         */
        for (int i = 0; i < starCount; i++) {
            starEmptyDrawble.setBounds((starDistance + starSize) * i, 0, (starDistance + starSize) * i + starSize, starSize);
            starEmptyDrawble.draw(canvas);
        }
        if (starMark > 1) {
            canvas.drawRect(0, 0, starSize, starSize, paint);
            if (starMark - (int) (starMark) == 0) {
                for (int i = 1; i < starMark; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
            } else {
                for (int i = 1; i < starMark - 1; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
                canvas.translate(starDistance + starSize, 0);
                canvas.drawRect(0, 0, starSize * (Math.round((starMark - (int) (starMark)) * 10) * 1.0f / 10), starSize, paint);
            }
        } else {
            canvas.drawRect(0, 0, starSize * starMark, starSize, paint);
        }
        Log.e("rating", "on draw end");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        Log.e("rating", "bofore x :" + x + "   measure width :" + getMeasuredWidth());
        if (x < 0) {
            x = 0;
        }
        if (x > getMeasuredWidth()) {
            x = getMeasuredWidth();
        }
        Log.e("rating", "after x :" + x);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            case MotionEvent.ACTION_MOVE:
                setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    /****
     * drawable to bitmap
     * @param drawable
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, starSize, starSize);
        drawable.draw(canvas);
        return bitmap;
    }

    /*****
     * 设置评分变化的通知
     * @param onStarChangeListener
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.onStarChangeListener = onStarChangeListener;
    }

    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }
}
