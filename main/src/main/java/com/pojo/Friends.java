package com.pojo;

public class Friends {
    private int uid;
    private int target;
    private boolean status;

    public Friends() {
    }

    public Friends(int uid, int target, boolean status) {
        this.uid = uid;
        this.target = target;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "uid=" + uid +
                ", target=" + target +
                ", status=" + status +
                '}';
    }
}
