package com.example.wrongTopicTwo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;

import org.opencv.android.OpenCVLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.wrongTopicTwo.Main2Activity_1ModifyTitle.Tess;
import static com.example.wrongTopicTwo.bag.ProBitmap.convertGray;
import static com.example.wrongTopicTwo.bag.ProBitmap.getBinaryzationBitmap2;
import static com.example.wrongTopicTwo.bag.SDUtils.saveCrop;

/**
 * 显示选取的照片并手动裁剪
 */
public class Main3Activity_1CropImage extends AppCompatActivity  implements View.OnClickListener{
    private CropImageView cropImageView;
    private Bitmap bt;
    //裁剪后图片保存地址
    private static final String storePath = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/";
    //主界面跳转或modify界面跳转
    //private String tip;
    //subject
    private String subject;
    private byte[] imagedata =null;//存储要回传的答案图片的字节数组
    private static String at_tip="";//判断answer Tess

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_1_cropimage);
        //控件
        Button cropBtn = (Button) findViewById(R.id.btn_crop);//裁剪按钮
        Button rotateBtn=(Button)findViewById(R.id.btn_rotate);//旋转按钮
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);//裁剪图片显示
        cropBtn.setOnClickListener(this);//裁剪图片
        rotateBtn.setOnClickListener(this);//旋转
        // 先获取屏幕宽高
        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        //解决方案2：设置cropImageView的最小宽高
        ViewGroup.LayoutParams params = cropImageView.getLayoutParams();
        params.width = width;
        params.height = width;
        cropImageView.setLayoutParams(params);

        staticLoadCVLibraries();//二值化答案图片
        //显示
        display1();
    }

    //OpenCV库静态加载并初始化
    private void staticLoadCVLibraries(){
        boolean load = OpenCVLoader.initDebug();
        if(load) {
            Log.i("CV", "Open CV Libraries loaded...");
        }
    }

    private void display1(){
        //获取intent信息
        Intent intent = getIntent();//获取用于启动Homepage的Intent
        int code=intent.getIntExtra("code",-1);//若没有取到数据，默认值为-1
        subject = intent.getStringExtra("subject");
        at_tip = intent.getStringExtra("at_tip");//判断answer Tess
        switch (code){
            case -1:
                Toast.makeText(this,"无法获取图片", Toast.LENGTH_SHORT).show();
                break;
            case 1://PICK_REQUEST_CODE=1
                String imagePath=intent.getStringExtra("imagePath");//获取imagePath
                if(imagePath!=null){
                    bt= BitmapFactory.decodeFile(imagePath);
                    cropImageView.setImageBitmap(bt);
                    //bt=getBitmap(bt);//调整图片大小
                    //picture.setImageBitmap(bitmap);
                }else{
                    Toast.makeText(this,"无法读取相册图片",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case 2://Take_Photo=2
                //显示图像
                Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));//获取Uri
                try {
                    bt = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    if (bt == null) {
                        Toast.makeText(this, "无法读取拍摄照片", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    cropImageView.setImageBitmap(bt); //显示拍照图片
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(this,"error : default", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     *在集成完cropper后，你会发现有些图片在显示的时，会出现填充不满布局的情况。
     *造成这种情况的原因也很简单，bitmap图片也是有高度宽度的，如果bitmap的高度宽度都小于CropImageView的宽高，这样就会导致图片显示无法填充满屏幕。
     *解决方案1：放大bitmap至屏幕大小

    public Bitmap getBitmap(Bitmap bitmap) {
        //WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        //先获取屏幕宽高
        //int width = wm.getDefaultDisplay().getWidth();
        //int height = wm.getDefaultDisplay().getHeight();
        //获取cropImageView宽高
        ViewGroup.LayoutParams params = cropImageView.getLayoutParams();
        int width = params.width ;
        int height = params.height ;
        float scaleWidth = 1, scaleHeight = 1;

        if (bitmap.getWidth() < width) {
            //强转为float类型，
            scaleWidth = (float)width / (float)bitmap.getWidth();
        }

        if (bitmap.getHeight() < height) {
            scaleHeight = (float)height / (float)bitmap.getHeight();
        }

        if (scaleWidth > scaleHeight)
            scaleHeight = scaleWidth;
        else
            scaleWidth = scaleHeight;

        Matrix matrix = new Matrix();
        //根据屏幕大小选择bitmap放大比例。
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_crop:
                bt = cropImageView.getCroppedImage();
                //cropImageView.setImageBitmap(bt);
                // saveCrop(Context context,Bitmap bitmap,String fileName)
                String Name = System.currentTimeMillis() + ".jpg";
                String fileName = storePath + Name;
                //保存裁剪后的照片
                saveCrop(this, bt, fileName);
                if(at_tip.equals("content")){
                    //裁剪后的照片显示在下一页
                    Intent intent = new Intent(this, Main3Activity_2Tessact.class);
                    intent.putExtra("imagePath", fileName);
                    intent.putExtra("subject",subject);
                    startActivityForResult(intent,Tess);
                }else if(at_tip.equals("answer")){
                    //二值化
                    bt = getBinaryzationBitmap2(bt);
                    //Toast.makeText(this, "answer照片", Toast.LENGTH_SHORT).show();
                    //图片转为二进制数据
                    int size = bt.getWidth() * bt.getHeight() * 4;
                    //创建一个字节数组输出流,流的大小为size
                    ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
                    try {
                        //设置位图的压缩格式，质量为100%，并放入字节数组输出流中!!!!!图片太大无法传递
                        bt.compress(Bitmap.CompressFormat.PNG, 50, baos);
                        /*
                        int options = 100;
                        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                            baos.reset();//重置baos即清空baos
                            bt.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                            options -= 10;//每次都减少10
                        }
                        */
                        //将字节数组输出流转化为字节数组byte[]
                        imagedata = baos.toByteArray();
                    }catch (Exception e){
                    }finally {
                        try {
                            bt.recycle();
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("imagedata", imagedata);
                    setResult(11, intent);
                    finish();
                }
                //裁剪后的照片显示在下一页
                //Intent intent = new Intent(this, Main3Activity_2Tessact.class);
                //intent.putExtra("imagePath", fileName);//传递照片保存地址
                //intent.putExtra("subject",subject);
                //startActivityForResult(intent,Tess);
                /*
                if(tip.equals("main")){
                    intent.putExtra("tip", "main");
                    startActivity(intent);
                    finish();
                }else if(tip.equals("modify")){
                    intent.putExtra("tip","modify");
                    startActivityForResult(intent,Tess);
                }
                */
                break;
            case R.id.btn_rotate:
                cropImageView.rotateImage(90);//设定图片的旋转角度
                break;
            default:
                break;
        }
    }

    //接受回传的文字信息并再次回传
    @Override
    protected void  onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == Tess && resultCode == 10){
            setResult(10, data);
            finish();
        }
    }
}
