package com.org.stem_project.controller;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {


    @GetMapping("/grades")
    public ResponseEntity<?> getMyGrades(){
        List<Map<String, Object>> grades = Arrays.asList(
                Map.of("subject", "Mathematics", "grade", "A", "score", 95),
                Map.of("subject", "Physics", "grade", "B+", "score", 88),
                Map.of("subject", "Computer Science", "grade", "A+", "score", 99)
        );

        return ResponseEntity.ok(grades);
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        FirebaseToken token = (FirebaseToken) authentication.getCredentials();

        Map<String, Object> profile = Map.of(
                "name", token.getName(),
                "email", token.getEmail(),
                "studentId", "STU" + token.getUid().substring(0, 8),
                "course", "Computer Science",
                "semester", 6,
                "cgpa", 8.5
        );

        return ResponseEntity.ok(profile);
    }


    @GetMapping("/attendance")
    public ResponseEntity<?> getMyAttendance() {
        List<Map<String, Object>> attendance = Arrays.asList(
                Map.of("subject", "Mathematics", "present", 28, "total", 30, "percentage", 93.3),
                Map.of("subject", "Physics", "present", 25, "total", 30, "percentage", 83.3),
                Map.of("subject", "Computer Science", "present", 30, "total", 30, "percentage", 100.0)
        );

        return ResponseEntity.ok(attendance);
    }

}
