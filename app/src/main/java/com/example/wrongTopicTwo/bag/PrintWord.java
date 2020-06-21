package com.example.wrongTopicTwo.bag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.wrongTopicTwo.Conlist;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by 15601_000 on 2019/3/2.
 */

public class PrintWord {

    public static void doc_print(Context context, List<Conlist> dataList22, String newPath){
        //打印，导出为Word文件
        try {
            //从assets读取我们的Word模板
            InputStream is = context.getAssets().open("q.doc");
            File newFile = new File(newPath);
            //使用poi的HWPFDocument方法
            HWPFDocument doc = new HWPFDocument(is);
            //获取Range
            Range range = doc.getRange();
            int j=1;
            for (int a=0;a<dataList22.size();a++) {
                Conlist c2 = dataList22.get(a);
                //Topic lv = dataList1.get(a);
                if (c2.getChecBox()) {
                    range.insertAfter( j + "、" + c2.getContent() + "\r");//在文件末尾插入String
                    j = j+1;
                }
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            doc.write(ostream);
            //输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
            //Toast.makeText(this, "文书已生成", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void word_open(String newPath, Context context){
        //String path = Environment.getExternalStorageDirectory().getPath().concat("/").concat("myDoc").concat("/").concat("a.doc");
        File docFile = new File(newPath);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            uri = FileProvider.getUriForFile(context, "com.example.wrongTopicTwo.fileprovider", docFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(docFile);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/msword");//使用office打开doc
        //intent.setDataAndType(uri, "*/*");//弹出所有打开方式
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "请下载相关软件！",Toast.LENGTH_SHORT).show();
        }
    }

}
