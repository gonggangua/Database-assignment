package com.pojo;

import com.dao.CategoryMapper;
import com.dao.CategoryVisibleMapper;
import com.dao.GroupMapper;
import com.exceptions.DuplicateNameException;
import com.exceptions.NoPermissionException;
import com.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class ServerInteract {
    private User cur;
    private SqlSession sqlSession;
    private Server server;

    public ServerInteract(User cur, SqlSession sqlSession, Server server) {
        this.cur = cur;
        this.sqlSession = sqlSession;
        this.server = server;
    }

    private Group getGroup(User user, Server server, SqlSession root) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        return groupMapper.getUserGroup(map).get(0);
    }

    private boolean checkCreate(User user, Server server, SqlSession root) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", user.getId());
        map.put("sid", server.getId());
        GroupMapper groupMapper =
                root.getMapper(GroupMapper.class);
        return groupMapper.getCanCreate(map);
    }

    public void createCategory(String name)
            throws NoPermissionException, DuplicateNameException {
        SqlSession root = MybatisUtil.getRootSqlSession();

        if (!checkCreate(cur, server, root)) {
            throw new NoPermissionException("create");
        }

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
}
