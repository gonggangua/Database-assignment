package com.pojo;

public class UserBlacklist {
    private int uid;
    private int blockid;

    public UserBlacklist() {
    }

    public UserBlacklist(int uid, int blockid) {
        this.uid = uid;
        this.blockid = blockid;
    }

    @Override
    public String toString() {
        return "UserBlacklist{" +
                "uid=" + uid +
                ", blockid=" + blockid +
                '}';
    }
}
