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

public class CategoryDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gastronome.db";
    //分类信息表
    private static final String TABLE_CATE_INFO = "category";
    private static final int DB_VERSION = 1;
    private static CategoryDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private CategoryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static CategoryDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new CategoryDBHelper(context);
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CATE_INFO + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "_wid INTEGER NOT NULL," +
                " name VARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void save(int wid, List<String> tagList) {
        ContentValues values = new ContentValues();
        for(String tag:tagList){
            values.put("_wid", wid);
            values.put("name", tag);
            Log.d("Jun",values.toString());
            mWDB.insert(TABLE_CATE_INFO, null, values);
        }
    }

    public List<String> getCategoryByWid(int wid) {
        List<String> list = new ArrayList<>();
        String sql = "select * from " + TABLE_CATE_INFO + " where _wid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(wid)});
        while (cursor.moveToNext()) {
            String tag = new String();
            tag = cursor.getString(2);
            list.add(tag);
        }
        return list;
    }

    public void updateCategoryByWid(int wid, List<String> selectedTagList) {
        //先删除，再写
        if(deleteCategoryByWid(wid) >0){
            save(wid,selectedTagList);
        }
    }

    public long deleteCategoryByWid(int wid) {
        return mWDB.delete(TABLE_CATE_INFO,"_wid=?",new String[]{Integer.toString(wid)});
    }

    public List<Integer> getWidByTag(String tag) {
        List<Integer> list = new ArrayList<>();
        String sql = "select * from " + TABLE_CATE_INFO + " where name = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{tag});
        while (cursor.moveToNext()) {
            int wid = cursor.getInt(1);
            list.add(wid);
        }
        return list;
    }
}
