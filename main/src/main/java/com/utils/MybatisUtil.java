package com.utils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MybatisUtil {
    private static String config = "mybatis-config.xml";
    private static String rootProperties = "root.properties";

    public static SqlSession getSqlSession(Properties prop) {
        InputStream resource = null;
        try {
            resource = Resources.getResourceAsStream(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory factory =
                new SqlSessionFactoryBuilder().build(resource, prop);
        return factory.openSession();
    }

    public static SqlSession getRootSqlSession() {
        InputStream resource = null;
        Properties root = null;
        try {
            InputStream rootInput = Resources.getResourceAsStream(rootProperties);
            root = new Properties();
            root.load(rootInput);
            resource = Resources.getResourceAsStream(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory factory =
                new SqlSessionFactoryBuilder().build(resource, root);
        return factory.openSession();
    }
}
