package com.example.gastronome.entity;

import java.util.Date;

public class User {

    public int id; // 序号
    public String name; // 昵称
    public String account; // 账号
    public String password; // 密码
    public String area; // 所在地区
    public String picPath;//头像
    public String date;//创建时间

    public User() {
    }

    public User(String name, String account, String password, String picPath, String date) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.picPath = picPath;
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", area='" + area + '\'' +
                ", picPath='" + picPath + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}