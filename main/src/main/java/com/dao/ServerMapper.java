package com.dao;

import com.pojo.Server;

import java.util.List;
import java.util.Map;

public interface ServerMapper {
    void insert(Server server);

    List<Server> selectAll();

    List<Server> selectById(int id);

    List<Server> selectByName(String name);

    void createServerMembers(Server server);

    void grantServerMembers(Map<String, Object> map);
}
