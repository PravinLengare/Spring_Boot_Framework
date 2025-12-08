package com.org.stem_project.service;

import com.org.stem_project.model.Teacher;
import com.org.stem_project.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class TeacherService implements TeacherServiceI{

    @Autowired
    TeacherRepo teacherRepo;
    @Override
    public Teacher add(Teacher teacher) {
        return teacherRepo.save(teacher);
    }

    @Override
    public Optional<Teacher> findByEmailT(String email) {
        return teacherRepo.findByEmail(email);

    }
}
