package com.interact;

import java.util.ArrayList;

public class GroupBody {
    public String label;
    public String to;
    public boolean unfolded;
    public ArrayList<ChildBody> children;

    public GroupBody(String label) {
        this.label = label;
        this.unfolded = true;
    }

    public void setTo(String user_name, String server_name, String group_name) {
        this.to = "group_info?user_name=" + user_name + "&server_name=" +
                server_name + "&group_name=" + group_name;
    }

    public void addChild(ChildBody child) {
        this.children.add(child);
    }
}
