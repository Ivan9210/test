package com.financiera.controller;

import com.financiera.dto.JwtAuthResponseDto;
import com.financiera.dto.LoginDto;
import com.financiera.utils.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication requests (login).
 * This endpoint is publicly accessible as per SecurityConfig.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    // Dependency injection
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * POST /api/auth/login
     * Authenticates the user and returns a JWT token if successful.
     *
     * @param loginDto The DTO containing username and password.
     * @return A response DTO containing the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
    	log.info("Authentication attempt for user: {}", loginDto.getUsername());
    	
    	try {
	        // 1. Authenticate the user using the credentials
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginDto.getUsername(),
	                        loginDto.getPassword()
	                )
	        );
	
	        // 2. Set the authentication object in the security context (optional for API, but good practice)
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	
	        // 3. Generate the JWT token
	        String token = tokenProvider.generateToken(authentication);
	        
	        log.info("User '{}' successfully authenticated", loginDto.getUsername());
	        
	        // 4. Return the token to the client
	        return ResponseEntity.ok(new JwtAuthResponseDto(token));
    	} catch (AuthenticationException e) {
    		log.warn("Authentication failed for user: {}. Reason: {}", loginDto.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Invalid username or password");
        }
    }
}