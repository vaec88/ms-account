package com.bank.msaccount.exception;

/**
 * Represents an exception for inactive customers
 */
public class CustomerInactiveException extends RuntimeException {

    public CustomerInactiveException(Long id) {
        super("Customer with id " + id + " is inactive");
    }
}
