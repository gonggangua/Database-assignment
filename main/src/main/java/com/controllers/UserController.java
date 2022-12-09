package com.controllers;

import com.exceptions.LoginFailException;
import com.pojo.Login;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
public class UserController {
    private Login login;

    @RequestMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        try {
            login = new Login(username, password);
            return "Login successfully.";
        } catch (LoginFailException e) {
            e.printStackTrace();
            return "Login failed.";
        }
    }
}
