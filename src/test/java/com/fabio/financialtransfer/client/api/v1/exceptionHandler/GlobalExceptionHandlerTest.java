package com.fabio.financialtransfer.client.api.v1.exceptionHandler;

import com.fabio.financialtransfer.client.dto.representation.TransferRepresentationTO;
import com.fabio.financialtransfer.domain.exception.BusinessException;
import com.fabio.financialtransfer.domain.exception.TechnicalException;
import com.fabio.financialtransfer.domain.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing GlobalExceptionHandler class")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Nested
    @DisplayName("When handling MethodArgumentNotValidException")
    class HandleValidationExceptions {

        @Test
        @DisplayName("It should handle validation exceptions and return appropriate response")
        void shouldHandleValidationExceptions() {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
            when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<TransferRepresentationTO> response = exceptionHandler.handleValidationExceptions(ex);

            assertAll(
                    () -> assertNotNull(response, "Response should not be null"),
                    () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                    () -> assertNotNull(response.getBody()),
                    () -> assertEquals("Failed", Objects.requireNonNull(response.getBody()).getStatus()),
                    () -> assertEquals("error message", Objects.requireNonNull(response.getBody()).getMessage())
            );
        }

        @Test
        @DisplayName("It should handle multiple validation errors")
        void shouldHandleMultipleValidationErrors() {
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError1 = new FieldError("objectName", "fieldName1", "error message 1");
            FieldError fieldError2 = new FieldError("objectName", "fieldName2", "error message 2");
            when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<TransferRepresentationTO> response = exceptionHandler.handleValidationExceptions(ex);

            assertAll(
                    () -> assertNotNull(response),
                    () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                    () -> assertNotNull(response.getBody()),
                    () -> assertEquals("Failed", Objects.requireNonNull(response.getBody()).getStatus()),
                    () -> assertEquals("error message 1 - error message 2", Objects.requireNonNull(response.getBody()).getMessage())
            );
        }
    }

    @Nested
    @DisplayName("When handling business exceptions")
    class HandleBusinessExceptions {

        @Test
        @DisplayName("It should return a bad request response with error message")
        void handleBusinessExceptionsShouldReturnBadRequest() {
            BusinessException ex = new BusinessException("Business error occurred") {};
            ResponseEntity<TransferRepresentationTO> response = exceptionHandler.handleBusinessExceptions(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Failed", Objects.requireNonNull(response.getBody()).getStatus());
            assertEquals("Business error occurred", response.getBody().getMessage());
        }
    }

    @Nested
    @DisplayName("When handling technical exceptions")
    class HandleTechnicalExceptions {

        @Test
        @DisplayName("It should return an internal server error response with generic error message")
        void handleTechnicalExceptionsShouldReturnInternalServerError() {
            TechnicalException ex = new TechnicalException("Technical error occurred") {};
            ResponseEntity<TransferRepresentationTO> response = exceptionHandler.handleTechnicalExceptions(ex);

            assertAll(
                    () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                    () -> assertEquals("Failed", Objects.requireNonNull(response.getBody()).getStatus()),
                    () -> assertEquals(Message.ERROR_TECHNICAL.getCode(),
                            Objects.requireNonNull(response.getBody()).getMessage())
            );
        }
    }
}
