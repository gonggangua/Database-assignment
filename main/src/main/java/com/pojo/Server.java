package com.pojo;

public class Server {
    private int id;
    private String name;
    private int creator;
    private boolean isPrivate;

    public Server() {
    }

    public Server(int id, String name, int creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
