package com.fabio.financialtransfer.domain.util;

import com.fabio.financialtransfer.domain.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing ASTConverterUtilTest class")
class ValidationUtilsTest {

    public static final String INPUT_PARAM = "inputParam";

    @Nested
    @DisplayName("When calling validateNonNull method")
    class ValidateNonNull{

        @Test
        @DisplayName("It should throws a IllegalArgumentException exception when the object is null")
        void validateNonNullThrowsException(){

            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateNonNull(null, INPUT_PARAM)
            );

            assertEquals("The " + INPUT_PARAM + " must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should pass when the object is NOT null")
        void validateNonNullOk(){
            assertDoesNotThrow(() -> ValidationUtils.validateNonNull(new Object(), INPUT_PARAM));
        }
    }

    @Nested
    @DisplayName("When calling validateNonBlank method")
    class ValidateNonBlank {

        @Test
        @DisplayName("It should throw an InvalidParameterException when the string is null")
        void validateNonBlankThrowsExceptionForNullString() {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateNonBlank(null, INPUT_PARAM)
            );

            assertEquals("The " + INPUT_PARAM + " must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should throw an InvalidParameterException when the string is blank")
        void validateNonBlankThrowsExceptionForBlankString() {
            String blankString = "   ";

            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateNonBlank(blankString, INPUT_PARAM)
            );

            assertEquals("The " + INPUT_PARAM + " (" + blankString +  ") must not be blank", exception.getMessage());
        }

        @Test
        @DisplayName("It should pass and return the string when the string is not blank")
        void validateNonBlankOk() {
            String validString = "valid";

            String result = assertDoesNotThrow(() -> ValidationUtils.validateNonBlank(validString, INPUT_PARAM));
            assertEquals(validString, result);
        }
    }

    @Nested
    @DisplayName("When calling validatePositive method")
    class ValidatePositive {

        @Test
        @DisplayName("It should throw an InvalidParameterException when the value is null")
        void validatePositiveThrowsExceptionForNullValue() {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validatePositive(null, INPUT_PARAM)
            );

            assertEquals("The " + INPUT_PARAM + " must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should throw an InvalidParameterException when the value is zero or negative")
        void validatePositiveThrowsExceptionForZeroOrNegativeValue() {
            BigDecimal zeroValue = BigDecimal.ZERO;
            BigDecimal negativeValue = BigDecimal.valueOf(-1);

            InvalidParameterException exceptionZero = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validatePositive(zeroValue, INPUT_PARAM)
            );

            InvalidParameterException exceptionNegative = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validatePositive(negativeValue, INPUT_PARAM)
            );

            assertAll("Validate zero and negative values",
                    () -> assertEquals("The " + INPUT_PARAM + " (" + zeroValue + ") must be positive", exceptionZero.getMessage()),
                    () -> assertEquals("The " + INPUT_PARAM + " (" + negativeValue + ") must be positive", exceptionNegative.getMessage())
            );
        }

        @Test
        @DisplayName("It should pass and return the value when the value is positive")
        void validatePositiveOk() {
            BigDecimal positiveValue = BigDecimal.valueOf(1);

            BigDecimal result = assertDoesNotThrow(() -> ValidationUtils.validatePositive(positiveValue, INPUT_PARAM));

            assertEquals(positiveValue, result);
        }
    }

    @Nested
    @DisplayName("When calling validateNonNegative method")
    class ValidateNonNegative {

        @Test
        @DisplayName("It should throw an InvalidParameterException when the value is null")
        void validateNonNegativeThrowsExceptionForNullValue() {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateNonNegative(null, INPUT_PARAM)
            );

            assertEquals("The " + INPUT_PARAM + " must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should throw an InvalidParameterException when the value is negative")
        void validateNonNegativeThrowsExceptionForNegativeValue() {
            BigDecimal negativeValue = BigDecimal.valueOf(-1);

            InvalidParameterException exceptionNegative = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateNonNegative(negativeValue, INPUT_PARAM)
            );

            assertEquals("The " + INPUT_PARAM + " (" + negativeValue + ") must be zero or positive", exceptionNegative.getMessage());
        }

        @Test
        @DisplayName("It should pass and return the value when the value is zero or positive")
        void validateNonNegativeOk() {
            BigDecimal zeroValue = BigDecimal.ZERO;
            BigDecimal positiveValue = BigDecimal.valueOf(1);

            assertDoesNotThrow(() -> ValidationUtils.validateNonNegative(zeroValue, INPUT_PARAM));
            assertDoesNotThrow(() -> ValidationUtils.validateNonNegative(positiveValue, INPUT_PARAM));
        }
    }

    @Nested
    @DisplayName("When calling validateIdentifier method")
    class ValidateIdentifier {

        @Test
        @DisplayName("It should throw an InvalidParameterException when the value is null")
        void validateIdentifierThrowsExceptionForNullValue() {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateIdentifier(null, INPUT_PARAM)
            );

            assertEquals("The " + INPUT_PARAM + " must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should throw an InvalidParameterException when the value is zero or negative")
        void validateIdentifierThrowsExceptionForZeroOrNegativeValue() {
            Long zeroValue = 0L;
            Long negativeValue = -1L;

            InvalidParameterException exceptionZero = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateIdentifier(zeroValue, INPUT_PARAM)
            );

            InvalidParameterException exceptionNegative = assertThrows(InvalidParameterException.class, () ->
                    ValidationUtils.validateIdentifier(negativeValue, INPUT_PARAM)
            );

            assertAll("Validate zero and negative values",
                    () -> assertEquals("The " + INPUT_PARAM + " (" + zeroValue + ") must be positive", exceptionZero.getMessage()),
                    () -> assertEquals("The " + INPUT_PARAM + " (" + negativeValue + ") must be positive", exceptionNegative.getMessage())
            );
        }

        @Test
        @DisplayName("It should pass and return the value when the value is positive")
        void validateIdentifierOk() {
            Long positiveValue = 1L;

            Long result = assertDoesNotThrow(() -> ValidationUtils.validateIdentifier(positiveValue, INPUT_PARAM));
            assertEquals(positiveValue, result);
        }
    }
}