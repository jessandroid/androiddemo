package com.example.dengquan.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dengquan.demo.IPlusAidlInterface;

/****
 * $DESC$
 * @auth dengquan@360.cn
 * Create by dengquan on 2018/7/18
 *
 *****/
public class BinderService extends Service{
    private ServiceBinder serviceBinder;
    private Binder binder;
    @Override
    public void onCreate() {
        super.onCreate();
        serviceBinder = new ServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class ServiceBinder extends IPlusAidlInterface.Stub{
        public void test(){
            Log.e("service","bind service ");
        }

        @Override
        public int add(int a, int b) throws RemoteException {
            Log.e("service","bind service add method");
            return a+b;
        }
    }
}
