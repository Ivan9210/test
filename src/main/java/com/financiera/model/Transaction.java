package com.financiera.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a financial transaction entity stored in the database.
 * Uses BigDecimal for precise monetary representation.
 */
@Entity
@Table(name = "transactions", schema = "skd_financiera")
@Data // Lombok: Generates getters, setters, toString, equals, and hashCode.
@NoArgsConstructor // Lombok: Generates a no-argument constructor.
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId; // Primary key using UUID for global uniqueness.

    @Column(nullable = false, length = 50)
    private String accountId; // The related account ID (e.g., IBAN or internal identifier).

    @Enumerated(EnumType.STRING) // Stores the Enum name (e.g., "DEBIT") in the database.
    @Column(nullable = false)
    private TransactionType type; // Type of transaction: DEBIT or CREDIT.

    /**
     * CRUCIAL: Uses BigDecimal for monetary amounts to prevent floating-point errors.
     * Precision (19 total digits) and scale (4 digits after decimal) are specified.
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount; 

    @Column(nullable = false, length = 3)
    private String currency; // ISO 4217 currency code (e.g., "USD", "EUR").

    @Column(length = 255)
    private String description; // Short description of the transaction purpose.

    @Column(nullable = false)
    private Instant timestamp; // Precise timestamp of when the transaction occurred or was created.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status; // Current state of the transaction (PENDING, COMPLETED, FAILED).

}