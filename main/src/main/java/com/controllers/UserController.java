package com.controllers;

import com.exceptions.LoginFailException;
import com.interact.RetBody;
import com.pojo.Login;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/user")
public class UserController {
    private Login login;

    @RequestMapping("/login")
    public Object login(@RequestBody Map<String, Object> map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        try {
            login = new Login(username, password);
            RetBody retBody = new RetBody("Login successful.");
            retBody.addData("correct", "");
            return retBody;
        } catch (LoginFailException e) {
            e.printStackTrace();
            return new RetBody("Login failed.");
        }
    }
}
