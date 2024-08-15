package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteConstants {

    // Route Paths
    public static final String AUTH_SERVICE_PATH = "/api/v1/auth/**";
    public static final String USER_SERVICE_PATH = "/api/v1/users/**";

    // Service URIs
    public static final String AUTH_SERVICE_URI = "lb://AUTH-SERVICE";
    public static final String USER_SERVICE_URI = "lb://USER-SERVICE";

}
