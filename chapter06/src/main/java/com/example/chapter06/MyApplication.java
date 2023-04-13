package com.example.chapter06;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.chapter06.database.BookDatabase;
import com.example.chapter06.database.ShoppingDBHelper;
import com.example.chapter06.enity.GoodsInfo;
import com.example.chapter06.enity.SharedUtil;
import com.example.chapter06.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MyApplication extends Application {

    private static MyApplication mAPp;
    //声明一个公共的信息映射对象，可当作全局变量使用
    public HashMap<String,String> infoMap=new HashMap<>();

    //声明一个书籍数据库对象
    private BookDatabase bookDatabase;

    //购物车中的商品总数量
    public int goodsCount;

    public static MyApplication getInstance(){
        return mAPp;
    }

    //在APP启动时调用
    @Override
    public void onCreate() {
        super.onCreate();
        mAPp=this;
        Log.d("yuexiake","MyApplication onCreate");

        //构建书籍数据库的实例
        bookDatabase= Room.databaseBuilder(this,BookDatabase.class,"book")
                // 允许迁移数据库（发生数据库变更时，Room默认删除原数据库再创建新数据库。如此一来原来的记录会丢失，故而要改为迁移方式以便保存原有记录）
                .addMigrations()
                // 允许在主线程中操作数据库（Room默认不能在主线程中操作数据库）
                .allowMainThreadQueries()
                .build();

        //初始化商品信息
        initGoodsInfo();
    }

    private void initGoodsInfo() {
        // 获取共享参数保存的是否首次打开参数
        boolean isFirst = SharedUtil.getInstance(this).readBoolean("first", true);
        // 获取当前App的私有下载路径
        String directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + File.separatorChar;
        if (isFirst) {
            // 模拟网络图片下载
            List<GoodsInfo> list = GoodsInfo.getDefaultList();
            for (GoodsInfo info : list) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), info.pic);
                String path = directory + info.id + ".jpg";
                // 往存储卡保存商品图片
                FileUtil.saveImage(path, bitmap);
                // 回收位图对象
                bitmap.recycle();
                info.picPath = path;
            }
            // 打开数据库，把商品信息插入到表中
            ShoppingDBHelper dbHelper = ShoppingDBHelper.getInstance(this);
            dbHelper.openWriteLink();
            dbHelper.insertGoodsInfos(list);
            dbHelper.closeLink();
            // 把是否首次打开写入共享参数
            SharedUtil.getInstance(this).writeBoolean("first", false);
        }
    }

    //在APP终止时调用
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("yuexiake","onTerminate");
    }

    //在配置改变时调用，列入从竖屏变横屏
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("yuexiake","onConfigurationChanged");
    }

    // 获取书籍数据库的实例
    public BookDatabase getBookDB() {
        return bookDatabase;
    }
}
