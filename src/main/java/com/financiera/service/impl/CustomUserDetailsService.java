package com.financiera.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}.
 * <p>This service is used by the authentication manager and the JWT filter to 
 * load user-specific data. It translates the application's user data into 
 * a {@link UserDetails} object that Spring Security can understand.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Locates the user based on the username.
     * <p>In this implementation, user data is simulated in memory. In a production 
     * environment, this method should call a persistence layer (e.g., UserRepository).</p>
     * * @param username the username identifying the user whose data is required.
     * @return a fully populated {@link UserDetails} object including credentials and authorities.
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // --- USER LOAD SIMULATION IN MEMORY ---
        // Note: In production, replace this with: 
        // UserEntity user = userRepository.findByUsername(username).orElseThrow(...)
    	
    	// Define the roles (Authorities)
        // Spring Security requires roles to be prefixed with "ROLE_" by default
        List<SimpleGrantedAuthority> adminAuthorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN"), 
                new SimpleGrantedAuthority("ROLE_USER")
        );
        
        List<SimpleGrantedAuthority> userAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        // Logic to simulate different users and their respective permissions
        if ("admin".equals(username)) {
            // Simulated user: "admin"
            // Password: "$2a$10$xUTGYeub6CqULQLS76J2kubBZ2pzC.QJk5UDARW7dJalkUn8Jlaxm" (BCrypt for "password")
            return new User(
                    "admin", 
                    "$2a$10$xUTGYeub6CqULQLS76J2kubBZ2pzC.QJk5UDARW7dJalkUn8Jlaxm", 
                    adminAuthorities
            );
        } else if ("user".equals(username)) {
            // Simulated user: "user"
             return new User(
                     "user", 
                     "$2a$10$xUTGYeub6CqULQLS76J2kubBZ2pzC.QJk5UDARW7dJalkUn8Jlaxm", 
                     userAuthorities
             );
        }
        
        // If no user matches the provided username, throw standard Security exception
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}