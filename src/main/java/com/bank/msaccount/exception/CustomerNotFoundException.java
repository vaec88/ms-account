package com.bank.msaccount.exception;

/**
 * Represents an exception for customer not found
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Customer with id " + id + " was not found");
    }
}
