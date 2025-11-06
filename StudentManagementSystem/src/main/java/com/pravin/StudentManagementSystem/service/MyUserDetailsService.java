package com.pravin.StudentManagementSystem.service;

import com.pravin.StudentManagementSystem.model.Student;
import com.pravin.StudentManagementSystem.model.StudentPrinciple;
import com.pravin.StudentManagementSystem.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = Optional.ofNullable(studentRepo.findByUsername(username));
        if (student.isEmpty() ){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User 404");
        }
        return new StudentPrinciple(student.orElse(null));
    }
}
