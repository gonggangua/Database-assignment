package com.pojo;

import com.dao.FriendsMapper;
import com.dao.JoiningServerMapper;
import com.dao.UserBlacklistMapper;
import com.dao.UserMapper;
import com.exceptions.DuplicateNameException;
import com.utils.MybatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;

public class Register {

    public static void register(String name, String password, String mail)
            throws DuplicateNameException {
        SqlSession sqlSession = MybatisUtil.getRootSqlSession();

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        HashMap<String,Object> map = new HashMap<>();
        map.put("name", name);
        map.put("password", password);

        User user = new User(name, mail);

        try {
            userMapper.insert(user);
            user = userMapper.selectByAccurateName(name).get(0);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                throw  new DuplicateNameException(name);
            }
        }
        map.put("id", user.getId());

        userMapper.createUser(map);

        userMapper.grantRegularUser(map);
        userMapper.createSelfView(map);
        userMapper.grantSelfView(map);

        FriendsMapper friendsMapper = sqlSession.getMapper(FriendsMapper.class);
        friendsMapper.createFriendsView(user);
        friendsMapper.createFriendsStatus(user);
        friendsMapper.grantFriendsView(user);
        friendsMapper.grantFriendsStatus(user);

        UserBlacklistMapper blacklistMapper =
                sqlSession.getMapper(UserBlacklistMapper.class);
        blacklistMapper.createBlacklistView(user);
        blacklistMapper.grantBlacklistView(user);

        JoiningServerMapper joiningServerMapper =
                sqlSession.getMapper(JoiningServerMapper.class);
        joiningServerMapper.createJoinedServers(user);
        joiningServerMapper.grantJoinedServers(user);

        sqlSession.commit();
        sqlSession.close();
    }
}
