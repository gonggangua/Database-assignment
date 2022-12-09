package com.pojo;

import java.sql.Timestamp;
import java.util.Date;

public class JoiningServer {
    private int uid;
    private int sid;
    private String gname;
    private Timestamp time;

    public JoiningServer() {
    }

    public JoiningServer(int uid, int sid, String gname) {
        this.uid = uid;
        this.sid = sid;
        this.gname = gname;
        time = new Timestamp(new Date().getTime());
    }

    public JoiningServer(int uid, int sid, String gname, Timestamp time) {
        this.uid = uid;
        this.sid = sid;
        this.gname = gname;
        this.time = time;
    }

    public int getUid() {
        return uid;
    }

    public int getSid() {
        return sid;
    }

    public String getGname() {
        return gname;
    }
}
