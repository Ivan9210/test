package com.financiera.model;

/**
 * Enumeration for the current processing status of a transaction.
 */
public enum TransactionStatus {
	
    /** Transaction has been initiated but not yet finalized. */
    PENDING, 
    /** Transaction processing has been successfully completed. */
    COMPLETED,
    /** Transaction failed during processing. */
    FAILED
    
}