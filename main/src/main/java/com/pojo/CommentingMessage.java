package com.pojo;

public class CommentingMessage {
    private int uid;
    private int mid;
    private String type;

    public CommentingMessage() {
    }

    public CommentingMessage(int uid, int mid, String type) {
        this.uid = uid;
        this.mid = mid;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CommentingMessage{" +
                "uid=" + uid +
                ", mid=" + mid +
                ", type='" + type + '\'' +
                '}';
    }
}
