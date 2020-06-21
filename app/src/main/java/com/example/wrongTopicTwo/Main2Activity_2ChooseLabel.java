package com.example.wrongTopicTwo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrongTopicTwo.bag.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * 4 选择标签
 */

public class Main2Activity_2ChooseLabel extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    private ListView listview;
    private LabelAdapter adapter1;
    private List<Lablist> dataList2 = new ArrayList<>();
    private Button ok;
    private String subject1;
    private SQLite dbHelper;
    private SQLiteDatabase db;
    private int i=0; // 记录选中的条目数量
    private String[] strArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_2chooselabel);
        //控件
        ok = (Button)findViewById(R.id.button_ok);
        ok.setOnClickListener(this);
        //获取前一页传递的内容
        Intent intent = getIntent();//获取用于启动Homepage的Intent
        subject1 = intent.getStringExtra("subject");
        //数据库
        dbHelper = new SQLite(this,"Problem.db",null,1);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_label);
        toolbar.setTitle("");
        //在toolbar中添加TextView使title居中
        TextView tv_title = (TextView) findViewById(R.id.title_label);
        tv_title.setText("选择分类标签");
        setSupportActionBar(toolbar);
        //在toolbar添加返回按钮
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        }
        //初始化listview
        InitlistView();
    }

    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void InitlistView(){
        //可尝试与22合并
        adapter1 = new LabelAdapter(Main2Activity_2ChooseLabel.this, R.layout.label_select, dataList2);
        listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter1);// 绑定Adapter
        listview.setOnItemClickListener(this);
        RefreshNotesList();
    }

    //在activity显示的时候更新listview
    @Override
    protected void onStart() {
        super.onStart();
        adapter1.notifyDataSetChanged();// 刷新，而不RefreshNotesList()可以保留勾选
        //RefreshNotesList();
    }

    //刷新listview
    public void RefreshNotesList(){
        /*
        //如果dataList已经有的内容，全部删掉
        int size = dataList2.size();
        if (size > 0) {
            dataList2.clear();
            //simple_adapter.notifyDataSetChanged();
            adapter1.notifyDataSetChanged();
        }
        */
        //从数据库读取信息
        db = dbHelper.getWritableDatabase();
        Cursor cursor;
        if(!subject1.equals("")){
            //按科目获取标签
            /**
             * 参数一：是否去重
             * 参数二：表名
             * 参数三：columns 表示查询的字段,new String[]{MODEL}表示查询该表当中的模式（也表示查询的结果）
             * 参数思：selection表示查询的条件，PHONE_NUMBER+" = ?" 表示根据手机号去查询模式
             * 参数五：selectionArgs 表示查询条件对应的值,new String[]{phoneNumber}表示查询条件对应的值
             * 参数六：String groupBy 分组
             * 参数七：String having
             * 参数八：orderBy 表示根据什么排序,
             * 参数九：limit  限制查询返回的行数，NULL表示无限制子句
             */
            if (subject1.equals("全部")){
                cursor=db.query(true,"note",new String[]{"label1"},null,null,null,null,null,null);
            }else{
                cursor=db.query(true,"note",new String[]{"subject","label1"},"subject=?",new String[]{subject1},null,null,null,null);
            }
            //遍历cursor对象取出数据
            if (cursor.moveToFirst()){
                do{
                    String lab1 = cursor.getString(cursor.getColumnIndex("label1"));
                    Lablist lv = new Lablist(lab1,false);
                    dataList2.add(lv);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }else{
            Toast.makeText(this,"subject is null",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
        LabelAdapter.ViewHolder holder = (LabelAdapter.ViewHolder) view.getTag();
        // 改变CheckBox的状态
        holder.cb.toggle();
        // 将CheckBox的选中状况记录下来
        Lablist lv = dataList2.get(position);
        lv.setChecBox(holder.cb.isChecked());
        dataList2.set(position,lv);
        // 记录选中的数量
        if (holder.cb.isChecked()) {
            i++;
            //Toast.makeText(this,String.valueOf(lv.getChecBox()),Toast.LENGTH_SHORT).show();
        } else {
            i--;
        }
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.button_ok){
            strArray = new String[i];
            int j=0;
            for(int a=0;a<dataList2.size();a++){
                Lablist lv = dataList2.get(a);
                if(lv.getChecBox()){
                    strArray[j]=lv.getLabel();
                    j++;
                }
            }
            Intent intent = new Intent(Main2Activity_2ChooseLabel.this, Main2Activity_DisplayTitle.class);
            Bundle b=new Bundle();
            b.putStringArray("lab",strArray);
            b.putString("subject", subject1);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }
}
