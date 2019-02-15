package com.example.dengquan.demo.Choice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dengquan.demo.R;

import java.util.List;

/**
 * 分类的种类的适配器
 * Created by dengquan on 2018/3/1.
 */

public class ClassificationChoiceAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<KidLiveClassificationBean> mList;
    private ClassificationViewHolder classificationViewHolder;
    private OnClassificationItemClickListener onClassificationItemClickListener;
    public ClassificationChoiceAdapter(Context context){
        mContext = context;
    }

    public void setDataChanged(List<KidLiveClassificationBean> list){
        mList = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassificationViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_classification_choice,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        classificationViewHolder = (ClassificationViewHolder) holder;
        classificationViewHolder.position = position;
        classificationViewHolder.classificationNameTxt.setText(mList.get(position).getClassificationName());
        classificationViewHolder.checkBox.setChecked(mList.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class ClassificationViewHolder extends RecyclerView.ViewHolder{
        private int position;
        private RelativeLayout homeView;
        private TextView classificationNameTxt;
        private CheckBox checkBox;
        public ClassificationViewHolder(View itemView) {
            super(itemView);
            homeView = (RelativeLayout)itemView.findViewById(R.id.homeView);
            classificationNameTxt = (TextView)itemView.findViewById(R.id.classificationNameTxt);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);
            homeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClassificationSelectedEvent(position);
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClassificationSelectedEvent(position);
                }
            });

        }

        private void onClassificationSelectedEvent(int position){
            if(onClassificationItemClickListener != null){
                onClassificationItemClickListener.onClassificationItemClick(position);
            }
            mList.get(position).setChecked(!mList.get(position).isChecked());
            notifyDataSetChanged();
        }
    }

    public void setOnClassificationItemClickListener(OnClassificationItemClickListener onClassificationItemClickListener) {
        this.onClassificationItemClickListener = onClassificationItemClickListener;
    }

    public interface OnClassificationItemClickListener{
        void onClassificationItemClick(int position);
    }
}
