package com.healthconnect.apigateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RouteConstants {

    // Service Ids
    public static final String USER_SERVICE_ID = "USER-SERVICE";
    public static final String LOCATION_SERVICE_ID = "LOCATION-SERVICE";
    public static final String HOSPITAL_SERVICE_ID = "HOSPITAL-SERVICE";
    public static final String DOCTOR_SERVICE_ID = "DOCTOR-SERVICE";

    // Route Paths
    public static final String LOGIN_PATH = "/api/v1/users/login";
    public static final String USER_SERVICE_PATH = "/api/v1/users/**";
    public static final String LOCATION_SERVICE_PATH = "/api/v1/locations/**";
    public static final String HOSPITAL_SERVICE_PATH = "/api/v1/hospitals/**";
    public static final String DOCTOR_SERVICE_PATH = "/api/v1/doctors/**";

    // Service URIs
    public static final String USER_SERVICE_URI = "lb://USER-SERVICE";
    public static final String LOCATION_SERVICE_URI = "lb://LOCATION-SERVICE";
    public static final String HOSPITAL_SERVICE_URI = "lb://HOSPITAL-SERVICE";
    public static final String DOCTOR_SERVICE_URI = "lb://DOCTOR-SERVICE";

}
