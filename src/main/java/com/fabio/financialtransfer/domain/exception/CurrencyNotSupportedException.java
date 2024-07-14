package com.fabio.financialtransfer.domain.exception;

/**
 * Exception for unsupported currency errors.
 */
public class CurrencyNotSupportedException extends BusinessException {

    /**
     * Constructor.
     * @param message the detail message.
     */
    public CurrencyNotSupportedException(final String message) {
        super(message);
    }
}
