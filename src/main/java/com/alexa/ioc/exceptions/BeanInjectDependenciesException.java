package com.alexa.ioc.exceptions;

public class BeanInjectDependenciesException extends RuntimeException {
    public BeanInjectDependenciesException(String message) {
        super(message);
    }

    public BeanInjectDependenciesException(String message, Throwable cause) {
        super(message, cause);
    }
}

