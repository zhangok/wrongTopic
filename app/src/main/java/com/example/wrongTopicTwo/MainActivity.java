package com.example.wrongTopicTwo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wrongTopicTwo.bag.SQLite;


/**
 * 科目或查看全部
 * 拍照or相册选照片
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private SQLite dbHelper;
    private SQLiteDatabase db;
    private static final int PERMISSION_REQUEST_CODE = 0;
    /*
    //拍照or相册
    private Uri imageUri;
    private static final int PICK_REQUEST_CODE = 1;
    private static final int Take_Photo=2;
    private String tip = "main";
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        //照片按钮
        ImageButton camera = (ImageButton)findViewById(R.id.imageButton_camera);
        ImageButton gallery = (ImageButton)findViewById(R.id.imageButton_gallery);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        */
        //添加错题按钮
        Button add = (Button)findViewById(R.id.button_main_add);
        add.setOnClickListener(this);
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("智能错题本");
        setSupportActionBar(toolbar);
        //cardview点击事件
        CardView card_chi=(CardView)findViewById(R.id.cardView_chi);
        CardView card_eng=(CardView)findViewById(R.id.cardView_eng);
        CardView card_math=(CardView)findViewById(R.id.cardView_math);
        CardView card_all=(CardView)findViewById(R.id.cardView_all);
        card_chi.setOnClickListener(this);
        card_eng.setOnClickListener(this);
        card_math.setOnClickListener(this);
        card_all.setOnClickListener(this);
        //请求权限
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
        //创建数据库
        InitView();
    }

    private void InitView(){
        dbHelper = new SQLite(this,"Problem.db",null,1);
        //当数据库不可写入时getReadableDatabase()返回的对象只读打开数据库，getWritableDatabase()将出现异常
        db = dbHelper.getWritableDatabase();
    }

    //请求权限回应
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //assets2SD(getApplicationContext(), LANGUAGE_PATH, DEFAULT_LANGUAGE_NAME);
                    Toast.makeText(this, "你已允许读写权限", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "你已禁止读写权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        String name;
        Intent intent;
        switch (v.getId()){
            case R.id.cardView_chi:
                name = "语文";
                intent = new Intent(MainActivity.this, Main2Activity_DisplayTitle.class);
                Bundle b=new Bundle();
                b.putString("subject", name);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.cardView_eng:
                name = "英语";
                intent = new Intent(MainActivity.this, Main2Activity_DisplayTitle.class);
                b=new Bundle();
                b.putString("subject", name);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.cardView_math:
                name = "数学";
                intent = new Intent(MainActivity.this, Main2Activity_DisplayTitle.class);
                b=new Bundle();
                b.putString("subject", name);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.cardView_all:
                name = "全部";
                intent = new Intent(MainActivity.this, Main2Activity_DisplayTitle.class);
                b=new Bundle();
                b.putString("subject", name);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.button_main_add:
                name = "全部";
                intent = new Intent(MainActivity.this, Main2Activity_1ModifyTitle.class);
                b=new Bundle();
                b.putString("info", "");
                b.putInt("enter_state", 0);
                b.putString("subject", name);
                intent.putExtras(b);
                startActivity(intent);
                break;
            /*
            case R.id.imageButton_camera:
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
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.wrongTopic.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Take_Photo);
                break;
            case R.id.imageButton_gallery:
                intent=new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,PICK_REQUEST_CODE );//打开相册
                break;
                */
            default:
                break;
        }
    }
/*
    //显示图片
    @Override
    protected void  onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case Take_Photo:
                if(resultCode==RESULT_OK){
                    //显示在下一个界面
                    Intent intent = new Intent(MainActivity.this, Main3Activity_1CropImage.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("code",Take_Photo);//Take_Photo=2
                    intent.putExtra("tip",tip);
                    startActivity(intent);
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
            Intent intent = new Intent(MainActivity.this, Main3Activity_1CropImage.class);
            intent.putExtra("imagePath", imagePath);
            intent.putExtra("code",PICK_REQUEST_CODE);//PICK_REQUEST_CODE=1
            intent.putExtra("tip",tip);
            startActivity(intent);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
 */
}
