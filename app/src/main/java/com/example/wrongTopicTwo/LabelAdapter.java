package com.example.wrongTopicTwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 15601_000 on 2019/1/5.
 * ChooseLabel
 */

public class LabelAdapter extends ArrayAdapter<Lablist>{
    private int resourceId;
    public LabelAdapter(Context context, int textViewResourceId, List<Lablist> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    /**
    //填充数据的list
    private ArrayList<String> list;
    //用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;
    // 构造器
    public LabelAdapter(ArrayList<String> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }
    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            getIsSelected().put(i,false);
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lablist lablist = getItem(position);//获取当前项的listv实例
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            holder.tv = (TextView) convertView.findViewById(R.id.textView_label);
            holder.cb = (CheckBox) convertView.findViewById(R.id.label_checkBox);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv.setText(lablist.getLabel());
        // 根据isSelected来设置checkbox的选中状况
        //holder.cb.setChecked(getIsSelected().get(position));
        holder.cb.setChecked(lablist.getChecBox());
        return convertView;
    }
    class ViewHolder{
        TextView tv;
        CheckBox cb;
    }
    /*
    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        LabelAdapter.isSelected = isSelected;
    }
    */
}
