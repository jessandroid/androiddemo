package com.example.dengquan.demo.bluetooth.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dengquan.demo.R;

import java.util.List;

/**
 * Created by dengquan on 2018/3/20.
 */

public class MyBluetoothAdapter extends RecyclerView.Adapter {
    private OnChooseDeviceListener onChooseDeviceListener;
    private Context mContext;
    private List<BluetoothDevice> mList;
    private BluetoothViewHolder mBluetoothViewHolder;
    public MyBluetoothAdapter(Context context){
        mContext = context;
    }

    public void setDataChanged(List<BluetoothDevice> list){
        mList = list;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BluetoothViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_blue_tooth,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mBluetoothViewHolder = (BluetoothViewHolder) holder;
        mBluetoothViewHolder.position = position;
        mBluetoothViewHolder.devicemsg.setText("MSG:"+ (!TextUtils.isEmpty(mList.get(position).getName()) ? mList.get(position).getName(): mList.get(position).getAddress()));
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    private class BluetoothViewHolder extends RecyclerView.ViewHolder{
        private int position;
        private TextView devicemsg;
        public BluetoothViewHolder(View itemView) {
            super(itemView);
            devicemsg = (TextView) itemView.findViewById(R.id.devicemsg);
            devicemsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onChooseDeviceListener != null){
                        onChooseDeviceListener.onChooseDevice(mList.get(position));
                    }
                }
            });
        }
    }

    public void setOnChooseDeviceListener(OnChooseDeviceListener listener){
        onChooseDeviceListener = listener;
    }

    public interface OnChooseDeviceListener{
        void onChooseDevice(BluetoothDevice device);
    }
}
