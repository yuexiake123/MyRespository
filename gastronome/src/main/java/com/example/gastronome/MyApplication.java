package com.example.gastronome;

import android.app.Application;
import android.util.Log;

import com.example.gastronome.entity.User;

import java.util.HashMap;

public class MyApplication extends Application {

    private static MyApplication mApp;
    // 声明一个公共的信息映射对象，可当作全局变量使用
    public HashMap<String, String> infoMap = new HashMap<>();

    public static MyApplication getInstance(){
        return mApp;
    }

    //注册页面是否有向登录页面传递登录信息的标志量
    public boolean registerToLogin = false;
    public String loginAccount = null;
    public String loginPassword = null;
    //当前用户
    public User user;

    //在App启动时调用
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
}
