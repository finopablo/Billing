package com.globant.ibacademy.billing.exceptions;

public class ValidationException extends Exception {
    public ValidationException(String message, Exception cause) {
        super(message, cause);
    }
    public ValidationException(String message) {
        super(message);
    }
}
