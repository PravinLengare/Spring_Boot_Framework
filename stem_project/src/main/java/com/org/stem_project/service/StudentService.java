package com.org.stem_project.service;

import com.org.stem_project.model.Student;
import com.org.stem_project.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService implements StudentServiceI {
    @Autowired
    StudentRepo studentRepo;

    @Override
    public Student add(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public Optional<Student> findByEmailS(String email) {
        return studentRepo.findByEmail(email);
    }
}
