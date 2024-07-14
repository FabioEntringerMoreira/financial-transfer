package com.fabio.financialtransfer.domain.service;

import com.fabio.financialtransfer.domain.dto.ExchangeRateResponse;

/**
 * Service interface for fetching exchange rates.
 */
public interface ExchangeRateService {

    /**
     * Retrieves the exchange rate from one currency to another.
     * @param fromCurrency the currency code of the source currency
     * @param toCurrency the currency code of the target currency
     * @return the exchange rate response containing the conversion rate
     * @throws IllegalArgumentException if any of the parameters are null or invalid
     */
    ExchangeRateResponse getExchangeRate(final String fromCurrency, final String toCurrency);
}
