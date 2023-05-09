package com.globant.ibacademy.billing.exceptions;

public class EntityAlreadyExistsException extends Exception {
    public EntityAlreadyExistsException(String message, Exception cause) {
        super(message, cause);
    }
}
