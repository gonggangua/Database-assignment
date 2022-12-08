package com.pojo;

public class Category {
    private int sid;
    private String name;

    public Category(int sid, String name) {
        this.sid = sid;
        this.name = name;
    }

    public int getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                '}';
    }
}
