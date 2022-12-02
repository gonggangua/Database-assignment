package com.dao;

import com.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    void insert(User user);

    void createUser(Map<String, Object> map);

    void createSelfView(Map<String, Object> map);

    void grantSelfView(Map<String, Object> map);

    void grantRegularUser(Map<String, Object> map);

    void charge(Map<String, Object> map);

    List<User> selectAll();

    List<User> selectById(int id);

    List<User> selectByName(String name);
}
