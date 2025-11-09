package com.dansmultipro.ops.constant;

public enum ResponseConstant {
    SAVED("Saved"),
    DELETED("Deleted"),
    UPDATED("Updated"),
    ENABLED("Enabled"),
    NOT_FOUND("Not Found"),
    ALREADY_EXISTS("Already Exists"),
    STALE_VERSION("Stale Version"),
    INVALID_CREDENTIAL("Email or password is incorrect.");

    private final String value;

    ResponseConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
