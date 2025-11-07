package com.pravin.StudentManagementSystem.repository;

import com.pravin.StudentManagementSystem.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Long> {
}
