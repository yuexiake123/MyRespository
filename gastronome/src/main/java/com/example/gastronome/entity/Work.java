package com.example.gastronome.entity;

public class Work {

    public int id; // 序号
    public int uid; // 作者序号
    public String name; // 名称
    public String description; // 简介
    public String time; // 用时
    public String burdening; // 食材
    public String ingredient; // 配料
    public String step; // 制作步骤
    public String picPath;//图片
    public int like; // 点赞
    public String date;//发布时间

    public Work() {
    }

    public Work(int id, int uid, String name, String description, String time, String burdening, String ingredient, String step, String picPath, int like, String date) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.time = time;
        this.burdening = burdening;
        this.ingredient = ingredient;
        this.step = step;
        this.picPath = picPath;
        this.like = like;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Work{" +
                "id=" + id +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", burdening='" + burdening + '\'' +
                ", ingredient='" + ingredient + '\'' +
                ", step='" + step + '\'' +
                ", picPath='" + picPath + '\'' +
                ", like=" + like +
                ", date='" + date + '\'' +
                '}';
    }
}
