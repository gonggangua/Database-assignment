package com.pojo;

public class Group {
    private int sid;
    private String name;
    private boolean canCreate;
    private boolean canManage;
    private boolean canBan;
    private boolean canStats;

    public Group(int sid, String name, boolean canCreate, boolean canManage, boolean canBan, boolean canStats) {
        this.sid = sid;
        this.name = name;
        this.canCreate = canCreate;
        this.canManage = canManage;
        this.canBan = canBan;
        this.canStats = canStats;
    }

    public int getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public boolean isCanManage() {
        return canManage;
    }

    public boolean isCanBan() {
        return canBan;
    }

    public boolean isCanStats() {
        return canStats;
    }

    @Override
    public String toString() {
        return "Group{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                ", canCreate=" + canCreate +
                ", canManage=" + canManage +
                ", canBan=" + canBan +
                ", canStats=" + canStats +
                '}';
    }
}
