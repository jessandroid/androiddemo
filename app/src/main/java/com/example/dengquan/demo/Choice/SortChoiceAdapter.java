package com.example.dengquan.demo.Choice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dengquan.demo.R;

import java.util.List;

/**
 * 排序的适配器
 * Created by dengquan on 2018/3/1.
 */

public class SortChoiceAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<KidLiveSortBean> mList;
    private SortViewHolder sortViewHolder;
    private OnSortChoiceListener onSortChoiceListener;
    public SortChoiceAdapter(Context context){
        mContext = context;
    }

    public void setDataChanged(List<KidLiveSortBean> list){
        mList = list;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SortViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_sort_choice,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        sortViewHolder = (SortViewHolder)holder;
        sortViewHolder.position = position;
        sortViewHolder.sortTypeNameTxt.setText(mList.get(position).getSortName());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class SortViewHolder extends RecyclerView.ViewHolder{
        private int position;
        private TextView sortTypeNameTxt;
        public SortViewHolder(View itemView) {
            super(itemView);
            sortTypeNameTxt = (TextView)itemView.findViewById(R.id.sortTypeNameTxt);
            sortTypeNameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onSortChoiceListener != null){
                        onSortChoiceListener.onSortChoiceResult(position);
                    }
                }
            });
        }
    }

    public void setOnSortChoiceListener(OnSortChoiceListener onSortChoiceListener) {
        this.onSortChoiceListener = onSortChoiceListener;
    }

    public interface OnSortChoiceListener{
        void onSortChoiceResult(int position);
    }
}
