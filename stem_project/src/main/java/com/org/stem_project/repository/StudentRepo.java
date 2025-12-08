package com.org.stem_project.repository;

import com.org.stem_project.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student,Long> {
    Optional<Student> findByEmail(String email);

    Student findByName(String name);
}
