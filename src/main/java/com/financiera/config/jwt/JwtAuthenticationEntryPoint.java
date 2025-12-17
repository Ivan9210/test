package com.financiera.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Component that handles unauthorized access attempts.
 * <p>This class is responsible for sending a 401 Unauthorized response when
 * a user tries to access a protected resource without valid credentials.</p>
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Triggered whenever an {@link AuthenticationException} is thrown during the filter chain execution.
     * <p>This usually happens if the JWT is missing, expired, or has an invalid signature.</p>
     * * @param request The request that resulted in an AuthenticationException.
     * @param response The response to be sent back to the client.
     * @param authException The exception that caused the unauthorized access.
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        // Logs the exception message to the standard error stream for server-side debugging
    	if (!request.getRequestURI().equals("/error")) {
    	    log.warn("Unauthorized error. Message - " + authException.getMessage());
    	}
        
        // Return an HTTP 401 Unauthorized status with a custom error message
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied: You must provide a valid JWT Token.");
    }
}