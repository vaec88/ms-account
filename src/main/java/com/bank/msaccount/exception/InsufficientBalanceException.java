package com.bank.msaccount.exception;

/**
 * Represents an exception for insufficient balance
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
