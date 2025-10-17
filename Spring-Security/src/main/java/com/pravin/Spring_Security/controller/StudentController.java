package com.pravin.Spring_Security.controller;

import com.pravin.Spring_Security.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    List<Student> students=new ArrayList<>(List.of(
            new Student(1,"Navin","Java"),
            new Student(2,"Kiran","Blockchain")
    ));
    @GetMapping("students")
    public List<Student> getStudents(){

        return students;
    }


    @PostMapping("students")
    public void addStudent(@RequestBody Student student){
        students.add(student);

    }

    @GetMapping("csrf-token")
    public CsrfToken getToken(HttpServletRequest request){

        return (CsrfToken) request.getAttribute("_csrf");
    }
}
