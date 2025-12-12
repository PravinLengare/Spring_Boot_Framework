package com.org.stem_project.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.org.stem_project.model.User;
import com.org.stem_project.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public static class LoginRequest {
        public String idToken;
    }

    // 1. LOGIN CHECK: User logs in, we check if they exist
    @PostMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestBody LoginRequest request, HttpServletResponse response) throws FirebaseAuthException {
       try {
           FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(request.idToken);
           String email = token.getEmail();

           ResponseCookie cookie = ResponseCookie.from("session_token", request.idToken)
                   .path("/")
                   .httpOnly(true)
                   .secure(false)
                   .maxAge(3600)
                   .build();

           response.addHeader("Set-Cookie", cookie.toString());

           Optional<User> userOpt = userService.findByEmailT(email);
           if (userOpt.isPresent()) {
               return ResponseEntity.ok(Map.of("status", "EXISTS", "role", userOpt.get().getRole()));
           }
           User user = new User();
           userService.add(user, token);
           return ResponseEntity.ok(Map.of("status", "NEW_USER"));

       }
       catch (FirebaseAuthException e){
           return ResponseEntity.status(401).body("Invalid Token");
       }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from("session_token","")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        System.out.println("after logout "+cookie.getValue());
        return ResponseEntity.ok("Logged out!");
    }
}