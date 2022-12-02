package com.dao;

import com.pojo.Server;

import java.util.List;

public interface ServerMapper {
    void insert(Server server);

    List<Server> selectAll();

    List<Server> selectById(int id);

    List<Server> selectByName(String name);
}
