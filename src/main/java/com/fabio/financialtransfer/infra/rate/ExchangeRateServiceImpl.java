package com.fabio.financialtransfer.infra.rate;

import com.fabio.financialtransfer.domain.dto.ExchangeRateResponse;
import com.fabio.financialtransfer.domain.exception.ApplicationException;
import com.fabio.financialtransfer.domain.exception.CurrencyConversionException;
import com.fabio.financialtransfer.domain.exception.CurrencyNotSupportedException;
import com.fabio.financialtransfer.domain.service.ExchangeRateService;
import com.fabio.financialtransfer.domain.util.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service implementation for fetching exchange rates from an external API.
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Value("${exchange.rate.api.key}")
    private String apiKey = "";

    @Value("${exchange.rate.api.url}")
    private String apiUrl = "";

    private final RestTemplate restTemplate;

    /**
     * Constructor
     * @param restTemplate the {@link RestTemplate} to be used for API calls
     */
    public ExchangeRateServiceImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches the exchange rate for the specified currencies. The result is cached.
     * @param fromCurrency the source currency code
     * @param toCurrency the target currency code
     * @return an {@link ExchangeRateResponse} containing the conversion rate
     * @throws CurrencyConversionException if an error occurs while fetching the rate
     */
    @Override
    @Cacheable(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public ExchangeRateResponse getExchangeRate(final String fromCurrency, final String toCurrency) {
        final String url = buildApiUrl(fromCurrency, toCurrency);
        try {
            return restTemplate.getForObject(url, ExchangeRateResponse.class);
        } catch (HttpClientErrorException e) {
            throw handleApiError(e);
        } catch (RestClientException e) {
            throw new CurrencyConversionException(Message.ERROR_FETCHING_CURRENCY_CONVERSION.getCode(), e);
        }
    }

    /**
     * Builds the API URL for fetching the exchange rate.
     * @param fromCurrency the source currency code
     * @param toCurrency the target currency code
     * @return the formatted API URL
     */
    private String buildApiUrl(final String fromCurrency, final String toCurrency) {
        return String.format("%s/%s/pair/%s/%s", apiUrl, apiKey, fromCurrency, toCurrency);
    }

    /**
     * Handles API errors by converting them to {@link CurrencyConversionException}.
     * @param e the {@link HttpClientErrorException} thrown by the {@link RestTemplate}
     * @return a {@link CurrencyConversionException} with an appropriate message
     */
    private ApplicationException handleApiError(final HttpClientErrorException e) {
        String errorBody = e.getResponseBodyAsString();
        if (errorBody.contains("unsupported-code")) {
            return new CurrencyNotSupportedException(Message.ERROR_UNSUPPORTED_CURRENCY.getCode());
        } else if (errorBody.contains("malformed-request")) {
            return new CurrencyConversionException(Message.ERROR_MALFORMED_REQUEST.getCode(), e);
        } else {
            return new CurrencyConversionException(Message.ERROR_UNKNOWN.getCode(), e);
        }
    }
}
