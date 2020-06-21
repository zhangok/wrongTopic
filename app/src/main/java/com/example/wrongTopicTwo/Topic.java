package com.example.wrongTopicTwo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 15601_000 on 2018/12/29.
 * DisplayTitle
 */

public class Topic implements Parcelable {
    private String number;
    private String label;
    private String content;
    private String date;

    public Topic(String number, String label, String content, String date){
        this.number = number;
        this.label = label;
        this.content=content;
        this.date = date;
    }
    public String getNum(){
        return number;
    }
    public String getLabel(){
        return label;
    }
    public String getContent(){
        return content;
    }
    public String getDate(){
        return date;
    }

    public Topic(Parcel in) {
        number = in.readString();
        label = in.readString();
        content = in.readString();
        date = in.readString();
    }
    //内容描述功能，这个方法一般情况下都返回0；
    @Override
    public int describeContents() {
        return 0;
    }
    //序列化功能，通过Parcel中一系列的的write方法来完成
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(label);
        dest.writeString(content);
        dest.writeString(date);
    }
    //反序列化功能由CREATOR 来完成，里边标明了如何创建序列化对象和数组，
    // 并通过一系列的read方法来完成反序列化过程；
    public static final Parcelable.Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }
        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

}