package com.interact;

import java.sql.Timestamp;
import java.util.Date;

public class MessageBody {
    public String time;
    public String title;

    public MessageBody(Timestamp timestamp, String sender_name, String content) {
        this.time = (new Date(timestamp.getTime())).toString();
        this.title = sender_name + " : " + content;
    }

    public MessageBody(Timestamp timestamp, String title) {
        this.time = (timestamp == null) ? "" :
                (new Date(timestamp.getTime())).toString();
        this.title = title;
    }
}
