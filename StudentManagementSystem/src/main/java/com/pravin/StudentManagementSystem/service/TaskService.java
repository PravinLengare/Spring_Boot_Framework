package com.pravin.StudentManagementSystem.service;

import com.pravin.StudentManagementSystem.model.Task;
import com.pravin.StudentManagementSystem.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepo taskRepository;
    public TaskService(TaskRepo taskRepository){
        this.taskRepository = taskRepository;
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void addTasks(String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void toggleTask(Long id) {
        Task task =  taskRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid task id "));
        task.setCompleted(!task.isCompleted()); // toggling the value here
        taskRepository.save(task);

    }
}
