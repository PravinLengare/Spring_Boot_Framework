package com.example.SSWIthAuthentication.Controller;


import com.example.SSWIthAuthentication.Config.LoginResponse;
import com.example.SSWIthAuthentication.Service.JwtService;
import com.example.SSWIthAuthentication.Service.UserService;
import com.example.SSWIthAuthentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    JwtService jwtService;



    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){

        return new ResponseEntity<>(service.addUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // 1. Attempt Authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            // 2. If we reach here, authentication was successful.
            // Set the context (Optional for stateless JWT, but good practice)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Extract User Details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 4. Generate Token
            // (Ensure your jwtService.generateToken accepts the username string)
            String jwtToken = jwtService.generateToken(user.getUsername());

            // 5. Extract Roles
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // 6. Create and Return Response
            LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException exception) {
            // 7. Handle Bad Credentials
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);

            // Use UNAUTHORIZED (401) instead of NOT_FOUND (404) for login errors
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
    }


//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) throws Exception{
//
//        Authentication authentication = authenticationManager.
//                authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
//
//        if (authentication.isAuthenticated()){
//
//         return new ResponseEntity<>(jwtService.generateToken(user.getUsername()),HttpStatus.OK);
//
//        }
//        else {
//            return new ResponseEntity<>("Failed",HttpStatus.NOT_ACCEPTABLE);
//        }
//
//    }
}
