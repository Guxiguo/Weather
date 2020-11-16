package com.example.weatherforecast_demo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDataBaseHelper extends SQLiteOpenHelper{     //对数据库进行创建
    public static  final String CREATE_NOTE = "create table Weather("
            + "id integer primary key not null,"
            + "data String not null )";
    public static final String CREATE_CONCERN = "create table Concern("
            +"city_code String primary key not null,"
            +"city_name String not null)";

    private Context mContext;
    public MyDataBaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void  onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_NOTE);
        db.execSQL(CREATE_CONCERN);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
