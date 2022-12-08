package com.pojo;

import java.sql.Time;
import java.sql.Timestamp;

public class Message implements Comparable<Message> {
    private int id;
    private int sid;
    private String chname;
    private String content;
    private int sender;
    private Timestamp sendTime;

    public Message(int sid, String chname, String content, int sender) {
        id = 0;
        this.sid = sid;
        this.chname = chname;
        this.content = content;
        this.sender = sender;
    }

    public Message(int id, int sid, String chname, String content, int sender, Timestamp sendTime) {
        this.id = id;
        this.sid = sid;
        this.chname = chname;
        this.content = content;
        this.sender = sender;
        this.sendTime = sendTime;
    }

    public int getId() {
        return id;
    }

    public int getSid() {
        return sid;
    }

    public String getChname() {
        return chname;
    }

    public String getContent() {
        return content;
    }

    public int getSender() {
        return sender;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sid=" + sid +
                ", chname='" + chname + '\'' +
                ", content='" + content + '\'' +
                ", sender=" + sender +
                ", sendTime=" + sendTime +
                '}';
    }

    @Override
    public int compareTo(Message o) {
        return Integer.compare(id, o.id);
    }
}
