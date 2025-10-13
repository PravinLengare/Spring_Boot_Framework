package com.pravin.todo.service;

import java.util.List;
import com.pravin.todo.model.Task;
import com.pravin.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository){
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
