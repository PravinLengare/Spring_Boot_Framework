 package com.ecommerce.project.security.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwtCookie}")
    private String  jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request,jwtCookie);
        if (cookie != null){
            return cookie.getValue();
        }else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImp userPrinciple){
        String jwt = generateTokenFromUsername(userPrinciple.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(true)
                .sameSite("Strict")
                .secure(true)
                .build();

        return cookie;
    }

    public ResponseCookie getCleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();

        return cookie;
    }

    // Generating Token from Username
    public String generateTokenFromUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date( (new Date().getTime() + jwtExpirationMs) ))
                .signWith(key())
                .compact();
    }

    // Getting username from Token
    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())           // 1. Set key (Old syntax)
                .build()
                .parseClaimsJws(token)          // 2. Parse JWS
                .getBody()                      // 3. Get Body
                .getSubject();                  // 4. Get Subject
    }

    //Generate Signing Key
    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)
        );
    }

    // Validate Jwt Token
    public boolean validateJwtToken(String authToken){
        try{
            System.out.println("Validate");
            Jwts.parserBuilder()
                    .setSigningKey(key())           // 1. Set the secret key
                    .build()
                    .parseClaimsJws(authToken);

            return true;
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: " + e.getMessage());
        }

        return false;
    }


}
