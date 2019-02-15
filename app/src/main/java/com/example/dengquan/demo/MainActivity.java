package com.example.dengquan.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chat.org.jni.HelloActivity;
import com.example.dengquan.demo.Choice.ClassificationChoicePopup;
import com.example.dengquan.demo.Choice.FilterChoicePopup;
import com.example.dengquan.demo.Choice.KidLiveChoiceItemView;
import com.example.dengquan.demo.Choice.SortChoicePopup;
import com.example.dengquan.demo.bluetooth.activity.BluetoothActivity;
import com.example.dengquan.demo.javademo.Person;
import com.example.dengquan.demo.javademo.Student;
import com.example.dengquan.demo.livedata.LiveDataActivity;
import com.example.dengquan.demo.opengl.SurfaceViewActivity;
import com.example.dengquan.demo.screenshot.ScreenShotActivity;
import com.example.dengquan.demo.service.BinderService;
import com.example.dengquan.demo.view.TestViewActivity;
import com.net.okhttp.rxdemo.RXDemoActivity;
import com.qihoo.kidswatch.route_api.RouteDemo;
import com.qihoo.kidswatch.sdk.commonutil.LogUtil;
import com.qihoo.kidswatch.sdk.meizu.KidsWatchApi;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ClassificationChoicePopup.OnClassificationChoiceListener,
        SortChoicePopup.OnSortChoiceListener, FilterChoicePopup.OnFilterChoiceListener {
    private Button button;
    private KidLiveChoiceItemView classificationItemView;
    private KidLiveChoiceItemView sortItemView;
    private KidLiveChoiceItemView filterItemView;
    private BinderService binderService;
    /***
     * 三种不同选择的弹窗
     */
    private ClassificationChoicePopup classificationChoicePopup;
    private SortChoicePopup sortChoicePopup;
    private FilterChoicePopup filterChoicePopup;

    private Person person;
    private Student  student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouteDemo.init(this.getApplication());
        setContentView(R.layout.activity_main);
        initView();
        testhandler();
        testChoice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(connection);
    }

    private void initView(){
        person = new Person();
        KidsWatchApi.getInstance().init(getApplicationContext());
//        Intent intent = new Intent(MainActivity.this, Accountlogin.class);
//        MainActivity.this.startActivity(intent);
        button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                testScore();
//                testBluetooth();
//                testView();
//                RouteDemo.open("test1");
//                testServiceBind();
//                testSurfaceViewActivity();
//                call();

//                testJava();
                startActivity(new Intent(MainActivity.this, HelloActivity.class));
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                student = new Student();
                student.setName("jane");
                student.setAge(56);

//                person.setStudent(student);
                LogUtil.e("java","name :"+person.getStudent().getName() +"  age :"+person.getStudent().getAge());
                return false;
            }
        });
    }
    ServiceConnection connection;
    private void testServiceBind(){
        Intent intent = new Intent(this,BinderService.class);
        this.bindService(intent, connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IPlusAidlInterface binder = IPlusAidlInterface.Stub.asInterface(service);
                try {
                    int x = binder.add(1,2);
                    Toast.makeText(MainActivity.this,"DDD:"+x,Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },BIND_AUTO_CREATE);
    }



    private void testJava(){
        student = new Student();
        student.setName("Michael");
        student.setAge(12);

        person.setStudent(student);
    }

    private void call(){
        Toast.makeText(this,"wewe",Toast.LENGTH_SHORT).show();
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"13521917782"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void testSurfaceViewActivity(){
        Intent intent = new Intent(this, SurfaceViewActivity.class);
        startActivity(intent);
    }

    private void testPackageInfo(){
//        PackageInfo packageInfo = this.getPackageManager().getPackageArchiveInfo();
//        ActivityInfo[] activities = packageInfo.activities;
//        for (ActivityInfo activityInfo : activities){
//            int theme = activityInfo.theme;
//        }
    }

    private void testChoice() {
        classificationItemView = (KidLiveChoiceItemView) this.findViewById(R.id.classificationItemView);
        sortItemView = (KidLiveChoiceItemView) this.findViewById(R.id.sortItemView);
        filterItemView = (KidLiveChoiceItemView) this.findViewById(R.id.filterItemView);

        classificationChoicePopup = new ClassificationChoicePopup(this);
        sortChoicePopup = new SortChoicePopup(this);
        filterChoicePopup = new FilterChoicePopup(this);

        classificationItemView.setOnClickListener(this);
        sortItemView.setOnClickListener(this);
        filterItemView.setOnClickListener(this);

        classificationChoicePopup.setOnClassificationChoiceListener(this);
        sortChoicePopup.setOnSortChoiceListener(this);
        filterChoicePopup.setOnFilterChoiceListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.classificationItemView:
                onClassificationClickEvent();
                break;
            case R.id.sortItemView:
                onSortClickEvent();
                break;
            case R.id.filterItemView:
                onFilterClickEvent();
                break;
        }
    }


    @Override
    public void onFilterChoiceResult() {

    }

    @Override
    public void onSortChoiceResult() {

    }

    @Override
    public void onClassificationChoiceResult() {

    }

    private void onClassificationClickEvent() {
        Log.e("popup","classification popup show or hide start ");
        if (sortItemView != null) {
            sortItemView.setChoiceViewSelected(false);
        }
        if (filterItemView != null) {
            filterItemView.setChoiceViewSelected(false);
        }
        classificationItemView.setChoiceViewSelected(!classificationItemView.ismIsSelected());
        if (classificationItemView.ismIsSelected()) {
            classificationChoicePopup.show(classificationItemView);
        }
        Log.e("popup","classification popup show or hide end ");
    }

    private void onSortClickEvent() {
        Log.e("popup","sort popup show or hide start ");
        if (classificationItemView != null) {
            classificationItemView.setChoiceViewSelected(false);
        }
        if (filterItemView != null) {
            filterItemView.setChoiceViewSelected(false);
        }
        sortItemView.setChoiceViewSelected(!sortItemView.ismIsSelected());
        if(sortItemView.ismIsSelected()){
            sortChoicePopup.show(sortItemView);
        }
        Log.e("popup","sort popup show or hide end ");
    }

    private void onFilterClickEvent() {
        Log.e("popup","filter popup show or hide start ");
        if (classificationItemView != null) {
            classificationItemView.setChoiceViewSelected(false);
        }
        if (sortItemView != null) {
            sortItemView.setChoiceViewSelected(false);
        }
        filterItemView.setChoiceViewSelected(!filterItemView.ismIsSelected());
        if(filterItemView.ismIsSelected()){
            filterChoicePopup.show(filterItemView);
        }
        Log.e("popup","filter popup show or hide end ");
    }


    private void testhandler() {
        new Thread(new Runnable() {
            Handler handler;

            @Override
            public void run() {
                handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.e("handelr", "msg :" + msg.obj + "   thread :" + Thread.currentThread().getName());
                    }
                };
                Message message = new Message();
                message.what = 1;
                message.obj = "test";
                handler.sendMessage(message);
            }
        }).start();
    }

    private void testScore(){
        startActivity(new Intent(this, ScreenShotActivity.class));
    }

    private void testBluetooth(){
        startActivity(new Intent(this, BluetoothActivity.class));
    }

    private void testView(){
        startActivity(new Intent(this, TestViewActivity.class));
    }
}
