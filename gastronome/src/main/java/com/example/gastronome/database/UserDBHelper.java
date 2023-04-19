package com.example.gastronome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.gastronome.entity.User;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gastronome.db";
    // 用户信息表
    private static final String TABLE_USER_INFO = "user_info";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static UserDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_INFO + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " name VARCHAR NOT NULL," +
                " account VARCHAR NOT NULL," +
                " password VARCHAR NOT NULL," +
                " area VARCHAR NOT NULL," +
                " picPath VARCHAR NOT NULL," +
                " date VARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public long save(User user) {
        ContentValues values = new ContentValues();
        values.put("name", user.name);
        values.put("account", user.account);
        values.put("password", user.password);
        values.put("area", user.area);
        values.put("picPath", user.picPath);
        values.put("date", user.date);
        return mWDB.insert(TABLE_USER_INFO, null, values);
    }


    public User loginVerify(String account, String password) {
        User user = null;
        String sql = "select * from " + TABLE_USER_INFO + " where account = ? and password = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{account,password});
        //Cursor cursor = mRDB.query(TABLE_USER_INFO,null,"account = ? and password = ?",new String[]{account,password},null,null,null);
        if(cursor.moveToNext()){
            user = new User();
            user.id = cursor.getInt(0);
            user.name = cursor.getString(1);
            user.account = cursor.getString(2);
            user.password = cursor.getString(3);
            user.area = cursor.getString(4);
            user.picPath = cursor.getString(5);
            user.date = cursor.getString(6);
        }
        Log.d("Jun",user.toString());
        return user;
    }
}
