package com.pojo;

import com.dao.GroupMapper;
import com.dao.JoiningServerMapper;
import com.dao.LoginMapper;
import com.dao.ServerBlacklistMapper;
import com.dao.ServerMapper;
import com.dao.UserMapper;
import com.exceptions.BlockedException;
import com.exceptions.CannotBeNullException;
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
    }

    //登出
    public void logout() {
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
}
