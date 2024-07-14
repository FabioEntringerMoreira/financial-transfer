package com.fabio.financialtransfer.domain.validation;

import com.fabio.financialtransfer.domain.exception.InsufficientBalanceException;
import com.fabio.financialtransfer.domain.exception.InvalidParameterException;
import com.fabio.financialtransfer.domain.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing AccountValidator class")
class AccountValidatorTest {

    @Nested
    @DisplayName("When calling validateSufficientBalance method")
    class ValidateSufficientBalance {

        private static Stream<Arguments> invalidParameterExceptionProvider() {
            return Stream.of(
                    Arguments.of(null, BigDecimal.valueOf(100), "The balance must not be null"),
                    Arguments.of(BigDecimal.valueOf(100), null, "The amount must not be null"),
                    Arguments.of(BigDecimal.valueOf(100), BigDecimal.valueOf(-1), "The amount (-1) must be zero or positive")
            );
        }

        @ParameterizedTest(name = "({index}) => balance={0}, amount={1}, message={2}")
        @MethodSource("invalidParameterExceptionProvider")
        @DisplayName("It should throw InvalidParameterException for invalid inputs")
        void validateSufficientBalanceThrowsInvalidParameterException(BigDecimal balance, BigDecimal amount, String message) {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    AccountValidator.validateSufficientBalance(balance, amount)
            );

            assertAll("Validate InvalidParameterException",
                    () -> assertNotNull(exception),
                    () -> assertEquals(message, exception.getMessage())
            );
        }

        @Test
        @DisplayName("It should throw CustomException for insufficient balance")
        void validateSufficientBalanceThrowsCustomExceptionForInsufficientBalance() {
            BigDecimal balance = BigDecimal.valueOf(50);
            BigDecimal amount = BigDecimal.valueOf(100);

            InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () ->
                    AccountValidator.validateSufficientBalance(balance, amount)
            );

            assertAll("Validate CustomException",
                    () -> assertNotNull(exception),
                    () -> assertEquals(Message.ERROR_INSUFFICIENT_BALANCE.getCode(), exception.getMessage())
            );
        }

        @Test
        @DisplayName("It should pass when the balance is equal to or greater than the amount")
        void validateSufficientBalancePassesForValidAmounts() {
            BigDecimal balanceEqual = BigDecimal.valueOf(100);
            BigDecimal balanceGreater = BigDecimal.valueOf(200);
            BigDecimal amount = BigDecimal.valueOf(100);

            assertAll("Validate valid amounts",
                    () -> assertDoesNotThrow(() -> AccountValidator.validateSufficientBalance(balanceEqual, amount)),
                    () -> assertDoesNotThrow(() -> AccountValidator.validateSufficientBalance(balanceGreater, amount))
            );
        }
    }
}
