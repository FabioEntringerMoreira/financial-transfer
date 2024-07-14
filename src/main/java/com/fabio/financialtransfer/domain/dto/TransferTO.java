package com.fabio.financialtransfer.domain.dto;

import com.fabio.financialtransfer.domain.util.ValidationUtils;

import java.math.BigDecimal;

/**
 * DTO for transfer operations.
 */
public final class TransferTO {
    private final Long debitAccountId;
    private final Long creditAccountId;
    private final BigDecimal amount;

    /**
     * Constructor
     * @param debitAccountId the ID of the debit account
     * @param creditAccountId the ID of the credit account
     * @param amount the transfer amount
     */
    public TransferTO(final Long debitAccountId, final Long creditAccountId, final BigDecimal amount) {
        this.debitAccountId = ValidationUtils.validateIdentifier(debitAccountId, "debitAccountId");
        this.creditAccountId = ValidationUtils.validateIdentifier(creditAccountId, "creditAccountId");
        this.amount = ValidationUtils.validatePositive(amount, "amount");
    }

    /**
     * Gets the ID of the debit account.
     * @return the ID of the debit account
     */
    public Long getDebitAccountId() {
        return debitAccountId;
    }

    /**
     * Gets the ID of the credit account.
     * @return the ID of the credit account
     */
    public Long getCreditAccountId() {
        return creditAccountId;
    }

    /**
     * Gets the transfer amount.
     * @return the transfer amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransferTO{" +
                "debitAccountId=" + debitAccountId +
                ", creditAccountId=" + creditAccountId +
                ", amount=" + amount +
                '}';
    }
}