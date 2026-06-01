package com.bank.msaccount.exception;

/**
 * Represents an exception for inactive accounts
 */
public class AccountInactiveException extends RuntimeException {

    public AccountInactiveException(Long id) {
        super("Account with id " + id + " is inactive");
    }

    public AccountInactiveException(String number) {
        super("Account with number " + number + " is inactive");
    }
}
