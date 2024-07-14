package com.fabio.financialtransfer.domain.util;

import com.fabio.financialtransfer.domain.exception.InvalidParameterException;

import java.math.BigDecimal;

/**
 * Utility class for validating input parameters.
 * This class provides static methods to validate various types of inputs, such as non-null values, non-blank strings,
 * positive and non-negative numbers, and identifiers.
 * <p>
 * All methods in this class throw {@link InvalidParameterException} if the validation fails.
 * </p>
 */
public final class ValidationUtils {

    /**
     * Private constructor to prevent instantiation
     */
    private ValidationUtils() {}

    /**
     * Validates that the specified object is not null.
     * @param obj the object to validate
     * @param paramName the name of the parameter to include in the error message if validation fails
     * @param <T> the type of the object
     * @throws InvalidParameterException if the object is null
     */
    public static <T> void validateNonNull(final T obj, final String paramName) {
        if (obj == null) {
            throw new InvalidParameterException(paramName, "must not be null");
        }
    }

    /**
     * Validates that the specified string is not null or blank.
     * @param str the string to validate
     * @param paramName the name of the parameter to include in the error message if validation fails
     * @return the validated string
     * @throws InvalidParameterException if the string is null or blank
     */
    public static String validateNonBlank(final String str, final String paramName) {
        validateNonNull(str, paramName);
        if (str.trim().isEmpty()) {
            throw new InvalidParameterException(paramName, str, "must not be blank");
        }

        return str;
    }

    /**
     * Validates that the specified BigDecimal value is positive.
     * @param value the value to validate
     * @param paramName the name of the parameter to include in the error message if validation fails
     * @return the validated value
     * @throws InvalidParameterException if the value is null or not positive
     */
    public static BigDecimal validatePositive(final BigDecimal value, final String paramName) {
        validateNonNull(value, paramName);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidParameterException(paramName, value, "must be positive");
        }

        return value;
    }

    /**
     * Validates that the specified BigDecimal value is not negative.
     * @param value the value to validate
     * @param paramName the name of the parameter to include in the error message if validation fails
     * @throws InvalidParameterException if the value is null or negative
     */
    public static void validateNonNegative(final BigDecimal value, final String paramName) {
        validateNonNull(value, paramName);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException(paramName, value, "must be zero or positive");
        }
    }

    /**
     * Validates that the specified Long identifier is positive.
     * @param value the identifier to validate
     * @param paramName the name of the parameter to include in the error message if validation fails
     * @return the validated identifier
     * @throws InvalidParameterException if the identifier is null or not positive
     */
    public static Long validateIdentifier(final Long value, final String paramName) {
        validateNonNull(value, paramName);
        if (value.compareTo(0L) <= 0) {
            throw new InvalidParameterException(paramName, value, "must be positive");
        }

        return value;
    }
}


