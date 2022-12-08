package com.pojo;

public class CommentStat {
    int likes;
    int dislikes;

    public CommentStat(int likes, int dislikes) {
        this.likes = likes;
        this.dislikes = dislikes;
    }

    @Override
    public String toString() {
        return "CommentStat{" +
                "likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
