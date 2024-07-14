package com.fabio.financialtransfer.domain.exception;

/**
 * Superclass for handling both technical and business exceptions in the application.
 * This class extends {@link RuntimeException} and provides constructors for creating exceptions
 * with a message and an optional cause.
 */
public abstract class ApplicationException extends RuntimeException {

    /**
     * Constructor.
     * @param message the detail message.
     */
    public ApplicationException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message the detail message.
     * @param cause the cause of the exception.
     */
    public ApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
