package com.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "login_page.html";
    }

    @RequestMapping("/signup_page")
    public String signup() {
        return "signup_page.html";
    }

    @RequestMapping("/login_page")
    public String login() {
        return "login_page.html";
    }
}
