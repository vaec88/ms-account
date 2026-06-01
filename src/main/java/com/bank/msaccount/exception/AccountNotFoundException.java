package com.bank.msaccount.exception;

/**
 * Represents an exception for account not found
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Account with id " + id + " was not found");
    }
}
