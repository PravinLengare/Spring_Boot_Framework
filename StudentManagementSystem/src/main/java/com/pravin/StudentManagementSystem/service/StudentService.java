package com.pravin.StudentManagementSystem.service;

import com.pravin.StudentManagementSystem.model.Student;
import com.pravin.StudentManagementSystem.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private StudentRepo studentRepo;

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public void saveStudent(Student student) {
        studentRepo.save(student);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepo.findById(id);
    }

    public void updateStudent(Student student) {
        studentRepo.save(student);
    }

    public void delete(Long id) {
        studentRepo.deleteById(id);
    }

    public void addStudent(String username, String password) {
        if (studentRepo.findByUsername(username)!=null){
            throw new RuntimeException("Username already exists");

        }
        Student newStudent = new Student();
        newStudent.setUsername(username);
        newStudent.setPassword(passwordEncoder.encode(password));
        newStudent.setRole("user_role");
        studentRepo.save(newStudent);

    }
}
