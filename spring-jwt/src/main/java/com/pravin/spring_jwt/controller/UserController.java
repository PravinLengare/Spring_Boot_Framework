package com.pravin.spring_jwt.controller;

import com.pravin.spring_jwt.model.User;
import com.pravin.spring_jwt.service.JwtService;
import com.pravin.spring_jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public User register(@RequestBody User user){

        return service.addUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws Exception{

        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if (authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());

        }
        else {
            return "Login Fail";
        }

    }


}
