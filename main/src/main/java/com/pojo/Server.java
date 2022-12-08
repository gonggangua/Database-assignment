package com.pojo;

public class Server {
    private int id;
    private String name;
    private int creator;
    private boolean isPrivate;

    public Server(int id, String name, int creator, boolean isPrivate) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.isPrivate = isPrivate;
    }

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

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creator=" + creator +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
