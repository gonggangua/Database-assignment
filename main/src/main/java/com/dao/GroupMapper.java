package com.dao;

import com.pojo.Group;

import java.util.List;
import java.util.Map;

public interface GroupMapper {
    void insert(Group group);

    List<Group> getUserGroup(Map<String, Object> map);

    boolean getCanCreate(Map<String, Object> map);

    boolean getCanBan(Map<String, Object> map);

    boolean getCanStat(Map<String, Object> map);

    boolean getCanManage(Map<String, Object> map);
}
