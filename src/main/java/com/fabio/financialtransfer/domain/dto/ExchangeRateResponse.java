package com.fabio.financialtransfer.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * This class represents the response of the exchange rate API.
 */
public final class ExchangeRateResponse {

    @JsonProperty("result")
    private final String result;
    @JsonProperty("base_code")
    private final String baseCode;
    @JsonProperty("target_code")
    private final String targetCode;
    @JsonProperty("conversion_rate")
    private final BigDecimal conversionRate;

    /**
     * Constructor.
     * @param result the result status of the exchange rate query
     * @param baseCode the base currency code
     * @param targetCode the target currency code
     * @param conversionRate the conversion rate from the base currency to the target currency
     */
    public ExchangeRateResponse(final String result, final String baseCode, final String targetCode,
                                final BigDecimal conversionRate) {
        this.result = result;
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.conversionRate = conversionRate;
    }

    /**
     * Gets the result status of the exchange rate query.
     * @return the result status
     */
    public String getResult() {
        return result;
    }

    /**
     * Gets the base currency code.
     * @return the base currency code
     */
    public String getBaseCode() {
        return baseCode;
    }

    /**
     * Gets the target currency code.
     * @return the target currency code
     */
    public String getTargetCode() {
        return targetCode;
    }

    /**
     * Gets the conversion rate from the base currency to the target currency.
     * @return the conversion rate
     */
    public BigDecimal getConversionRate() {
        return conversionRate;
    }
}
