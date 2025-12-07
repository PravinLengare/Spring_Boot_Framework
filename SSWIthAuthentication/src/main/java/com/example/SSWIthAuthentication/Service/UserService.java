package com.example.SSWIthAuthentication.Service;

import com.example.SSWIthAuthentication.dao.UserRepo;
import com.example.SSWIthAuthentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("The encoded password is : "+user.getPassword());
        return repo.save(user);
    }

    public void saveGoogleUser(String email, String name) {
        User existingUser = repo.findByUsername(email);

        if (existingUser == null){
            User user = new User();
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode("OAUTH_dummy_password"));
            repo.save(user);
        }
        else {
            System.out.println("welcome back , "+name);
        }

    }

    public User findByEmail_(String email) {
        return repo.findByEmail(email);
    }
}
