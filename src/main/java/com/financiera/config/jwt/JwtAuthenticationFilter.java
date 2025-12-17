package com.financiera.config.jwt;

import com.financiera.utils.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter executed once per request to process and validate the JWT token 
 * from the HTTP Authorization header.
 * * <p>This filter intercepts incoming requests, extracts the Bearer token, 
 * validates it using {@link JwtTokenProvider}, and populates the 
 * {@link SecurityContextHolder} with the user's authentication details.</p>
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for JwtAuthenticationFilter using dependency injection.
     * * @param tokenProvider the utility class for JWT generation and validation
     * @param userDetailsService the service to load user-specific data
     */
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Determines whether the filter should be skipped for specific URIs.
     * * @param request the current HTTP request
     * @return true if the URI starts with /api/auth, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Skip JWT validation for authentication endpoints (login/register)
        return request.getRequestURI().startsWith("/api/auth");
    }

    /**
     * Core filter logic to intercept requests and establish the Security Context.
     * * @param request the servlet request
     * @param response the servlet response
     * @param filterChain the chain of subsequent filters
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract token from the "Authorization" header
        String jwt = getJwtFromRequest(request);

        // Check if token exists and is cryptographically valid
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

            // Retrieve username from the token claims
            String username = tokenProvider.getUsernameFromJWT(jwt);
            
            // Log successful validation for debugging purposes
            log.debug(">>> JWT validated: Token OK for user: " + username);

            // Load user details including roles/authorities from the database (simulated)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Create an authentication object with userDetails and their granted authorities
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Password is not needed after token validation
                            userDetails.getAuthorities()
                    );

            // Attach extra request details (IP address, Session ID) to the authentication object
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // Establish the security context for the current request thread
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue with the next filter in the Spring Security filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to extract the Bearer token from the "Authorization" header.
     * * @param request the HTTP request
     * @return the JWT token string if found, otherwise null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        // JWT tokens are standardly prefixed with "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}