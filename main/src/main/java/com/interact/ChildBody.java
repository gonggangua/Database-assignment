package com.interact;

public class ChildBody {
    public String label;
    public String to;

    public ChildBody(String label) {
        this.label = label;
    }

    public void setTo(String user_name, String server_name, String other_user_name) {
        this.to = "other_user_info?user_name=" + user_name +
            "&server_name=" + server_name + "&other_user_name=" + other_user_name;
    }
}
