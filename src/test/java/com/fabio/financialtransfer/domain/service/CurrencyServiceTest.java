package com.fabio.financialtransfer.domain.service;

import com.fabio.financialtransfer.domain.dto.ExchangeRateResponse;
import com.fabio.financialtransfer.domain.exception.InvalidParameterException;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing CurrencyService class")
class CurrencyServiceTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private CurrencyService currencyService;

    @Nested
    @DisplayName("When calling convert method")
    class Convert {

        @Test
        @DisplayName("It should throw InvalidParameterException when amount is null")
        void convertShouldThrowExceptionWhenAmountIsNull() {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    currencyService.convert(null, "USD", "EUR")
            );

            assertEquals("The Amount must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should return zero when amount is zero")
        void convertShouldReturnZeroWhenAmountIsZero() {
            BigDecimal result = assertDoesNotThrow(() -> currencyService.convert(BigDecimal.ZERO, "USD", "EUR"));

            assertEquals(BigDecimal.ZERO, result);
            verify(exchangeRateService, never()).getExchangeRate(anyString(), anyString());
        }

        static Stream<Arguments> validConversionProvider() {
            return Stream.of(
                    Arguments.of(
                            BigDecimal.valueOf(100),
                            "USD",
                            "EUR",
                            BigDecimal.valueOf(0.85),
                            BigDecimal.valueOf(85).setScale(CurrencyService.DEFAULT_SCALE, RoundingMode.HALF_UP)
                    ),
                    Arguments.of(
                            BigDecimal.valueOf(200),
                            "USD",
                            "JPY",
                            BigDecimal.valueOf(110),
                            BigDecimal.valueOf(22000).setScale(CurrencyService.DEFAULT_SCALE, RoundingMode.HALF_UP)
                    )
            );
        }

        @ParameterizedTest(name = "({index}) => amount={0}, fromCurrency={1}, toCurrency={2}, rate={3}, expected={4}")
        @MethodSource("validConversionProvider")
        @DisplayName("It should convert correctly for valid inputs and rates")
        void convertShouldReturnCorrectValue(BigDecimal amount, String fromCurrency, String toCurrency, BigDecimal rate, BigDecimal expected) {
            ExchangeRateResponse rateResponse = new ExchangeRateResponse("success", fromCurrency, toCurrency, rate);
            when(exchangeRateService.getExchangeRate(fromCurrency, toCurrency)).thenReturn(rateResponse);

            BigDecimal result = currencyService.convert(amount, fromCurrency, toCurrency);

            assertEquals(expected, result);
        }

        @Test
        @DisplayName("It should handle exchange rate service error")
        void convertShouldHandleExchangeRateServiceError() {
            when(exchangeRateService.getExchangeRate("USD", "EUR")).thenThrow(new RuntimeException("Service unavailable"));

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    currencyService.convert(BigDecimal.valueOf(100), "USD", "EUR")
            );

            assertEquals("Service unavailable", exception.getMessage());
        }
    }
}
