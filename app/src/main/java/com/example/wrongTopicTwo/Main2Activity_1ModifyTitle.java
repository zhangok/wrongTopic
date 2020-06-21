package com.example.wrongTopicTwo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrongTopicTwo.bag.SQLite;

/**
 * 3 题目修改或添加题目的编辑界面
 * 拍照or相册选图
 */

public class Main2Activity_1ModifyTitle extends AppCompatActivity implements View.OnClickListener{

    private SQLite dbHelper;
    private SQLiteDatabase db;
    private EditText sub;
    private EditText pro;
    private EditText lab1;
    private EditText editText_answer;//答案文字
    private ImageView answerView;//显示答案
    private Button read_answer;//查看答案
    private byte[] imgData=null;//答案图片
    //获取上个活动信息
    public int enter_state = 0;//0是新建一个note,1是更改原来的note
    public String last_content;//用来获取edittext内容
    public String subject1;
    //确定的科目
    private String subject_l;
    //保存未修改之前的值
    private String subject_s;
    private String label1_s;
    private String answer_s;//答案文字
    private byte[] imgData_s=null;//答案图片
    //调用文字识别的requestcode
    public static final int Tess=3;
    //拍照or相册选取
    private Uri imageUri;
    private static final int PICK_REQUEST_CODE = 1;
    private static final int Take_Photo=2;
    //添加答案图片的requestcode
    public static final int Answer=4;
    private static String at_tip="";//判断answer Tess
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_1modifytitle);
        //控件
        sub = (EditText)findViewById(R.id.edit_sub);
        pro = (EditText)findViewById(R.id.edit_pro);
        lab1 = (EditText)findViewById(R.id.edit_lab1);
        Button save = (Button)findViewById(R.id.button_save);
        ImageButton camera = (ImageButton)findViewById(R.id.imageButton_modify_camera);
        ImageButton gallery = (ImageButton)findViewById(R.id.imageButton_modify_gallery);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        save.setOnClickListener(this);
        //答案控件
        editText_answer = (EditText) findViewById(R.id.edit_answer);
        answerView = (ImageView)findViewById(R.id.ImageView_answer);
        read_answer = (Button)findViewById(R.id.button_answer_pic_display);
        Button answer_camera = (Button)findViewById(R.id.button_answer_pic_camera);
        Button answer_gallery = (Button)findViewById(R.id.button_answer_pic_gallery);
        read_answer.setOnClickListener(this);
        answer_camera.setOnClickListener(this);
        answer_gallery.setOnClickListener(this);
        //接收内容和id
        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        enter_state = myBundle.getInt("enter_state");
        subject1 = myBundle.getString("subject");
        //String content_tess=myBundle.getString("content_tess");//显示识别的文字
        //pro.setText(content_tess);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_modify);
        toolbar.setTitle("");
        //在toolbar中添加TextView使title居中
        TextView tv_title = (TextView) findViewById(R.id.title_modify);
        if(enter_state == 0){
            tv_title.setText("添加题目");
        }else if(enter_state == 1){
            tv_title.setText("修改题目");
        }
        setSupportActionBar(toolbar);
        //在toolbar添加返回按钮
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        }
        //初始化页面内容
        InitView();
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

    private void InitView(){
        dbHelper = new SQLite(this,"Problem.db",null,1);
        db = dbHelper.getWritableDatabase();
        //新建一个note
        if(enter_state==0){
            if ((!subject1.equals("")) && (!subject1.equals("全部"))){
                sub.setText(subject1);
            }
        }
        //更改原来的note，查询内容获得科目和标签
        if(enter_state==1){
            Cursor cursor=db.query("note",new String[]{"label1","subject","content","answer","answer_pic"},"content=?",new String[]{last_content},null,null,null);
            if (cursor.moveToFirst()){
                do{
                    //遍历cursor对象取出数据
                    String subject = cursor.getString(cursor.getColumnIndex("subject"));
                    String label1 = cursor.getString(cursor.getColumnIndex("label1"));
                    //答案
                    //将Blob数据转化为字节数组
                    imgData=cursor.getBlob(cursor.getColumnIndex("answer_pic"));
                    String answer = cursor.getString(cursor.getColumnIndex("answer"));
                    editText_answer.setText(answer);
                    sub.setText(subject);
                    lab1.setText(label1);
                    //保存原始值
                    subject_s=subject;
                    label1_s=label1;
                    answer_s=answer;
                    imgData_s=imgData;
                }while (cursor.moveToNext());
            }
            cursor.close();
            pro.setText(last_content);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton_modify_camera:
                at_tip = "content";
                //获取科目
                subject_l = sub.getText().toString();
                if(subject_l.equals("")){
                    Toast.makeText(this,"请先选择科目！",Toast.LENGTH_SHORT).show();
                    break;
                }
                //创建file对象，用于存储怕照后的图片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(Main2Activity_1ModifyTitle.this, "com.example.wrongTopicTwo.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Take_Photo);
                break;
            case R.id.imageButton_modify_gallery:
                at_tip = "content";
                subject_l = sub.getText().toString();
                if(subject_l.equals("")){
                    Toast.makeText(this,"请先选择科目！",Toast.LENGTH_SHORT).show();
                    break;
                }
                intent=new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,PICK_REQUEST_CODE );//打开相册
                break;
            case R.id.button_save:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //获取此刻时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateString = sdf.format(date);
                //获取edittext内容
                String content = pro.getText().toString();
                String subject = sub.getText().toString();
                String label1 = lab1.getText().toString();
                String answer = editText_answer.getText().toString();
                if(!content.equals("")){
                    if(enter_state==0){
                        //添加一个新的问题，向数据库添加信息
                        //如果题目重复不允许添加
                        Cursor cursor=db.query("note",new String[]{"content"},"content=?",new String[]{content},null,null,null);
                        int con_num = cursor.getCount();
                        cursor.close();
                        if(con_num==0){
                            ContentValues values = new ContentValues();
                            values.put("content", content);
                            values.put("date", dateString);
                            values.put("label1", label1);
                            values.put("subject", subject);
                            values.put("answer", answer);//////插入答案
                            values.put("answer_pic", imgData);//////插入答案图片
                            db.insert("note", null, values);
                            finish();
                        }else{
                            Toast.makeText(this, "已有此题！", Toast.LENGTH_SHORT).show();
                        }
                    } else if(enter_state==1){
                        //向数据库修改信息
                        ContentValues values = new ContentValues();
                        values.put("content", content);
                        values.put("date", dateString);
                        values.put("label1", label1);
                        //values.put("label2", label2);
                        values.put("subject", subject);
                        values.put("answer", answer);//////插入答案
                        values.put("answer_pic", imgData);//////插入答案图片
                        db.update("note", values, "content = ?", new String[]{last_content});
                        //如果题目重复,撤销修改
                        Cursor cursor=db.query("note",new String[]{"content"},"content=?",new String[]{content},null,null,null);
                        int con_num = cursor.getCount();
                        cursor.close();
                        if(con_num == 1){
                            finish();
                        }else{
                            values = new ContentValues();
                            values.put("content", last_content);
                            values.put("subject", subject_s);
                            values.put("label1", label1_s);
                            values.put("answer", answer_s);//////插入原答案
                            values.put("answer_pic", imgData_s);//////插入原答案图片
                            db.update("note", values, "content=? and date=?", new String[]{content,dateString});
                            Toast.makeText(this, "题目重复，修改无效！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(this, "请输入你的内容！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_answer_pic_display:
                if(imgData==null){
                    Toast.makeText(this, "答案为空，请先添加答案！", Toast.LENGTH_SHORT).show();
                }else{
                    //将字节数组转化为位图
                    Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                    //将位图显示为图片
                    answerView.setImageBitmap(imagebitmap);
                }
                break;
            case R.id.button_answer_pic_camera://拍摄答案
                at_tip = "answer";
                //创建file对象，用于存储怕照后的图片
                //File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(Main2Activity_1ModifyTitle.this, "com.example.wrongTopicTwo.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Take_Photo);
                break;
            case R.id.button_answer_pic_gallery:
                at_tip = "answer";
                intent=new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,PICK_REQUEST_CODE );//打开相册
                break;
            default:
                Toast.makeText(this,"YOU CLICK NULL",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //显示图片
    //接受回传的文字信息并显示
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case Take_Photo:
                if(resultCode==RESULT_OK){
                    //显示在下一个界面
                    Intent intent = new Intent(Main2Activity_1ModifyTitle.this, Main3Activity_1CropImage.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("code",Take_Photo);//Take_Photo=2
                    if(at_tip.equals("content")){
                        intent.putExtra("at_tip",at_tip);
                        intent.putExtra("subject",subject_l);
                        startActivityForResult(intent,Tess);
                    }else if(at_tip.equals("answer")){
                        intent.putExtra("at_tip",at_tip);
                        intent.putExtra("subject","");
                        startActivityForResult(intent,Answer);
                    }

                }
                break;
            case PICK_REQUEST_CODE:
                if (resultCode==RESULT_OK){
                    //判断手机版本号
                    if (Build.VERSION.SDK_INT>=19){
                        //4.4以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下系统
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case Tess:
                //接受回传的文字信息并再次回传
                if (resultCode==10){
                    String content_tess=data.getStringExtra("content_tess");
                    pro.setText(content_tess);//显示识别的文字
                    Toast.makeText(this,content_tess.length()+"",Toast.LENGTH_SHORT).show();
                }
                break;
            case Answer:
                if( resultCode == 11){
                    //传回byte[] imgData
                    imgData = data.getByteArrayExtra("imagedata");
                    //将字节数组转化为位图
                    Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                    //将位图显示为图片
                    answerView.setImageBitmap(imagebitmap);
                }else{
                    Toast.makeText(this,"添加或修改答案失败！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //相册选取
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri，通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri,则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath=uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //在下一个界面显示
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Intent intent = new Intent(Main2Activity_1ModifyTitle.this, Main3Activity_1CropImage.class);
            intent.putExtra("imagePath", imagePath);
            intent.putExtra("code",PICK_REQUEST_CODE);//PICK_REQUEST_CODE=1
            if(at_tip.equals("content")){
                intent.putExtra("at_tip",at_tip);
                intent.putExtra("subject",subject_l);
                startActivityForResult(intent,Tess);
            }else if(at_tip.equals("answer")){
                intent.putExtra("at_tip",at_tip);
                intent.putExtra("subject","");
                startActivityForResult(intent,Answer);
            }

        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}
