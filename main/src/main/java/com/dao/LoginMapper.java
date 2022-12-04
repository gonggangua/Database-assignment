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

    List<Server> getJoinedServers(int id);

    void login(int id);

    void logout(int id);

    void setStatus(User user);

    List<User> getFriends(User user);

    List<User> getRequests(User user);

    List<User> getBlockedUsers(User user);
}
