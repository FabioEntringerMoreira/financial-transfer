package com.fabio.financialtransfer.infra.rate;

import com.fabio.financialtransfer.domain.dto.ExchangeRateResponse;
import com.fabio.financialtransfer.domain.exception.ApplicationException;
import com.fabio.financialtransfer.domain.exception.CurrencyNotSupportedException;
import com.fabio.financialtransfer.domain.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing ExchangeRateServiceImpl class")
class ExchangeRateServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    private final String fromCurrency = "USD";
    private final String toCurrency = "EUR";
    private final String apiUrlFormatted = String.format("//pair/%s/%s", fromCurrency, toCurrency);

    @Nested
    @DisplayName("When calling getExchangeRate method")
    class GetExchangeRate {

        @Test
        @DisplayName("It should return exchange rate successfully")
        void getExchangeRateShouldReturnSuccessfully() {
            ExchangeRateResponse response = new ExchangeRateResponse("", "", "", new BigDecimal("0.85"));

            when(restTemplate.getForObject(apiUrlFormatted, ExchangeRateResponse.class)).thenReturn(response);

            ExchangeRateResponse result = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);

            assertNotNull(result);
            assertEquals(new BigDecimal("0.85"), result.getConversionRate());
            verify(restTemplate, times(1)).getForObject(apiUrlFormatted, ExchangeRateResponse.class);
        }

        @Test
        @DisplayName("It should throw CurrencyConversionException for RestClientException")
        void getExchangeRateShouldThrowRestClientException() {
            when(restTemplate.getForObject(apiUrlFormatted, ExchangeRateResponse.class)).thenThrow(new RestClientException("Client error"));

            ApplicationException exception = assertThrows(ApplicationException.class, () ->
                    exchangeRateService.getExchangeRate(fromCurrency, toCurrency)
            );

            assertEquals(Message.ERROR_FETCHING_CURRENCY_CONVERSION.getCode(), exception.getMessage());
        }

        @ParameterizedTest
        @MethodSource("provideHttpClientErrorExceptions")
        @DisplayName("It should throw appropriate ApplicationException for various HTTP client errors")
        void getExchangeRateShouldThrowAppropriateApplicationExceptionForHttpClientErrors(String responseBody, String expectedMessage) {
            HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

            when(httpClientErrorException.getResponseBodyAsString()).thenReturn(responseBody);
            when(restTemplate.getForObject(apiUrlFormatted, ExchangeRateResponse.class)).thenThrow(httpClientErrorException);

            ApplicationException exception = assertThrows(ApplicationException.class, () ->
                    exchangeRateService.getExchangeRate(fromCurrency, toCurrency)
            );

            assertEquals(expectedMessage, exception.getMessage());
        }

        private static Stream<Arguments> provideHttpClientErrorExceptions() {
            return Stream.of(
                    Arguments.of("unsupported-code", Message.ERROR_UNSUPPORTED_CURRENCY.getCode()),
                    Arguments.of("malformed-request", Message.ERROR_MALFORMED_REQUEST.getCode()),
                    Arguments.of("unknown-error", Message.ERROR_UNKNOWN.getCode())
            );
        }

        @Test
        @DisplayName("It should throw CurrencyNotSupportedException for unsupported currency code")
        void getExchangeRateShouldThrowCurrencyNotSupportedExceptionForUnsupportedCurrencyCode() {
            HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

            when(httpClientErrorException.getResponseBodyAsString()).thenReturn("unsupported-code");
            when(restTemplate.getForObject(apiUrlFormatted, ExchangeRateResponse.class)).thenThrow(httpClientErrorException);

            CurrencyNotSupportedException exception = assertThrows(CurrencyNotSupportedException.class, () ->
                    exchangeRateService.getExchangeRate(fromCurrency, toCurrency)
            );

            assertEquals(Message.ERROR_UNSUPPORTED_CURRENCY.getCode(), exception.getMessage());
        }
    }
}