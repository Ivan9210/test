package com.financiera.dto;

import lombok.Data;

/**
 * DTO for the authentication response, containing the JWT token.
 */
@Data
public class JwtAuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}