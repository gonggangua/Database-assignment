package com.pojo;

import java.sql.Time;
import java.sql.Timestamp;

public class Call {
    private int id;
    private int sid;
    private String chname;
    private Timestamp startTime;
    private Timestamp endTime;

    public Call(int id, int sid, String chname, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.sid = sid;
        this.chname = chname;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
