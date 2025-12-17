package com.financiera.repository;

import com.financiera.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Transaction entities.
 * Extends JpaRepository to provide standard CRUD operations, 
 * leveraging Spring Data JPA's power.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Finds all transactions associated with a specific account ID.
     * This is an example of a derived query method provided by Spring Data JPA.
     *
     * @param accountId The ID of the account to search for.
     * @return A list of transactions linked to the given account ID.
     */
    List<Transaction> findByAccountId(String accountId);
    
}