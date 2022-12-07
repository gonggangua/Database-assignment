package com.pojo;

import java.security.Timestamp;

public class Member {
    private int id;
    private String name;
    private String status;
    private String gname;
    private Timestamp time;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getGname() {
        return gname;
    }

    public Timestamp getTime() {
        return time;
    }
}
