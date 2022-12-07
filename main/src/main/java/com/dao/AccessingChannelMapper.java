package com.dao;

import com.pojo.AccessingChannel;
import com.pojo.Category;
import com.pojo.Channel;

import java.util.List;

public interface AccessingChannelMapper {
    void access(AccessingChannel accessingChannel);

    void leave(AccessingChannel accessingChannel);
}
