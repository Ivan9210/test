package com.financiera.dto;

import com.financiera.model.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.*; 

/**
 * DTO used for creating a new transaction via the POST endpoint.
 * Includes necessary validation constraints for financial data integrity.
 */
@Data
public class TransactionCreateRequest {

    @NotBlank(message = "Account ID cannot be empty")
    @Size(min = 10, max = 50, message = "Account ID must be between 10 and 50 characters")
    private String accountId; // Target/Source account ID.

    @NotNull(message = "Transaction type is required")
    private TransactionType type; // CREDIT or DEBIT type.

    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 15, fraction = 4, message = "Amount format is invalid")
    @NotNull(message = "Amount is required")
    private BigDecimal amount; // Monetary amount.

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters (ISO 4217)")
    private String currency; // Currency code.

    private String description; // Optional description.
    
}