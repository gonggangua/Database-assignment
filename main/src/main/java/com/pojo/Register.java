package com.pojo;

import com.dao.UserMapper;
import com.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;

public class Register {

    public static void register(String name, String password, String mail) {
        SqlSession sqlSession = MybatisUtil.getRootSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        HashMap<String,Object> map = new HashMap<>();
        map.put("name", name);
        map.put("password", password);
        map.put("mail", mail);

        mapper.createUser(map);
        mapper.grantRegularUser(map);
        mapper.insertUser(map);

        sqlSession.commit();
        sqlSession.close();
    }
}
