package com.financiera.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

/**
 * Utility class for generating, validating, and extracting user information from JWTs.
 */
@Component
public class JwtTokenProvider {

    // Key is loaded from application.properties
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // Token expiration time in milliseconds
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // The key used for signing and verification
    private Key key;

    /**
     * Initializes the signing key after the dependency injection has set the jwtSecret value.
     * This is the clean way to handle initialization of properties loaded via @Value.
     */
    @PostConstruct
    public void init() {
        // Derives a secure key from the secret string provided in application.properties
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token for an authenticated user.
     * @param authentication The Spring Security Authentication object containing user details.
     * @return The generated JWT as a String.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Subject: The principal/username
                .setIssuedAt(now) 
                .setExpiration(expiryDate) 
                .signWith(key, SignatureAlgorithm.HS256) // Sign the token with the secret key and algorithm
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT.
     * @param token The JWT string.
     * @return The username (String).
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) 
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Validates the integrity and expiration of a JWT.
     * @param authToken The JWT string.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // Logs all JWT related exceptions (Security, Malformed, Expired, Unsupported, IllegalArgument)
            // In a real app, we would use a logging framework (e.g., SLF4J) here
            System.err.println("JWT Validation Error: " + e.getMessage());
        }
        return false;
    }
}