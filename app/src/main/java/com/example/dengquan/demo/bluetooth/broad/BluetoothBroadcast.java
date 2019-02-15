package com.example.dengquan.demo.bluetooth.broad;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听广播（蓝牙变化的广播）
 * Created by dengquan on 2018/3/19.
 */

public class BluetoothBroadcast extends BroadcastReceiver {
    private List<BluetoothDevice> list = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("bluetooth","intent :"+intent.getAction());
        if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.e("bluetooth", "STATE_OFF 手机蓝牙关闭");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.e("bluetooth", "STATE_TURNING_OFF 手机蓝牙正在关闭");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.e("bluetooth", "STATE_ON 手机蓝牙开启");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.e("bluetooth", "STATE_TURNING_ON 手机蓝牙正在开启");
                    break;
            }
        }else if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.e("bluetooth","device ："+device.getName() +"  "+device.getAddress());
            list.add(device);
        }
    }
}
