package com.pojo;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class PrivateMessage implements Comparable<PrivateMessage> {
    private int id;
    private int sender;
    private int receiver;
    private Timestamp sendTime;
    private String content;

    public PrivateMessage(int sender, int receiver, String content) {
        id = 0;
        this.sender = sender;
        this.receiver = receiver;
        sendTime = new Timestamp(new Date().getTime());
        this.content = content;
    }

    public PrivateMessage(int id, int sender, int receiver, Timestamp sendTime, String content) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.sendTime = sendTime;
        this.content = content;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", sendTime=" + sendTime +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int compareTo(PrivateMessage o) {
        return Integer.compare(id, o.id);
    }
}
