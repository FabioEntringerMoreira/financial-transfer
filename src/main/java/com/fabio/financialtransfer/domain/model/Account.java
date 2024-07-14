package com.fabio.financialtransfer.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

/**
 * Represents a bank account with a specific currency and balance.
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currency;
    private BigDecimal balance;

    /**
     * Default constructor for JPA.
     */
    public Account() {
    }

    /**
     * Constructor.
     * @param id the unique identifier of the account
     * @param currency the currency of the account
     * @param balance the initial balance of the account
     */
    public Account(final Long id, final String currency, final BigDecimal balance) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
    }

    /**
     * Gets the unique identifier of the account.
     * @return the id of the account
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the account.
     * @param id the new id of the account
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the currency of the account.
     * @return the currency of the account
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the account.
     * @param currency the new currency of the account
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the balance of the account.
     * @return the balance of the account
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account.
     * @param balance the new balance of the account
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
