package com.fabio.financialtransfer.domain.validation;

import com.fabio.financialtransfer.domain.exception.InsufficientBalanceException;
import com.fabio.financialtransfer.domain.util.Message;
import com.fabio.financialtransfer.domain.util.ValidationUtils;
import jakarta.annotation.Nonnull;

import java.math.BigDecimal;

/**
 * Validator class for account-related validations.
 */
public class AccountValidator {

    /**
     * Validates if the account has sufficient balance for the specified amount.
     * @param balance the balance of the account
     * @param amount  the amount to be withdrawn
     * @throws InsufficientBalanceException if the balance is insufficient
     */
    public static void validateSufficientBalance(@Nonnull final BigDecimal balance, @Nonnull final BigDecimal amount) {
        ValidationUtils.validateNonNull(balance, "balance");
        ValidationUtils.validateNonNegative(amount, "amount");

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(Message.ERROR_INSUFFICIENT_BALANCE.getCode());
        }
    }
}
