package com.dao;

import com.pojo.Server;
import com.pojo.User;

import java.util.List;

public interface LoginMapper {
    List<User> selectUserById(int id);

    List<User> selectUserByName(String name);

    List<User> selectSelf(String name);

    List<Server> selectServerById(int id);

    List<Server> selectServerByName(String name);

    void login(int id);

    void logout(int id);

    List<User> getFriends(User user);

    List<User> getRequests(User user);

    List<User> getBlockedUsers(User user);
}
