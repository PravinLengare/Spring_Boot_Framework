package com.org.stem_project.service;

import com.google.firebase.auth.FirebaseToken;
import com.org.stem_project.model.User;
import com.org.stem_project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepo userRepo;

    @Override
    public Optional<User> findByEmailT(String email) {
        return userRepo.findByEmail(email);
    }
    @Override
    public User add(User user, FirebaseToken token){
        user.setEmail(token.getEmail());
        user.setName(token.getName());
        user.setFirebaseUid(token.getUid());
        user.setRole("USER");
        return userRepo.save(user);
    }
    @Override
    public User add(User user){
        return userRepo.save(user);
    }

}
