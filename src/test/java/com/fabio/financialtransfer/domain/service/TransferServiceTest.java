package com.fabio.financialtransfer.domain.service;

import com.fabio.financialtransfer.domain.dto.TransferTO;
import com.fabio.financialtransfer.domain.exception.InsufficientBalanceException;
import com.fabio.financialtransfer.domain.exception.ObjectNotFoundException;
import com.fabio.financialtransfer.domain.exception.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing TransferService class")
class TransferServiceTest {

    @Mock
    private AccountService accountService;

    private ReentrantLock lock;

    @InjectMocks
    private TransferService transferService;

    private TransferTO transferTO;

    @BeforeEach
    void setUp() {
        lock = new ReentrantLock();
        transferService = new TransferService(accountService, lock);
        transferTO = new TransferTO(1L, 2L, new BigDecimal("1000"));
    }

    @Nested
    @DisplayName("When calling transferFunds method")
    class TransferFunds {

        @Test
        @DisplayName("It should throw InvalidParameterException when transferTO is null")
        void transferFundsShouldThrowExceptionWhenTransferTONull() {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    transferService.transferFunds(null)
            );

            assertEquals("The transferTO must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should execute transfer successfully and return transactionId")
        void transferFundsShouldExecuteSuccessfully() {
            doNothing().when(accountService).executeTransferProcess(transferTO);

            Long transactionId = transferService.transferFunds(transferTO);

            assertNotNull(transactionId, "Transaction ID should not be null");
            verify(accountService, times(1)).executeTransferProcess(transferTO);
        }

        @Test
        @DisplayName("It should throw Business exception when accountService throws CustomException")
        void transferFundsShouldThrowCustomException() {
            doThrow(new InsufficientBalanceException("Insufficient balance")).when(accountService).executeTransferProcess(transferTO);

            InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () ->
                    transferService.transferFunds(transferTO)
            );

            assertEquals("Insufficient balance", exception.getMessage());
        }

        @Test
        @DisplayName("It should always unlock the lock")
        void transferFundsShouldAlwaysUnlockTheLock() {
            doNothing().when(accountService).executeTransferProcess(transferTO);

            transferService.transferFunds(transferTO);

            assertFalse(lock.isLocked(), "Lock should be released after the transfer");
        }

        @Test
        @DisplayName("It should handle concurrent transfers correctly")
        void transferFundsShouldHandleConcurrentTransfers() throws InterruptedException {
            /*
             * For consistency in the log, it's recommended to add a sleep inside the TransferService to
             * ensure the logs reflect accurate lock and unlock behavior.
             */
            int numberOfThreads = 10;
            ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
            // Allows threads to wait until a set of operations completes.
            CountDownLatch latch = new CountDownLatch(numberOfThreads);
            for (int i = 0; i < numberOfThreads; i++) {
                executor.submit(() -> {
                    try {
                        transferService.transferFunds(transferTO);
                    } finally {
                        latch.countDown();// Ensure the latch is decremented after task completion
                    }
                });
            }
            // Wait for all threads to complete their execution
            latch.await();
            executor.shutdown();

            verify(accountService, times(numberOfThreads)).executeTransferProcess(transferTO);
        }
    }
}
