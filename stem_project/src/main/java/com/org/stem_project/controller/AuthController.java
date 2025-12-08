package com.org.stem_project.controller;

import com.google.firebase.auth.FirebaseToken;
import com.org.stem_project.model.Student;
import com.org.stem_project.model.Teacher;
import com.org.stem_project.service.StudentService;
import com.org.stem_project.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    // DTO to receive role selection
    public static class RoleRequest {
        public String role; // "STUDENT" or "TEACHER"
    }

    // 1. LOGIN CHECK: User logs in, we check if they exist
    @PostMapping("/check-user")
    public ResponseEntity<?> checkUser(Authentication authentication) {
        FirebaseToken token = (FirebaseToken) authentication.getCredentials();
        String email = token.getEmail();

        if (studentService.findByEmailS(email).isPresent()) {
            return ResponseEntity.ok(Map.of("status", "EXISTS", "role", "STUDENT"));
        }
        if (teacherService.findByEmailT(email).isPresent()) {
            return ResponseEntity.ok(Map.of("status", "EXISTS", "role", "TEACHER"));
        }

        // User is new! Tell frontend to show buttons
        return ResponseEntity.ok(Map.of("status", "NEW_USER"));
    }

    // 2. REGISTER ROLE: User clicked a button
    @PostMapping("/register-role")
    public ResponseEntity<?> registerRole(Authentication authentication, @RequestBody RoleRequest request) {
         FirebaseToken token = (FirebaseToken) authentication.getCredentials();

        if ("STUDENT".equalsIgnoreCase(request.role)) {
            Student s = new Student();
            s.setEmail(token.getEmail());
            s.setName(token.getName());
            s.setFirebaseUid(token.getUid());
            s.setCourse("Not assigned ");
            studentService.add(s);
            return ResponseEntity.ok(Map.of("message", "Student Registered"));
        }
        else if ("TEACHER".equalsIgnoreCase(request.role)) {
            Teacher t = new Teacher();
            t.setEmail(token.getEmail());
            t.setName(token.getName());
            t.setFirebaseUid(token.getUid());
            t.setDepartment("General");
            teacherService.add(t);
            return ResponseEntity.ok(Map.of("message", "Teacher Registered"));
        }

        return ResponseEntity.badRequest().body("Invalid Role");
    }
}