package com.example.gastronome.entity;

public class Reply {
    public Comment comment;
    public User from_user;
    public User to_user;

    public Reply() {
    }

    public Reply(Comment comment, User from_user, User to_user) {
        this.comment = comment;
        this.from_user = from_user;
        this.to_user = to_user;
    }
}
