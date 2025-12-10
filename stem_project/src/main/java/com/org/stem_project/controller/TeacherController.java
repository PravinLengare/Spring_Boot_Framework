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
@RequestMapping("/api/teacher")
public class TeacherController {

    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        FirebaseToken token = (FirebaseToken) authentication.getCredentials();

        Map<String, Object> profile = Map.of(
                "name", token.getName(),
                "email", token.getEmail(),
                "teacherId", "TCH" + token.getUid().substring(0, 8),
                "department", "Computer Science",
                "designation", "Assistant Professor",
                "experience", "5 years"
        );

        return ResponseEntity.ok(profile);
    }


    @GetMapping("/classes")
    public ResponseEntity<?> getMyClasses(Authentication authentication) {
        List<Map<String, Object>> classes = Arrays.asList(
                Map.of(
                        "className", "Data Structures & Algorithms",
                        "code", "CS301",
                        "semester", 3,
                        "students", 45,
                        "schedule", "Mon, Wed, Fri - 10:00 AM"
                ),
                Map.of(
                        "className", "Database Management Systems",
                        "code", "CS402",
                        "semester", 4,
                        "students", 38,
                        "schedule", "Tue, Thu - 2:00 PM"
                ),
                Map.of(
                        "className", "Machine Learning",
                        "code", "CS501",
                        "semester", 5,
                        "students", 30,
                        "schedule", "Mon, Wed - 3:00 PM"
                )
        );

        return ResponseEntity.ok(classes);
    }


    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        List<Map<String, Object>> students = Arrays.asList(
                Map.of(
                        "name", "John Doe",
                        "studentId", "STU001",
                        "class", "CS301",
                        "attendance", 92.5,
                        "grade", "A"
                ),
                Map.of(
                        "name", "Jane Smith",
                        "studentId", "STU002",
                        "class", "CS301",
                        "attendance", 88.0,
                        "grade", "A-"
                ),
                Map.of(
                        "name", "Mike Johnson",
                        "studentId", "STU003",
                        "class", "CS402",
                        "attendance", 95.0,
                        "grade", "A+"
                ),
                Map.of(
                        "name", "Sarah Williams",
                        "studentId", "STU004",
                        "class", "CS501",
                        "attendance", 90.0,
                        "grade", "A"
                )
        );

        return ResponseEntity.ok(students);
    }


    @GetMapping("/pending-assignments")
    public ResponseEntity<?> getPendingAssignments() {
        List<Map<String, Object>> pendingAssignments = Arrays.asList(
                Map.of(
                        "assignmentTitle", "Data Structures Project",
                        "class", "CS301",
                        "submittedBy", 35,
                        "totalStudents", 45,
                        "dueDate", "2025-12-15",
                        "pendingGrading", 10
                ),
                Map.of(
                        "assignmentTitle", "SQL Query Assignment",
                        "class", "CS402",
                        "submittedBy", 38,
                        "totalStudents", 38,
                        "dueDate", "2025-12-10",
                        "pendingGrading", 15
                )
        );

        return ResponseEntity.ok(pendingAssignments);
    }


    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics() {
        Map<String, Object> stats = Map.of(
                "totalClasses", 3,
                "totalStudents", 113,
                "averageAttendance", 91.2,
                "pendingGradingTasks", 25,
                "upcomingLectures", 5
        );

        return ResponseEntity.ok(stats);
    }
}