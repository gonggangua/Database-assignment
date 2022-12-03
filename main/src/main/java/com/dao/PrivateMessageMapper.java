package com.dao;

import com.pojo.PrivateMessage;

import java.util.List;
import java.util.Map;

public interface PrivateMessageMapper {
    public List<PrivateMessage> selectByUsers(Map<String, Object> map);

    public void insert(PrivateMessage privateMessage);
}
