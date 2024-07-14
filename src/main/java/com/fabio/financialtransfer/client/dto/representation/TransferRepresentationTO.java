package com.fabio.financialtransfer.client.dto.representation;

/**
 * Data representation model for representing transfer details.
 */
public final class TransferRepresentationTO {
    private final Long transactionId;
    private final String status;
    private final String message;

    /**
     * Constructor for TransferRepresentationTO.
     * @param transactionId the ID of the transaction
     * @param status the status of the transfer
     * @param message a message related to the transfer
     */
    public TransferRepresentationTO(final Long transactionId, final String status, final String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
    }

    /**
     * Gets the ID of the transaction.
     * @return the ID of the transaction
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the status of the transfer.
     * @return the status of the transfer
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the message related to the transfer.
     * @return the message related to the transfer
     */
    public String getMessage() {
        return message;
    }
}
