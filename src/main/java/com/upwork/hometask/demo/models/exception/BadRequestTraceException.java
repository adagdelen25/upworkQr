package com.upwork.hometask.demo.models.exception;

public class BadRequestTraceException extends RuntimeException {
    public BadRequestTraceException() {
        super();
    }

    public BadRequestTraceException(String message) {
        super(message);
    }
}