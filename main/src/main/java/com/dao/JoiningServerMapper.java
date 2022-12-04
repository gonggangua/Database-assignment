package com.dao;

import com.pojo.JoiningServer;
import com.pojo.User;

import java.util.HashMap;
import java.util.List;

public interface JoiningServerMapper {
    void insert(JoiningServer joiningServer);

    List<JoiningServer> selectByIds(HashMap<String, Object> map);

    void createJoinedServers(User user);

    void grantJoinedServers(User user);
}
