package com.example.SSWIthAuthentication.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //@GetMapping("/")
    public String greet(){
        return "redirect:/login";
    }

//    @GetMapping("/about")
//    public String about(HttpServletRequest request){
//        return "Pravin Lengare "+request.getSession().getId();
//    }

    @GetMapping("/about")
    public String about(HttpServletRequest request){
        return "Pravin Lengare ";
    }
}
