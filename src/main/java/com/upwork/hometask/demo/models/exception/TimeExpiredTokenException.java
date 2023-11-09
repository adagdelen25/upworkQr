package com.upwork.hometask.demo.models.exception;

public class TimeExpiredTokenException extends RuntimeException {
    public TimeExpiredTokenException() {
        super("This token is expired");
    }

    public TimeExpiredTokenException(String message) {
        super(message);
    }
}