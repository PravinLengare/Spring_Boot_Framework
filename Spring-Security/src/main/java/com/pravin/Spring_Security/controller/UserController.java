package com.pravin.Spring_Security.controller;

import com.pravin.Spring_Security.model.User;
import com.pravin.Spring_Security.service.UserService;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService service;
    @PostMapping("register")
    public User register(@RequestBody User user){

        return service.addUser(user);
    }
}
