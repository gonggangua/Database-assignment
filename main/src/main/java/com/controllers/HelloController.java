package com.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "login_page.html";
    }

    @RequestMapping("/login_page")
    public String login() {
        return "login_page.html";
    }

    @RequestMapping("/signup_page")
    public String signup() {
        return "signup_page.html";
    }

    @RequestMapping("/create")
    public String create() {
        return "create.html";
    }

    @RequestMapping("/create_category")
    public String create_category() {
        return "create_category.html";
    }

    @RequestMapping("/create_channel")
    public String create_channel() {
        return "create_channel.html";
    }

    @RequestMapping("/create_group")
    public String create_group() {
        return "create_group.html";
    }

    @RequestMapping("/group_info")
    public String group_info() {
        return "group_info.html";
    }

    @RequestMapping("/main_page")
    public String main_page() {
        return "main_page.html";
    }

    @RequestMapping("/other_user_info")
    public String other_user_info() {
        return "other_user_info.html";
    }

    @RequestMapping("/search")
    public String search() {
        return "search.html";
    }

    @RequestMapping("/server_info")
    public String server_info() {
        return "server_info.html";
    }

    @RequestMapping("/user_group")
    public String user_group() {
        return "user_group.html";
    }

    @RequestMapping("/user_info")
    public String user_info() {
        return "user_info.html";
    }
}
