package com.interact;

import java.util.HashMap;
import java.util.Map;

public class RetBody {
    public int status;
    public String msg;
    public Map<String, Object> data;

    public RetBody(String message) {
        status = 0;
        this.msg = message;
        if (message.equals("User is not logged in!")) {
            status = 1;
        }
        data = new HashMap<>();
    }

    public RetBody(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }
}
