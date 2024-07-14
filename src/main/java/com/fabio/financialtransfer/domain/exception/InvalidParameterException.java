package com.fabio.financialtransfer.domain.exception;

/**
 * Exception for invalid parameter errors.
 */
public class InvalidParameterException extends TechnicalException {

    /**
     * Constructor.
     * @param paramName the name of the invalid parameter.
     * @param message the detail message explaining why the parameter is invalid.
     */
    public InvalidParameterException(final String paramName, final String message) {
        super("The " + paramName + " " + message);
    }

    /**
     * Constructor.
     * @param paramName the name of the invalid parameter.
     * @param paramValue the value of the invalid parameter.
     * @param message the detail message explaining why the parameter is invalid.
     */
    public InvalidParameterException(final String paramName, final Object paramValue, final String message) {
        super("The " + paramName + " (" + paramValue + ") " + message);
    }
}
