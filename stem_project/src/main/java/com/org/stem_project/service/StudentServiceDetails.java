package com.org.stem_project.service;

import com.org.stem_project.model.Student;
import com.org.stem_project.model.StudentPrinciple;
import com.org.stem_project.model.Teacher;
import com.org.stem_project.model.TeacherPrinciple;
import com.org.stem_project.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class StudentServiceDetails implements UserDetailsService {
    @Autowired
    private StudentRepo studentRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepo.findByName(username);
        if (student == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User 404");
        }
        return new StudentPrinciple(student);
    }
}
