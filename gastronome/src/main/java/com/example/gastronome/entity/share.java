package com.example.gastronome.entity;

public class share {
    public int id; // 序号
    public int uid; // 用户
    public int wid; // 作品
    public String date;//转发时间

    public share() {
    }

    public share(int id, int uid, int wid, String date) {
        this.id = id;
        this.uid = uid;
        this.wid = wid;
        this.date = date;
    }

    @Override
    public String toString() {
        return "share{" +
                "id=" + id +
                ", uid=" + uid +
                ", wid=" + wid +
                ", date='" + date + '\'' +
                '}';
    }
}
