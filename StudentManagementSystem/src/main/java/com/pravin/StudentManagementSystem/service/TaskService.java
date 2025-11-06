//package com.pravin.StudentManagementSystem.service;
//
//import com.pravin.StudentManagementSystem.repository.StudentRepo;
//import org.aspectj.lang.annotation.After;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TaskService {
//    @Autowired
//    private StudentRepo studentRepo;
//    public int getCompletedCount() {
//
//        // --- FOR A REAL APP ---
//        // You would write a database query, for example:
//        // return taskRepository.countByStatus("COMPLETED");
//        // ------------------------
//
//        // For now, you can just return a test number
//        // to make sure it's working.
//        return studentRepo.countByStatus("COMPLETED");
//    }
//}
