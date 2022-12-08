package com.pojo;

public class Channel {
    private int sid;
    private String name;
    private String cname;
    private boolean type;

    public Channel(int sid, String name, String cname, boolean type) {
        this.sid = sid;
        this.name = name;
        this.cname = cname;
        this.type = type;
    }

    public int getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public String getCname() {
        return cname;
    }

    public boolean isType() {
        return type;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                ", cname='" + cname + '\'' +
                ", type=" + type +
                '}';
    }
}
