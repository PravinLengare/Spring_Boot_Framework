package com.org.stem_project.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.org.stem_project.repository.StudentRepo;
import com.org.stem_project.repository.TeacherRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private TeacherRepo teacherRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // DEBUG 1: Did we get a header?
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("Filter: No Bearer Token found in header");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            // DEBUG 2: Verifying...
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String email = decodedToken.getEmail();

            System.out.println("Filter: Token Valid for email: " + email);

            List<GrantedAuthority> authorities = new ArrayList<>();

            if (studentRepo.findByEmail(email).isPresent()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
            }
            else if (teacherRepo.findByEmail(email).isPresent()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
            }
            else {
                authorities.add(new SimpleGrantedAuthority("ROLE_NEW_USER"));
            }

            // Authenticate
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, decodedToken, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (FirebaseAuthException e) {
            System.out.println(" Filter: Firebase Verification Failed! Error: " + e.getMessage());

        }

        filterChain.doFilter(request, response);
    }
}