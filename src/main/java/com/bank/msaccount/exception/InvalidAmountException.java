package com.bank.msaccount.exception;

/**
 * Represents an exception for invalid amount
 */
public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
