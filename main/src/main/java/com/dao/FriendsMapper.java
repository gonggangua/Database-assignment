package com.dao;

import com.pojo.Friends;
import com.pojo.Group;
import com.pojo.User;

public interface FriendsMapper {
    void insert(Friends friends);

    void createFriendsView(User user);

    void grantFriendsView(User user);

    void createFriendsStatus(User user);

    void grantFriendsStatus(User user);

    void delete(Friends friends);

    void accept(Friends friends);
}
