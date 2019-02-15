package com.net.okhttp.rxdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****
 * $DESC$
 * @auth dengquan@360.cn
 * Create by dengquan on 2018/9/25
 *
 *****/
public class RXDemoActivity extends AppCompatActivity {
    private TextView mTextView;
    Subscription subscription;
    private final String TAG = "Student";
    private int count;
    private static HashMap<Integer, Student> currentUserMap;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_demo);
        mTextView = (TextView) this.findViewById(R.id.demo);
        testMap();
    }


    private void testMap() {
        for (int i = 0; i < 10; i++) {
            Observable.create(new rx.Observable.OnSubscribe<Student>() {
                @Override
                public void call(Subscriber<? super Student> subscriber) {
                    subscriber.onNext(getCurrentUser(count));
                    count++;
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Student>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Student student) {
                            Log.e("map", "student :" + student.getName() + "   " + student.toString());
                        }
                    });
        }
    }


    public Student getCurrentUser(int studentId) {
        Student student = null;
        if (currentUserMap != null) {
            student = currentUserMap.get(studentId);
        } else {
            currentUserMap = new HashMap<Integer, Student>();
        }

        if (student == null) {
            student = new Student();
            student.setName(TAG + "" + studentId);
            Log.e("map", "before :" + student.getName() + "   " + student.toString());
            if (student != null) {
                currentUserMap.put(studentId, student);
            }
        }

        return student;
    }


    public void clearCurrentUserMap() {
        if (currentUserMap != null) {
            currentUserMap.clear();
        }
        currentUserMap = null;
    }
}
