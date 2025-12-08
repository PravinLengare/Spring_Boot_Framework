package com.org.stem_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class FirebaseController {
    @GetMapping("/api/profile")
    public String getProfile(Principal principal) {
        return "Hello, your email is: " + principal.getName();
    }
}
