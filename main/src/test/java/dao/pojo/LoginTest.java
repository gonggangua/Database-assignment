package dao.pojo;

import com.exceptions.AlreadyExistException;
import com.exceptions.BlockedException;
import com.exceptions.CannotBeNullException;
import com.exceptions.DoNotExistException;
import com.exceptions.DuplicateNameException;
import com.exceptions.LoginFailException;
import com.exceptions.NoPermissionException;
import com.pojo.AccessingChannel;
import com.pojo.Category;
import com.pojo.Channel;
import com.pojo.Group;
import com.pojo.Login;
import com.pojo.Member;
import com.pojo.Message;
import com.pojo.PrivateMessage;
import com.pojo.Register;
import com.pojo.Server;
import com.pojo.ServerInteract;
import com.pojo.ServerStat;
import com.pojo.User;
import org.junit.Test;

import java.util.List;

public class LoginTest {

    @Test
    public void testCreateServer() throws Exception {
        Register.register("Elon", "password", "ElonMa@twitter.com");
        Login testLogin = new Login("Elon", "password");
        testLogin.charge(110);
        testLogin.upgrade();
        testLogin.createServer("Tesla", false);
        testLogin.logout();
    }

    @Test
    public void testCheckPrivateMessage() {
        try {
            //Register.register("Elon", "password", "ElonMa@twitter.com");
            Login testLoginElon = new Login("Elon", "password");
            Login testLoginSan = new Login("张三", "233333");
            User elon = testLoginSan.searchUser("Elon").get(0);
            User san = testLoginSan.self();
            testLoginElon.sendPrivateMessage(san, "borrow me 50.");
            testLoginSan.sendPrivateMessage(elon, "fuck off.");
            for (PrivateMessage message : testLoginElon.checkPrivateMessage(san)) {
                System.out.println(message);
            }
            testLoginElon.logout();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testGetFriends() {
        try {
            //Register.register("Elon", "password", "ElonMa@twitter.com");
            Login testLoginElon = new Login("Elon", "password");
            Login testLoginSan = new Login("张三", "233333");
            User elon = testLoginElon.self();
            User san = testLoginSan.self();
            testLoginElon.requestFriend(san);
            testLoginSan.acceptFriend(elon);
            for (User friend : testLoginElon.getFriends()) {
                System.out.println(friend);
            }
            for (User friend : testLoginSan.getFriends()) {
                System.out.println(friend);
            }
            testLoginElon.logout();
            testLoginSan.logout();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testChannel() {
        try {
            //Register.register("Elon", "password", "ElonMa@twitter.com");
            Login testLoginElon = new Login("Elon", "password");
            User elon = testLoginElon.self();
            //testLoginElon.createServer("Twitter", false);
            for (Server joinedServer : testLoginElon.getJoinedServers()) {
                System.out.println(joinedServer);
            }
            Server twitter = testLoginElon.getJoinedServers().get(0);
            ServerInteract testInteract =
                    testLoginElon.enterServer(twitter);
            //testInteract.createCategory("Fired");
            for (Category category : testInteract.getCategories()) {
                System.out.println(category);
            }
            Category Fired = testInteract.getCategories().get(0);
            //testInteract.createChannel(Fired, "announcement", false);
            //testInteract.createChannel(Fired, "conversation", true);
            Channel announcement = null;
            Channel conversation = null;
            for (Channel channel : testInteract.getChannels(Fired)) {
                if (channel.getName().equals("announcement")) {
                    announcement = channel;
                }
                if (channel.getName().equals("conversation")) {
                    conversation = channel;
                }
                System.out.println(channel);
            }
            assert announcement != null;
            assert conversation != null;
            AccessingChannel accessingAnnouncement =
                    testInteract.accessChannel(announcement);
            testInteract.sendMessage(announcement, "You are fired.");
            for (Message message : testInteract.getMessages(announcement)) {
                testInteract.commentMessage(message, "like");
                System.out.println(message);
                System.out.println(testInteract.getComments(message));
            }
            testInteract.leaveChannel(accessingAnnouncement);
            AccessingChannel accessingConversation =
                    testInteract.accessChannel(conversation);
            Thread.sleep(5000);
            testInteract.leaveChannel(accessingConversation);
            testLoginElon.logout();
            ServerStat stat = testInteract.getServerStat();
            System.out.println(stat);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testBlockUser() {
        Login testLoginElon = null;
        Login testLoginSan = null;
        User elon = null;
        User san = null;
        try {
            testLoginElon = new Login("Elon", "password");
            testLoginSan = new Login("张三", "233333");
            elon = testLoginElon.self();
            san = testLoginSan.self();
        } catch (LoginFailException e) {
            e.printStackTrace();
        }

        try {
            testLoginElon.blockUser(san);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (User friend : testLoginElon.getFriends()) {
            System.out.println(friend);
        }

        try {
            testLoginSan.requestFriend(elon);
        } catch (AlreadyExistException e) {
            System.out.println("block failed.");
        } catch (BlockedException e) {
            System.out.println("block succeeded.");
        }

        try {
            testLoginElon.removeBlock(san);
        } catch (DoNotExistException e) {
            e.printStackTrace();
        }

        try {
            testLoginSan.requestFriend(elon);
            testLoginElon.rejectFriend(san);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            testLoginElon.requestFriend(san);
            testLoginSan.acceptFriend(elon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*testLoginElon.logout();
        try {
            testLoginElon = new Login("Elon", "password");
        } catch (LoginFailException e) {
            e.printStackTrace();
        }*/

        System.out.println(testLoginElon.getFriends().size());
        for (User friend : testLoginElon.getFriends()) {
            System.out.println(friend);
        }
        testLoginSan.logout();
    }

    @Test
    public void testServerManagement() {
        Login testLoginElon = null;
        Login testLoginSan = null;
        User elon = null;
        User san = null;
        Server server = null;
        ServerInteract interactElon = null;
        ServerInteract interactSan = null;
        Channel announcement = null;
        try {
            testLoginElon = new Login("Elon", "password");
            testLoginSan = new Login("张三", "233333");
            elon = testLoginElon.self();
            san = testLoginSan.self();
            server = testLoginSan.searchServer("Twitter").get(0);
            //testLoginSan.joinServer(server);
            interactElon = testLoginElon.enterServer(server);
            interactSan = testLoginSan.enterServer(server);
            Category category = interactElon.getCategories().get(0);
            /*for (Group group : interactElon.getGroups()) {
                if (group.getName().equals("regular")) {
                    interactElon.setVisible(category, group);
                }
            }*/
            for (Channel channel : interactSan.getChannels(category)) {
                if (channel.getName().equals("announcement")) {
                    announcement = channel;
                    break;
                }
            }
            AccessingChannel accessSan =
                    interactSan.accessChannel(announcement);
            interactSan.sendMessage(announcement, "you bastard");
            interactSan.sendMessage(announcement, "shameless");
            interactSan.sendMessage(announcement, "fxxk you");
            AccessingChannel accessElon =
                    interactElon.accessChannel(announcement);
            for (Message newMessage : interactElon.getNewMessages(announcement)) {
                System.out.println(newMessage);
            }
            Thread.sleep(3000);
            interactElon.leaveChannel(accessElon);
            interactSan.leaveChannel(accessSan);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            interactSan.getServerStat();
        } catch (NoPermissionException e) {
            System.out.println("no permission success");
        }

        try {
            Member memberSan = null;
            for (Member member : interactElon.getMembers()) {
                if (member.getName().equals("张三")) {
                    memberSan = member;
                    break;
                }
            }
            Group admin = null;
            for (Group group : interactElon.getGroups()) {
                if (group.getName().equals("admin")) {
                    admin = group;
                }
            }
            interactElon.ManageMember(memberSan, admin);
            System.out.println(
                    interactSan.getServerStat());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            interactElon.ban(san);
            testLoginSan.joinServer(server);
            interactElon.unban(san);
        } catch (BlockedException e) {
            System.out.println("blocked successfully");
        } catch (NoPermissionException e) {
            e.printStackTrace();
        } catch (AlreadyExistException e) {
            e.printStackTrace();
        }

    }
}



