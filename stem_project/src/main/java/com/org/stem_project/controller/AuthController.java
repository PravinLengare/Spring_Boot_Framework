package com.org.stem_project.controller;

import com.google.firebase.auth.FirebaseToken;
import com.org.stem_project.model.User;
import com.org.stem_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    // DTO to receive role selection
    public static class RoleRequest {
        public String role;
    }

    // 1. LOGIN CHECK: User logs in, we check if they exist
    @PostMapping("/check-user")
    public ResponseEntity<?> checkUser(Authentication authentication) {
        FirebaseToken token = (FirebaseToken) authentication.getCredentials();
        String email = token.getEmail();

        Optional<User> userOpt = userService.findByEmailT(email);
        if (userOpt.isPresent()){
            User user = userOpt.get();
            if (!"NEW_USER".equals(user.getRole())){
                return  ResponseEntity.ok(Map.of("status","EXISTS","role",user.getRole()));
            }
            return ResponseEntity.ok(Map.of("status","NEW_USER"));
        }
        User user = new User();
        userService.add(user,token);
        return ResponseEntity.ok(Map.of("status", "NEW_USER"));
    }

    // 2. REGISTER ROLE: User clicked a button
    /*
    @PostMapping("/register-role")
    public ResponseEntity<?> registerRole(Authentication authentication, @RequestBody RoleRequest request) {
         FirebaseToken token = (FirebaseToken) authentication.getCredentials();
         String email = token.getEmail();
         User user = userServiceImp.findByEmailT(email).orElseThrow(()->
                 new RuntimeException("User Not Found"));

        if ("STUDENT".equalsIgnoreCase(request.role)) {
            user.setRole("STUDENT");
            userServiceImp.add(user);

            Student student = new Student();
            student.setUser(user);
            student.setCourse("General");
            student.setCgpa(9);
            studentServiceImp.add(student);
            return ResponseEntity.ok(Map.of("message", "Student Registered"));
        }
        else if ("TEACHER".equalsIgnoreCase(request.role)) {

            user.setRole("TEACHER");
            userServiceImp.add(user);

            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setDepartment("IT");
            teacher.setDepartment("Head");
            teacherServiceImp.add(teacher);
            return ResponseEntity.ok(Map.of("message", "Teacher Registered"));
        }

        return ResponseEntity.badRequest().body("Invalid Role");
    }

     */
}