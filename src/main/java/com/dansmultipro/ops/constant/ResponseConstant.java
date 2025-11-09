package com.dansmultipro.ops.constant;

public enum ResponseConstant {
    SAVED("Saved"),
    DELETED("Deleted"),
    UPDATED("Updated"),
    ENABLED("Enabled"),
    NOT_FOUND("Not Found"),
    ALREADY_EXISTS("Already Exists"),
    STALE_VERSION("Stale Version"),
    INVALID_CREDENTIAL("Email or password is incorrect."),
    ACCOUNT_INACTIVE("is not active yet. Please wait for admin approval."),
    AUTH_REQUIRED("is required."),
    OLD_PASSWORD_INVALID("Old password is incorrect."),
    SUPER_ADMIN_REQUIRED("requires Super Admin privileges.");

    private final String value;

    ResponseConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
