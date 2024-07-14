package com.fabio.financialtransfer.domain.service;

import com.fabio.financialtransfer.domain.dto.ExchangeRateResponse;
import com.fabio.financialtransfer.domain.util.ValidationUtils;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service class for handling currency conversion operations.
 */
@Service
public class CurrencyService {

    /**
     * The default scale for currency conversion results.
     */
    public static final Integer DEFAULT_SCALE = 4;

    private final ExchangeRateService exchangeRateService;

    /**
     * Constructor.
     * @param exchangeRateService {@link ExchangeRateService} the exchange rate service to be used for currency conversion
     */
    public CurrencyService(final ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Converts the given amount from one currency to another using the exchange rate service.
     * @param amount the amount to convert
     * @param fromCurrency the currency code of the source currency
     * @param toCurrency the currency code of the target currency
     * @return the converted amount in the target currency, scaled to {@link #DEFAULT_SCALE}
     * @throws IllegalArgumentException if the amount is null or if any validation fails
     */
    public BigDecimal convert(@Nonnull final BigDecimal amount, final String fromCurrency, final String toCurrency) {
        ValidationUtils.validateNonNull(amount, "Amount");

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return amount;
        }

        ExchangeRateResponse rateResponse = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        BigDecimal conversionRate = rateResponse.getConversionRate();

        return amount.multiply(conversionRate).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
}
