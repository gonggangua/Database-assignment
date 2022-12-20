package com.controllers;

import com.exceptions.*;
import com.interact.*;
import com.pojo.*;
import com.pojo.types.UserStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {
    private static final HashMap<String, Login> loginHashMap = new HashMap<>();
    private static final HashMap<String, AccessingChannel> accessingChannelHashMap = new HashMap<>();

    @RequestMapping("/user/checkcorrect")
    public Object loginCheck(@RequestParam String user_name, @RequestParam String password) {
        try {
            if (user_name == null || password == null) {
                RetBody retBody =  new RetBody("information missing!");
                retBody.addData("correct", false);
                return retBody;
            }
            new Login(user_name, password);
            RetBody retBody = new RetBody("");
            retBody.addData("correct", true);
            return retBody;
        } catch (LoginFailException e) {
            e.printStackTrace();
            RetBody retBody =  new RetBody("");
            retBody.addData("correct", false);
            return retBody;
        }
    }

    @RequestMapping("/user/login")
    public Object login(@RequestBody Map<String, Object> map) {
        String username = (String) map.get("user_name");
        String password = (String) map.get("password");
        try {
            Login login = new Login(username, password);
            loginHashMap.put(username, login);
            login.setStatus(UserStatus.BUSY);
            return new RetBody("Login successful.");
        } catch (LoginFailException e) {
            e.printStackTrace();
            return new RetBody("Login failed.");
        }
    }

    @RequestMapping("/user/checkexist")
    public Object registerCheck(@RequestParam String user_name) {
        try {
            Register.checkName(user_name);
            RetBody retBody = new RetBody("");
            retBody.addData("exist", false);
            return retBody;
        } catch (DuplicateNameException e) {
            e.printStackTrace();
            RetBody retBody = new RetBody("");
            retBody.addData("exist", true);
            return retBody;
        }
    }

    @RequestMapping("/user/signup")
    public Object register(@RequestBody Map<String, Object> map) {
        try {
            Register.register((String) map.get("user_name"), (String) map.get("password"), (String) map.get("mail"));
            return new RetBody("Register successfully.");
        } catch (DuplicateNameException e) {
            e.printStackTrace();
            return new RetBody("Your name has been used.\nTry another one.");
        }
    }

    private User getUser(String user_name) {
        if (!loginHashMap.containsKey(user_name)) {
            return null;
        }
        return loginHashMap.get(user_name).self();
    }

    private Login getLogin(String user_name) {
        return loginHashMap.get(user_name);
    }

    @RequestMapping("/user/info")
    public Object getInfo(@RequestParam String user_name) {
        User user = getUser(user_name);
        if (user == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("");
        retBody.addData("user_name", user.getName());
        retBody.addData("mail", user.getMail());
        retBody.addData("level", user.getLevel());
        retBody.addData("registry", user.getRegistry());
        retBody.addData("money", user.getMoney());
        return retBody;
    }

    @RequestMapping("/user/change")
    public Object changeInfo(@RequestParam String user_name, @RequestBody Map<String, Object> map) {
        User user = getUser(user_name);
        if (user == null) {
            return new RetBody("User is not logged in!");
        }
        if (map.containsKey("user_name")) {
            user.setName((String) map.get("user_name"));
        }
        if (map.containsKey("mail")) {
            user.setName((String) map.get("mail"));
        }
        if (map.containsKey("level")) {
            user.setLevel((int) map.get("level"));
        }
        if (map.containsKey("money")) {
            user.setMoney((int) map.get("money"));
        }
        return new RetBody("Successful!");
    }

    @RequestMapping("/user/other_info")
    public Object getOtherInfo(@RequestParam String user_name, @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> users = login.searchAccurateName(other_user_name);
        if (users.size() == 0) {
            return new RetBody("Cannot find user!");
        }
        User user = users.get(0);
        RetBody retBody = new RetBody("Successful!");
        retBody.addData("other_user_name", user.getName());
        retBody.addData("mail", user.getMail());
        retBody.addData("level", user.getLevel());
        retBody.addData("status", user.getStatus());
        retBody.addData("registry", user.getRegistry());
        return retBody;
    }

    @RequestMapping("/user/charge")
    public Object charge(@RequestParam String user_name, @RequestParam int amount) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        login.charge(amount);
        return new RetBody("Charge successful!");
    }

    @RequestMapping("/user/levelup")
    public Object levelUp(@RequestParam String user_name) {
        User user = getUser(user_name);
        if (user == null) {
            return new RetBody("User is not logged in!");
        }
        user.setMoney(user.getMoney() - 300);
        user.setLevel(user.getLevel() + 1);
        return new RetBody("LevelUp successful!");
    }

    @RequestMapping("/user/friend_request")
    public Object friendRequest(@RequestParam String user_name, @RequestParam String other_user_name) {
        try {
            Login login = getLogin(user_name);
            if (login == null) {
                return new RetBody("User is not logged in!");
            }
            List<User> otherUsers = login.searchAccurateName(other_user_name);
            if (otherUsers.size() == 0) {
                return new RetBody("Request user not found!");
            }
            User otherUser = otherUsers.get(0);
            login.requestFriend(otherUser);
            return new RetBody("Friend request successful!");
        } catch (AlreadyExistException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        } catch (BlockedException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
    }

    @RequestMapping("/user/block")
    public Object block(@RequestParam String user_name, @RequestParam String other_user_name) {
        try {
            Login login = getLogin(user_name);
            if (login == null) {
                return new RetBody("User is not logged in!");
            }
            List<User> otherUsers = login.searchAccurateName(other_user_name);
            if (otherUsers.size() == 0) {
                return new RetBody("Request user not found!");
            }
            User otherUser = otherUsers.get(0);
            login.blockUser(otherUser);
            return new RetBody("Block successful!");
        } catch (AlreadyExistException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
    }

    @RequestMapping("/servers/main")
    public Object serversMain(@RequestParam String user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        List<ServerBody> serversBodies = new ArrayList<>();
        retBody.addData("servers", serversBodies);
        for (Server server : servers) {
            ServerBody serverBody = new ServerBody(server.getName());
            ServerInteract si = login.enterServer(server);
            HashMap<String, HashSet<String>> userNames = new HashMap<>(); // <gname, <userName>>
            for (Member member : si.getMembers()) {
                String gname = member.getGname();
                if (!userNames.containsKey(gname)) {
                    userNames.put(gname, new HashSet<>());
                }
                userNames.get(gname).add(member.getName());
            }
            for (Group group : si.getGroups()) {
                GroupBody groupBody = new GroupBody(group.getName());
                groupBody.setTo(user_name, server.getName(), group.getName());
                if (userNames.get(group.getName()) != null) {
                    for (String groupUser : userNames.get(group.getName())) {
                        ChildBody childBody = new ChildBody(groupUser);
                        childBody.setTo(user_name, server.getName(), groupUser);
                        groupBody.addChild(childBody);
                    }
                }
                serverBody.addGroup(groupBody);
            }
            for (Category category : si.getCategories()) {
                CategoryBody categoryBody = new CategoryBody(category.getName());
                for (Channel channel : si.getChannels(category)) {
                    int type = channel.isType() ? 2 : 1;
                    ChannelBody channelBody = new ChannelBody(channel.getName(), type);
                    categoryBody.addChannel(channelBody);
                }
                serverBody.addCategory(categoryBody);
            }
            serversBodies.add(serverBody);
        }
        List<FriendBody> friendBodies = new ArrayList<>();
        retBody.addData("friends", friendBodies);
        for (User friend : login.getFriends()) {
            FriendBody friendBody = new FriendBody(friend.getName());
            friendBodies.add(friendBody);
        }
        return retBody;
    }

    @RequestMapping("/servers/group_info")
    public Object getGroupInfo(@RequestParam String user_name, @RequestParam String server_name,
                             @RequestParam String group_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        Server server = null;
        for (Server temp : servers) {
            if (temp.getName().equals(server_name)) {
                server = temp;
                break;
            }
        }
        if (server == null) {
            return new RetBody("Server name invalid!");
        }
        ServerInteract si = login.enterServer(server);
        Group group = null;
        for (Group temp : si.getGroups()) {
            if (temp.getName().equals(group_name)) {
                group = temp;
                break;
            }
        }
        if (group == null) {
            return new RetBody("Group name invalid!");
        }
        retBody.addData("group_name", group.getName());
        retBody.addData("canCreate", group.isCanCreate());
        retBody.addData("canBan", group.isCanBan());
        retBody.addData("canManage", group.isCanManage());
        retBody.addData("canStats", group.isCanStats());
        return retBody;
    }

    @RequestMapping("/servers/checkowner")
    public Object checkOwner(@RequestParam String user_name, @RequestParam String server_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        Server server = null;
        for (Server temp : servers) {
            if (temp.getName().equals(server_name)) {
                server = temp;
                break;
            }
        }
        boolean isOwner;
        if (server == null) {
            isOwner = false;
        } else {
            isOwner = server.getCreator() == login.self().getId();
        }
        retBody.addData("isowner", isOwner);
        return retBody;
    }

    @RequestMapping("/servers/ban")
    public Object ban(@RequestParam String user_name, @RequestParam String server_name,
                      @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        Server server = null;
        for (Server temp : servers) {
            if (temp.getName().equals(server_name)) {
                server = temp;
                break;
            }
        }
        if (server == null) {
            return new RetBody("You are not in this server!");
        }
        try {
            ServerInteract si = login.enterServer(server);
            List<User> others = login.searchAccurateName(other_user_name);
            if (others.size() == 0) {
                return new RetBody("Invalid other_user_name!");
            }
            si.ban(others.get(0));
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }

    @RequestMapping("/servers/messages")
    public Object getChannelMessages(@RequestParam String user_name, @RequestParam String server_name,
                              @RequestParam String category_name, @RequestParam String channel_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        Server server = null;
        for (Server temp : servers) {
            if (temp.getName().equals(server_name)) {
                server = temp;
                break;
            }
        }
        if (server == null) {
            return new RetBody("You are not in this server!");
        }
        ServerInteract si = login.enterServer(server);
        Category category = null;
        for (Category temp : si.getCategories()) {
            if (temp.getName().equals(category_name)) {
                category = temp;
                break;
            }
        }
        if (category == null) {
            return new RetBody("Invalid category_name!");
        }
        Channel channel = null;
        for (Channel temp : si.getChannels(category)) {
            if (temp.getName().equals(channel_name)) {
                channel = temp;
                break;
            }
        }
        if (channel == null) {
            return new RetBody("Invalid channel_name!");
        }
        List<MessageBody> messageBodies = new ArrayList<>();
        for (Message message : si.getMessages(channel)) {
            if (!si.getNewMessages(channel).contains(message)) {
                messageBodies.add(new MessageBody(message.getSendTime(),
                        login.searchUser(message.getSender()).get(0).getName(),
                        message.getContent()));
            }
        }
        messageBodies.add(new MessageBody(null, "以下为新消息"));
        for (Message message : si.getNewMessages(channel)) {
            messageBodies.add(new MessageBody(message.getSendTime(),
                    login.searchUser(message.getSender()).get(0).getName(),
                    message.getContent()));
        }
        retBody.addData("title", messageBodies);
        return retBody;
    }

    @RequestMapping("/servers/send_message")
    public Object sendChannelMessages(@RequestParam String user_name, @RequestParam String server_name,
                              @RequestParam String category_name, @RequestParam String channel_name,
                               @RequestParam String content) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        Server server = null;
        for (Server temp : servers) {
            if (temp.getName().equals(server_name)) {
                server = temp;
                break;
            }
        }
        if (server == null) {
            return new RetBody("You are not in this server!");
        }
        ServerInteract si = login.enterServer(server);
        Category category = null;
        for (Category temp : si.getCategories()) {
            if (temp.getName().equals(category_name)) {
                category = temp;
                break;
            }
        }
        if (category == null) {
            return new RetBody("Invalid category_name!");
        }
        Channel channel = null;
        for (Channel temp : si.getChannels(category)) {
            if (temp.getName().equals(channel_name)) {
                channel = temp;
                break;
            }
        }
        if (channel == null) {
            return new RetBody("Invalid channel_name!");
        }
        si.sendMessage(channel, content);
        return retBody;
    }

    private RetBody joinChannel(String user_name, String server_name,
                               String category_name, String channel_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        Server server = null;
        for (Server temp : servers) {
            if (temp.getName().equals(server_name)) {
                server = temp;
                break;
            }
        }
        if (server == null) {
            return new RetBody("You are not in this server!");
        }
        ServerInteract si = login.enterServer(server);
        Category category = null;
        for (Category temp : si.getCategories()) {
            if (temp.getName().equals(category_name)) {
                category = temp;
                break;
            }
        }
        if (category == null) {
            return new RetBody("Invalid category_name!");
        }
        Channel channel = null;
        for (Channel temp : si.getChannels(category)) {
            if (temp.getName().equals(channel_name)) {
                channel = temp;
                break;
            }
        }
        if (channel == null) {
            return new RetBody("Invalid channel_name!");
        }
        AccessingChannel accessingChannel = si.accessChannel(channel);
        String key = "username=" + user_name + "&channel_name=" + channel_name;
        accessingChannelHashMap.put(key, accessingChannel);
        return retBody;
    }

    @RequestMapping("/servers/join_call")
    public Object joinCall(@RequestParam String user_name, @RequestParam String server_name,
                               @RequestParam String category_name, @RequestParam String channel_name) {
        return joinChannel(user_name, server_name, category_name, channel_name);
    }

    @RequestMapping("/servers/join_channel")
    public Object joinOtherChannel(@RequestParam String user_name, @RequestParam String server_name,
                           @RequestParam String category_name, @RequestParam String channel_name) {
        return joinChannel(user_name, server_name, category_name, channel_name);
    }

    public RetBody leaveChannel(String user_name, String server_name,
                                String category_name, String channel_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getJoinedServers();
        Server server = null;
        for (Server temp : servers) {
            if (temp.getName().equals(server_name)) {
                server = temp;
                break;
            }
        }
        if (server == null) {
            return new RetBody("You are not in this server!");
        }
        ServerInteract si = login.enterServer(server);
        Category category = null;
        for (Category temp : si.getCategories()) {
            if (temp.getName().equals(category_name)) {
                category = temp;
                break;
            }
        }
        if (category == null) {
            return new RetBody("Invalid category_name!");
        }
        Channel channel = null;
        for (Channel temp : si.getChannels(category)) {
            if (temp.getName().equals(channel_name)) {
                channel = temp;
                break;
            }
        }
        if (channel == null) {
            return new RetBody("Invalid channel_name!");
        }
        String key = "username=" + user_name + "&channel_name=" + channel_name;
        AccessingChannel accessingChannel = accessingChannelHashMap.get(key);
        si.leaveChannel(accessingChannel);
        //accessingChannelHashMap.remove(key);
        return retBody;
    }

    @RequestMapping("/servers/leave_call")
    public Object leaveCall(@RequestParam String user_name, @RequestParam String server_name,
                           @RequestParam String category_name, @RequestParam String channel_name) {
        return leaveChannel(user_name, server_name, category_name, channel_name);
    }

    @RequestMapping("/servers/leave_channel")
    public Object leaveOtherChannel(@RequestParam String user_name, @RequestParam String server_name,
                            @RequestParam String category_name, @RequestParam String channel_name) {
        return leaveChannel(user_name, server_name, category_name, channel_name);
    }

    @RequestMapping("/friends/messages")
    public Object getFriendMessages(@RequestParam String user_name, @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> friends = login.searchAccurateName(other_user_name);
        if (friends.size() == 0) {
            return new RetBody("Invalid other_user_name");
        }
        User friend = friends.get(0);
        List<PrivateMessage> privateMessages = login.checkPrivateMessage(friend);
        RetBody retBody = new RetBody("Successful!");
        List<MessageBody> messageBodies = new ArrayList<>();
        retBody.addData("items", messageBodies);
        for (PrivateMessage privateMessage : privateMessages) {
            messageBodies.add(new MessageBody(privateMessage.getSendTime(),
                    login.searchUser(privateMessage.getSender()).get(0).getName(),
                    privateMessage.getContent()));
        }
        return retBody;
    }

    @RequestMapping("/friends/send_message")
    public Object sendFriendMessage(@RequestParam String user_name, @RequestParam String other_user_name, @RequestParam String content) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> friends = login.searchAccurateName(other_user_name);
        if (friends.size() == 0) {
            return new RetBody("Invalid other_user_name");
        }
        User friend = friends.get(0);
        RetBody retBody = new RetBody("Successful!");
        try {
            login.sendPrivateMessage(friend, content);
        } catch (BlockedException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }

    @RequestMapping("/servers/search")
    public Object searchServers(@RequestParam String user_name, @RequestParam String keyword) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        try {
            List<Server> servers = login.searchServer(keyword);
            List<HashMap<String, String>> bodies = new ArrayList<>();
            for (Server server : servers) {
                if (server.isPrivate()) {
                    continue;
                }
                String serverName = server.getName();
                HashMap<String, String> body = new HashMap<>();
                body.put("label", serverName);
                body.put("to", "server_info?user_name=" + user_name +
                        "&server_name=" + serverName);
                bodies.add(body);
            }
            retBody.addData("servers", bodies);
        } catch (CannotBeNullException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }

    @RequestMapping("/user/search")
    public Object searchUser(@RequestParam String user_name, @RequestParam String keyword) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<HashMap<String, String>> bodies = new ArrayList<>();
        try {
            List<User> users = login.searchUser(keyword);
            for (User user : users) {
                String other_user_name = user.getName();
                HashMap<String, String> body = new HashMap<>();
                body.put("label", other_user_name);
                body.put("to", "other_user_info?user_name=" + user_name +
                        "&other_user_name=" + other_user_name);
                bodies.add(body);
            }
            retBody.addData("users", bodies);
        } catch (CannotBeNullException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }

    public List<Object> findLoginAndServer(String user_name, String server_name) {
        List<Object> res = new ArrayList<>();
        Login login = getLogin(user_name);
        if (login == null) {
            res.add(new RetBody("User is not logged in!"));
            return res;
        }
        res.add(login);
        Server server = //TODO
        if (server == null) {
            res.add(new RetBody("Invalid server_name!"));
            return res;
        }
        res.add(server);
        res.add(new RetBody("Successful!"));
        return res;
    }

    @RequestMapping("/servers/server_info")
    public Object getServerInfo(@RequestParam String user_name, @RequestParam String server_name) {
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        //Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        RetBody retBody = (RetBody) objects.get(2);
        retBody.addData("server_name", server.getName());
        retBody.addData("isPrivate", server.isPrivate());
        return retBody;
    }

    @RequestMapping("/servers/server_info_change")
    public Object changeServerInfo(@RequestParam String user_name, @RequestParam String server_name,
                                   @RequestBody Map<String, Object> map) {
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        ServerInteract si = login.enterServer(server);
        si.//TODO
        return (RetBody) objects.get(2);
    }
}
