package com.org.stem_project.service;

import com.org.stem_project.model.Teacher;
import com.org.stem_project.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImp implements TeacherService{

    @Autowired
    TeacherRepo teacherRepo;

    @Override
    public Teacher add(Teacher teacher) {
        return teacherRepo.save(teacher);
    }

}
