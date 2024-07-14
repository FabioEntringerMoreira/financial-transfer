package com.fabio.financialtransfer.client.api.v1.mapper;

import com.fabio.financialtransfer.client.dto.input.TransferInputTO;
import com.fabio.financialtransfer.domain.dto.TransferTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing TransferMapper class")
class TransferMapperTest {

    @Nested
    @DisplayName("When mapping TransferInputTO to TransferTO")
    class ToTransferDTO {

        @Test
        @DisplayName("It should return null when input model is null")
        void shouldReturnNullWhenInputModelIsNull() {
            TransferTO result = assertDoesNotThrow(() -> TransferMapper.toTransferDTO(null));

            assertNull(result, "Result should be null when input model is null");
        }

        @Test
        @DisplayName("It should map correctly when all fields are provided")
        void shouldMapCorrectlyWhenAllFieldsAreProvided() {
            // Given
            TransferInputTO input = new TransferInputTO(123L, 456L, new BigDecimal("100.00"));

            TransferTO result = assertDoesNotThrow(() -> TransferMapper.toTransferDTO(input));

            assertAll(
                    () -> assertEquals(123L, result.getDebitAccountId()),
                    () -> assertEquals(456L, result.getCreditAccountId()),
                    () -> assertEquals(new BigDecimal("100.00"), result.getAmount())
            );
        }
    }
}
