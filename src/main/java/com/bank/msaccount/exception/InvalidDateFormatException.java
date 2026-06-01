package com.bank.msaccount.exception;

/**
 * Represents an exception for invalid date format
 */
public class InvalidDateFormatException extends RuntimeException {

    public InvalidDateFormatException(String message) {
        super(message);
    }
}
