package com.bank.msaccount.exception;

/**
 * Represents an exception when source account is the same as destination account
 */
public class SameAccountException extends RuntimeException {

    public SameAccountException(String message) {
        super(message);
    }
}
