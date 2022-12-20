package com.dao;

import com.pojo.Group;
import com.pojo.ServerBlacklist;

import java.util.List;
import java.util.Map;

public interface ServerBlacklistMapper {
    void insert(ServerBlacklist blacklist);

    List<ServerBlacklist> select(ServerBlacklist blacklist);

    void delete(ServerBlacklist blacklist);

    List<ServerBlacklist> selectByIds(Map<String, Object> map);

}
