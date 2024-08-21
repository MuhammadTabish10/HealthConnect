package com.healthconnect.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationMessages {
    public static final String EMAIL_VALID = "Email should be valid.";
    public static final String EMAIL_MANDATORY = "Email is mandatory.";
    public static final String PASSWORD_MANDATORY = "Password is mandatory.";
}