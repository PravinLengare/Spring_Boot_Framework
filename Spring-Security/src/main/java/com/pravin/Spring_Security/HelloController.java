package com.pravin.Spring_Security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;

@RestController
public class HelloController {

    @GetMapping("hello")
    public String greet(HttpServletRequest request){
        return "Hello World "+request.getSession().getId();
    }

    @GetMapping("about")
    public String about(HttpServletRequest request){
        return "Pravin Lengare "+request.getSession().getId();
    }
}
