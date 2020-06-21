package com.example.wrongTopicTwo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 15601_000 on 2019/1/29.
 * PrintTitle
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Conlist> mConlist;

    //声明自定义的监听接口
    private ContentAdapter.OnItemClickListener mOnItemClickListener;

    //定义接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //提供set方法供Activity或Fragment调用
    public void setOnItemClickListener(ContentAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView_print;
        CheckBox check_content;
        TextView textView_num;
        TextView textView_label;
        TextView textView_content;

        public ViewHolder(View view) {
            super(view);
            cardView_print = (CardView) view;
            check_content = (CheckBox) view.findViewById(R.id.checkBox_print);
            textView_num = (TextView) view.findViewById(R.id.tv_print_number);
            textView_label = (TextView) view.findViewById(R.id.tv_print_label);
            textView_content = (TextView) view.findViewById(R.id.tv_print_content);
        }
    }

    public ContentAdapter(List<Conlist> conlist) {
        mConlist = conlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.printtitle_select, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //绑定监听事件
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Conlist conlist = mConlist.get(position);
        holder.textView_num.setText(conlist.getNum());
        holder.textView_label.setText(conlist.getLabel());
        holder.textView_content.setText(conlist.getContent());
        //设置checkbox的选中状况
        holder.check_content.setChecked(conlist.getChecBox());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.cardView_print.setTag(position);
        holder.check_content.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (mConlist != null) {
            //这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mConlist.size();
    }
}