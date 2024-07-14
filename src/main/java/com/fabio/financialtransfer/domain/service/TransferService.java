package com.fabio.financialtransfer.domain.service;

import com.fabio.financialtransfer.domain.dto.TransferTO;
import com.fabio.financialtransfer.domain.exception.ApplicationException;
import com.fabio.financialtransfer.domain.util.ValidationUtils;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Service class for handling transfer operations.
 */
@Service
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private final AccountService accountService;
    private final ReentrantLock lock;

    /**
     * Constructor
     * @param accountService the account service used to execute the transfer process
     * @param lock the lock used to ensure thread safety during the transfer process
     */
    public TransferService(final AccountService accountService, final ReentrantLock lock) {
        this.accountService = accountService;
        this.lock = lock;
    }

    /**
     * Transfers funds between accounts.
     * @param transferTO the transfer data
     * @return the transaction ID of the transfer
     */
    @Transactional
    public Long transferFunds(@Nonnull final TransferTO transferTO) {
        ValidationUtils.validateNonNull(transferTO, "transferTO");

        logger.info("Starting transfer process lock: " + transferTO);
        lock.lock();
        try {
            logger.info("Locked: " + transferTO);
            this.accountService.executeTransferProcess(transferTO);
            return generateTransactionId();
        } catch (ApplicationException e) {
            logger.error("Transfer failed due to business logic: {}", e.getMessage(), e);
            throw e;
        } finally {
            lock.unlock();
            logger.info("Unlock");
        }
    }

    /**
     * Generates a transaction ID based on the current time in milliseconds.
     * @return the generated transaction ID
     */
    private Long generateTransactionId() {
        return System.currentTimeMillis();
    }
}
