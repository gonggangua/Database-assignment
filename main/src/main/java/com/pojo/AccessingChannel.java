package com.pojo;

import java.sql.Timestamp;

public class AccessingChannel {
    private int id;
    private int uid;
    private int sid;
    private String chname;
    private Timestamp accessTime;
    private Timestamp leaveTime;

    public AccessingChannel(int uid, int sid, String chname) {
        id = 0;
        this.uid = uid;
        this.sid = sid;
        this.chname = chname;
        accessTime = null;
        leaveTime = null;
    }
}
