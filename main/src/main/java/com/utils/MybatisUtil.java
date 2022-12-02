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
    private static InputStream resource;
    private static Properties root;

    static {
        String config = "mybatis-config.xml";
        String rootProperties = "root.properties";
        try {
            resource = Resources.getResourceAsStream(config);
            InputStream rootInput = Resources.getResourceAsStream(rootProperties);
            root = new Properties();
            root.load(rootInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(Properties prop) {
        SqlSessionFactory factory =
                new SqlSessionFactoryBuilder().build(resource, prop);
        return factory.openSession();
    }

    public static SqlSession getRootSqlSession() {
        SqlSessionFactory factory =
                new SqlSessionFactoryBuilder().build(resource, root);
        return factory.openSession();
    }
}
