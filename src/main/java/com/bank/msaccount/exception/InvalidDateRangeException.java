package com.bank.msaccount.exception;

/**
 * Represents an exception for invalid date range
 */
public class InvalidDateRangeException extends RuntimeException {

    public InvalidDateRangeException(String message) {
        super(message);
    }
}
