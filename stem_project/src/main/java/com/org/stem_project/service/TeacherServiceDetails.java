package com.org.stem_project.service;

import com.org.stem_project.model.Teacher;
import com.org.stem_project.model.TeacherPrinciple;
import com.org.stem_project.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TeacherServiceDetails implements UserDetailsService {
    @Autowired
    private TeacherRepo teacherRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = teacherRepo.findByName(username);
        if (teacher == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User 404");
        }
        return new TeacherPrinciple(teacher);
    }
}
