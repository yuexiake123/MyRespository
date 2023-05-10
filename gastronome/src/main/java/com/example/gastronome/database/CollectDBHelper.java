package com.example.gastronome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class CollectDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gastronome.db";
    //关注信息表
    private static final String TABLE_COLLECT_INFO = "collect_info";
    private static final int DB_VERSION = 1;
    private static CollectDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private CollectDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static CollectDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new CollectDBHelper(context);
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COLLECT_INFO + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "_uid INTEGER NOT NULL," +
                "_wid INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean checkIsCollected(int uid, int wid) {
        String sql = "select * from " + TABLE_COLLECT_INFO + " where _uid = ? and _wid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid),Integer.toString(wid)});
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public long deleteCollect(int uid, int wid) {
        return mWDB.delete(TABLE_COLLECT_INFO,"_uid=? and _wid=?",new String[]{Integer.toString(uid),Integer.toString(wid)});
    }

    public long addCollect(int uid, int wid) {
        ContentValues values = new ContentValues();
        values.put("_uid",uid);
        values.put("_wid",wid);
        return mWDB.insert(TABLE_COLLECT_INFO, null, values);
    }

    public int getCollectCountByWid(int wid) {
        int count = 0;
        String sql = "select * from " + TABLE_COLLECT_INFO + " where _wid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(wid)});
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }

    public int getCollectCountByUid(int uid) {
        int count = 0;
        String sql = "select * from " + TABLE_COLLECT_INFO + " where _uid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }

    public List<Integer> getWorkListByUid(int uid) {
        List<Integer> list = new ArrayList<>();
        String sql = "select * from " + TABLE_COLLECT_INFO + " where _uid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(2);
            list.add(id);
        }
        return list;
    }

    public List<Integer> getWidListByUid(int uid) {
        List<Integer> list = new ArrayList<>();
        String sql = "select * from " + TABLE_COLLECT_INFO + " where _uid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
        while (cursor.moveToNext()) {
            int wid = cursor.getInt(2);
            list.add(wid);
        }
        return list;
    }

    public void deleteCollectByWid(int wid) {
        //可能为空，不返回
        mWDB.delete(TABLE_COLLECT_INFO,"_wid=?",new String[]{Integer.toString(wid)});
    }
}
