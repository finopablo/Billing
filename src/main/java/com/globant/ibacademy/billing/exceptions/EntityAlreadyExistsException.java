package com.globant.ibacademy.billing.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message, Exception cause) {
        super(message, cause);
    }
}
