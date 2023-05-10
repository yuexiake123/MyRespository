package com.example.gastronome.entity;

public class Comment {
    public int id; // 序号
    public int wid; // 作品
    public int from_uid; // 回复用户
    public int to_uid; // 回复用户
    public int parent_id; // 回复作品为-1，回复评论为评论id
    public String content; // 回复内容
    public String date;//回复时间

    public Comment() {
    }

    public Comment(int id, int wid, int from_uid, int to_uid, int parent_id, String content, String date) {
        this.id = id;
        this.wid = wid;
        this.from_uid = from_uid;
        this.to_uid = to_uid;
        this.parent_id = parent_id;
        this.content = content;
        this.date = date;
    }

    @Override
    public String toString() {
        return "comment{" +
                "id=" + id +
                ", wid=" + wid +
                ", from_uid=" + from_uid +
                ", to_uid=" + to_uid +
                ", parent_id=" + parent_id +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
