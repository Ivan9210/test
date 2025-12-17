package com.financiera.service.impl;

import com.financiera.dto.TransactionCreateRequest;
import com.financiera.dto.TransactionResponse;
import com.financiera.dto.TransactionUpdateRequest;
import com.financiera.model.Transaction;
import com.financiera.model.TransactionStatus;
import com.financiera.model.TransactionType;
import com.financiera.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransactionServiceImpl.
 * Validates business logic using Mockito to isolate the persistence layer.
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Test Create Transaction - Success")
    void createTransaction_Success() {
        // Arrange
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAccountId("ACC123456789");
        request.setType(TransactionType.CREDIT);
        request.setAmount(new BigDecimal("1500.50"));
        request.setCurrency("USD");

        Transaction savedTransaction = new Transaction();
        // Use setTransactionId instead of setId
        savedTransaction.setTransactionId(UUID.randomUUID()); 
        savedTransaction.setAccountId(request.getAccountId());
        savedTransaction.setAmount(request.getAmount());
        savedTransaction.setStatus(TransactionStatus.PENDING);
        savedTransaction.setTimestamp(Instant.now());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        TransactionResponse response = transactionService.createTransaction(request);

        // Assert
        assertNotNull(response);
        assertEquals(savedTransaction.getTransactionId(), response.getTransactionId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Test Get Transaction By ID - Success")
    void getTransactionById_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction();
        transaction.setTransactionId(id);
        transaction.setAmount(new BigDecimal("500.00"));

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        // Act
        TransactionResponse response = transactionService.getTransactionById(id);

        // Assert
        assertNotNull(response);
        assertEquals(id, response.getTransactionId());
        verify(transactionRepository).findById(id);
    }

    @Test
    @DisplayName("Test Update Transaction Status - Success")
    void updateTransaction_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest();
        updateRequest.setStatus(TransactionStatus.COMPLETED);
        updateRequest.setDescription("Payment processed");

        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(id);
        existingTransaction.setStatus(TransactionStatus.PENDING);

        when(transactionRepository.findById(id)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(existingTransaction);

        // Act
        TransactionResponse response = transactionService.updateTransaction(id, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TransactionStatus.COMPLETED, response.getStatus());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Test Delete Transaction - Success")
    void deleteTransaction_Success() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(transactionRepository.existsById(id)).thenReturn(true);
        doNothing().when(transactionRepository).deleteById(id);

        // Act
        transactionService.deleteTransaction(id);

        // Assert
        verify(transactionRepository).deleteById(id);
    }
}