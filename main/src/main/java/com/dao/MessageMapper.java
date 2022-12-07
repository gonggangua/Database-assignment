package com.dao;

import com.pojo.Channel;
import com.pojo.Message;
import com.pojo.PrivateMessage;

import java.util.List;
import java.util.Map;

public interface MessageMapper {
    List<Message> selectByChannel(Channel channel);

    List<Message> getNewMessages(Map<String, Object> map);

    void insert(Message message);
}
