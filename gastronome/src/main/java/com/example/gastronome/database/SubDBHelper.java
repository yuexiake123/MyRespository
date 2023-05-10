package com.example.gastronome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gastronome.entity.Work;

import java.util.ArrayList;
import java.util.List;


public class SubDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gastronome.db";
    //关注信息表
    private static final String TABLE_SUB_INFO = "subscription";
    private static final int DB_VERSION = 1;
    private static SubDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private SubDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static SubDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new SubDBHelper(context);
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SUB_INFO + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "_uid INTEGER NOT NULL," +
                "_suid INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }


    public boolean checkIsSubscribed(int uid, int suid) {
        String sql = "select * from " + TABLE_SUB_INFO + " where _uid = ? and _suid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid),Integer.toString(suid)});
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public long deleteSubscription(int uid, int suid) {
        return mWDB.delete(TABLE_SUB_INFO,"_uid=? and _suid=?",new String[]{Integer.toString(uid),Integer.toString(suid)});
    }

    public long addSubscription(int uid, int suid) {
        ContentValues values = new ContentValues();
        values.put("_uid",uid);
        values.put("_suid",suid);
        return mWDB.insert(TABLE_SUB_INFO, null, values);
    }

    public List<Integer> getSuidListByUid(int uid) {
        List<Integer> list = new ArrayList<>();
        String sql = "select * from " + TABLE_SUB_INFO + " where _uid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
        while (cursor.moveToNext()) {
            int suid = cursor.getInt(2);
            list.add(suid);
        }
        return list;
    }

    public List<Integer> getUidListBySuid(int suid) {
        List<Integer> list = new ArrayList<>();
        String sql = "select * from " + TABLE_SUB_INFO + " where _suid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(suid)});
        while (cursor.moveToNext()) {
            int uid = cursor.getInt(1);
            list.add(uid);
        }
        return list;
    }

    public int getSubCountByUid(int uid) {
        int count = 0;
        String sql = "select * from " + TABLE_SUB_INFO + " where _uid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }

    public int getSubCountBySuid(int suid) {
        int count = 0;
        String sql = "select * from " + TABLE_SUB_INFO + " where _suid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(suid)});
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }


}
