package com.fabio.financialtransfer.domain.exception;

/**
 * Exception thrown when an object is not found on database.
 */
public class ObjectNotFoundException extends BusinessException {

    /**
     * Constructor.
     * @param message the detail message.
     */
    public ObjectNotFoundException(final String message) {
        super(message);
    }
}