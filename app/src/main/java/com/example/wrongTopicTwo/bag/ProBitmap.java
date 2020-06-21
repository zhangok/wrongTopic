package com.example.wrongTopicTwo.bag;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by 15601_000 on 2019/3/3.
 */

public class ProBitmap {
    //灰度化
    public static Bitmap convertGray(Bitmap bt) {
        Mat src = new Mat();
        Mat temp = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bt, src);//获取彩色图像所对应的像素数据
        Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGRA2BGR);//图像灰度化,将彩色图像数据转换为灰度图像数据并存储到temp中
        Imgproc.cvtColor(temp, dst, Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(dst, bt);
        return bt;
    }
    //二值化
    //局部自适应阈值法
    public static Bitmap getBinaryzationBitmap2(Bitmap bm) {
        Mat src = new Mat();
        Mat temp = new Mat();
        Mat temp1 = new Mat();
        Mat temp2 = new Mat();
        Mat temp3 = new Mat();
        Mat dst = new Mat();
        Mat dst1 = new Mat();
        Mat dst2 = new Mat();
        Utils.bitmapToMat(bm, src);//获取彩色图像所对应的像素数据
        //Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGRA2BGR);//图像灰度化,将彩色图像数据转换为灰度图像数据并存储到temp中
        Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(temp, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 25, 10);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(2,2));
        Imgproc.dilate(dst,temp1,kernel);//膨胀
        Imgproc.erode(temp1,temp2, kernel);//腐蚀
        Imgproc.erode(temp2,temp3, kernel);//腐蚀
        Imgproc.dilate(temp3,dst1,kernel);//膨胀
        Imgproc.erode(dst1,dst2, kernel);//腐蚀
        Utils.matToBitmap(dst2, bm);
        return bm;
    }
    //大津法
    public static Bitmap getBinaryzationBitmap3(Bitmap bm){
        Mat src = new Mat();
        Mat temp = new Mat();
        Mat dst = new Mat();
        Mat dst1 = new Mat();
        Utils.bitmapToMat(bm, src);//获取彩色图像所对应的像素数据
        //Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGRA2BGR);//图像灰度化,将彩色图像数据转换为灰度图像数据并存储到temp中
        Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(temp, dst, 0, 255, Imgproc.THRESH_OTSU);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(2,2));
        Imgproc.erode(dst,dst1, kernel);//腐蚀
        Utils.matToBitmap(dst1, bm);
        return bm;
    }
    //倾斜校正
    public static void rotate(Bitmap bt){}
}
