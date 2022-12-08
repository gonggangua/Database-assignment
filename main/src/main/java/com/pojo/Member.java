package com.pojo;

import java.security.Timestamp;

public class Member {
    private int id;
    private String name;
    private String status;
    private String gname;
    private Timestamp time;

    public Member(int id, String name, String status, String gname, Timestamp time) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.gname = gname;
        this.time = time;
    }

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

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", gname='" + gname + '\'' +
                ", time=" + time +
                '}';
    }
}
