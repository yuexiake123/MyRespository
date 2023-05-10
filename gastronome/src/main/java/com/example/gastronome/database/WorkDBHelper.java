package com.example.gastronome.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gastronome.entity.Work;

import java.util.ArrayList;
import java.util.List;

public class WorkDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gastronome.db";
    //作品信息表
    private static final String TABLE_WORK_INFO = "work_info";
    private static final int DB_VERSION = 1;
    private static WorkDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private WorkDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static WorkDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new WorkDBHelper(context);
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_WORK_INFO + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "_uid INTEGER NOT NULL," +
                " name VARCHAR NOT NULL," +
                " description VARCHAR," +
                " time VARCHAR," +
                " burdening VARCHAR NOT NULL," +
                " ingredient VARCHAR," +
                " step VARCHAR NOT NULL," +
                " picPath VARCHAR NOT NULL," +
                "_like INTEGER NOT NULL," +
                " date VARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public long save(Work work) {
        ContentValues values = new ContentValues();
        values.put("_uid",work.uid);
        values.put("name", work.name);
        values.put("description",work.description);
        values.put("time", work.time);
        values.put("burdening",work.burdening);
        values.put("ingredient", work.ingredient);
        values.put("step", work.step);
        values.put("picPath", work.picPath);
        values.put("_like", work.like);
        values.put("date", work.date);
        Log.d("Jun",values.toString());
        return mWDB.insert(TABLE_WORK_INFO, null, values);
    }

    public int getLastWorkId() {
        String sql = "select * from " + TABLE_WORK_INFO + " order by _id desc limit 1 ";
        Cursor cursor = mRDB.rawQuery(sql, null);
        int wid = 0;
        if(cursor.moveToNext()){
            wid = cursor.getInt(0);
        }
        return wid;
    }

    @SuppressLint("Range")
    public List<Work> getWorkByUid(int uid) {
        List<Work> list = new ArrayList<>();
        String sql = "select * from " + TABLE_WORK_INFO + " where _uid = ? order by _id desc";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
        while (cursor.moveToNext()) {
            Work work = new Work();
            work.id = cursor.getInt(cursor.getColumnIndex("_id"));
            work.uid = cursor.getInt(cursor.getColumnIndex("_uid"));
            work.name = cursor.getString(cursor.getColumnIndex("name"));
            work.description = cursor.getString(cursor.getColumnIndex("description"));
            work.time = cursor.getString(cursor.getColumnIndex("time"));
            work.burdening = cursor.getString(cursor.getColumnIndex("burdening"));
            work.ingredient = cursor.getString(cursor.getColumnIndex("ingredient"));
            work.step = cursor.getString(cursor.getColumnIndex("step"));
            work.picPath = cursor.getString(cursor.getColumnIndex("picPath"));
            work.like = cursor.getInt(cursor.getColumnIndex("_like"));
            work.date = cursor.getString(cursor.getColumnIndex("date"));
            list.add(work);
        }
        return list;
    }

    @SuppressLint("Range")
    public Work getWorkById(int work_id) {
        Work work =new Work();
        String sql = "select * from " + TABLE_WORK_INFO + " where _id = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(work_id)});
        if (cursor.moveToNext()) {
            work.id = cursor.getInt(cursor.getColumnIndex("_id"));
            work.uid = cursor.getInt(cursor.getColumnIndex("_uid"));
            work.name = cursor.getString(cursor.getColumnIndex("name"));
            work.description = cursor.getString(cursor.getColumnIndex("description"));
            work.time = cursor.getString(cursor.getColumnIndex("time"));
            work.burdening = cursor.getString(cursor.getColumnIndex("burdening"));
            work.ingredient = cursor.getString(cursor.getColumnIndex("ingredient"));
            work.step = cursor.getString(cursor.getColumnIndex("step"));
            work.picPath = cursor.getString(cursor.getColumnIndex("picPath"));
            work.like = cursor.getInt(cursor.getColumnIndex("_like"));
            work.date = cursor.getString(cursor.getColumnIndex("date"));
        }
        return work;
    }

    public long updateWork(Work tmpWork) {
        ContentValues values = new ContentValues();
        values.put("name", tmpWork.name);
        values.put("description", tmpWork.description);
        values.put("time", tmpWork.time);
        values.put("burdening", tmpWork.burdening);
        values.put("ingredient", tmpWork.ingredient);
        values.put("step", tmpWork.step);
        values.put("picPath", tmpWork.picPath);
        //Log.d("Jun",tmpWork.toString());
        return mWDB.update(TABLE_WORK_INFO,values,"_id=?",new String[]{Integer.toString(tmpWork.id)});
    }

    public long deleteWorkById(int id) {
        return mWDB.delete(TABLE_WORK_INFO,"_id=?",new String[]{Integer.toString(id)});
    }

    public int getWorkCountByUid(int uid) {
        int count = 0;
        String sql = "select * from " + TABLE_WORK_INFO + " where _uid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }

    @SuppressLint("Range")
    public List<Work> getWorkListById(List<Integer> widList) {
        List<Work> list = new ArrayList<>();
        for(int id : widList){
            Work work =new Work();
            String sql = "select * from " + TABLE_WORK_INFO + " where _id = ?";
            Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(id)});
            if (cursor.moveToNext()) {
                work.id = cursor.getInt(cursor.getColumnIndex("_id"));
                work.uid = cursor.getInt(cursor.getColumnIndex("_uid"));
                work.name = cursor.getString(cursor.getColumnIndex("name"));
                work.description = cursor.getString(cursor.getColumnIndex("description"));
                work.time = cursor.getString(cursor.getColumnIndex("time"));
                work.burdening = cursor.getString(cursor.getColumnIndex("burdening"));
                work.ingredient = cursor.getString(cursor.getColumnIndex("ingredient"));
                work.step = cursor.getString(cursor.getColumnIndex("step"));
                work.picPath = cursor.getString(cursor.getColumnIndex("picPath"));
                work.like = cursor.getInt(cursor.getColumnIndex("_like"));
                work.date = cursor.getString(cursor.getColumnIndex("date"));
            }
            list.add(work);
        }
        return list;
    }

    @SuppressLint("Range")
    public List<Work> getWorkListByUId(List<Integer> subscribedIdList) {
        List<Work> list = new ArrayList<>();
        for(int uid : subscribedIdList){
            String sql = "select * from " + TABLE_WORK_INFO + " where _uid = ? order by _id desc";
            Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(uid)});
            while (cursor.moveToNext()) {
                Work work =new Work();
                work.id = cursor.getInt(cursor.getColumnIndex("_id"));
                work.uid = cursor.getInt(cursor.getColumnIndex("_uid"));
                work.name = cursor.getString(cursor.getColumnIndex("name"));
                work.description = cursor.getString(cursor.getColumnIndex("description"));
                work.time = cursor.getString(cursor.getColumnIndex("time"));
                work.burdening = cursor.getString(cursor.getColumnIndex("burdening"));
                work.ingredient = cursor.getString(cursor.getColumnIndex("ingredient"));
                work.step = cursor.getString(cursor.getColumnIndex("step"));
                work.picPath = cursor.getString(cursor.getColumnIndex("picPath"));
                work.like = cursor.getInt(cursor.getColumnIndex("_like"));
                work.date = cursor.getString(cursor.getColumnIndex("date"));
                list.add(work);
            }
        }
        return list;
    }

    public long changeLike(int work_id, int like_count) {
        ContentValues values = new ContentValues();
        values.put("_like", Integer.toString(like_count));
        //Log.d("Jun",tmpWork.toString());
        return mWDB.update(TABLE_WORK_INFO,values,"_id=?",new String[]{Integer.toString(work_id)});
    }
}
