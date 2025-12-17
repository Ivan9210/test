package com.financiera.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to indicate that a requested resource (e.g., a Transaction) 
 * could not be found in the system.
 * <p>The {@link ResponseStatus} annotation ensures that whenever this exception 
 * is thrown and not caught, Spring returns an HTTP 404 Not Found status.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND) 
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Unique identifier for serialization. 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with a specific error message.
     * * @param message The detail message describing which resource was not found.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}