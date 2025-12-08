package com.org.stem_project.service;

import com.org.stem_project.model.Teacher;

import java.util.Optional;

public interface TeacherServiceI {
    Teacher add(Teacher teacher);
    Optional<Teacher> findByEmailT(String email);
}
