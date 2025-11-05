package com.pravin.StudentManagementSystem.repository;

import com.pravin.StudentManagementSystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student,Long> {
    Student findByUsername(String username);
}
