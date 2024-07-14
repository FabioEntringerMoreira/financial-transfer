package com.fabio.financialtransfer.client.api.v1.controller;

import com.fabio.financialtransfer.client.api.v1.mapper.TransferMapper;
import com.fabio.financialtransfer.client.dto.input.TransferInputTO;
import com.fabio.financialtransfer.client.dto.representation.TransferRepresentationTO;
import com.fabio.financialtransfer.domain.service.TransferService;
import com.fabio.financialtransfer.domain.util.Message;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling transfer operations in the API.
 */
@RestController
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final TransferService transferService;

    /**
     * Constructor
     * @param transferService {@link TransferController} the transfer service to be used by this controller
     */
    public TransferController(final TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Handles the transfer of funds between accounts.
     * @param transferInputTO {@link TransferInputTO} the transfer input details
     * @return a {@link ResponseEntity} containing the transfer representation
     */
    @PostMapping
    public ResponseEntity<TransferRepresentationTO> transferFunds(@RequestBody @Valid final TransferInputTO transferInputTO) {
        Long transactionId = transferService.transferFunds(TransferMapper.toTransferDTO(transferInputTO));
        return ResponseEntity.ok(buildTransfer(transactionId, Message.TRANSFER_SUCCESS.getCode()));
    }

    /**
     * Builds a {@link TransferRepresentationTO} object with the specified details.
     * @param transactionId the transaction ID
     * @param message a message related to the transfer
     * @return a {@link TransferRepresentationTO} object
     */
    private static TransferRepresentationTO buildTransfer(final Long transactionId, final String message) {
        return new TransferRepresentationTO(transactionId,"Success", message);
    }
}