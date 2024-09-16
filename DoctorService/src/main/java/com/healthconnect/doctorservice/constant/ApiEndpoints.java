package com.healthconnect.doctorservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {
    public static final String BASE_API = "/api/v1";
    public static final String DOCTORS = BASE_API + "/doctors";
}
