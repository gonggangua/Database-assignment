package com.controllers;

import com.interact.RetBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloTest {
    @RequestMapping("/helloworld")
    public Object helloWorld() {
        return new RetBody("hello world!");
    }

    @RequestMapping("/hello")
    public Object hello(@RequestBody Map<String, Object> map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        if (username == null || password == null) {
            return new RetBody("please input complete information");
        }
        RetBody retBody = new RetBody("hello, " + username + "!");
        int strength = password.length() / 3;
        retBody.addData("strength", strength);
        return retBody;
    }
}
