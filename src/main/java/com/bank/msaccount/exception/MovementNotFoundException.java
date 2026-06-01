package com.bank.msaccount.exception;

/**
 * Represents an exception for movement not found
 */
public class MovementNotFoundException extends RuntimeException {

    public MovementNotFoundException(Long id) {
        super("Movement with id " + id + " was not found");
    }
}
