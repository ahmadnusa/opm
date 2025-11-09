package com.dansmultipro.ops.constant;

public enum RoleType {
    SA("Super Admin"),
    CUSTOMER("Customer"),
    GATEWAY("Gateway"),
    SYSTEM("System");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
