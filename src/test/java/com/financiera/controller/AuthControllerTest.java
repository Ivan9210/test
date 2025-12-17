package com.financiera.controller;

import com.financiera.dto.JwtAuthResponseDto;
import com.financiera.dto.LoginDto;
import com.financiera.utils.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthController}.
 * This class tests the authentication flow, including successful login
 * and error handling for invalid credentials.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    private LoginDto loginDto;

    /**
     * Sets up the test data before each test execution.
     */
    @BeforeEach
    void setUp() {
        loginDto = new LoginDto();
        loginDto.setUsername("admin");
        loginDto.setPassword("password123");
    }

    /**
     * Test case for successful user authentication.
     * Verifies that a valid login request returns an OK status and a JWT token.
     */
    @Test
    @DisplayName("Should return JWT Token when credentials are valid")
    void authenticateUser_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String mockToken = "eyJhbGciOiJIUzI1NiJ9.mockToken";

        // Mocking the security principal to avoid NullPointerException during JWT generation
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        // Simulating a successful authentication process
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        
        // Simulating JWT token generation
        when(tokenProvider.generateToken(authentication)).thenReturn(mockToken);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        
        JwtAuthResponseDto authResponse = (JwtAuthResponseDto) response.getBody();
        assertEquals(mockToken, authResponse.getAccessToken(), "The returned token must match the mocked token");
        assertEquals("Bearer", authResponse.getTokenType(), "The token type should be Bearer");

        // Verify that the dependencies were called exactly once
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, times(1)).generateToken(authentication);
    }

    /**
     * Test case for failed user authentication.
     * Verifies that invalid credentials return a 401 UNAUTHORIZED status.
     */
    @Test
    @DisplayName("Should return 401 Unauthorized when credentials are invalid")
    void authenticateUser_InvalidCredentials() {
        // Arrange
        // Simulating the exception thrown by Spring Security when authentication fails
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Response status should be 401 Unauthorized");
        assertTrue(response.getBody().toString().contains("Login failed"), "Response body should contain an error message");
        
        // Verify that the token provider was NEVER called due to authentication failure
        verify(tokenProvider, never()).generateToken(any());
    }
}