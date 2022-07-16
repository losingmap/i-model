package com.lft.imodel.exception;

public class CustomFailureException extends RuntimeException {
    public CustomFailureException(String message) {
        super(message);
    }
}
