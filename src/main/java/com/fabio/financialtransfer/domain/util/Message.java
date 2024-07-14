package com.fabio.financialtransfer.domain.util;

/**
 * Enum to manage the messages (internalization with MessageSource necessary due the scope of the API).
 */
public enum Message {
    ERROR_UNSUPPORTED_CURRENCY("Error: Unsupported currency code."),
    ERROR_FETCHING_CURRENCY_CONVERSION("Error fetching currency conversion rate"),
    ERROR_MALFORMED_REQUEST("Error: Malformed request. Please check the request structure."),
    ERROR_UNKNOWN("Error: An unknown error occurred."),
    ERROR_INSUFFICIENT_BALANCE("Insufficient balance in debit account"),
    ERROR_TECHNICAL("A technical error occurred. Please contact support."),
    TRANSFER_SUCCESS("Transfer completed successfully");

    private final String code;

    Message(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
