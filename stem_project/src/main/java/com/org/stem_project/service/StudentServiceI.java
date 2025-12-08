package com.org.stem_project.service;

import com.org.stem_project.model.Student;

import java.util.Optional;

public interface StudentServiceI {
    Student add(Student student);
    Optional<Student> findByEmailS(String email);
}
