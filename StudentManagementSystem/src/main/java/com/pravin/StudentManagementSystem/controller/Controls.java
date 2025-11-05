package com.pravin.StudentManagementSystem.controller;

import com.pravin.StudentManagementSystem.model.Student;
import com.pravin.StudentManagementSystem.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Controls {
    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public String showLoginPage() {
        // This tells Spring to find "login.html"
        // in the "src/main/resources/templates/" folder and show it.
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        // This is your successful login destination
        return "dashboard";
    }

    @GetMapping("/register")
    public String doRegister(){

        return "register";
    }

    @PostMapping("/register")
    public String postR(@RequestParam String username, @RequestParam String password){

        studentService.addStudent(username,password);

        return "redirect:/login?register_success";

    }
}
