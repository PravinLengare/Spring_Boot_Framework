package com.org.stem_project.service;

import com.google.firebase.auth.FirebaseToken;
import com.org.stem_project.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmailT(String email);
    User add(User user, FirebaseToken token);
    User add(User user);

}
