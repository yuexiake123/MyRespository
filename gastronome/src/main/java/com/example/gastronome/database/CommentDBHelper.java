package com.example.gastronome.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gastronome.entity.Comment;
import com.example.gastronome.entity.Work;

import java.util.ArrayList;
import java.util.List;


public class CommentDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gastronome.db";
    //关注信息表
    private static final String TABLE_COMMENT_INFO = "comment_info";
    private static final int DB_VERSION = 1;
    private static CommentDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private CommentDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static CommentDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new CommentDBHelper(context);
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
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENT_INFO + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "_wid INTEGER NOT NULL," +
                "_from_uid INTEGER NOT NULL," +
                "_to_uid INTEGER NOT NULL," +
                "_parent_id INTEGER NOT NULL," +
                " content VARCHAR NOT NULL," +
                " date VARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }


    public long deletelike(int uid, int wid) {
        return mWDB.delete(TABLE_COMMENT_INFO,"_uid=? and _wid=?",new String[]{Integer.toString(uid),Integer.toString(wid)});
    }

    public int getCommentCountByWid(int wid) {
        int count = 0;
        String sql = "select * from " + TABLE_COMMENT_INFO + " where _wid = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(wid)});
        while (cursor.moveToNext()) {
            count++;
        }
        return count;
    }

    @SuppressLint("Range")
    public List<Comment> getCommentListByWid(int wid) {
        List<Comment> list = new ArrayList<>();
        String sql = "select * from " + TABLE_COMMENT_INFO + " where _wid = ? order by _id desc";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{Integer.toString(wid)});
        while (cursor.moveToNext()) {
            Comment comment = new Comment();
            comment.id = cursor.getInt(cursor.getColumnIndex("_id"));
            comment.wid = cursor.getInt(cursor.getColumnIndex("_wid"));
            comment.from_uid = cursor.getInt(cursor.getColumnIndex("_from_uid"));
            comment.to_uid = cursor.getInt(cursor.getColumnIndex("_to_uid"));
            comment.parent_id = cursor.getInt(cursor.getColumnIndex("_parent_id"));
            comment.content = cursor.getString(cursor.getColumnIndex("content"));
            comment.date = cursor.getString(cursor.getColumnIndex("date"));
            list.add(comment);
        }
        return list;
    }

    public long addComment(Comment comment) {
        ContentValues values = new ContentValues();
        values.put("_wid",comment.wid);
        values.put("_from_uid",comment.from_uid);
        values.put("_to_uid",comment.to_uid);
        values.put("_parent_id",comment.parent_id);
        values.put("content",comment.content);
        values.put("date",comment.date);
        return mWDB.insert(TABLE_COMMENT_INFO, null, values);
    }

    public void deleteLikeByWid(int wid) {
        mWDB.delete(TABLE_COMMENT_INFO,"_wid=?",new String[]{Integer.toString(wid)});
    }


}
