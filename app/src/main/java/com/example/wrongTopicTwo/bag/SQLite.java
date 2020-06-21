package com.example.wrongTopicTwo.bag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 15601_000 on 2018/11/17.
 */

public class SQLite extends SQLiteOpenHelper {
    //建表语句
    public static final String CreateNote = "create table note ("
            + "id integer primary key autoincrement,"//id列自增长
            + "subject text,"
            + "label1 text,"
            + "content text,"
            + "answer text,"
            + "answer_pic blob,"
            + "date text)";

    private Context mContext;

    public SQLite (Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CreateNote);//执行建表语句
        Toast.makeText(mContext,"create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
