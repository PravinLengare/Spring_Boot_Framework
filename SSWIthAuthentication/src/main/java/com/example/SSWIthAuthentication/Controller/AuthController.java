package com.example.SSWIthAuthentication.Controller;


import com.example.SSWIthAuthentication.dao.UserRepo;
import com.example.SSWIthAuthentication.model.User;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(Authentication authentication) {
        // 1. Get the details passed from the Filter
        FirebaseToken token = (FirebaseToken) authentication.getCredentials();
        String email = token.getEmail();

        // 2. Check if user exists
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(email));

        User user;
        if (existingUser.isPresent()) {
            // UPDATE: User exists, update their pic/name if it changed on Google
            user = existingUser.get();
            user.setUsername(token.getEmail());
            user.setEmail(token.getEmail());
            user.setPassword(token.getEmail());
            userRepository.save(user); // Save updates
        } else {
            // CREATE: New user
            user = new User();
            user.setEmail(email);
            user.setUsername(token.getEmail());
            user.setPassword(token.getEmail());
            userRepository.save(user); // Insert
        }

        // 3. Return the user info to the frontend
        return ResponseEntity.ok(user);
    }
}