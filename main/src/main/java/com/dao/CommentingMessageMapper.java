package com.dao;

import com.pojo.Channel;
import com.pojo.CommentStat;
import com.pojo.CommentingMessage;
import com.pojo.Message;

import java.util.List;
import java.util.Map;

public interface CommentingMessageMapper {
    void insert(CommentingMessage comment);

    CommentStat getComments(Message message);
}
