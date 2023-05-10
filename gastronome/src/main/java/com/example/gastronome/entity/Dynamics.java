package com.example.gastronome.entity;

public class Dynamics {
    public User author;
    public Work work;
    public int shareCount;
    public int commentCount;
    public int likeCount;

    public Dynamics() {
    }

    public Dynamics(User author, Work work, int shareCount, int commentCount, int likeCount) {
        this.author = author;
        this.work = work;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }
}
