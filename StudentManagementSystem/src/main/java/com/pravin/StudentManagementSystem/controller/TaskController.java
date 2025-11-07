package com.pravin.StudentManagementSystem.controller;

import com.pravin.StudentManagementSystem.model.Task;
import com.pravin.StudentManagementSystem.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public String getTasks(Model model){
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks",tasks);
        return "tasks";
    }


    @PostMapping("/tasks")
    public String createTasks(@RequestParam String title){
         taskService.addTasks(title);
        return "redirect:/tasks";
    }

    @GetMapping("tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") Long id){
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("tasks/{id}/toggle")
    public String toggleTask(@PathVariable("id") Long id){
        taskService.toggleTask(id);
        return "redirect:/tasks";
    }

}
