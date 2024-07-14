package com.fabio.financialtransfer.domain.exception;

/**
 * Superclass for business exceptions in the application.
 */
public abstract class BusinessException extends ApplicationException {

    /**
     * Constructor.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public BusinessException(final String message) {
        super(message);
    }
}
