package com.example.wrongTopicTwo;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.wrongTopicTwo.bag.PrintWord.doc_print;
import static com.example.wrongTopicTwo.bag.PrintWord.word_open;


/**再
 * 22 选择题目并确定打印
 */

public class Main2Activity_3PrintTitle extends AppCompatActivity implements View.OnClickListener {

    private List<Topic> dataList1 = new ArrayList<>();
    private List<Conlist> contentList = new ArrayList<>();
    private ContentAdapter adapter;
    private String subject1;
    //private int i=0;
    //生成文件的所在的地址
    private static final String Path = "/storage/emulated/0/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_3printtitle);
        //控件
        Button ok = (Button)findViewById(R.id.button_22);
        Button all = (Button)findViewById(R.id.button_all_22);
        ok.setOnClickListener(this);
        all.setOnClickListener(this);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_print);
        toolbar.setTitle("");
        //在toolbar中添加TextView使title居中
        TextView tv_title = (TextView) findViewById(R.id.title_print);
        tv_title.setText("选题打印");
        setSupportActionBar(toolbar);
        //在toolbar添加返回按钮
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        }
        //初始化listview
        InitReView();
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

    private void InitReView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_print);
        //指定recyclerview为线性布局，类似listview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ContentAdapter(contentList);
        //设置item及item中控件的点击事件
        adapter.setOnItemClickListener(new ContentAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                //在此处理点击事件
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ContentAdapter.ViewHolder holder = new ContentAdapter.ViewHolder(view);
                // 改变CheckBox的状态
                holder.check_content.toggle();
                // 将CheckBox的选中状况记录下来
                Conlist cl = contentList.get(position);
                cl.setChecBox(holder.check_content.isChecked());
                contentList.set(position,cl);
            }
        });
        recyclerView.setAdapter(adapter);
        RefreshNotesList();
    }

    //在activity显示的时候更新view
    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();// 刷新，而不RefreshNotesList()可以保留勾选
    }

    public void RefreshNotesList(){
        /*
        //如果dataList已经有的内容，全部删掉
        int size = dataList22.size();
        if (size > 0) {
            dataList22.clear();
            //simple_adapter.notifyDataSetChanged();
            adapter1.notifyDataSetChanged();
        }
        */
        //读取intent信息
        Bundle myBundle = this.getIntent().getExtras();
        dataList1 = myBundle.getParcelableArrayList("dataList");
        subject1 = myBundle.getString("subject");
        if(dataList1.size()!=0){
            for(int a=0;a<dataList1.size();a++){
                Topic lv = dataList1.get(a);
                Conlist cl = new Conlist(lv.getNum(), lv.getLabel(), lv.getContent(), false);
                contentList.add(cl);
            }
        }else{
            Toast.makeText(this,"content is null",Toast.LENGTH_SHORT).show();
        }
    }

/**
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
        ContentAdapter.ViewHolder holder = (ContentAdapter.ViewHolder) view.getTag();
        // 改变CheckBox的状态
        holder.cb.toggle();
        // 将CheckBox的选中状况记录下来
        Conlist cl = dataList22.get(position);
        cl.setChecBox(holder.cb.isChecked());
        dataList22.set(position,cl);
        // 记录选中的个数
        /*
        if (holder.cb.isChecked()) {
            i++;
            Toast.makeText(this,String.valueOf(cl.getChecBox()),Toast.LENGTH_SHORT).show();
        } else {
            i--;
        }
    }
*/

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_22:
                //获取此刻时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                String dateString = sdf.format(date);
                //创建生成的文件路径
                String newPath = Path + dateString+"错题复习.doc";
                doc_print(Main2Activity_3PrintTitle.this, contentList, newPath);
                Toast.makeText(this, "文档已生成，正在打开...", Toast.LENGTH_SHORT).show();
                word_open(newPath, Main2Activity_3PrintTitle.this);
                //finish();
                break;
            case R.id.button_all_22:
                for(int a=0;a<contentList.size();a++){
                    Conlist cl = contentList.get(a);
                    cl.setChecBox(true);
                    contentList.set(a,cl);
                }
                adapter.notifyDataSetChanged();// 刷新
                break;
            default:
                break;
        }
    }
}
