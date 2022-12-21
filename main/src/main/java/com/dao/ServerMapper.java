package com.dao;

import com.pojo.Server;
import com.pojo.ServerStat;
import com.pojo.User;

import java.util.List;
import java.util.Map;

public interface ServerMapper {
    void insert(Server server);

    void dismiss(Server server);

    List<Server> selectAll();

    List<Server> selectById(int id);

    List<Server> selectByName(String name);

    List<Server> selectByAccurateName(Server server);

    void update(Server server);

    List<Server> getPrivateServer(User user);

    void createServerMembers(Server server);

    void grantServerMembers(Map<String, Object> map);

    ServerStat getServerStats(Server server);
}
