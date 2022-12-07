package com.dao;

import com.pojo.Category;
import com.pojo.Channel;

import java.util.List;

public interface ChannelMapper {
    void insert(Channel channel);

    List<Channel> selectByCategory(Category category);
}
