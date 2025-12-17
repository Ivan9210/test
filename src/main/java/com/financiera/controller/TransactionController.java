package com.financiera.controller;

import com.financiera.dto.TransactionCreateRequest;
import com.financiera.dto.TransactionResponse;
import com.financiera.dto.TransactionUpdateRequest;
import com.financiera.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing financial transactions.
 * Provides standard CRUD endpoints for the Transaction resource.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructor for dependency injection of the service layer.
     * @param transactionService The business logic service for transactions.
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /api/v1/transactions
     * Creates a new financial transaction. Requires authentication.
     *
     * @param request The validated DTO containing the transaction details.
     * @return A ResponseEntity with the created transaction and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionCreateRequest request) {
        // The @Valid annotation triggers the bean validation defined in the DTO
    	
    	log.info("REST request to create transaction of type: {} for Account ID: {}", 
                request.getType(), request.getAccountId());
    	
        TransactionResponse response = transactionService.createTransaction(request);
        
        log.info("Transaction created successfully with ID: {}", response.getTransactionId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/transactions/{id}
     * Retrieves a specific transaction by its UUID. Requires authentication.
     *
     * @param id The UUID of the transaction to retrieve.
     * @return A ResponseEntity with the transaction data and HTTP status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID id) {
    	log.debug("REST request to get transaction by ID: {}", id);
    	
        TransactionResponse response = transactionService.getTransactionById(id);
        
        log.debug("Found transaction: {}", response.getTransactionId());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/transactions
     * Retrieves a list of all transactions. Requires authentication.
     *
     * @return A ResponseEntity with the list of transactions and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
    	log.info("REST request to fetch all transactions");
    	
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        
        log.info("Total transactions retrieved: {}", transactions.size());
        return ResponseEntity.ok(transactions);
    }

    /**
     * PUT /api/v1/transactions/{id}
     * Updates an existing transaction (e.g., status or description). Requires authentication.
     *
     * @param id The UUID of the transaction to update.
     * @param request The DTO containing the fields to update.
     * @return A ResponseEntity with the updated transaction and HTTP status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable UUID id, 
                                                               @Valid @RequestBody TransactionUpdateRequest request) {
    	log.info("REST request to update transaction ID: {}", id);
        TransactionResponse response = transactionService.updateTransaction(id, request);
        
        log.info("Transaction ID: {} updated successfully", id);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/transactions/{id}
     * Deletes a transaction by its UUID. Requires authentication.
     *
     * @param id The UUID of the transaction to delete.
     * @return A ResponseEntity with no content and HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
    	log.warn("REST request to delete transaction ID: {}", id);
        transactionService.deleteTransaction(id);
        
        log.info("Transaction ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}