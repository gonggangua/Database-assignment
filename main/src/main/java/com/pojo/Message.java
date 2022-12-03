package com.pojo;

import java.sql.Timestamp;

public class Message {
    private int id;
    private int sid;
    private String chname;
    private String content;
    private int sender;
    private Timestamp sendTime;

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
}
