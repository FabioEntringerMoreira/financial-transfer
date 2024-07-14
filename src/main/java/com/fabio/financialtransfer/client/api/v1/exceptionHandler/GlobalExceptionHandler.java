package com.fabio.financialtransfer.client.api.v1.exceptionHandler;

import com.fabio.financialtransfer.client.dto.representation.TransferRepresentationTO;
import com.fabio.financialtransfer.domain.exception.BusinessException;
import com.fabio.financialtransfer.domain.exception.TechnicalException;
import com.fabio.financialtransfer.domain.util.Message;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * Global exception handler for handling various exceptions in the application.
 * This class uses {@link ControllerAdvice} to intercept exceptions thrown by controllers
 * and provides appropriate responses to the client.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions thrown during method argument validation.
     * @param ex the {@link MethodArgumentNotValidException} that was thrown
     * @return a {@link ResponseEntity} containing the error details and HTTP status code 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TransferRepresentationTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" - "));

        TransferRepresentationTO errorResponse = new TransferRepresentationTO(null, "Failed", errors.trim());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles business exceptions thrown during the execution of business logic.
     * @param ex the {@link BusinessException} that was thrown
     * @return a {@link ResponseEntity} containing the error message and HTTP status code 400 (Bad Request)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<TransferRepresentationTO> handleBusinessExceptions(BusinessException ex) {
        TransferRepresentationTO errorResponse = new TransferRepresentationTO(null, "Failed", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles technical exceptions thrown due to technical issues in the application.
     * @param ex the {@link TechnicalException} that was thrown
     * @return a {@link ResponseEntity} containing a generic error message and HTTP status code 500 (Internal Server Error)
     */
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<TransferRepresentationTO> handleTechnicalExceptions(TechnicalException ex) {
        TransferRepresentationTO errorResponse = new TransferRepresentationTO(null, "Failed", Message.ERROR_TECHNICAL.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
