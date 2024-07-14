package com.fabio.financialtransfer.client.dto.input;

import com.fabio.financialtransfer.client.dto.input.validation.DifferentAccountIds;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Data input model for capturing transfer details.
 */
@DifferentAccountIds
public final class TransferInputTO {

    @NotNull(message = "Debit account ID cannot be null")
    @Positive(message = "Debit account ID must be positive")
    private final Long debitAccountId;

    @NotNull(message = "Credit account ID cannot be null")
    @Positive(message = "Credit account ID must be positive")
    private final Long creditAccountId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private final BigDecimal amount;

    /**
     * Constructor for TransferInputTO.
     * @param debitAccountId the ID of the debit account
     * @param creditAccountId the ID of the credit account
     * @param amount the amount to be transferred
     */
    public TransferInputTO(final Long debitAccountId, final Long creditAccountId, final BigDecimal amount) {
        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        this.amount = amount;
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
     * Gets the amount to be transferred.
     * @return the amount to be transferred
     */
    public BigDecimal getAmount() {
        return amount;
    }
}
