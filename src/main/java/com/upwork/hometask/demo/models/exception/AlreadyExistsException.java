package com.upwork.hometask.demo.models.exception;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException() {
        super("Entity already exist!");
    }

    public AlreadyExistsException(String message) {
        super(message);
    }
}
