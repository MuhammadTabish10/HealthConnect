package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteConstants {

    // Route Paths
    public static final String LOGIN_PATH = "/api/v1/users/login";
    public static final String USER_SERVICE_PATH = "/api/v1/users/**";

    // Service URIs
    public static final String USER_SERVICE_URI = "lb://USER-SERVICE";

}
