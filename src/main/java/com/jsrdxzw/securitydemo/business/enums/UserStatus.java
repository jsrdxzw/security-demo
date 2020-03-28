package com.jsrdxzw.securitydemo.business.enums;

import org.springframework.lang.Nullable;

import java.util.Arrays;

public enum UserStatus {
    CAN_USE("can use in system"),
    CAN_NOT_USE("can not use in system");

    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    @Nullable
    public static UserStatus fromRole(String status) {
        return Arrays.stream(UserStatus.values()).filter(it -> it.status.equals(status)).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        System.out.println(fromRole("can use in system"));
    }
}
