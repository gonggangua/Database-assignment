package com.pojo;

import com.dao.LoginMapper;
import com.dao.UserMapper;
import com.exceptions.CannotBeNullException;
import com.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Properties;

public class Login {
    private SqlSession sqlSession;

    public Login(String username, String password) {
        Properties login = new Properties();
        login.setProperty("username", username);
        login.setProperty("password", password);
        sqlSession = MybatisUtil.getSqlSession(login);
    }

    public List<User> searchUser(int id) {
        return searchUser(id, null);
    }

    public List<User> searchUser(String name) throws CannotBeNullException{
        if (name == null) {
            throw new CannotBeNullException("name");
        }
        return searchUser(null, name);
    }

    public List<User> searchUser(Integer id, String name) {
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        if (id != null) {
            return mapper.selectById(id);
        } else {
            return mapper.selectByName(name);
        }
    }

    public void logout() {
        sqlSession.close();
    }
}
