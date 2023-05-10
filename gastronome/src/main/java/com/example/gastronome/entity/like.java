package com.example.gastronome.entity;

public class like {
    public int id; // 序号
    public int uid; // 用户
    public int wid; // 作品

    public like() {
    }

    public like(int id, int uid, int wid) {
        this.id = id;
        this.uid = uid;
        this.wid = wid;
    }

    @Override
    public String toString() {
        return "like{" +
                "id=" + id +
                ", uid=" + uid +
                ", wid=" + wid +
                '}';
    }
}
