package com.financiera.dto;

import com.financiera.model.TransactionStatus;
import lombok.Data;
import jakarta.validation.constraints.Size;

/**
 * DTO used for updating an existing transaction (e.g., status or description).
 */
@Data
public class TransactionUpdateRequest {
    
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description; // Description is allowed to be updated.

    private TransactionStatus status; // Status transition update.
    
}