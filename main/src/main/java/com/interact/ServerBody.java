package com.interact;

import java.util.ArrayList;

public class ServerBody {
    public String server_name;
    public ArrayList<GroupBody> groups;
    public ArrayList<CategoryBody> categories;

    public ServerBody(String server_name) {
        this.server_name = server_name;
        this.groups = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    public void addGroup(GroupBody group) {
        this.groups.add(group);
    }

    public void addCategory(CategoryBody category) {
        this.categories.add(category);
    }
}
