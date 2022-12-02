package com.pojo;

public class Server {
    private int id;
    private String name;
    private int creator;
    private boolean isPrivate;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCreator() {
        return creator;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Server(String name, int creator, boolean isPrivate) {
        id = 0;
        this.name = name;
        this.creator = creator;
        this.isPrivate = isPrivate;
    }
}
