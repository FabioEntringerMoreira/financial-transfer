package com.fabio.financialtransfer.domain.exception;

/**
 * Exception for currency conversion errors.
 */
public class CurrencyConversionException extends TechnicalException {

    /**
     * Constructor.
     * @param message the detail message.
     * @param cause the cause of the exception.
     */
    public CurrencyConversionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
