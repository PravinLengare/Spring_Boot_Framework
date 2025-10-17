package com.pravin.Spring_Security.service;

import com.pravin.Spring_Security.dao.UserRepo;
import com.pravin.Spring_Security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public User addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println("The encoded password is : "+user.getPassword());
        return repo.save(user);
    }
}
