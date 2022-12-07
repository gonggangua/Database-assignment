package com.dao;

import com.pojo.JoiningServer;
import com.pojo.Member;
import com.pojo.Server;
import com.pojo.User;

import java.util.HashMap;
import java.util.List;

public interface JoiningServerMapper {
    void insert(JoiningServer joiningServer);

    List<JoiningServer> selectByIds(HashMap<String, Object> map);

    void createJoinedServers(User user);

    void grantJoinedServers(User user);

    List<Member> getMembers(Server server);

    void setGroup(JoiningServer joiningServer);
}
