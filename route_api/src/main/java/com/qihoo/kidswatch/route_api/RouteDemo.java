package com.qihoo.kidswatch.route_api;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

/****
 * $DESC$
 * @auth dengquan@360.cn
 * Create by dengquan on 2018/6/29
 *
 *****/
public class RouteDemo {
    private static HashMap<String,Class> activityMap = new HashMap<>();
    private static Application mApplication;

    public static void init(Application application){
        mApplication = application;
        try {
            Class clazz = Class.forName("com.example.dengquan.demoqw.AutoCreateModuleActivityMap_app");
            Method method = clazz.getMethod("initActivityMap",HashMap.class);
            method.invoke(null,activityMap);
            for (String key : activityMap.keySet()){
                Log.e("route"," activity :"+activityMap.get(key));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void open(String url){
        for (String key : activityMap.keySet()){
            if(url.equals(key)){
                Intent intent = new Intent(mApplication,activityMap.get(key));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mApplication.startActivity(intent);
            }
        }
    }
}
