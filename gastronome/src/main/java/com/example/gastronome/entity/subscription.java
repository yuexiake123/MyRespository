package com.example.gastronome.entity;

public class subscription {
    public int id; // 序号
    public int uid; // 用户
    public int suid; // 用户

    public subscription() {
    }

    public subscription(int id, int uid, int suid) {
        this.id = id;
        this.uid = uid;
        this.suid = suid;
    }

    @Override
    public String toString() {
        return "subscription{" +
                "id=" + id +
                ", uid=" + uid +
                ", suid=" + suid +
                '}';
    }
}
