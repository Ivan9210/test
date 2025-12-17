package com.financiera.service;

import com.financiera.dto.TransactionCreateRequest;
import com.financiera.dto.TransactionResponse;
import com.financiera.dto.TransactionUpdateRequest;

import java.util.List;
import java.util.UUID;

/**
 * Interface defining the business logic operations for Transaction entities.
 * All core application logic should reside here.
 */
public interface TransactionService {

    /**
     * Creates a new financial transaction based on the provided request data.
     * Assigns initial status (e.g., PENDING) and timestamp.
     *
     * @param request The DTO containing the details for the new transaction.
     * @return The response DTO of the newly created transaction.
     */
    TransactionResponse createTransaction(TransactionCreateRequest request);

    /**
     * Retrieves a transaction by its unique ID.
     *
     * @param id The UUID of the transaction to retrieve.
     * @return The response DTO of the found transaction.
     */
    TransactionResponse getTransactionById(UUID id);

    /**
     * Retrieves all transactions available in the system.
     *
     * @return A list of all transaction response DTOs.
     */
    List<TransactionResponse> getAllTransactions();

    /**
     * Updates an existing transaction with the provided data.
     *
     * @param id The UUID of the transaction to update.
     * @param request The DTO containing the updated transaction details.
     * @return The response DTO of the updated transaction.
     */
    TransactionResponse updateTransaction(UUID id, TransactionUpdateRequest request);

    /**
     * Deletes a transaction by its unique ID.
     *
     * @param id The UUID of the transaction to delete.
     */
    void deleteTransaction(UUID id);
    
}