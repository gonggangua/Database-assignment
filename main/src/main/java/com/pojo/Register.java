package com.pojo;

import com.dao.UserMapper;
import com.exceptions.DuplicateNameException;
import com.utils.MybatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;

public class Register {

    public static void register(String name, String password, String mail)
            throws DuplicateNameException {
        SqlSession sqlSession = MybatisUtil.getRootSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        HashMap<String,Object> map = new HashMap<>();
        map.put("name", name);
        map.put("password", password);

        User user = new User(name, mail);

        try {
            mapper.insert(user);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                throw  new DuplicateNameException(name);
            }
        }
        mapper.createUser(map);

        mapper.grantRegularUser(map);
        mapper.createSelfView(map);
        mapper.grantSelfView(map);

        sqlSession.commit();
        sqlSession.close();
    }
}
