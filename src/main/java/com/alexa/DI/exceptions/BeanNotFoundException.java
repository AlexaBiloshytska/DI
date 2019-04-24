package com.alexa.DI.exceptions;

public class BeanNotFoundException extends RuntimeException {
    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
