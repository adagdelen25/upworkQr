package com.upwork.hometask.demo.models.exception;

public class InvalidITokenException extends RuntimeException {
    public InvalidITokenException() {
        super("This token is not valid");
    }

    public InvalidITokenException(String message) {
        super(message);
    }
}