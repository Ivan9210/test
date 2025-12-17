package com.financiera.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * <p>
 * This class configures the Swagger UI to support JWT Bearer Token authentication
 * and provides metadata about the Financial API.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Defines the OpenAPI bean that handles the global API documentation.
     * * @return a configured {@link OpenAPI} instance.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // Name used to link the security scheme with the security requirement
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // API Metadata
                .info(new Info()
                        .title("Financial Transactions API")
                        .version("1.0.0")
                        .description("Microservice for managing internal financial transactions. " +
                                     "All endpoints (except authentication) require a valid JWT token.")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@financiera.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                
                // Add a global security requirement so every endpoint shows the lock icon
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                
                // Define the security scheme (JWT Bearer)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token in the format: {token}")));
    }
}