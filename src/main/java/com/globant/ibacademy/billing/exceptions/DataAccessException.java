package com.globant.ibacademy.billing.exceptions;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Exception cause) {
        super(message, cause);
    }
}
