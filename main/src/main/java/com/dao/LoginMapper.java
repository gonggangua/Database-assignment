package com.dao;

import com.pojo.User;

import java.util.List;

public interface LoginMapper {
    List<User> selectById(int id);
    List<User> selectByName(String name);
}
