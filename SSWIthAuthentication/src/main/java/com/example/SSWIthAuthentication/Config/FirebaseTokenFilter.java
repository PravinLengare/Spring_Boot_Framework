package com.example.SSWIthAuthentication.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

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

            // Authenticate
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, decodedToken, new ArrayList<>());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (FirebaseAuthException e) {
            // DEBUG 3: Why did it fail?
            System.out.println(" Filter: Firebase Verification Failed! Error: " + e.getMessage());
            // NOTE: We do NOT throw exception here, we just don't set the authentication.
            // Spring Security will handle the 403 downstream.
        }

        filterChain.doFilter(request, response);
    }
}