package com.fabio.financialtransfer.domain.service;

import com.fabio.financialtransfer.domain.dto.TransferTO;
import com.fabio.financialtransfer.domain.exception.InsufficientBalanceException;
import com.fabio.financialtransfer.domain.exception.ObjectNotFoundException;
import com.fabio.financialtransfer.domain.exception.ApplicationException;
import com.fabio.financialtransfer.domain.model.Account;
import com.fabio.financialtransfer.domain.repository.AccountRepository;
import com.fabio.financialtransfer.domain.util.ValidationUtils;
import com.fabio.financialtransfer.domain.validation.AccountValidator;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service class for managing accounts and executing transfer processes.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;

    /**
     * Constructor.
     * @param accountRepository the account repository
     * @param currencyService the currency service
     */
    public AccountService(final AccountRepository accountRepository, final CurrencyService currencyService) {
        this.accountRepository = accountRepository;
        this.currencyService = currencyService;
    }

    /**
     * Retrieves an account by its ID.
     * @param accountId the ID of the account
     * @return {@link Account} the account
     * @throws ObjectNotFoundException if the account is not found
     */
    public Account getAccount(@Nonnull final Long accountId) {
        ValidationUtils.validateNonNull(accountId, "accountId");

        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ObjectNotFoundException("Account not found for id: " + accountId));
    }

    /**
     * Executes the transfer process between two accounts.
     * @param transferTO the transfer details
     * @throws ApplicationException if any validation or processing error occurs
     */
    public void executeTransferProcess(@Nonnull final TransferTO transferTO) {
        ValidationUtils.validateNonNull(transferTO, "transferTO");

        Account debitAccount = getAccountWithCheckedBalance(transferTO.getDebitAccountId(), transferTO.getAmount());
        Account creditAccount = getAccount(transferTO.getCreditAccountId());

        BigDecimal amountInTargetCurrency = currencyService.convert(transferTO.getAmount(), debitAccount.getCurrency(), creditAccount.getCurrency());

        debitAccount.setBalance(debitAccount.getBalance().subtract(transferTO.getAmount()));
        creditAccount.setBalance(creditAccount.getBalance().add(amountInTargetCurrency));
        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);
    }

    /**
     * Retrieves an account and checks if it has sufficient balance for a withdrawal.
     * @param accountId the ID of the account
     * @param amountToWithdraw the amount to withdraw
     * @return the account
     * @throws InsufficientBalanceException if the account does not have sufficient balance
     */
    private Account getAccountWithCheckedBalance(@Nonnull final Long accountId, @Nonnull final BigDecimal amountToWithdraw) {
        ValidationUtils.validateIdentifier(accountId, "accountId");
        ValidationUtils.validatePositive(amountToWithdraw, "withdrawAmount");

        Account debitAccount = getAccount(accountId);
        AccountValidator.validateSufficientBalance(debitAccount.getBalance(), amountToWithdraw);
        return debitAccount;
    }
}
