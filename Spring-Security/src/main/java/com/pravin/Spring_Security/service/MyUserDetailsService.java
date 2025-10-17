package com.pravin.Spring_Security.service;

import com.pravin.Spring_Security.dao.UserRepo;
import com.pravin.Spring_Security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findbyUsername(username);
        if (user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User 404");
        }
        return user;
    }
}
