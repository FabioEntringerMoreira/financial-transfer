package com.fabio.financialtransfer.domain.service;

import com.fabio.financialtransfer.domain.dto.TransferTO;
import com.fabio.financialtransfer.domain.exception.InsufficientBalanceException;
import com.fabio.financialtransfer.domain.exception.ObjectNotFoundException;
import com.fabio.financialtransfer.domain.exception.InvalidParameterException;
import com.fabio.financialtransfer.domain.model.Account;
import com.fabio.financialtransfer.domain.repository.AccountRepository;
import com.fabio.financialtransfer.domain.util.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing AccountService class")
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private AccountService accountService;

    @Nested
    @DisplayName("When calling getAccount method")
    class GetAccount {

        @Test
        @DisplayName("It should throw InvalidParameterException when accountId is null")
        void getAccountShouldThrowExceptionWhenAccountIdIsNull() {
            Long accountId = null;

            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    accountService.getAccountById(accountId)
            );

            assertEquals("The accountId must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should return the account when accountId is valid")
        void getAccountShouldReturnAccountWhenAccountIdIsValid() {
            Long accountId = 1L;
            String currency = "USD";
            BigDecimal balance = BigDecimal.valueOf(1000);

            Account account = new Account(accountId, currency, balance);
            when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

            Account result = accountService.getAccountById(accountId);

            assertAll("account",
                    () -> assertNotNull(result, "The account should not be null"),
                    () -> assertEquals(accountId, result.getId(), "Account ID should match"),
                    () -> assertEquals(currency, result.getCurrency(), "Currency should match"),
                    () -> assertEquals(balance, result.getBalance(), "Balance should match")
            );
        }

        @Test
        @DisplayName("It should throw CustomException when account is not found")
        void getAccountShouldThrowExceptionWhenAccountNotFound() {
            Long accountId = 1L;
            when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

            ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () ->
                    accountService.getAccountById(accountId)
            );

            assertEquals("Account not found for id: " + accountId, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("When calling executeTransferProcess method")
    class ExecuteTransferProcess {

        private final Long debitAccountId = 1L;
        private final Long creditAccountId = 2L;
        private final String currencyDebit = "USD";
        private final String currencyCredit = "EUR";

        private Account createDebitAccount(BigDecimal balance) {
            return new Account(debitAccountId, currencyDebit, balance);
        }

        private Account createCreditAccount(BigDecimal balance) {
            return new Account(creditAccountId, currencyCredit, balance);
        }

        @Test
        @DisplayName("It should throw InvalidParameterException when transferTO is null")
        void executeTransferProcessShouldThrowExceptionWhenTransferTONull() {
            InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                    accountService.executeTransferProcess(null)
            );

            assertEquals("The transferTO must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("It should execute transfer successfully")
        void executeTransferProcessShouldTransferSuccessfully() {
            BigDecimal transferAmount = BigDecimal.valueOf(1000);
            TransferTO transferTO = new TransferTO(debitAccountId, creditAccountId, transferAmount);

            Account debitAccount = createDebitAccount(BigDecimal.valueOf(2000));
            Account creditAccount = createCreditAccount(BigDecimal.valueOf(500));

            when(accountRepository.findById(debitAccountId)).thenReturn(Optional.of(debitAccount));
            when(accountRepository.findById(creditAccountId)).thenReturn(Optional.of(creditAccount));
            when(currencyService.convert(transferAmount, currencyDebit, currencyCredit)).thenReturn(BigDecimal.valueOf(850));

            accountService.executeTransferProcess(transferTO);

            verify(accountRepository).save(debitAccount);
            verify(accountRepository).save(creditAccount);

            assertAll("account balances",
                    () -> assertEquals(BigDecimal.valueOf(1000), debitAccount.getBalance()),
                    () -> assertEquals(BigDecimal.valueOf(1350), creditAccount.getBalance())
            );
        }

        @Test
        @DisplayName("It should throw CustomException when debit account does not have sufficient balance")
        void executeTransferProcessShouldThrowExceptionWhenInsufficientBalance() {
            BigDecimal transferAmount = BigDecimal.valueOf(3000);
            TransferTO transferTO = new TransferTO(debitAccountId, creditAccountId, transferAmount);
            Account debitAccount = createDebitAccount(BigDecimal.valueOf(200));

            when(accountRepository.findById(debitAccountId)).thenReturn(Optional.of(debitAccount));

            InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () ->
                    accountService.executeTransferProcess(transferTO)
            );

            assertEquals(Message.ERROR_INSUFFICIENT_BALANCE.getCode(), exception.getMessage());
        }

        @Test
        @DisplayName("It should throw CustomException when debit account is not found")
        void executeTransferProcessShouldThrowExceptionWhenDebitAccountNotFound() {
            BigDecimal transferAmount = BigDecimal.valueOf(100);
            TransferTO transferTO = new TransferTO(debitAccountId, creditAccountId, transferAmount);

            when(accountRepository.findById(debitAccountId)).thenReturn(Optional.empty());

            ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () ->
                    accountService.executeTransferProcess(transferTO)
            );

            assertEquals("Account not found for id: " + debitAccountId, exception.getMessage());
        }

        @Test
        @DisplayName("It should throw CustomException when credit account is not found")
        void executeTransferProcessShouldThrowExceptionWhenCreditAccountNotFound() {
            BigDecimal transferAmount = BigDecimal.valueOf(100);
            TransferTO transferTO = new TransferTO(debitAccountId, creditAccountId, transferAmount);

            Account debitAccount = createDebitAccount(BigDecimal.valueOf(200));

            when(accountRepository.findById(debitAccountId)).thenReturn(Optional.of(debitAccount));
            when(accountRepository.findById(creditAccountId)).thenReturn(Optional.empty());

            ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () ->
                    accountService.executeTransferProcess(transferTO)
            );

            assertEquals("Account not found for id: " + creditAccountId, exception.getMessage());
        }
    }
}
