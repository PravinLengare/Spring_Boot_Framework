package com.pravin.Spring_Security.dao;

import com.pravin.Spring_Security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findbyUsername(String username);
}
