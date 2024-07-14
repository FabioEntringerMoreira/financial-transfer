package com.fabio.financialtransfer.domain.exception;

/**
 * Superclass for technical exceptions in the application.
 * <p>
 * This class serves as the base class for all technical-related exceptions that can occur in the application.
 * Examples of technical exceptions include issues such as database connection failures,
 * communication errors with external services, or invalid input parameters.
 * </p>
 */
public abstract class TechnicalException extends ApplicationException {

    /**
     * Constructor.
     * @param message the detail message explaining the reason for the exception.
     * @param cause the cause of the exception (a null value is permitted, and indicates that the cause is nonexistent or unknown).
     */
    public TechnicalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     * @param message the detail message explaining the reason for the exception.
     */
    public TechnicalException(final String message) {
        super(message);
    }
}
