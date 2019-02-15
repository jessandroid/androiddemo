package com.example.dengquan.demo.screenshot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.dengquan.demo.R;

/**
 * 截图
 * Created by dengquan on 2018/3/7.
 */

public class ScreenShotActivity extends Activity implements ScreenShotAndSaveManager.OnScreenShotAndSaveResultListener{
    private ScrollView scroll;
    private Button button;
    private ImageView qrImg;
    private ScreenShotAndSaveManager manager;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
        manager = new ScreenShotAndSaveManager(this);
        manager.setOnScreenShotAndSaveResultListener(this);
        scroll = (ScrollView)this.findViewById(R.id.scroll);
        button = (Button)this.findViewById(R.id.button);
        qrImg = (ImageView)this.findViewById(R.id.qrImg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrImg.setVisibility(View.VISIBLE);
                qrImg.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("shot","runnable thread :"+Thread.currentThread().getName());
                        manager.onScreenShotScrollView(scroll);
                    }
                });
            }
        });
    }

    @Override
    public void onStarting() {
        Log.e("shot","onstartng:"+Thread.currentThread().getName());
        onShowProgressDialog();
    }

    @Override
    public void onError() {
        Toast.makeText(this,"截图保存失败",Toast.LENGTH_SHORT).show();
        setQRImgGone();
        onDismissProgressDialog();
    }

    @Override
    public void onScreenShotSuccess(Bitmap bitmap) {
        manager.onSaveBitmapToExteralStorage(bitmap);
    }

    @Override
    public void onSaveBitmapSuccess(String imgPath) {
        Toast.makeText(this,"imag path:"+imgPath,Toast.LENGTH_SHORT).show();
        setQRImgGone();
        onDismissProgressDialog();
    }

    private void setQRImgGone(){
        if(qrImg != null){
            qrImg.setVisibility(View.GONE);
        }
    }

    private void onShowProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            return;
        }
        progressDialog = ProgressDialog.show(this,null,"starting...");
    }


    private void onDismissProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}
