package com.pojo;

import com.dao.AccessingChannelMapper;
import com.dao.CategoryMapper;
import com.dao.CategoryVisibleMapper;
import com.dao.ChannelMapper;
import com.dao.CommentingMessageMapper;
import com.dao.GroupMapper;
import com.dao.JoiningServerMapper;
import com.dao.MessageMapper;
import com.dao.ServerBlacklistMapper;
import com.dao.ServerMapper;
import com.exceptions.AlreadyExistException;
import com.exceptions.DuplicateNameException;
import com.exceptions.LoginFailException;
import com.exceptions.NoPermissionException;
import com.utils.MybatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ServerInteract {
    private User cur;
    private Properties login;
    private Server server;

    public ServerInteract(User cur, Properties login, Server server) {
        this.cur = cur;
        this.login = login;
        this.server = server;
    }

    private SqlSession getSqlSession() throws LoginFailException {
        Properties login = new Properties();
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
        return ret;
    }


    public List<Member> getMembers() {
        SqlSession root = MybatisUtil.getRootSqlSession();

        JoiningServerMapper mapper =
                root.getMapper(JoiningServerMapper.class);

        List<Member> ret = mapper.getMembers(server);
        root.close();
        return ret;
    }

    private Group getGroup(User user, Server server, SqlSession root) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        return groupMapper.getUserGroup(map).get(0);
    }

    private void checkCreate(User user, Server server, SqlSession root)
            throws NoPermissionException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        if (!groupMapper.getCanCreate(map)) {
            root.close();
            throw new NoPermissionException("create");
        }
    }

    private void checkBan(User user, Server server, SqlSession root)
            throws NoPermissionException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        if (!groupMapper.getCanBan(map)) {
            root.close();
            throw new NoPermissionException("ban or unban user");
        }
    }

    private void checkStat(User user, Server server, SqlSession root)
            throws NoPermissionException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        if (!groupMapper.getCanStats(map)) {
            root.close();
            throw new NoPermissionException("access statistics");
        }
    }

    private void checkManage(User user, Server server, SqlSession root)
            throws NoPermissionException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        if (!groupMapper.getCanManage(map)) {
            root.close();
            throw new NoPermissionException("manage users");
        }
    }

    public List<Group> getGroups() {
        SqlSession root = MybatisUtil.getRootSqlSession();

        GroupMapper mapper = root.getMapper(GroupMapper.class);
        List<Group> groups = mapper.selectByServer(server);

        root.close();
        return groups;
    }

    public void ban(User user)
            throws NoPermissionException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        checkBan(cur, server, root);

        ServerBlacklistMapper mapper =
                root.getMapper(ServerBlacklistMapper.class);
        ServerBlacklist blacklist = new ServerBlacklist(server.getId(), user.getId());
        mapper.insert(blacklist);

        root.commit();
        root.close();
    }

    public void unban(User user)
            throws NoPermissionException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        checkBan(cur, server, root);

        ServerBlacklistMapper mapper =
                root.getMapper(ServerBlacklistMapper.class);
        ServerBlacklist blacklist = new ServerBlacklist(server.getId(), user.getId());
        mapper.delete(blacklist);

        root.commit();
        root.close();
    }

    public ServerStat getServerStat()
            throws NoPermissionException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        checkStat(cur, server, root);

        ServerMapper mapper = root.getMapper(ServerMapper.class);

        ServerStat stat = mapper.getServerStats(server);
        root.close();
        return stat;
    }

    public void ManageMember(Member member, Group group)
            throws NoPermissionException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        checkManage(cur, server, root);

        JoiningServerMapper mapper =
                root.getMapper(JoiningServerMapper.class);
        JoiningServer join =
                new JoiningServer(member.getId(), server.getId(), group.getName());

        mapper.setGroup(join);
        root.commit();
        root.close();
    }

    public void createCategory(String name)
            throws NoPermissionException, DuplicateNameException {
        SqlSession root = MybatisUtil.getRootSqlSession();

        checkCreate(cur, server, root);

        Category category = new Category(server.getId(), name);
        try {
            CategoryMapper categoryMapper = root.getMapper(CategoryMapper.class);
            categoryMapper.insert(category);
        } catch (Exception e) {
            throw new DuplicateNameException(name);
        }


        Group group = getGroup(cur, server, root);
        CategoryVisible sameVisible = new CategoryVisible(server.getId(), name, group.getName());
        CategoryVisibleMapper visibleMapper =
                root.getMapper(CategoryVisibleMapper.class);
        visibleMapper.insert(sameVisible);
        if (!group.getName().equals("creator")) {
            CategoryVisible creatorVisible = new CategoryVisible(server.getId(), name, "creator");
            visibleMapper.insert(creatorVisible);
        }

        root.commit();
        root.close();
    }

    public void setVisible(Category category, Group group)
            throws NoPermissionException, AlreadyExistException {
        SqlSession root = MybatisUtil.getRootSqlSession();

        checkCreate(cur, server, root);

        CategoryVisible visible =
                new CategoryVisible(category.getSid(), category.getName(), group.getName());
        CategoryVisibleMapper visibleMapper =
                root.getMapper(CategoryVisibleMapper.class);
        try {
            visibleMapper.insert(visible);
        } catch (Exception e) {
            if (e instanceof PersistenceException) {
                throw new AlreadyExistException();
            }
        }
        root.commit();
        root.close();
    }

    public void removeVisible(Category category, Group group)
            throws NoPermissionException, AlreadyExistException {
        SqlSession root = MybatisUtil.getRootSqlSession();

        checkCreate(cur, server, root);

        CategoryVisible visible =
                new CategoryVisible(category.getSid(), category.getName(), group.getName());
        CategoryVisibleMapper visibleMapper =
                root.getMapper(CategoryVisibleMapper.class);
        visibleMapper.delete(visible);
        root.commit();
        root.close();
    }

    public List<Category> getCategories() {
        SqlSession root = MybatisUtil.getRootSqlSession();
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", cur.getId());
        map.put("sid", server.getId());
        CategoryVisibleMapper mapper = root.getMapper(CategoryVisibleMapper.class);
        List <Category> list = mapper.getVisibleCategories(map);
        root.close();
        return list;
    }

    public void createChannel(Category category, String name, boolean type)
            throws NoPermissionException, DuplicateNameException {
        SqlSession root = MybatisUtil.getRootSqlSession();

        checkCreate(cur, server, root);

        Channel channel = new Channel(server.getId(), name,
                category.getName(), type);
        try {
            ChannelMapper channelMapper =
                    root.getMapper(ChannelMapper.class);
            channelMapper.insert(channel);
        } catch (Exception e) {
            root.commit();
            root.close();
            throw new DuplicateNameException(name);
        }

        root.commit();
        root.close();
    }

    public List<Channel> getChannels(Category category) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        ChannelMapper channelMapper = root.getMapper(ChannelMapper.class);
        List<Channel> ret = channelMapper.selectByCategory(category);

        root.close();
        return ret;
    }

    public AccessingChannel accessChannel(Channel channel) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        AccessingChannel ret =
                new AccessingChannel(cur.getId(), server.getId(), channel.getName());
        AccessingChannelMapper mapper =
                root.getMapper(AccessingChannelMapper.class);
        mapper.access(ret);

        root.commit();
        root.close();
        return ret;
    }

    public void leaveChannel(AccessingChannel accessing) {
        SqlSession root = MybatisUtil.getRootSqlSession();
        AccessingChannelMapper mapper =
                root.getMapper(AccessingChannelMapper.class);
        mapper.leave(accessing);

        root.commit();
        root.close();
    }

    public void sendMessage(Channel channel, String content) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        MessageMapper mapper = root.getMapper(MessageMapper.class);
        Message message = new Message(channel.getSid(),
                channel.getName(), content, cur.getId());
        mapper.insert(message);

        root.commit();
        root.close();
    }

    public List<Message> getMessages(Channel channel) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        MessageMapper mapper = root.getMapper(MessageMapper.class);
        List<Message> ret = mapper.selectByChannel(channel);
        Collections.sort(ret);

        root.close();
        return ret;
    }

    public List<Message> getNewMessages(Channel channel) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        HashMap<String, Object> map = new HashMap<>();
        map.put("sid", channel.getSid());
        map.put("name", channel.getName());
        map.put("uid", cur.getId());

        MessageMapper mapper = root.getMapper(MessageMapper.class);
        List<Message> ret = mapper.getNewMessages(map);
        Collections.sort(ret);

        root.close();
        return ret;
    }

    public void commentMessage(Message message, String type) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        CommentingMessage comment =
                new CommentingMessage(cur.getId(), message.getId(), type);
        CommentingMessageMapper mapper =
                root.getMapper(CommentingMessageMapper.class);
        mapper.insert(comment);

        root.commit();
        root.close();
    }

    public CommentStat getComments(Message message) {
        SqlSession root = MybatisUtil.getRootSqlSession();
        CommentingMessageMapper mapper =
                root.getMapper(CommentingMessageMapper.class);
        CommentStat ret = mapper.getComments(message);

        root.close();
        return ret;
    }


}
