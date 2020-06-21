package com.example.wrongTopicTwo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrongTopicTwo.bag.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * 2 按科目和标签显示题目
 */
public class Main2Activity_DisplayTitle extends AppCompatActivity implements View.OnClickListener{

    private TopicAdapter adapter;
    private List<Topic> topicList = new ArrayList<>();
    private String subject1;
    private SQLite dbHelper;
    private SQLiteDatabase db;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_displaytitle);
        //获取科目
        Bundle myBundle = this.getIntent().getExtras();
        subject1 = myBundle.getString("subject");
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main2_display);
        toolbar.setTitle("");
        //在toolbar中添加TextView使title居中
        TextView tv_title = (TextView) findViewById(R.id.toolbar_title);
        tv_title.setText(subject1);
        setSupportActionBar(toolbar);
        //在toolbar添加返回按钮
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        }
        //按钮
        Button add = (Button) findViewById(R.id.button_add);
        Button select = (Button)findViewById(R.id.button_select);
        Button print = (Button)findViewById(R.id.button_print);
        add.setOnClickListener(this);
        select.setOnClickListener(this);
        print.setOnClickListener(this);
        //下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPink1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                RefreshNotesList();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        dbHelper = new SQLite(this,"Problem.db",null,1);
        InitRecyclerView();//初始化
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


    private void InitRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_display);
        //指定recyclerview为线性布局，类似listview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TopicAdapter(topicList);
        //设置item及item中控件的点击事件
        adapter.setOnItemClickListener(MyItemClickListener);
        recyclerView.setAdapter(adapter);
    }

    private TopicAdapter.OnItemClickListener MyItemClickListener = new TopicAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position){
            //在此处理点击事件
            if(view.getId() == R.id.imageButton_delete){
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity_DisplayTitle.this);
                builder.setTitle("删除该题目");
                builder.setMessage("确认删除吗？");
                final Topic lv = topicList.get(position);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取listview中此个item中的内容
                        String content1 = lv.getContent();
                        db.delete("note", "content = ?", new String[]{content1});
                        //删除该行后刷新
                        RefreshNotesList();
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                builder.show();
            }else{
                Topic topic =topicList.get(position);
                String content1 = topic.getContent();
                //跳转到题目编辑活动，1表示编辑
                Intent intent = new Intent(Main2Activity_DisplayTitle.this, Main2Activity_1ModifyTitle.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", content1);
                bundle.putString("subject", subject1);
                bundle.putInt("enter_state", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    //在activity显示的时候更新view
    @Override
    protected void onStart() {
        super.onStart();
        //adapter.notifyDataSetChanged();// 刷新
        RefreshNotesList();
        //若使用adapter.notifyDataSetChanged()，修改题目返回后不会改变
    }

    //刷新view
    public void RefreshNotesList(){
        //获取上一个活动传递的数据
        Bundle myBundle = this.getIntent().getExtras();
        String[] lab = myBundle.getStringArray("lab");
        subject1 = myBundle.getString("subject");
        //如果dataList已经有的内容，全部删掉
        int size = topicList.size();
        if (size > 0) {
            topicList.clear();
            adapter.notifyDataSetChanged();
        }
        //从数据库读取信息
        db = dbHelper.getWritableDatabase();
        if(subject1!=null){
            //查询
            if(lab==null){
                //按科目查询
                Cursor cursor;
                if (subject1.equals("全部")){
                    cursor=db.query("note",new String[]{"subject","label1","content","date"},null,null,null,null,null);
                }else{
                    cursor=db.query("note",new String[]{"subject","label1","content","date"},"subject=?",new String[]{subject1},null,null,null);
                }
                //遍历cursor对象取出数据
                if (cursor.moveToFirst()){
                    int number=0;
                    do{
                        String label = cursor.getString(cursor.getColumnIndex("label1"));
                        String con = cursor.getString(cursor.getColumnIndex("content"));
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        number++;
                        String num = String.valueOf(number)+"题";
                        Topic lv = new Topic(num, label, con, date);
                        topicList.add(lv);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                //Toast.makeText(this,"lab==null",Toast.LENGTH_SHORT).show();
            } else{
                int number=0;
                //按科目和标签查询
                for(int i=0;i<lab.length;i++){
                    Cursor cursor;
                    if (subject1.equals("全部")){
                        cursor=db.query("note",new String[]{"label1","content","date"},"label1=?",new String[]{lab[i]},null,null,null);
                    }else{
                        cursor=db.query("note",new String[]{"label1","subject","content","date"},"label1=? and subject=?",new String[]{lab[i],subject1},null,null,null);
                    }
                    //遍历cursor对象取出数据
                    if (cursor.moveToFirst()){
                        do{
                            //String label = cursor.getString(cursor.getColumnIndex("label1"));
                            String con = cursor.getString(cursor.getColumnIndex("content"));
                            String date = cursor.getString(cursor.getColumnIndex("date"));
                            number++;
                            String num = String.valueOf(number)+"题";
                            Topic lv = new Topic(num, lab[i], con, date);
                            topicList.add(lv);
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                //Toast.makeText(this,"lab!=null",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"查询科目为空",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_add:
                intent = new Intent(Main2Activity_DisplayTitle.this, Main2Activity_1ModifyTitle.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                bundle.putInt("enter_state", 0);
                bundle.putString("subject", subject1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button_select:
                intent = new Intent(Main2Activity_DisplayTitle.this, Main2Activity_2ChooseLabel.class);
                intent.putExtra("subject",subject1);
                //startActivityForResult(intent, REQUESTCODE);//标签筛选并返回结果
                startActivity(intent);//标签筛选并返回结果
                break;
            case R.id.button_print:
                intent = new Intent(Main2Activity_DisplayTitle.this, Main2Activity_3PrintTitle.class);
                Bundle b=new Bundle();
                //将dataList1传递到活动22
                b.putParcelableArrayList("dataList",(ArrayList<Topic>) topicList);
                b.putString("subject", subject1);
                intent.putExtras(b);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

