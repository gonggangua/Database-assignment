package com.pojo;

import com.dao.UserMapper;
import com.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class User {
    private int id;
    private String name;
    private String mail;
    private int level;
    private String status;
    private Timestamp registry;
    private int money;

    public User() {
    }

    public User(String name, String mail) {
        id = 0;
        this.name = name;
        this.mail = mail;
        level = 1;
        status = "offline";
        registry = new Timestamp(new Date().getTime());
        money = 0;
    }

    public User(int id, String name, String mail, int level, String status, Timestamp registry, int money) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.level = level;
        this.status = status;
        this.registry = registry;
        this.money = money;
    }

    public User(int id, String name, String mail, int level, String status, Timestamp registry) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.level = level;
        this.status = status;
        this.registry = registry;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public int getLevel() {
        return level;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getRegistry() {
        return registry;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", level=" + level +
                ", status='" + status + '\'' +
                ", registry=" + registry +
                '}';
    }
}
