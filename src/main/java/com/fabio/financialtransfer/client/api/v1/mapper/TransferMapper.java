package com.fabio.financialtransfer.client.api.v1.mapper;

import com.fabio.financialtransfer.client.dto.input.TransferInputTO;
import com.fabio.financialtransfer.domain.dto.TransferTO;

/**
 * Mapper class for converting between {@link TransferInputTO} and {@link TransferTO}.
 */
public class TransferMapper {

    /**
     * Converts a {@link TransferInputTO} to a {@link TransferTO}.
     * @param inputModel the {@link TransferInputTO} to convert
     * @return the converted {@link TransferTO}, or {@code null} if the input model is {@code null}
     */
    public static TransferTO toTransferDTO(final TransferInputTO inputModel) {
        if (inputModel == null) {
            return null;
        }
        return new TransferTO(inputModel.getDebitAccountId(), inputModel.getCreditAccountId(), inputModel.getAmount());
    }
}
