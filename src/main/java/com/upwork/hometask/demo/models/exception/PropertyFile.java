package com.upwork.hometask.demo.models.exception;

public enum PropertyFile {
    ERROR_ACCESS_DENIED("error.access-denied"),
    ERROR_BAD_CREDENTIALS("error.bad-credentials"),
    ERROR_ENTITY_NOT_FOUND("error.entity-not-found"),
    ERROR_MESSAGE_NOT_READABLE("error.message-not-readable"),
    ERROR_METHOD_ARGUMENT_NOT_VALID("error.method-argument-not-valid"),
    ERROR_METHOD_ARGUMENT_TYPE_MISMATCH("error.method-argument-type-mismatch"),
    ERROR_MISSING_PATH_VARIABLE("error.missing-path-variable"),
    ERROR_MISSING_REQUEST_HEADER("error.missing-request-header"),
    ERROR_MISSING_REQUEST_PARAMETER("error.missing-request-parameter"),
    ERROR_MISSING_REQUEST_QUERY("error.missing-request-query"),
    ERROR_MISSING_TOKEN_HEADER("error.missing-token-header"),
    ERROR_RECORD_NOT_FOUND("error.record-not-found"),
    ERROR_SERVER_ERROR("error.server-error");

    private final String key;

    private PropertyFile(String key) {
        this.key = key;
    }

    public String key() {
        return this.key;
    }

    public String toString() {
        String var10000 = this.name();
        return "PropertyFile." + var10000 + "(key=" + this.key + ")";
    }
}
