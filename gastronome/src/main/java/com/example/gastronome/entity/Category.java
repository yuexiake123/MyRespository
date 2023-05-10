package com.example.gastronome.entity;

public class Category {
    public int id; // 序号
    public int wid; // 作品序号
    public String name; // 类别名称

    public Category() {
    }

    public Category(int id, int wid, String name) {
        this.id = id;
        this.wid = wid;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", wid=" + wid +
                ", name='" + name + '\'' +
                '}';
    }
}
