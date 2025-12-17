package com.financiera.config;

import com.financiera.config.jwt.JwtAuthenticationEntryPoint;
import com.financiera.config.jwt.JwtAuthenticationFilter;
import com.financiera.service.impl.CustomUserDetailsService; 
import com.financiera.utils.JwtTokenProvider; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; 
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main configuration class for Spring Security (Stateless/JWT).
 * <p>This class defines the security filter chain, authorization rules, and 
 * infrastructure beans required to secure the application using JWT.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) 
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider tokenProvider; 

    /**
     * Constructor injection for security-related dependencies.
     * * @param authenticationEntryPoint Custom handler for unauthorized access attempts.
     * @param customUserDetailsService Custom implementation to load user data.
     * @param tokenProvider Utility class for handling JWT operations.
     */
    public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint,
                          CustomUserDetailsService customUserDetailsService,
                          JwtTokenProvider tokenProvider) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Bean for creating the JWT authentication filter instance.
     * * @return An instance of {@link JwtAuthenticationFilter}.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    /**
     * Defines the password hashing strategy using BCrypt.
     * * @return A {@link PasswordEncoder} bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the authentication provider using CustomUserDetailsService.
     * This provider links the user loading logic with the password encoder.
     * * @return A configured {@link DaoAuthenticationProvider}.
     */
//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	} This version is for Spring 4.0
    
    /**
     * Defines the authentication provider using a custom {@link UserDetailsService}.
     * It links user loading logic with the configured {@link PasswordEncoder}.
     *
     * @return a configured {@link DaoAuthenticationProvider}
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Exposes the AuthenticationManager bean from the global configuration.
     * * @param authenticationConfiguration Spring's internal authentication configuration.
     * @return The {@link AuthenticationManager} bean.
     * @throws Exception If there is an error retrieving the manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Defines the security filter chain and HTTP request authorization rules.
     * * @param http The {@link HttpSecurity} object to configure.
     * @return The built {@link SecurityFilterChain}.
     * @throws Exception If an error occurs during the security setup.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF protection since JWT is used in a stateless architecture
            .csrf(csrf -> csrf.disable())
            
            // Disable default HTTP Basic authentication
            .httpBasic(httpBasic -> httpBasic.disable())

            // Configure the custom entry point for authentication exceptions
            .exceptionHandling(exceptions ->
                exceptions.authenticationEntryPoint(authenticationEntryPoint)
            )

            // Set session management to STATELESS to prevent JSESSIONID generation
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Set the custom authentication provider
            .authenticationProvider(authenticationProvider())

            // Request authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow public access to the login endpoint
                .requestMatchers("/api/v1/auth/login",
		                		 "/v3/api-docs/**",
		                	     "/swagger-ui/**",
		                	     "/swagger-ui.html",
		                	     "/swagger-resources/**",
		                	     "/webjars/**").permitAll()
                // All other requests require a valid authentication token
                .anyRequest().authenticated()
            );

        // 

        // Inject the JWT Authentication Filter before the standard UsernamePasswordAuthenticationFilter
        http.addFilterBefore(
            jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}