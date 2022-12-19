package com.controllers;

import com.exceptions.DuplicateNameException;
import com.exceptions.LoginFailException;
import com.interact.RetBody;
import com.pojo.Login;
import com.pojo.Register;
import com.pojo.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("/user")
public class UserController {
    private HashMap<String, Login> loginHashMap;

    @RequestMapping("/checkcorrect")
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

    @RequestMapping("/login")
    public Object login(@RequestBody Map<String, Object> map) {
        String username = (String) map.get("user_name");
        String password = (String) map.get("password");
        try {
            Login login = new Login(username, password);
            loginHashMap.put(username, login);
            return new RetBody("Login successful.");
        } catch (LoginFailException e) {
            e.printStackTrace();
            return new RetBody("Login failed.");
        }
    }

    @RequestMapping("/checkexist")
    public Object registerCheck(@RequestParam String user_name) {

    }

    @RequestMapping("/signup")
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

    @RequestMapping("/info")
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
}
