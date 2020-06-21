package com.example.wrongTopicTwo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;

import java.io.File;

import static com.example.wrongTopicTwo.bag.ProBitmap.convertGray;
import static com.example.wrongTopicTwo.bag.ProBitmap.getBinaryzationBitmap2;
import static com.example.wrongTopicTwo.bag.ProBitmap.getBinaryzationBitmap3;
import static com.example.wrongTopicTwo.bag.SDUtils.assets2SD;

/**
 * 识别裁剪后照片文字并回传
 */
public class Main3Activity_2Tessact extends AppCompatActivity implements View.OnClickListener{

    private ImageView ImageView_crop;//显示上页裁剪图片
    private TextView resultTv;
    private TextView tip;//识别所用时间
    private EditText result;//显示文字识别结果
    private Bitmap bt;
    //首页跳转modify 或 modify回跳
    //private String tip2;
    //subject
    private String subject;

    //TessBaseAPI初始化用到的第一个参数，是个目录。
    private static final String DATAPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    //在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。File.separator分隔符
    private static final String tessdata = DATAPATH + File.separator + "tessdata";
    //TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
    private static String DEFAULT_LANGUAGE;
    private static String CHI_LANGUAGE = "chi_sim";//中文字库
    private static String ENG_LANGUAGE = "eng";//英文字库
    private static String NUM_LANGUAGE = "tryy";//序号字库
    //assets中的文件名
    private static String CHI_LANGUAGE_NAME = CHI_LANGUAGE + ".traineddata";
    private static String ENG_LANGUAGE_NAME = ENG_LANGUAGE + ".traineddata";
    private static String NUM_LANGUAGE_NAME = NUM_LANGUAGE + ".traineddata";
    //保存到SD卡中的完整文件名
    private static String CHI_LANGUAGE_PATH = tessdata + File.separator + CHI_LANGUAGE_NAME;
    private static String ENG_LANGUAGE_PATH = tessdata + File.separator + ENG_LANGUAGE_NAME;
    private static String NUM_LANGUAGE_PATH = tessdata + File.separator + NUM_LANGUAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_2_tessact);
        //OpenCV库静态加载并初始化
        staticLoadCVLibraries();
        //控件
        Button RecBtn = (Button) findViewById(R.id.btn_rec);
        //Button engRecBtn = (Button) findViewById(R.id.btn_eng_rec);
        Button ok = (Button) findViewById(R.id.btn_ok);
        //Button proBtn = (Button) findViewById(R.id.btn_cv);//图片处理
        ImageView_crop = (ImageView) findViewById(R.id.ImageView_crop);
        resultTv = (TextView) findViewById(R.id.tv);
        result = (EditText) findViewById(R.id.editText);
        tip=(TextView)findViewById(R.id.textView);
        RecBtn.setOnClickListener(this);//中文识别图片
        //engRecBtn.setOnClickListener(this);//英文识别图片
        //proBtn.setOnClickListener(this);//图片处理
        ok.setOnClickListener(this);//页面跳转并传递题目
        //获取裁剪图片并显示
        displayCrop();
    }

    //获取裁剪图片并显示
    private void displayCrop(){
        Intent intent = getIntent();//获取用于启动Homepage的Intent
        String imagePath = intent.getStringExtra("imagePath");//获取imagePath
        //tip2 = intent.getStringExtra("tip");
        subject = intent.getStringExtra("subject");
        if(imagePath!=null){
            bt= BitmapFactory.decodeFile(imagePath);
            //二值化
            bt = getBinaryzationBitmap3(bt);
            ImageView_crop.setImageBitmap(bt);
        }else{
            Toast.makeText(this,"无法读取相册图片",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //OpenCV库静态加载并初始化
    private void staticLoadCVLibraries(){
        boolean load = OpenCVLoader.initDebug();
        if(load) {
            Log.i("CV", "Open CV Libraries loaded...");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*
             case R.id.btn_chi_rec:
                DEFAULT_LANGUAGE = "tryy+chi_sim";
                //DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
                //LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;
                resultTv.setText("请稍候，正在进行中文识别......");
                tip.setText(null);
                result.setText(null);
                recognition(bt);
                break;
            case R.id.btn_eng_rec:
                DEFAULT_LANGUAGE = "eng";
                //DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".traineddata";
                //LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;
                resultTv.setText("请稍候，正在进行英文识别......");
                tip.setText(null);
                result.setText(null);
                recognition(bt);
                break;
            case R.id.btn_cv:
                //图片处理
                bt = convertGray(bt);
                ImageView_crop.setImageBitmap(bt);
                break;
 */
            case R.id.btn_rec:
                if(subject.equals("语文")){
                    //DEFAULT_LANGUAGE = "tryy+chi_sim";
                    DEFAULT_LANGUAGE = "chi_sim";
                }else if(subject.equals("数学")){
                    DEFAULT_LANGUAGE = "chi_sim";
                }else if(subject.equals("英语")){
                    DEFAULT_LANGUAGE = "eng";
                }else{
                    DEFAULT_LANGUAGE = "chi_sim";
                }
                //Toast.makeText(this,DEFAULT_LANGUAGE,Toast.LENGTH_SHORT).show();
                resultTv.setText("请稍候，正在识别......");
                tip.setText(null);
                result.setText(null);
                recognition(bt);
                break;
            case R.id.btn_ok:
                //回传文字识别结果
                String con = result.getText().toString();
                //Toast.makeText(this,con,Toast.LENGTH_SHORT).show();
                /*
                if(tip2.equals("main") ){
                    Intent intent = new Intent(Main3Activity_2Tessact.this, Main2Activity_1ModifyTitle.class);
                    intent.putExtra("content_tess", con);
                    intent.putExtra("enter_state",0);
                    intent.putExtra("subject","全部");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("content_tess", con);
                    setResult(10, intent);
                }
                */
                Intent intent = new Intent();
                intent.putExtra("content_tess", con);
                setResult(10, intent);
                finish();
            default:
                break;
        }
    }

    //识别
    private void recognition(final Bitmap bt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String text = "";
                if (!checkCHITraineddataExists()) {
                    text += CHI_LANGUAGE_PATH + "字库不存在，开始复制\r\n";
                    assets2SD(getApplicationContext(), CHI_LANGUAGE_PATH, CHI_LANGUAGE_NAME);
                }
                if (!checkENGTraineddataExists()) {
                    text += ENG_LANGUAGE_PATH + "字库不存在，开始复制\r\n";
                    assets2SD(getApplicationContext(), ENG_LANGUAGE_PATH, ENG_LANGUAGE_NAME);
                }
                if (!checkNUMTraineddataExists()) {
                    text += NUM_LANGUAGE_PATH + "字库不存在，开始复制\r\n";
                    assets2SD(getApplicationContext(), NUM_LANGUAGE_PATH, NUM_LANGUAGE_NAME);
                }
                text +="字库已经存在，开始识别\r\n";

                long startTime = System.currentTimeMillis();
                //TessBaseAPI初始化，文字识别
                TessBaseAPI tessBaseAPI = new TessBaseAPI();
                tessBaseAPI.init(DATAPATH, DEFAULT_LANGUAGE);
                tessBaseAPI.setImage(bt);
                final String finalText1 = tessBaseAPI.getUTF8Text();//识别结果
                //计算识别时间
                long finishTime = System.currentTimeMillis();
                text = text+" 耗时" + (finishTime - startTime) + "毫秒";
                final String finalText=text;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultTv.setText("识别结果(可自行修改)：");
                        result.setText(finalText1);
                        tip.setText(finalText);
                    }
                });
                tessBaseAPI.end();
            }
        }).start();
    }

    public boolean checkCHITraineddataExists() {
        File file = new File(CHI_LANGUAGE_PATH);
        return file.exists();
    }
    public boolean checkENGTraineddataExists() {
        File file = new File(ENG_LANGUAGE_PATH);
        return file.exists();
    }
    public boolean checkNUMTraineddataExists() {
        File file = new File(NUM_LANGUAGE_PATH);
        return file.exists();
    }
}
