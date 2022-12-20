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

    //获取频道成员
    public List<Member> getMembers() {
        SqlSession root = MybatisUtil.getRootSqlSession();

        JoiningServerMapper mapper =
                root.getMapper(JoiningServerMapper.class);

        List<Member> ret = mapper.getMembers(server);
        root.close();
        return ret;
    }

    //获取某人在当前服务器的用户组
    public Group getUserGroup(User user) {
        SqlSession root = MybatisUtil.getRootSqlSession();
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper = root.getMapper(GroupMapper.class);
        Group group = groupMapper.getUserGroup(map).get(0);
        root.close();
        return group;
    }

    public boolean checkIfBanned() {
        SqlSession root = MybatisUtil.getRootSqlSession();
        ServerBlacklist blacklist = new ServerBlacklist(server.getId(), cur.getId());

        ServerBlacklistMapper mapper = root.getMapper(ServerBlacklistMapper.class);

        boolean ret;
        if (!mapper.select(blacklist).isEmpty()) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    //获取某人在某服务器所属用户组
    private Group getGroup(User user, Server server, SqlSession root) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        return groupMapper.getUserGroup(map).get(0);
    }

    //以下为确认四种权限
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

    //解散服务器
    public void dismiss() throws NoPermissionException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        if (!getGroup(cur, server, root).getName().equals("creator")) {
            root.close();
            throw new NoPermissionException("dismiss server");
        }

        ServerMapper serverMapper = root.getMapper(ServerMapper.class);
        serverMapper.dismiss(server);
        root.commit();
        root.close();
    }

    //获取权限组
    public List<Group> getGroups() {
        SqlSession root = MybatisUtil.getRootSqlSession();

        GroupMapper mapper = root.getMapper(GroupMapper.class);
        List<Group> groups = mapper.selectByServer(server);

        root.close();
        return groups;
    }

    //禁止用户
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

    //解禁用户
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

    //获取服务器统计数据
    public ServerStat getServerStat()
            throws NoPermissionException {
        SqlSession root = MybatisUtil.getRootSqlSession();
        checkStat(cur, server, root);

        ServerMapper mapper = root.getMapper(ServerMapper.class);

        ServerStat stat = mapper.getServerStats(server);
        root.close();
        return stat;
    }

    //改变某成员所属用户组
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

    //创建分区
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

    //设置分区对某用户组可见
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

    //设置不可见
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

    //获取可见的分区
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

    //创建频道
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

    //获取分区内频道
    public List<Channel> getChannels(Category category) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        ChannelMapper channelMapper = root.getMapper(ChannelMapper.class);
        List<Channel> ret = channelMapper.selectByCategory(category);

        root.close();
        return ret;
    }

    //访问频道
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

    //结束访问
    public void leaveChannel(AccessingChannel accessing) {
        SqlSession root = MybatisUtil.getRootSqlSession();
        AccessingChannelMapper mapper =
                root.getMapper(AccessingChannelMapper.class);
        mapper.leave(accessing);

        root.commit();
        root.close();
    }

    //发送消息
    public void sendMessage(Channel channel, String content) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        MessageMapper mapper = root.getMapper(MessageMapper.class);
        Message message = new Message(channel.getSid(),
                channel.getName(), content, cur.getId());
        mapper.insert(message);

        root.commit();
        root.close();
    }

    //获取消息
    public List<Message> getMessages(Channel channel) {
        SqlSession root = MybatisUtil.getRootSqlSession();

        MessageMapper mapper = root.getMapper(MessageMapper.class);
        List<Message> ret = mapper.selectByChannel(channel);
        Collections.sort(ret);

        root.close();
        return ret;
    }

    //获取从上次离开之后的新消息
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

    //评价某消息(重复两次相同类型则可以取消评价，用触发器实现)
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

    //获取某消息评价统计
    public CommentStat getComments(Message message) {
        SqlSession root = MybatisUtil.getRootSqlSession();
        CommentingMessageMapper mapper =
                root.getMapper(CommentingMessageMapper.class);
        CommentStat ret = mapper.getComments(message);

        root.close();
        return ret;
    }


}
