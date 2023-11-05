package com.upwork.hometask.demo.models.exception;

public class WarningException extends RuntimeException {
    private String message;
    private Object[] args;
    private boolean init = false;

    public WarningException(String message, Object... args) {
        super(message);
        this.message = message;
        this.args = args;
    }

    public String getMessage() {
        this.initMessage();
        return this.message;
    }

    public String toString() {
        return this.getMessage();
    }

    private void initMessage() {
        if (!this.init) {
            if (this.args != null && this.args.length > 0) {
                this.message = java.text.MessageFormat.format(message,args);
                this.init = true;
            }

        }
    }
}
