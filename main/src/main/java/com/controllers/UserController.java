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
        System.out.println("in!");
        System.out.println("user_name: " + user_name + "password: " + password);
        try {
            if (user_name == null || password == null ||
                user_name.equals("") || password.equals("")) {
                RetBody retBody =  new RetBody("Information missing!");
                retBody.addData("correct", false);
                return retBody;
            }
            new Login(user_name, password);
            RetBody retBody = new RetBody("");
            retBody.addData("correct", true);
            System.out.println("true");
            return retBody;
        } catch (LoginFailException e) {
            RetBody retBody = new RetBody("");
            retBody.addData("correct", false);
            System.out.println("false");
            return retBody;
        }
    }

    @RequestMapping("/user/login")
    public Object login(@RequestBody Map<String, Object> map) {
        if (map == null) {
            return new RetBody("");
        }
        String username = (String) map.get("user_name");
        String password = (String) map.get("password");
        if (username == null || username.equals("") ||
            password == null || password.equals("")) {
            return new RetBody("Please input your information");
        }
        try {
            Login login = new Login(username, password);
            loginHashMap.put(username, login);
            login.setStatus(UserStatus.BUSY);
            return new RetBody("Login successful.");
        } catch (LoginFailException e) {
            e.printStackTrace();
            RetBody retBody = new RetBody("Login failed.");
            retBody.setStatus(1);
            return retBody;
        }
    }

    @RequestMapping("/user/logout")
    public Object logout(@RequestParam String user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        login.logout();
        loginHashMap.remove(user_name);
        return new RetBody("Logout successful!");
    }

    @RequestMapping("/user/checkexist")
    public Object registerCheck(@RequestParam String user_name) {
        if (user_name == null || user_name.equals("")) {
            RetBody retBody = new RetBody("Please input your name");
            retBody.addData("exist", false);
            return retBody;
        }
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
            RetBody retBody = new RetBody("Your name has been used.\nTry another one.");
            retBody.setStatus(1);
            return retBody;
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
        retBody.addData("registry", (user.getRegistry() == null) ? "" :
                (new Date(user.getRegistry().getTime())).toString());
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
            user.setMail((String) map.get("mail"));
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
        List<User> friends = login.getFriends();
        User user = null;
        for (User temp : friends) {
            if (temp.getName().equals(other_user_name)) {
                user = temp;
                break;
            }
        }
        if (user == null) {
            List<User> users = login.searchUserByAccurateName(other_user_name);
            if (users.size() == 0) {
                return new RetBody("Cannot find user!");
            }
            user = users.get(0);
        }
        RetBody retBody = new RetBody("Successful!");
        retBody.addData("other_user_name", user.getName());
        retBody.addData("mail", user.getMail());
        retBody.addData("level", user.getLevel());
        retBody.addData("status", user.getStatus());
        retBody.addData("registry",(user.getRegistry() == null) ? "" :
                (new Date(user.getRegistry().getTime())).toString());
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
        //System.out.println("in levelUp!");
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        try {
            login.upgrade();
        } catch (NoEnoughMoneyException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return new RetBody("LevelUp successful!");
    }

    @RequestMapping("/user/friend_request")
    public Object friendRequest(@RequestParam String user_name, @RequestParam String other_user_name) {
        try {
            Login login = getLogin(user_name);
            if (login == null) {
                return new RetBody("User is not logged in!");
            }
            List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
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

    @RequestMapping("/friends/accept")
    public Object acceptFriend(@RequestParam String user_name, @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
        if (otherUsers.size() == 0) {
            return new RetBody("Request user not found!");
        }
        User otherUser = otherUsers.get(0);
        try {
            login.acceptFriend(otherUser);
        } catch (DoNotExistException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        } catch (BlockedException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return new RetBody("Successful!");
    }

    @RequestMapping("/friends/reject")
    public Object rejectFriend(@RequestParam String user_name, @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
        if (otherUsers.size() == 0) {
            return new RetBody("Request user not found!");
        }
        User otherUser = otherUsers.get(0);
        try {
            login.rejectFriend(otherUser);
        } catch (DoNotExistException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return new RetBody("Successful!");
    }

    @RequestMapping("/user/block")
    public Object block(@RequestParam String user_name, @RequestParam String other_user_name) {
        try {
            Login login = getLogin(user_name);
            if (login == null) {
                return new RetBody("User is not logged in!");
            }
            List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
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
            //System.out.println("id:"+server.getId()+",name"+server.getName());
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
        List<FriendBody> newFriendApps = new ArrayList<>();
        retBody.addData("new_friend_applications", newFriendApps);
        for (User friendRequest : login.getRequests()) {
            FriendBody friendBody = new FriendBody(friendRequest.getName());
            newFriendApps.add(friendBody);
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

    @RequestMapping("/servers/group_info_change")
    public Object changeGroupInfo(/*@RequestParam String user_name, @RequestParam String server_name,
                                  @RequestParam String group_name, @RequestBody Map<String, Object> map*/) {
        return new RetBody("该功能尚未开发，请以后再来探索吧！");
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
            List<User> others = login.searchUserByAccurateName(other_user_name);
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
        if (!channel.isType()) {
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
        }
        retBody.addData("items", messageBodies);
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
        //System.out.println("user_name: " + user_name);
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
        //System.out.println("channel: " + channel.getName());
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
        List<User> friends = login.searchUserByAccurateName(other_user_name);
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
        //System.out.println("user_name: " + user_name + ", other_user: " + other_user_name);
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> friends = login.searchUserByAccurateName(other_user_name);
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
        //System.out.println("servers_keyword: " + keyword);
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        try {
            List<Server> serverObjects = login.searchServer(keyword);
            List<HashMap<String, String>> servers = new ArrayList<>();
            for (Server server : serverObjects) {
                if (server.isPrivate()) {
                    continue;
                }
                String serverName = server.getName();
                HashMap<String, String> body = new HashMap<>();
                body.put("label", serverName);
                body.put("to", "server_info?user_name=" + user_name +
                        "&server_name=" + serverName);
                servers.add(body);
            }
            retBody.addData("servers", servers);
        } catch (CannotBeNullException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }

    @RequestMapping("/user/search")
    public Object searchUser(@RequestParam String user_name, @RequestParam String keyword) {
        //System.out.println("user_keyword: " + keyword);
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<HashMap<String, String>> users = new ArrayList<>();
        try {
            List<User> userObjects = login.searchUser(keyword);
            for (User user : userObjects) {
                String other_user_name = user.getName();
                HashMap<String, String> body = new HashMap<>();
                body.put("label", other_user_name);
                body.put("to", "other_user_info?user_name=" + user_name +
                        "&other_user_name=" + other_user_name);
                users.add(body);
            }
            retBody.addData("users", users);
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
        List<Server> servers = login.getServerByAccurateName(server_name);
        if (servers.size() == 0) {
            res.add(new RetBody("Server not found!"));
            return res;
        }
        res.add(servers.get(0));
        res.add(new RetBody("Successful!"));
        return res;
    }

    @RequestMapping("/servers/server_info")
    public Object getServerInfo(@RequestParam String user_name, @RequestParam String server_name) {
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        //ServerInteract si = login.enterServer(server);
        RetBody retBody = (RetBody) objects.get(2);
        retBody.addData("new_server_name", server.getName());
        retBody.addData("owner_name", login.searchUser(server.getCreator()).get(0).getName());
        retBody.addData("isPrivate", server.isPrivate());
        return retBody;
    }

    @RequestMapping("/servers/server_info_change")
    public Object changeServerInfo(@RequestParam String user_name, @RequestParam String server_name,
                                   @RequestBody Map<String, Object> map) {
        String new_name = (String) map.get("new_server_name");
        //System.out.println("oldName: " + server_name);
        //System.out.println("newName: " + new_name);
        boolean isPrivate = (boolean) map.get("isPrivate");
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        ServerInteract si = login.enterServer(server);
        try {
            si.updateServerInfo(new_name, isPrivate);
        } catch (NoPermissionException e) {
            e.printStackTrace();
            RetBody retBody = new RetBody(e.toString());
            retBody.setStatus(1);
            return retBody;
        } catch (DuplicateNameException e) {
            e.printStackTrace();
            RetBody retBody = new RetBody(e.toString());
            retBody.setStatus(1);
            return retBody;
        }
        return new RetBody(("Successful!"));
    }

    @RequestMapping("/servers/create")
    public Object createServer(@RequestParam String user_name, @RequestBody Map<String, Object> map) {
        //System.out.println("in createServer!");
        String server_name = (String) map.get("server_name");
        //String owner_name = (String) map.get("owner_name");
        boolean isPrivate = (boolean) map.get("isPrivate");
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        try {
            login.createServer(server_name, isPrivate);
        } catch (LevelLimitException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return new RetBody("Successful!");
    }

    @RequestMapping("/servers/checkcreate")
    public Object checkCreateServer(@RequestParam String user_name, @RequestParam String server_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        RetBody retBody = new RetBody("Successful!");
        List<Server> servers = login.getServerByAccurateName(server_name);
        retBody.addData("exist", servers.size() > 0);
        int userId = login.self().getId();
        int cnt = 0;
        List<Server> joinServers = login.getJoinedServers();
        for (Server server : joinServers) {
            if (server.getCreator() == userId) {
                cnt++;
            }
        }
        retBody.addData("full", cnt >= login.self().getLevel());
        return retBody;
    }

    @RequestMapping("/servers/checkuser")
    public Object checkUserAndServer(@RequestParam String user_name, @RequestParam String server_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<Server> servers = login.getServerByAccurateName(server_name);
        if (servers.size() == 0) {
            return new RetBody("Server not found!");
        }
        Server server = servers.get(0);
        ServerInteract si = login.enterServer(server);
        Group group = si.getUserGroup(login.self());
        RetBody retBody = new RetBody("Successful!");
        if (group == null) {
            retBody.addData("ismember", false);
            retBody.addData("isbanned", false);
            retBody.addData("isowner", false);
            retBody.addData("canStats", false);
            retBody.addData("canCreate", false);
            retBody.addData("canManage", false);
            retBody.addData("canBan", false);
        } else {
            boolean ismember = false;
            for (Server temp : login.getJoinedServers()) {
                if (temp.getId() == server.getId()) {
                    ismember = true;
                    break;
                }
            }
            retBody.addData("ismember", ismember);
            retBody.addData("isbanned", si.checkIfBanned());
            retBody.addData("isowner", server.getCreator() == login.self().getId());
            retBody.addData("canStats", group.isCanStats());
            retBody.addData("canCreate", group.isCanCreate());
            retBody.addData("canManage", group.isCanManage());
            retBody.addData("canBan", group.isCanBan());
        }
        return retBody;
    }

    @RequestMapping("/servers/join")
    public Object joinServer(@RequestParam String user_name, @RequestParam String server_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<Server> servers = login.getServerByAccurateName(server_name);
        if (servers.size() == 0) {
            return new RetBody("Server not found!");
        }
        Server server = servers.get(0);
        try {
            login.joinServer(server);
        } catch (AlreadyExistException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        } catch (BlockedException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return new RetBody("Successful!");
    }

    /*@RequestMapping("/servers/quit")
    public Object quitServer(@RequestParam String user_name, @RequestParam String server_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<Server> servers = login.getServerByAccurateName(server_name);
        if (servers.size() == 0) {
            return new RetBody("Server not found!");
        }
        Server server = servers.get(0);
        ServerInteract si = login.enterServer(server);
        //si.
        return new RetBody("Successful!");
    }*/

    @RequestMapping("/servers/delete")
    public Object dismissServer(@RequestParam String user_name, @RequestParam String server_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<Server> servers = login.getServerByAccurateName(server_name);
        if (servers.size() == 0) {
            return new RetBody("Server not found!");
        }
        Server server = servers.get(0);
        ServerInteract si = login.enterServer(server);
        try {
            si.dismiss();
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return new RetBody("Successful!");
    }

    @RequestMapping("/friends/checkrelationship")
    public Object getUserRelationship(@RequestParam String user_name, @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
        if (otherUsers.size() == 0) {
            return new RetBody("Other_user not found!");
        }
        User other_user = otherUsers.get(0);
        RetBody retBody = new RetBody("Successful!");
        boolean isfriend = false, blocking = false;
        for (User user : login.getFriends()) {
            if (user.getId() == other_user.getId()) {
                isfriend = true;
                break;
            }
        }
        for (User user : login.getBlockedUsers()) {
            if (user.getId() == other_user.getId()) {
                blocking = true;
                break;
            }
        }
        retBody.addData("isfriend", isfriend);
        retBody.addData("blocked", login.checkIfBlocked(other_user));
        retBody.addData("blocking", blocking);
        return retBody;
    }

    @RequestMapping("/friends/delete")
    public Object deleteFriend(@RequestParam String user_name, @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
        if (otherUsers.size() == 0) {
            return new RetBody("Other_user not found!");
        }
        User other_user = otherUsers.get(0);
        try {
            login.blockUser(other_user);
        } catch (AlreadyExistException e) {
            e.printStackTrace();
            return new RetBody("He/She is blocked by you");
        }
        try {
            login.removeBlock(other_user);
        } catch (DoNotExistException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return new RetBody("Successful!");
    }

    @RequestMapping("/friends/unblock")
    public Object removeBlock(@RequestParam String user_name, @RequestParam String other_user_name) {
        Login login = getLogin(user_name);
        if (login == null) {
            return new RetBody("User is not logged in!");
        }
        List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
        if (otherUsers.size() == 0) {
            return new RetBody("Other_user not found!");
        }
        User other_user = otherUsers.get(0);
        try {
            login.removeBlock(other_user);
        } catch (DoNotExistException e) {
            e.printStackTrace();
            return new RetBody(1, "Failed: " + e);
        }
        return new RetBody("Successful!");
    }

    @RequestMapping("/servers/status")
    public Object getServerStatus(@RequestParam String user_name, @RequestParam String server_name) {
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        RetBody retBody = (RetBody) objects.get(2);
        ServerInteract si = login.enterServer(server);
        try {
            ServerStat serverStat = si.getServerStat();
            retBody.addData("messageCnt", serverStat.getMessageCnt());
            retBody.addData("totalLength", serverStat.getTotalLength());
            retBody.addData("callCnt", serverStat.getCallCnt());
            retBody.addData("totalTime", serverStat.getTotalTime());
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }

    @RequestMapping("/servers/create_category")
    public Object createCategory(@RequestParam String user_name, @RequestParam String server_name,
                                 @RequestBody Map<String, Object> map) {
        String category_name = (String) map.get("category_name");
        ArrayList<Object> group_names = (ArrayList<Object>) map.get("visible");
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        RetBody retBody = (RetBody) objects.get(2);
        ServerInteract si = login.enterServer(server);
        try {
            si.createCategory(category_name);
            Category category = null;
            for (Category temp : si.getCategories()) {
                if (temp.getName().equals(category_name)) {
                    category = temp;
                    break;
                }
            }
            if (category == null) {
                return new RetBody(1, "Category not found!");
            }
            List<Group> groups = si.getGroups();
            HashMap<String, Group> groupHashMap = new HashMap<>();
            for (Group group : groups) {
                groupHashMap.put(group.getName(), group);
            }
            for (Object group_name : group_names) {
                groupHashMap.remove((String) group_name);
            }
            for (Group group : groupHashMap.values()) {
                try {
                    si.removeVisible(category, group);
                } catch (AlreadyExistException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoPermissionException e) {
            e.printStackTrace();
            RetBody retBody1 = new RetBody(e.toString());
            retBody1.setStatus(1);
            return retBody1;
        } catch (DuplicateNameException e) {
            e.printStackTrace();
            RetBody retBody1 = new RetBody(e.toString());
            retBody1.setStatus(1);
            return retBody1;
        }
        return retBody;
    }

    @RequestMapping("/group_options")
    public Object getGroupOptions(@RequestParam String user_name, @RequestParam String server_name) {
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        RetBody retBody = (RetBody) objects.get(2);
        ServerInteract si = login.enterServer(server);
        List<HashMap<String, String>> options = new ArrayList<>();
        for (Group group : si.getGroups()) {
            HashMap<String, String> option = new HashMap<>();
            option.put("label", group.getName());
            option.put("value", group.getName());
            options.add(option);
        }
        retBody.addData("options", options);
        return retBody;
    }

    @RequestMapping("/servers/create_channel")
    public Object createChannel(@RequestParam String user_name, @RequestParam String server_name,
                                @RequestParam String category_name, @RequestBody Map<String, Object> map) {
        String channel_name = (String) map.get("channel_name");
        int channel_type = (int) map.get("channel_type");
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        RetBody retBody = (RetBody) objects.get(2);
        ServerInteract si = login.enterServer(server);
        Category category = null;
        for (Category temp : si.getCategories()) {
            if (temp.getName().equals(category_name)) {
                category = temp;
                break;
            }
        }
        if (category == null) {
            return new RetBody("Category not found!");
        }
        try {
            si.createChannel(category, channel_name, channel_type == 2);
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        } catch (DuplicateNameException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }

    @RequestMapping("/servers/create_group")
    public Object createGroup(/*@RequestParam String user_name, @RequestParam String server_name,
                              @RequestBody Map<String, Object> map*/) {
        return new RetBody("该功能尚未开发，请以后再来探索吧！");
    }

    @RequestMapping("/servers/other_user_group")
    public Object getOtherUserGroup(@RequestParam String user_name, @RequestParam String server_name,
                                     @RequestParam String other_user_name) {
        System.out.println("get info: " + other_user_name);
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        RetBody retBody = (RetBody) objects.get(2);
        ServerInteract si = login.enterServer(server);
        List<HashMap<String, String>> options = new ArrayList<>();
        for (Group group : si.getGroups()) {
            String groupName = group.getName();
            HashMap<String, String> body = new HashMap<>();
            body.put("label", groupName);
            body.put("value", groupName);
            options.add(body);
        }
        retBody.addData("options", options);
        List<User> otherUsers = login.searchUserByAccurateName(other_user_name);
        if (otherUsers.size() == 0) {
            return new RetBody("Other_user not found!");
        }
        User other_user = otherUsers.get(0);
        retBody.addData("value", si.getUserGroup(other_user));
        return retBody;
    }

    @RequestMapping("/servers/other_user_group_change")
    public Object changeOtherUserGroup(@RequestParam String user_name, @RequestParam String server_name,
                                       @RequestParam String other_user_name, @RequestBody Map<String, Object> map) {
        String group_name = (String) map.get("group_name");
        List<Object> objects = findLoginAndServer(user_name, server_name);
        if (objects.size() < 3) {
            return objects.get(objects.size() - 1);
        }
        Login login = (Login) objects.get(0);
        Server server = (Server) objects.get(1);
        RetBody retBody = (RetBody) objects.get(2);
        ServerInteract si = login.enterServer(server);
        Member member = null;
        for (Member temp : si.getMembers()) {
            if (temp.getName().equals(other_user_name)) {
                member = temp;
                break;
            }
        }
        if (member == null) {
            return new RetBody("Other user is not in this server!");
        }
        Group group = null;
        for (Group temp : si.getGroups()) {
            if (temp.getName().equals(group_name)) {
                group = temp;
                break;
            }
        }
        if (group == null) {
            return new RetBody("Group not found!");
        }
        try {
            si.ManageMember(member, group);
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return new RetBody(e.toString());
        }
        return retBody;
    }
}
