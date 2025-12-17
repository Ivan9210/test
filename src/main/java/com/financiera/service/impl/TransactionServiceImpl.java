package com.financiera.service.impl;

import com.financiera.dto.TransactionCreateRequest;
import com.financiera.dto.TransactionResponse;
import com.financiera.dto.TransactionUpdateRequest;
import com.financiera.exception.ResourceNotFoundException; 
import com.financiera.model.Transaction;
import com.financiera.model.TransactionStatus;
import com.financiera.repository.TransactionRepository;
import com.financiera.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the TransactionService interface.
 * Contains the core business logic for transaction management.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
    private final TransactionRepository transactionRepository;

    /**
     * Constructor for Dependency Injection.
     * @param transactionRepository Repository for transaction data access.
     */
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional // Ensures the operation is atomic
    public TransactionResponse createTransaction(TransactionCreateRequest request) {
        // 1. Convert DTO to Entity
        Transaction transaction = new Transaction();
        transaction.setAccountId(request.getAccountId());
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setDescription(request.getDescription()); 

        // 2. Apply Financial Business Rules: Set system-controlled values
        transaction.setTimestamp(Instant.now());
        // All new transactions start as PENDING
        transaction.setStatus(TransactionStatus.PENDING); 

        // 3. Save to Database
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 4. Convert Entity back to Response DTO
        return mapToResponseDTO(savedTransaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionResponse getTransactionById(UUID id) {
        // Find the transaction or throw a 404 (handled by ResourceNotFoundException)
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
        
        return mapToResponseDTO(transaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransactionResponse> getAllTransactions() {
        // Use Java 8 Stream API (Collectors) to map the list of Entities to DTOs
        return transactionRepository.findAll().stream()
                .map(this::mapToResponseDTO) // Method reference usage
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransactionResponse updateTransaction(UUID id, TransactionUpdateRequest request) {
        // 1. Find the existing entity
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));

        // 2. Apply updates for allowed fields
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            transaction.setStatus(request.getStatus());
        }

        // 3. Save and return
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponseDTO(updatedTransaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTransaction(UUID id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + id);
        }
        transactionRepository.deleteById(id);
    }

    /**
     * Helper method to map a Transaction Entity to a TransactionResponse DTO.
     * @param transaction The Transaction Entity.
     * @return The corresponding TransactionResponse DTO.
     */
    private TransactionResponse mapToResponseDTO(Transaction transaction) {
        TransactionResponse dto = new TransactionResponse();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setAccountId(transaction.getAccountId());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setDescription(transaction.getDescription());
        dto.setTimestamp(transaction.getTimestamp());
        dto.setStatus(transaction.getStatus());
        return dto;
    }
}