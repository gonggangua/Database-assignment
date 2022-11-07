package com.pojo;

import com.dao.UserMapper;
import com.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;

public class User {
    private int id;
    private String name;
    private String mail;
    private int level;
    private String status;
    private Timestamp registry;

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
