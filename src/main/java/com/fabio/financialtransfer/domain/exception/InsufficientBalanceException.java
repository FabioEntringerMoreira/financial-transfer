package com.fabio.financialtransfer.domain.exception;

/**
 * Exception thrown when an account has insufficient balance for a transfer.
 */
public class InsufficientBalanceException extends BusinessException {

    /**
     * Constructor.
     * @param message the detail message
     */
    public InsufficientBalanceException(final String message) {
        super(message);
    }
}
