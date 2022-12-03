package com.dao;

import com.pojo.ServerBlacklist;
import com.pojo.User;
import com.pojo.UserBlacklist;

import java.util.List;
import java.util.Map;

public interface UserBlacklistMapper {
    List<ServerBlacklist> select(UserBlacklist userBlacklist);

    void insert(UserBlacklist userBlacklist);

    void delete(UserBlacklist userBlacklist);

    void createBlacklistView(User user);

    void grantBlacklistView(User user);
}
