package com.healthconnect.userservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {
    public static final String BASE_API = "/api/v1";
    public static final String USERS = BASE_API + "/users";
    public static final String REGISTER = "/register";
}
