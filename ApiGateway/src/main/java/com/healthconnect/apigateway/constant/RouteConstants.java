package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteConstants {

    // Service Ids
    public static final String USER_SERVICE_ID = "USER-SERVICE";

    // Route Paths
    public static final String LOGIN_PATH = "/api/v1/users/login";
    public static final String USER_SERVICE_PATH = "/api/v1/users/**";

    // Service URIs
    public static final String USER_SERVICE_URI = "lb://USER-SERVICE";

}
