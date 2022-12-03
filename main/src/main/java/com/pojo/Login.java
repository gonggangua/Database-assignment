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
import com.exceptions.LoginFailException;
import com.utils.MybatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Login {
    private SqlSession sqlSession;
    private User cur;

    //构造登录, 连接到数据库
    public Login(String username, String password)
            throws LoginFailException {
        Properties login = new Properties();
        login.setProperty("username", username);
        login.setProperty("password", password);
        try {
            sqlSession = MybatisUtil.getSqlSession(login);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                throw new LoginFailException();
            }
        }
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        cur = mapper.selectSelf(username).get(0);
        mapper.login(username);
        sqlSession.commit();
    }

    //登出
    public void logout() {
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        mapper.logout(cur.getName());
        sqlSession.commit();
        sqlSession.close();
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
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        if (id != null) {
            return mapper.selectUserById(id);
        } else {
            return mapper.selectUserByName(name);
        }
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

    //创建服务器
    public void createServer(String name, boolean isPrivate) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        Server server = new Server(name, cur.getId(), isPrivate);

        ServerMapper serverMapper = root.getMapper(ServerMapper.class);

        serverMapper.insert(server);
        int sid = server.getId();

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
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        if (id != null) {
            return mapper.selectServerById(id);
        } else {
            return mapper.selectServerByName(name);
        }
    }

    //加入服务器
    public void joinServer(Server server)
            throws BlockedException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        int sid = server.getId();
        int uid = cur.getId();

        HashMap<String, Object> map = new HashMap<>();
        map.put("sid", sid);
        map.put("uid", uid);

        ServerBlacklistMapper mapper = root.getMapper(ServerBlacklistMapper.class);
        List<ServerBlacklist> list = mapper.selectByIds(map);
        if (!list.isEmpty()) {
            throw new BlockedException();
        }

        JoiningServer join = new JoiningServer(uid, sid, "regular");
        JoiningServerMapper joiningServerMapper =
                root.getMapper(JoiningServerMapper.class);
        joiningServerMapper.insert(join);

        root.commit();
        root.close();
    }

    //查看好友
    public List<User> getFriends() {
        LoginMapper loginMapper = sqlSession.getMapper(LoginMapper.class);

        return loginMapper.getFriends(cur);
    }

    //查看待通过好友请求
    public List<User> getRequests() {
        LoginMapper loginMapper = sqlSession.getMapper(LoginMapper.class);

        return loginMapper.getRequests(cur);
    }

    //发送好友请求
    public void requestFriend(User user)
            throws AlreadyExistException, BlockedException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        UserBlacklistMapper userBlacklistMapper =
                root.getMapper(UserBlacklistMapper.class);
        UserBlacklist blacklist = new UserBlacklist( user.getId(), cur.getId());
        if (!userBlacklistMapper.select(blacklist).isEmpty()) {
            throw new BlockedException();
        }

        FriendsMapper friendsMapper = root.getMapper(FriendsMapper.class);
        if (user.getId() == cur.getId()) {
            return;
        }

        Friends friends = new Friends(cur.getId(), user.getId(),false);
        try {
            friendsMapper.insert(friends);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                throw new AlreadyExistException();
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
                throw new DoNotExistException();
            }
        }

        root.commit();
        root.close();
    }

    //接受好友请求
    public void acceptFriend(User user)
            throws DoNotExistException, AlreadyExistException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        FriendsMapper mapper = root.getMapper(FriendsMapper.class);

        Friends friends = new Friends(user.getId(), cur.getId(), true);
        try {
            mapper.accept(friends);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                throw new DoNotExistException();
            }
        }
        Friends both = new Friends(cur.getId(), user.getId(), true);
        try {
            mapper.insert(both);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                throw new AlreadyExistException();
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
                throw new DoNotExistException();
            }
        }

        root.commit();
        root.close();
    }

    //获取黑名单用户
    public List<User> getBlockedUsers() {
        LoginMapper mapper = sqlSession.getMapper(LoginMapper.class);
        return mapper.getBlockedUsers(cur);
    }

    //发送私信
    public void sendPrivateMessage(User user, String content)
            throws BlockedException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        UserBlacklistMapper userBlacklistMapper =
                root.getMapper(UserBlacklistMapper.class);
        UserBlacklist blacklist = new UserBlacklist(user.getId(), cur.getId());
        if (!userBlacklistMapper.select(blacklist).isEmpty()) {
            throw new BlockedException();
        }

        PrivateMessageMapper messageMapper =
                root.getMapper(PrivateMessageMapper.class);
        PrivateMessage message = new PrivateMessage(cur.getId(), user.getId(), content);
        messageMapper.insert(message);

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
        return mapper.selectByUsers(map);
    }
}
