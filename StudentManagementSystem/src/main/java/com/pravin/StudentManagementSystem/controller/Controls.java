package com.pravin.StudentManagementSystem.controller;

import com.pravin.StudentManagementSystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Controls {
    @Autowired
    private StudentService studentService;

//    @Autowired
//    private  TaskService taskService;

    @GetMapping("/login")
    public String showLoginPage() {
        // This tells Spring to find "login.html"
        // in the "src/main/resources/templates/" folder and show it.
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
//        // This is your successful login destination
//        int tasks = taskService.getCompletedCount();
//
//        // Get your other stats (for now, we'll hardcode them)
//        int messages = 5;
//        int views = 42;
//
//        // 4. Add the dynamic data to the model
//        model.addAttribute("tasksCompleted", tasks);
//        model.addAttribute("unreadMessages", messages);
//        model.addAttribute("profileViews", views);
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

    @GetMapping("/logout")
    public String doLogout(){

        return "redirect:/login";
    }
}
