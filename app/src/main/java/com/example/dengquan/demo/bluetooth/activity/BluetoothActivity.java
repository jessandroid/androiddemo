package com.example.dengquan.demo.bluetooth.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dengquan.demo.R;
import com.example.dengquan.demo.bluetooth.adapter.MyBluetoothAdapter;
import com.example.dengquan.demo.bluetooth.broad.BluetoothBroadcast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengquan on 2018/3/19.
 */

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener, MyBluetoothAdapter.OnChooseDeviceListener {
    private static final int BLUETOOTH_REQUEST_CODE = 0x324;
    private BluetoothBroadcast mBluetoothBroadcat;   //广播协议
    private List<BluetoothDevice> mList = new ArrayList<>();
    private BluetoothAdapter mAdapter;
    private BluetoothLeScanner mScanner;
    private Button button;
    private RecyclerView mRecyclerview;
    private MyBluetoothAdapter bluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blue_tooth);
        init();
    }

    private void init() {
        initView();
        addListener();
        initData();
//        initBluetooth();
//        initBroadcast();
//        getBondedDevice();
//        startDiscovery();
//        startSyBluetoothSetting();
    }

    private void initView() {
        button = (Button) this.findViewById(R.id.button);
        mRecyclerview = (RecyclerView) this.findViewById(R.id.recyclerView);
        bluetoothAdapter = new MyBluetoothAdapter(this);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(bluetoothAdapter);
    }

    private void addListener() {
        button.setOnClickListener(this);
        bluetoothAdapter.setOnChooseDeviceListener(this);
    }

    private void initData() {
        if (isSupportBLE(this)) {
            initBluetoothAdapter();
            enableBluetooth();
        } else {
            Toast.makeText(this, "不支持蓝牙低能耗", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isSupportBLE(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    private void initBluetoothAdapter() {
        BluetoothManager manager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = manager.getAdapter();
    }

    private void enableBluetooth() {
        if (mAdapter != null && !mAdapter.isEnabled()) {
//            mAdapter.enable();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,BLUETOOTH_REQUEST_CODE);
        }else {
            scanBluetoothDevice();
        }
    }

    private void scanBluetoothDevice(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mScanner = mAdapter.getBluetoothLeScanner();
            mAdapter.getBondedDevices();
            mScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        if(!mList.contains(result.getDevice())){
                            if(!TextUtils.isEmpty(result.getDevice().getName())){
                                mList.add(result.getDevice());
                            }
                        }
                        bluetoothAdapter.setDataChanged(mList);
                    }
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                }
            });
        }else {
            mAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if(!mList.contains(device)){
                        if(!TextUtils.isEmpty(device.getName())){
                            mList.add(device);
                        }
                    }
                    bluetoothAdapter.setDataChanged(mList);
                }
            });
        }
    }

    private void connectDevice(BluetoothDevice bluetoothDevice){
        mBluetoothGatt = bluetoothDevice.connectGatt(this, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(final BluetoothGatt gatt, int status, final int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                Log.e("bluetooth","thread name :"+Thread.currentThread().getName());
                BluetoothActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BluetoothActivity.this,"连接"+newState,Toast.LENGTH_SHORT).show();
                        if(newState == BluetoothProfile.STATE_CONNECTED){
                            mBluetoothGatt.discoverServices();
                        }else if(newState == BluetoothProfile.STATE_DISCONNECTED){

                        }
                    }
                });
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                BluetoothActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BluetoothActivity.this,"service changed",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                BluetoothActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BluetoothActivity.this,"读操作",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
               BluetoothActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(BluetoothActivity.this,"写操作",Toast.LENGTH_SHORT).show();
                   }
               });
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
            }


        });
    }

//    private void initBluetooth(){
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();   //整个系统的单一变量
//        if(mBluetoothAdapter == null){
//            Toast.makeText(this,"该手机不支持蓝牙操作",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(!mBluetoothAdapter.isEnabled()){
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent,BLUETOOTH_REQUEST_CODE);
//        }
//    }

    /*****
     * 初始化监听蓝牙变化的广播
     */
    private void initBroadcast() {
//        mBluetoothBroadcat = new BluetoothBroadcast();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(mBluetoothBroadcat,filter);
    }

    /****
     * 注销监听蓝牙变化的广播
     */
    private void unRegisterBroad() {
        unregisterReceiver(mBluetoothBroadcat);
    }

    /***
     * 开启发现设备，需要定位权限
     * 异步操作，不要再有设备连接时执行该操作
     */
//    private void startDiscovery(){
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mBluetoothBroadcat,filter);
//        if(mBluetoothAdapter.isDiscovering()){
//            mBluetoothAdapter.cancelDiscovery();
//        }
//        mBluetoothAdapter.startDiscovery();
//    }

    /****
     * 系统的蓝牙设备页面（对于无法发现周围设备的手机使用该方法）
     */
    private void startSyBluetoothSetting() {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intent);
    }

    /****
     * 停止发现设备
     *及时关闭发现设备的操作，避免浪费资源
     */
//    private void cancelDiscovery(){
//        mBluetoothAdapter.cancelDiscovery();
//    }

    /***
     * 查找已经配对的设备集合
     */
//    private void getBondedDevice(){
//        Set<BluetoothDevice> datas = mBluetoothAdapter.getBondedDevices();
//        if(datas != null && datas.size() > 0){
//            Toast.makeText(this,"已经配对的设备"+datas.size(),Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this,"没有任何已经配对的设备",Toast.LENGTH_SHORT).show();
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "您已经打开手机蓝牙功能", Toast.LENGTH_SHORT).show();
            scanBluetoothDevice();
        } else {
            Toast.makeText(this, "您已经关闭手机蓝牙功能", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                break;
        }
    }

    @Override
    public void onChooseDevice(BluetoothDevice device) {
        String msg = !TextUtils.isEmpty(device.getName())? device.getName():device.getAddress();
        Toast.makeText(this,"连接设备:"+msg,Toast.LENGTH_SHORT).show();
        connectDevice(device);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unRegisterBroad();
//        cancelDiscovery();
    }
}
