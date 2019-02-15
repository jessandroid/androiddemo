package com.example.dengquan.demo.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 截图ScollView的图片并且保存到本地，所有的方法运行在子线程。回调在主线程
 * Created by dengquan on 2018/3/8.
 */

public class ScreenShotAndSaveManager {
    private Activity mActivity;
    private OnScreenShotAndSaveResultListener onScreenShotAndSaveResultListener;

    public ScreenShotAndSaveManager(Activity activity) {
        mActivity = activity;
    }

    /****
     * ScrollView
     */
    public void onScreenShotScrollView(final ScrollView scrollView) {
        if(onScreenShotAndSaveResultListener != null){
            onScreenShotAndSaveResultListener.onStarting();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
               try {
                   if(onScreenShotAndSaveResultListener != null && mActivity != null){
                       mActivity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               onScreenShotAndSaveResultListener.onScreenShotSuccess(onShotScrollView(scrollView));
                           }
                       });
                   }
               }catch (Exception e){
                   onScreenShotAndSaveError();
               }
            }
        }).start();
    }

    /***
     * 保存Bitmap
     */
    public void onSaveBitmapToExteralStorage(final Bitmap bitmap){
        if(onScreenShotAndSaveResultListener != null){
            onScreenShotAndSaveResultListener.onStarting();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(bitmap != null){
                    String path = Environment.getExternalStorageDirectory().getPath()+ File.separator+"shot";
                    File file = new File(path);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    final File bitmapFile = new File(file.getPath(),System.currentTimeMillis()+".png");
                    try {
                        OutputStream outputStream = new FileOutputStream(bitmapFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                        outputStream.flush();
                        outputStream.close();
                        if(onScreenShotAndSaveResultListener != null && mActivity != null){
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onScreenShotAndSaveResultListener.onSaveBitmapSuccess(bitmapFile.getPath());
                                }
                            });
                        }
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                        onScreenShotAndSaveError();
                    }catch (Exception e){
                        e.printStackTrace();
                        onScreenShotAndSaveError();
                    }
                    Log.e("shot","shot path :"+path);
                }else {
                    onScreenShotAndSaveError();
                }
            }
        }).start();
    }

    private Bitmap onShotScrollView(ScrollView scrollView){
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        Log.e("shot","scrollView height :"+h);
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    private void onScreenShotAndSaveError(){
        if(onScreenShotAndSaveResultListener != null && mActivity != null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onScreenShotAndSaveResultListener.onError();
                }
            });
        }
    }


    public void setOnScreenShotAndSaveResultListener(OnScreenShotAndSaveResultListener onScreenShotAndSaveResultListener) {
        this.onScreenShotAndSaveResultListener = onScreenShotAndSaveResultListener;
    }

    public interface OnScreenShotAndSaveResultListener {

        void onStarting();  //开始的回调

        void onError();  //所有的失败回调

        void onScreenShotSuccess(Bitmap bitmap);  //截取Bitmap成功

        void onSaveBitmapSuccess(String imgPath);   //保存成功
    }
}
