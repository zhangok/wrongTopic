package com.example.wrongTopicTwo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 15601_000 on 2018/12/23.
 * DisplayTitle
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<Topic> mTopicList;

    //定义接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    //声明自定义的监听接口
    private OnItemClickListener mOnItemClickListener;
    //提供set方法供Activity或Fragment调用
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageButton delete;
        TextView textView_num;
        TextView textView_label;
        TextView textView_content;
        TextView textView_date;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            delete = (ImageButton) view.findViewById(R.id.imageButton_delete);
            textView_num = (TextView) view.findViewById(R.id.tv_number);
            textView_label = (TextView) view.findViewById(R.id.tv_label);
            textView_content = (TextView) view.findViewById(R.id.tv_content);
            textView_date = (TextView) view.findViewById(R.id.tv_date);
        }
    }

    public TopicAdapter(List<Topic> topicList) {
        mTopicList = topicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_display, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //绑定监听事件
        view.setOnClickListener(this);
        holder.delete.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topic topic = mTopicList.get(position);
        holder.textView_num.setText(topic.getNum());
        holder.textView_label.setText(topic.getLabel());
        holder.textView_content.setText(topic.getContent());
        holder.textView_date.setText(topic.getDate());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.cardView.setTag(position);
        holder.delete.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (mTopicList!=null){
            //这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return mTopicList.size();
    }
}
