package com.interact;

import java.sql.Timestamp;

public class MessageBody {
    public Timestamp time;
    public String title;

    public MessageBody(Timestamp time, String sender_name, String content) {
        this.time = time;
        this.title = sender_name + " : " + content;
    }
}
