package com.controllers;

import com.exceptions.DuplicateNameException;
import com.pojo.Register;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterController {
    public String register(@RequestBody Map<String, Object> map) {
        try {
            Register.register((String) map.get("username"), (String) map.get("password"), (String) map.get("mail"));
            return "Register successfully.";
        } catch (DuplicateNameException e) {
            e.printStackTrace();
            return "Your name has been used.\nTry another one.";
        }
    }
}
