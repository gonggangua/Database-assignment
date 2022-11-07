package com.dao;

import com.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    void insertUser(Map<String, Object> map);
    void createUser(Map<String, Object> map);
    void createSelfView(Map<String, Object> map);
    void grantSelView(Map<String, Object> map);
    void grantRegularUser(Map<String, Object> map);
    List<User> selectAll();
    List<User> selectById(int id);
    List<User> selectByName(String name);
}
