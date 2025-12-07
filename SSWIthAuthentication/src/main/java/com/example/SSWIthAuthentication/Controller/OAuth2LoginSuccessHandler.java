package com.example.SSWIthAuthentication.Controller;

import com.example.SSWIthAuthentication.Service.JwtService;
import com.example.SSWIthAuthentication.Service.UserService;
import com.example.SSWIthAuthentication.model.User;
import io.jsonwebtoken.Jws;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        User user = userService.findByEmail_(email);
        if (user == null){
            userService.saveGoogleUser(email,name);
        }
        String token = jwtService.generateToken(email);
        response.getWriter().write("Login Successful! Your JWT: " + token);
    }
}
