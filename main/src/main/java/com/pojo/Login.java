package com.pojo;

import com.dao.FriendsMapper;
import com.dao.GroupMapper;
import com.dao.JoiningServerMapper;
import com.dao.LoginMapper;
import com.dao.PrivateMessageMapper;
import com.dao.ServerBlacklistMapper;
import com.dao.ServerMapper;
import com.dao.UserBlacklistMapper;
import com.dao.UserMapper;
import com.exceptions.AlreadyExistException;
import com.exceptions.BlockedException;
import com.exceptions.CannotBeNullException;
import com.exceptions.DoNotExistException;
import com.exceptions.LevelLimitException;
import com.exceptions.LoginFailException;
import com.exceptions.NoEnoughMoneyException;
import com.pojo.types.UserStatus;
import com.utils.MybatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Login {
    Properties login;
    private User cur;

    private SqlSession getSqlSession() throws LoginFailException {
        SqlSession ret = null;
        try {
            ret = MybatisUtil.getSqlSession(login);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                e.printStackTrace();
                throw new LoginFailException();
            }
        }
        assert ret != null;
        LoginMapper mapper = ret.getMapper(LoginMapper.class);
        mapper.setRole();
        return ret;
    }

    //构造登录, 连接到数据库
    public Login(String username, String password)
            throws LoginFailException {
        login = new Properties();
        login.setProperty("username", username);
        login.setProperty("password", password);

        SqlSession sqlSession = getSqlSession();
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        SqlSession root = MybatisUtil.getRootSqlSession();
        UserMapper userMapper = root.getMapper(UserMapper.class);
        cur = userMapper.selectByAccurateName(username).get(0);
        root.close();
        mapper.login(cur.getId());
        sqlSession.commit();
        sqlSession.close();
    }

    //登出
    public void logout() {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        mapper.logout(cur.getId());
        sqlSession.commit();
        sqlSession.close();
    }

    //设置个人状态
    public void setStatus(UserStatus status) {
        String statusString;
        switch (status) {
            case BUSY:
                statusString = "busy";
                break;
            case OFFLINE:
                statusString = "offline";
                break;
            default:
                statusString = "online";
                break;
        }
        cur.setStatus(statusString);
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        mapper.setStatus(cur);
        sqlSession.commit();
        sqlSession.close();
    }

    public User self() {
        return cur;
    }

    //id搜索用户
    public List<User> searchUser(int id) {
        return searchUser(id, null);
    }

    //用户名搜索用户
    public List<User> searchUser(String name)
            throws CannotBeNullException {
        if (name == null) {
            throw new CannotBeNullException("name");
        }
        return searchUser(null, name);
    }

    //合并上两者
    public List<User> searchUser(Integer id, String name) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        List<User> users = null;
        if (id != null) {
            users = mapper.selectUserById(id);
        } else {
            users = mapper.selectUserByName(name);
        }
        sqlSession.close();
        return users;
    }

    //充值
    public void charge(int money) {
        SqlSession root = MybatisUtil.getRootSqlSession();
        UserMapper mapper = root.getMapper(UserMapper.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", cur.getId());
        map.put("money", money);
        mapper.charge(map);
        cur.setMoney(cur.getMoney() + money);

        root.commit();
        root.close();
    }

    public void upgrade()
            throws NoEnoughMoneyException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        UserMapper mapper = root.getMapper(UserMapper.class);
        try {
            mapper.upgrade(cur);
        } catch (PersistenceException e) {
            root.commit();
            root.close();
            throw new NoEnoughMoneyException();
        }
        cur = mapper.selectById(cur.getId()).get(0);

        root.commit();
        root.close();
    }

    //创建服务器
    public void createServer(String name, boolean isPrivate)
            throws LevelLimitException {
        SqlSession root = MybatisUtil.getRootSqlSession();

        Server server = new Server(name, cur.getId(), isPrivate);

        ServerMapper serverMapper = root.getMapper(ServerMapper.class);

        try {
            serverMapper.insert(server);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new LevelLimitException();
            }
        }
        int sid = server.getId();

        serverMapper.createServerMembers(server);

        HashMap<String, Object> map = new HashMap<>();
        map.put("uname", cur.getName());
        map.put("sid", server.getId());
        serverMapper.grantServerMembers(map);

        Group creatorGroup = new Group(sid, "creator",
                true, true, true, true);
        Group adminGroup = new Group(sid, "admin",
                false, false, true, true);
        Group regularGroup = new Group(sid, "regular",
                false, false, false, false);

        GroupMapper groupMapper = root.getMapper(GroupMapper.class);

        groupMapper.insert(creatorGroup);
        groupMapper.insert(adminGroup);
        groupMapper.insert(regularGroup);


        int uid = cur.getId();
        JoiningServer join = new JoiningServer(uid, sid, "creator");
        JoiningServerMapper joiningServerMapper =
                root.getMapper(JoiningServerMapper.class);
        joiningServerMapper.insert(join);

        root.commit();
        root.close();
    }

    //id搜索服务器
    public List<Server> searchServer(int id) {
        return searchServer(id, null);
    }

    //服务器名搜索服务器
    public List<Server> searchServer(String name)
            throws CannotBeNullException {
        if (name == null) {
            throw new CannotBeNullException("name");
        }
        return searchServer(null, name);
    }

    //合并上两者
    public List<Server> searchServer(Integer id, String name) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        List<Server> servers = null;
        if (id != null) {
            servers = mapper.selectServerById(id);
        } else {
            servers = mapper.selectServerByName(name);
        }
        sqlSession.close();
        return servers;
    }

    //获得已加入的服务器
    public List<Server> getJoinedServers() {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        List<Server> joinedServers = mapper.getJoinedServers(cur.getId());
        sqlSession.close();
        return joinedServers;
    }

    //加入服务器
    public void joinServer(Server server)
            throws AlreadyExistException, BlockedException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        int sid = server.getId();
        int uid = cur.getId();

        List<Server> joinedServers = getJoinedServers();
        for (Server joinedServer : joinedServers) {
            if (joinedServer.getId() == sid) {
                root.close();
                throw new AlreadyExistException();
            }
        }

        ServerMapper serverMapper = root.getMapper(ServerMapper.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("sid", sid);
        map.put("uid", uid);
        map.put("uname", cur.getName());
        serverMapper.grantServerMembers(map);

        JoiningServer join = new JoiningServer(uid, sid, "regular");
        JoiningServerMapper joiningServerMapper =
                root.getMapper(JoiningServerMapper.class);
        try {
            joiningServerMapper.insert(join);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new BlockedException();
            }
        }

        root.commit();
        root.close();
    }

    //查看好友
    public List<User> getFriends() {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper loginMapper = sqlSession.getMapper(LoginMapper.class);
        List<User> friends = loginMapper.getFriends(cur);
        sqlSession.close();
        return friends;
    }

    //查看待通过好友请求
    public List<User> getRequests() {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper loginMapper = sqlSession.getMapper(LoginMapper.class);

        List<User> requests = loginMapper.getRequests(cur);
        sqlSession.close();
        return  requests;
    }

    //发送好友请求
    public void requestFriend(User user)
            throws AlreadyExistException, BlockedException {
        if (user.getId() == cur.getId()) {
            return;
        }

        SqlSession root = MybatisUtil.getRootSqlSession();

        FriendsMapper friendsMapper = root.getMapper(FriendsMapper.class);
        Friends friends = new Friends(cur.getId(), user.getId(),false);
        try {
            friendsMapper.insert(friends);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new BlockedException();
            }
        }

        root.commit();
        root.close();
    }

    //拒绝好友请求
    public void rejectFriend(User user)
            throws DoNotExistException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        FriendsMapper mapper = root.getMapper(FriendsMapper.class);

        Friends friends = new Friends(user.getId(), cur.getId(), false);
        try {
            mapper.delete(friends);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new DoNotExistException();
            }
        }

        root.commit();
        root.close();
    }

    //接受好友请求
    public void acceptFriend(User user)
            throws DoNotExistException, BlockedException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        FriendsMapper mapper = root.getMapper(FriendsMapper.class);

        Friends friends = new Friends(user.getId(), cur.getId(), true);
        try {
            mapper.accept(friends);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new DoNotExistException();
            }
        }
        Friends both = new Friends(cur.getId(), user.getId(), true);
        try {
            mapper.insert(both);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new BlockedException();
            }
        }

        root.commit();
        root.close();
    }

    //拉黑用户
    public void blockUser(User user) throws AlreadyExistException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        UserBlacklistMapper mapper = root.getMapper(UserBlacklistMapper.class);

        UserBlacklist blacklist = new UserBlacklist(cur.getId(), user.getId());

        try {
            mapper.insert(blacklist);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new AlreadyExistException();
            }
        }

        root.commit();
        root.close();
    }

    //解除拉黑
    public void removeBlock(User user) throws DoNotExistException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        UserBlacklistMapper mapper = root.getMapper(UserBlacklistMapper.class);

        UserBlacklist blacklist = new UserBlacklist(cur.getId(), user.getId());

        try {
            mapper.delete(blacklist);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new DoNotExistException();
            }
        }

        root.commit();
        root.close();
    }

    //获取黑名单用户
    public List<User> getBlockedUsers() {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        List<User> blockedUsers = mapper.getBlockedUsers(cur);
        sqlSession.close();
        return blockedUsers;
    }

    //发送私信
    public void sendPrivateMessage(User user, String content)
            throws BlockedException {
        SqlSession root = MybatisUtil.getRootSqlSession();

        PrivateMessageMapper messageMapper =
                root.getMapper(PrivateMessageMapper.class);
        PrivateMessage message = new PrivateMessage(cur.getId(), user.getId(), content);
        try {
            messageMapper.insert(message);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                root.commit();
                root.close();
                throw new BlockedException();
            }
        }
        root.commit();
        root.close();
    }

    //查看私信
    public List<PrivateMessage> checkPrivateMessage(User user) {
        SqlSession root = MybatisUtil.getRootSqlSession();
        PrivateMessageMapper mapper =
                root.getMapper(PrivateMessageMapper.class);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id1", cur.getId());
        map.put("id2", user.getId());
        List<PrivateMessage> ret = mapper.selectByUsers(map);
        Collections.sort(ret);
        root.close();
        return ret;
    }

    //进入服务器
    public ServerInteract enterServer(Server server) {
        return new ServerInteract(cur, login, server);
    }
}
