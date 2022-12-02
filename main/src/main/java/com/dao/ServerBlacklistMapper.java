package com.dao;

import com.pojo.Group;
import com.pojo.ServerBlacklist;

import java.util.List;
import java.util.Map;

public interface ServerBlacklistMapper {
    List<ServerBlacklist> selectByIds(Map<String, Object> map);
}
