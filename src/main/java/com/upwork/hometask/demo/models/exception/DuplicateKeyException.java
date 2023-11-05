package com.upwork.hometask.demo.models.exception;

public class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException() {
        super("The key already exists!");
    }
    public DuplicateKeyException(String message) {
        super(message);
    }
}
