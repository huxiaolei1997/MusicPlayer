package com.example.mystery.musicplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mystery on 2017/12/26.
 */

public class HistorySQLOpenHelper extends SQLiteOpenHelper {
    //private HistorySQLOpenHelper historySQLOpenHelper;
    public HistorySQLOpenHelper(Context context) {
        super(context, "play_history.db", null, 5);
       // System.out.println("创建了一个数据库");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("HistorySQLOpenHelper", "HistorySQLOpenHelper onCreate");
        // 初始化数据库的表结构，执行一条建表的SQL语句
        db.execSQL("create table play_history(" +
                " id integer primary key autoincrement," +
                " song_name varchar(60)," +
                " singer varchar(20)," +
                " path varchar(200)) ");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.e("HistorySQLOpenHelper", "HistorySQLOpenHelper onOpen");
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
